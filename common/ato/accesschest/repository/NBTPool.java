package ato.accesschest.repository;

import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

/**
 * Access Chest の NBT をキャッシュしておく
 * ファイル I/O は遅い
 */
public class NBTPool {

    private static final NBTIOUtil util = new NBTIOUtil();
    /**
     * このクラスの唯一のインスタンス
     */
    protected static NBTPool instance = new NBTPool();
    /**
     * 中身の実体
     */
    private DataManagerNBT[] pool;

    public NBTPool() {
        pool = new DataManagerNBT[16];
    }

    public DataManagerNBT getNBT(int color) {
        if (pool[color] == null) {
            pool[color] = new DataManagerNBT();
            try {
                pool[color].readFromNBT(util.getAccessChestNBT(color));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pool[color];
    }

    public void save() {
        for (int color = 0; color < pool.length; ++color) {
            if (pool[color] != null) {
                NBTTagCompound nbt = new NBTTagCompound();
                pool[color].writeToNBT(nbt);
                try {
                    util.saveAccessChestNBT(color, nbt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
