package ato.accesschest.initializer;

import ato.accesschest.game.TileEntityAtoChest;
import ato.accesschest.game.TileEntityAtoChestRenderer;
import ato.accesschest.ui.Localization;
import cpw.mods.fml.client.registry.ClientRegistry;

/**
 * 初期化処理
 * クライアントサイドのみ実行される
 */
public class ProxyClient extends ProxyCommon {

    @Override
    public void init() {
        super.init();
        registerLocalization();
        registerRenderer();
    }

    /**
     * 言語ファイルの登録
     */
    private void registerLocalization() {
        Localization.register();
    }

    /**
     * レンダラの登録
     */
    private void registerRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAtoChest.class, new TileEntityAtoChestRenderer());
    }
}
