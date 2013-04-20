package ato.accesschest.repository;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

/**
 * リポジトリのデータ(ワールド単位)管理に必要な機能の実装
 */
public class DataManagerWorldSaveData extends WorldSavedData implements IDataManager {

    private DataManagerNBT data;

    public DataManagerWorldSaveData(String id) {
        super(id);
        data = new DataManagerNBT();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        data.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        data.writeToNBT(nbt);
    }

    @Override
    public void setItem(int index, ItemStack is) {
        data.setItem(index, is);
        markDirty();
    }

    @Override
    public ItemStack getItem(int index) {
        return data.getItem(index);
    }

    @Override
    public int getMaxSize() {
        return data.getMaxSize();
    }

    @Override
    public ComparatorAtoChest getComparator() {
        return data.getComparator();
    }

    @Override
    public String getName() {
        return data.getName();
    }

    @Override
    public void setName(String name) {
        data.setName(name);
        markDirty();
    }
}
