package ato.accesschest.repository;

import net.minecraft.world.WorldServerMulti;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

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

    /**
     * ワールド読み込み時に Access Chest のリポジトリ群をリロードする
     */
    @ForgeSubscribe
    public void reloadPool(WorldEvent.Load event) {
        // イベントは 3 回発生し、それぞれの event.world は
        // WorldServer, WorldServerMulti, WorldClient
        // となる。それぞれの役割は不明。
        // 一度だけ実行するために以下の条件をつけた。
        if (event.world instanceof WorldServerMulti) {
            instance = new NBTPool();
        }
    }

    public DataManagerNBT getNBT(int color) {
        if (pool[color] == null) {
            pool[color] = new DataManagerNBT(color);
        }
        return pool[color];
    }
}
