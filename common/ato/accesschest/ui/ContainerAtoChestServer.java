package ato.accesschest.ui;

import ato.accesschest.game.ItemAccessChest;
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

    public ContainerAtoChestServer(IInventory chestInventory, IInventory playerInventory) {
        super(chestInventory, playerInventory);
        setScrollIndex(0);
        filter = new ArrayList<Integer>();
        setFilter("");
        lastInventorySize = -1;
    }

    private ArrayList<Integer> filter;
    private String filterText;

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
        this.filterText = str;
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

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (lastInventorySize != filter.size()) {
            for (int i = 0; i < this.crafters.size(); ++i) {
                ICrafting crafter = (ICrafting) this.crafters.get(i);
                crafter.sendProgressBarUpdate(this, INFO_TYPE_INVENTORY_SIZE, filter.size());
            }
            lastInventorySize = filter.size();
        }
    }
}
