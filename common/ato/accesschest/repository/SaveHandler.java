package ato.accesschest.repository;

import net.minecraft.world.WorldServerMulti;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

/**
 * ゲーム内でセーブ、ロードが行われた時に呼ばれるハンドラ
 */
public class SaveHandler {

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
            NBTPool.instance = new NBTPool();
        }
    }
}
