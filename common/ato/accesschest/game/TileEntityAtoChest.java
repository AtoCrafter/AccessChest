package ato.accesschest.game;

import ato.accesschest.repository.Repository;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityEnderChest;

/**
 * TileEntity として扱うための Repository のラッパー
 */
public abstract class TileEntityAtoChest extends TileEntityEnderChest implements IInventory {

    /**
     * 実体
     */
    private Repository repo;

    public TileEntityAtoChest(Repository repo) {
        this.repo = repo;
    }

    public Repository getRepository() {
        return this.repo;
    }

    /* IInventory を実装するため、そのまま Repository に移譲 */

    @Override
    public int getSizeInventory() {
        return repo.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return repo.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        return repo.decrStackSize(var1, var2);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        return repo.getStackInSlotOnClosing(var1);
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        repo.setInventorySlotContents(var1, var2);
    }

    @Override
    public String getInvName() {
        return repo.getInvName();
    }

    @Override
    public int getInventoryStackLimit() {
        return repo.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return repo.isUseableByPlayer(var1);
    }

    @Override
    public void openChest() {
        repo.openChest();
    }

    @Override
    public void closeChest() {
        repo.closeChest();
    }
}
