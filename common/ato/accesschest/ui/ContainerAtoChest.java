package ato.accesschest.ui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * この MOD で追加されるチェストの GUI でプレイヤーインベントリとチェスト内を組み合わせるコンテナ
 */
public abstract class ContainerAtoChest extends Container {

    protected static final int INFO_TYPE_INVENTORY_SIZE = 1;
    protected static final int INFO_TYPE_SCROLL_INDEX = 2;
    protected IInventory chestInventory;
    protected IInventory playerInventory;
    /**
     * 現在のスクロール位置
     */
    protected int scrollIndex;

    /**
     * @param chestInventory  対象とするチェストのインベントリ
     * @param playerInventory GUI を開いているプレイヤーのインベントリ
     */
    public ContainerAtoChest(IInventory chestInventory, IInventory playerInventory) {
        this.chestInventory = chestInventory;
        this.playerInventory = playerInventory;
        chestInventory.openChest();
    }

    /**
     * スロットの更新
     */
    protected void refreshSlot() {
        inventorySlots.clear();
        // chest inventory
        refreshSlotChest();
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
    }

    protected abstract void refreshSlotChest();

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return chestInventory.isUseableByPlayer(player);
    }

    @Override
    public abstract ItemStack transferStackInSlot(EntityPlayer player, int index);

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
    public abstract int getScrollMax();

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        chestInventory.closeChest();
    }
}
