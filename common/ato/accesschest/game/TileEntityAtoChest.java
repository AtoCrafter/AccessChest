package ato.accesschest.game;

import ato.accesschest.repository.Repository;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityEnderChest;

/**
 * TileEntity として扱うための Repository のラッパー
 */
public abstract class TileEntityAtoChest extends TileEntityEnderChest implements IInventory {

    /**
     * 実体
     */
    protected Repository repo;

    public Repository getRepository() {
        return this.repo;
    }

    protected abstract Repository createRepository(int color, int grade);

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        byte color = nbt.getByte("Color");
        byte grade = nbt.getByte("Grade");
        repo = createRepository(color, grade);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttc) {
        super.writeToNBT(nbttc);
        nbttc.setByte("Color", (byte)(repo.getColor() & 0xF));
        nbttc.setByte("Grade", (byte)(repo.getGrade() & 0xF));
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
