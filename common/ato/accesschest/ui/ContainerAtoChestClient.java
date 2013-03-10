package ato.accesschest.ui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * クライアント側のコンテナ
 */
public class ContainerAtoChestClient extends ContainerAtoChest {

    private int chestInventorySize;

    public ContainerAtoChestClient(IInventory playerInventory) {
        super(new InventoryBasic("dammyInventoryClient", 12*8), playerInventory);
        chestInventorySize = chestInventory.getSizeInventory();
        refreshSlot();
    }

    @Override
    protected void refreshSlotChest() {
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 12; l++) {
                int index = k * 12 + l;
                if (index < chestInventorySize) {
                    addSlotToContainer(new Slot(chestInventory, index, l * 18 + 12, k * 18 + 17));
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return null;
    }

    @Override
    public int getScrollMax() {
        return Math.max(0, (chestInventorySize - 1) / 12 - 7);
    }
}
