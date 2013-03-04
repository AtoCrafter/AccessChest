package ato.accesschest.repository;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

/**
 * リポジトリをキャッシュしておく
 * ファイル I/O は遅い
 */
public class RepositoriesPool {

    /**
     * このクラスの唯一のインスタンス
     */
    public static RepositoriesPool instance;

    /**
     * 中身の実体
     */
    private RepositoryAccessChest[] pool;

    public RepositoriesPool() {
        pool = new RepositoryAccessChest[16];
    }

    /**
     * ワールド読み込み時に Access Chest のリポジトリ群をリロードする
     */
    @ForgeSubscribe
    public void reloadPool(WorldEvent.Load event) {
        instance = new RepositoriesPool();
    }

    public RepositoryAccessChest getRepository(int color) {
        if (pool[color] == null) {
            pool[color] = new RepositoryAccessChest(color);
        }
        return pool[color];
    }
}
