package ato.accesschest.ui;

import ato.accesschest.repository.Repository;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * この MOD で追加されるチェストの GUI でプレイヤーインベントリとチェスト内を組み合わせるコンテナ
 */
public class ContainerAtoChest extends Container {

    private IInventory chestInventory;
    private IInventory playerInventory;

    /**
     * @param chestInventory 対象とするチェストのインベントリ
     * @param playerInventory GUI を開いているプレイヤーのインベントリ
     */
    public ContainerAtoChest(IInventory chestInventory, IInventory playerInventory) {
        this.chestInventory = chestInventory;
        this.playerInventory = playerInventory;
        refreshSlot();
    }

    /**
     * スロットの更新
     */
    private void refreshSlot() {
        inventorySlots.clear();
        // chest inventory
        int max = chestInventory.getSizeInventory();
        for ( int k=0; k<8; k++ ) {
            for ( int l=0; l<12; l++ ) {
//                int stackIndex = getCurrentScroll()*12 + k*12+l;
                int index = getDisplayBaseIndex() + k*12 + l;
                if ( index < max ) {
                    addSlotToContainer(new Slot(chestInventory, index, l*18+12, k*18+17));
                }
            }
        }
        // player inventory
        for ( int k=0; k<3; k++ ) {
            for ( int l=0; l<9; l++ ) {
                addSlotToContainer(new Slot(playerInventory, k*9+l+9, l*18+12, k*18+165));
            }
        }
        // player itemslot
        for  ( int l=0; l<9; l++ ) {
            addSlotToContainer(new Slot(playerInventory, l, l*18+12, 223));
        }
        // inventoryItemStacks size
        int limit = 12*8 + 9*4;
        while ( inventoryItemStacks.size() > limit ) {
            inventoryItemStacks.remove(inventoryItemStacks.size()-1);
        }
//        updateSlotSize();
    }

    /**
     * インベントリ内容を表示するスロット群の先頭がしめすインベントリの番号
     */
    protected int getDisplayBaseIndex() {
        return 0;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
