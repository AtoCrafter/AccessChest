package ato.accesschest.ui;

import ato.accesschest.game.ItemAccessChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * この MOD で追加されるチェストの GUI でプレイヤーインベントリとチェスト内を組み合わせるコンテナ
 */
public class ContainerAtoChest extends Container {

    private IInventory chestInventory;
    private IInventory playerInventory;
    /**
     * 現在のスクロール位置
     */
    private int scrollIndex;

    /**
     * @param chestInventory  対象とするチェストのインベントリ
     * @param playerInventory GUI を開いているプレイヤーのインベントリ
     */
    public ContainerAtoChest(IInventory chestInventory, IInventory playerInventory) {
        this.chestInventory = chestInventory;
        this.playerInventory = playerInventory;
        setScrollIndex(0);
        refreshSlot();
    }

    /**
     * スロットの更新
     */
    private void refreshSlot() {
        inventorySlots.clear();
        // chest inventory
        int max = chestInventory.getSizeInventory();
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 12; l++) {
                int index = getDisplayBaseIndex() + k * 12 + l;
                if (index < max) {
                    addSlotToContainer(new Slot(chestInventory, index, l * 18 + 12, k * 18 + 17));
                }
            }
        }
        // player inventory
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 9; l++) {
                addSlotToContainer(new Slot(playerInventory, k * 9 + l + 9, l * 18 + 12, k * 18 + 165));
            }
        }
        // player itemslot
        for (int l = 0; l < 9; l++) {
            addSlotToContainer(new Slot(playerInventory, l, l * 18 + 12, 223));
        }
        // inventoryItemStacks size
        int limit = 12 * 8 + 9 * 4;
        while (inventoryItemStacks.size() > limit) {
            inventoryItemStacks.remove(inventoryItemStacks.size() - 1);
        }
//        updateSlotSize();
    }

    /**
     * インベントリ内容を表示するスロット群の先頭がしめすインベントリの番号
     */
    protected int getDisplayBaseIndex() {
        return getScrollIndex() * 12;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
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
     * @see Container#mergeItemStack(net.minecraft.item.ItemStack, int, int, boolean)
     */
    public boolean customMergeItemStack(ItemStack stack) {
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

    // スクロール関連

    /**
     * スクロールの現在位置を取得
     */
    public int getScrollIndex() {
        return scrollIndex;
    }

    /**
     * スクロールの現在位置を設定
     */
    public void setScrollIndex(int index) {
        if (index != scrollIndex) {
            scrollIndex = Math.max(0, Math.min(index, getScrollMax()));
            refreshSlot();
        }
    }

    /**
     * スクロール可能な最大値を取得
     */
    public int getScrollMax() {
        return Math.max(0, (chestInventory.getSizeInventory() - 1) / 12 - 7);
    }
}
