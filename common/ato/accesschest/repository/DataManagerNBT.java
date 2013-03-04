package ato.accesschest.repository;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.IOException;

/**
 * リポジトリのデータ(NBT)管理に必要な機能の実装
 */
public class DataManagerNBT extends DataManagerArray {

    private static final NBTIOUtil util = new NBTIOUtil();

    private final int color;

    public DataManagerNBT(int color) {
        super();
        this.color = color;
        try {
            loadFromNBT();
        } catch (IOException e) {
            for (int i=0; i<getMaxSize(); ++i) {
                setItem(i, null);
            }
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * NBT ファイルから読み込み
     */
    private void loadFromNBT() throws IOException {
        NBTTagCompound nbt = util.getAccessChestNBT(color);
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
//        // Comparator
//        NBTTagCompound nbtcomparator = (NBTTagCompound)nbt.getTag("Comparator");
//        if ( nbtcomparator != null ) {
//            comparator.readFromNBT(nbtcomparator);
//        }
    }
}
