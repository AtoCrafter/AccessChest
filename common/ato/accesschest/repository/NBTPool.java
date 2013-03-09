package ato.accesschest.repository;

/**
 * Access Chest の NBT をキャッシュしておく
 * ファイル I/O は遅い
 */
public class NBTPool {

    /**
     * このクラスの唯一のインスタンス
     */
    protected static NBTPool instance;

    /**
     * 中身の実体
     */
    private DataManagerNBT[] pool;

    public NBTPool() {
        pool = new DataManagerNBT[16];
    }

    public DataManagerNBT getNBT(int color) {
        if (pool[color] == null) {
            pool[color] = new DataManagerNBT(color);
        }
        return pool[color];
    }
}
