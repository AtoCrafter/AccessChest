package ato.accesschest.repository;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

/**
 * ゲーム内でセーブ、ロードが行われた時に呼ばれるハンドラ
 */
public class SaveHandler {

    private static final OldSavedataConverter converter = new OldSavedataConverter();

    /**
     * ワールド読み込み時に Access Chest のリポジトリ群をリロードする
     */
    @ForgeSubscribe
    public void reloadPool(WorldEvent.Load event) {
        for (int i = 0; i < 16; ++i) {
            String name = "AccessChest" + i;
            if (event.world.loadItemData(DataManagerNBT.class, name) == null) {
                if (converter.doesOldSavedataExists(i)) {
                    DataManagerWorldSaveData manager = new DataManagerWorldSaveData(name);
                    manager.readFromNBT(converter.getOldNBT(i));
                    event.world.setItemData(name, manager);
                } else {
                    // セーブデータが存在しない場合は新規作成
                    event.world.setItemData(name, new DataManagerWorldSaveData(name));
                }
            }
        }
    }
}
