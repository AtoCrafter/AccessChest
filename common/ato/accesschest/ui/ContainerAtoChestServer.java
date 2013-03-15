package ato.accesschest.ui;

import ato.accesschest.game.ItemAccessChest;
import ato.accesschest.game.TileEntityAtoChest;
import ato.accesschest.repository.Repository;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * サーバー側のコンテナ
 */
public class ContainerAtoChestServer extends ContainerAtoChest {

    private int lastInventorySize;
    private int lastScrollIndex;
    private ArrayList<Integer> filter;

    public ContainerAtoChestServer(IInventory chestInventory, IInventory playerInventory) {
        super(chestInventory, playerInventory);
        setScrollIndex(0);
        filter = new ArrayList<Integer>();
        setFilter("");
        lastInventorySize = -1;
        lastScrollIndex = -1;
    }

    @Override
    protected void refreshSlotChest() {
        int max = filter.size();
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 12; l++) {
                int index = getDisplayBaseIndex() + k * 12 + l;
                if (index < max) {
                    addSlotToContainer(new Slot(chestInventory, filter.get(index), l * 18 + 12, k * 18 + 17));
                }
            }
        }
    }

    /**
     * インベントリ内容を表示するスロット群の先頭がしめすインベントリの番号
     */
    protected int getDisplayBaseIndex() {
        return getScrollIndex() * 12;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stackBackup = null;
        Slot slot = (Slot) inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stackBackup = slotStack.copy();

            int border = inventorySlots.size() - 36;
            if (index < border) {
                if (!mergeItemStack(slotStack, border, inventorySlots.size(), false)) {
                    return null;
                }
            } else if (slotStack.getItem() instanceof ItemAccessChest ||  // forbit to put AccessChest away in AccessChest automatically
                    !customMergeItemStack(slotStack)) {
                return null;
            }
            if (slotStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return stackBackup;
    }

    /**
     * プレイヤーのインベントリからこの MOD のチェストのインベントリに Shift クリックなどで移動させるとき
     * 画面に表示されていないスロットも含めて、自動的に移動させる
     *
     * @param stack 移動させたいアイテムスタック
     * @return 成否
     * @see net.minecraft.inventory.Container#mergeItemStack(net.minecraft.item.ItemStack, int, int, boolean)
     */
    private boolean customMergeItemStack(ItemStack stack) {
        boolean isSuccess = false;
        int max = chestInventory.getSizeInventory();
        ItemStack chestStack;

        // 同じ種類のスタック可能なスロットを探して、可能な数だけ結合させる
        if (stack.isStackable()) {
            for (int i = 0; i < max; ++i) {
                chestStack = chestInventory.getStackInSlot(i);
                if (chestStack != null && chestStack.itemID == stack.itemID &&
                        (!stack.getHasSubtypes() || stack.getItemDamage() == chestStack.getItemDamage()) &&
                        ItemStack.areItemStackTagsEqual(stack, chestStack)) {
                    int sum = chestStack.stackSize + stack.stackSize;
                    if (sum <= chestStack.getMaxStackSize()) {
                        stack.stackSize = 0;
                        chestStack.stackSize = sum;
                        isSuccess = true;
                    } else if (chestStack.stackSize < stack.getMaxStackSize()) {
                        stack.stackSize -= stack.getMaxStackSize() - chestStack.stackSize;
                        chestStack.stackSize = chestStack.getMaxStackSize();
                        isSuccess = true;
                    }
                }
                if (stack.stackSize == 0) break;
            }
        }

        // 空きスロットを探して、可能な数だけ挿入する
        if (stack.stackSize > 0) {
            for (int i = 0; i < max; ++i) {
                chestStack = chestInventory.getStackInSlot(i);
                if (chestStack == null) {
                    chestInventory.setInventorySlotContents(i, stack.copy());
                    stack.stackSize = 0;
                    isSuccess = true;
                    break;
                }
            }
        }

        return isSuccess;
    }

    @Override
    public int getScrollMax() {
        return Math.max(0, (filter.size() - 1) / 12 - 7);
    }

    // フィルタ関連

    public void setFilter(String str) {
        filter.clear();
        if ("".equals(str)) {
            for (int index = 0; index < chestInventory.getSizeInventory(); ++index) {
                filter.add(index);
            }
        } else {
            for (int index = 0; index < chestInventory.getSizeInventory(); ++index) {
                if (isMatchFilter(chestInventory.getStackInSlot(index), str)) {
                    filter.add(index);
                }
            }
        }
        setScrollIndex(0);
        refreshSlot();
    }

    /**
     * アイテムスタックの情報にフィルタがマッチするか
     */
    private boolean isMatchFilter(ItemStack is, String filter) {
        if (is == null) {
            return false;
        }
        return is.getDisplayName().contains(filter);
    }

    // ボタン関連

    public void setName(String name) {
        getChestRepository().setName(name);
    }

    public void sort() {
        getChestRepository().sort();
    }

    public void eject(EntityPlayer player) {
        ItemStack[] list = getChestRepository().eject();
        for (ItemStack is : list) {
            if (is != null) {
                player.dropPlayerItem(is);
            }
        }
    }

    public void storeInventory() {
        int max = inventorySlots.size();
        for (int i = max - 9 * 4; i < max - 9; ++i) {
            transferStackInSlot(null, i);
        }
    }

    public void storeEquipment() {
        int max = inventorySlots.size();
        for (int i = max - 9; i < max; ++i) {
            transferStackInSlot(null, i);
        }
    }

    @Override
    public void detectAndSendChanges() {
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting crafter = (ICrafting) this.crafters.get(i);
            if (lastScrollIndex != scrollIndex) {
                crafter.sendProgressBarUpdate(this, INFO_TYPE_SCROLL_INDEX, scrollIndex);
            }
            if (lastInventorySize != filter.size()) {
                crafter.sendProgressBarUpdate(this, INFO_TYPE_INVENTORY_SIZE, filter.size());
            }
        }
        lastScrollIndex = scrollIndex;
        lastInventorySize = filter.size();
        super.detectAndSendChanges();
    }

    protected Repository getChestRepository() {
        if (chestInventory instanceof Repository) {
            return ((Repository) chestInventory);
        } else if (chestInventory instanceof TileEntityAtoChest) {
            return ((TileEntityAtoChest) chestInventory).getRepository();
        } else {
            throw new RuntimeException("unexpected IInventory object in chestInventory");
        }
    }
}
