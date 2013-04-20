package ato.accesschest.repository;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * リポジトリのデータ(NBT)管理に必要な機能の実装
 */
public class DataManagerNBT extends DataManagerArray {

    private ComparatorAtoChest comparator;

    public DataManagerNBT() {
        super();
        comparator = new ComparatorAtoChest();
        for (int i = 0; i < getMaxSize(); ++i) {
            setItem(i, null);
        }
    }

    /**
     * NBT ファイルから読み込み
     */
    public void readFromNBT(NBTTagCompound nbt) {
        // Items
        NBTTagList list = nbt.getTagList("Items");
        // clear
        for (int i = 0; i < getMaxSize(); ++i) {
            setItem(i, null);
        }
        // load
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound nbtitem = (NBTTagCompound) list.tagAt(i);
            int j = nbtitem.getInteger("Slot");
            if (0 <= j && j < getMaxSize()) {
                setItem(j, ItemStack.loadItemStackFromNBT(nbtitem));
            }
        }
        // Comparator
        NBTTagCompound nbtcomparator = (NBTTagCompound) nbt.getTag("Comparator");
        if (nbtcomparator != null) {
            comparator.readFromNBT(nbtcomparator);
        }
        // Name
        setName(nbt.getString("Name"));
    }

    /**
     * NBT ファイルへ保存
     */
    public void writeToNBT(NBTTagCompound nbt) {
        // Items
        NBTTagList list = new NBTTagList("Items");
        for (int i = 0; i < getMaxSize(); ++i) {
            ItemStack is = getItem(i);
            if (is != null) {
                NBTTagCompound nbtstack = new NBTTagCompound();
                nbtstack.setInteger("Slot", i);
                is.writeToNBT(nbtstack);
                list.appendTag(nbtstack);
            }
        }
        nbt.setTag("Items", list);
        // Comparator
        NBTTagCompound nbtcomparator = new NBTTagCompound();
        comparator.writeToNBT(nbtcomparator);
        nbt.setTag("Comparator", nbtcomparator);
        // Name
        nbt.setString("Name", getName());
    }

    @Override
    public ComparatorAtoChest getComparator() {
        return comparator;
    }
}
