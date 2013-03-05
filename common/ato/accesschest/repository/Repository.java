package ato.accesschest.repository;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * この MOD で追加されるチェストの実体
 * リポジトリに関する機能の実装
 */
public abstract class Repository implements IInventory {

    /** データ管理を行うオブジェクト */
    protected DataManager data;
    /** チェストのランク。"** Chest Class-?" と表現される */
    protected int grade;

    public Repository(DataManager data) {
        this.data = data;
    }

    @Override
    public int getSizeInventory() {
        return (int)(27 * Math.pow(8, grade));
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return data.getItem(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        return data.bringItem(index, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack is) {
        data.setItem(index, is);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}
}
