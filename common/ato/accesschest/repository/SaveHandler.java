package ato.accesschest.repository;

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
        for (int i = 0; i < 16; ++i) {
            String name = "AccessChest" + i;
            if (event.world.loadItemData(DataManagerNBT.class, name) == null) {
                event.world.setItemData(name, new DataManagerNBT(name));
            }
        }
    }
}
