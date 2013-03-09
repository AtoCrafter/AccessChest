package ato.accesschest.initializer;

import ato.accesschest.AccessChest;
import ato.accesschest.game.BlockAccessChest;
import ato.accesschest.game.ItemAccessChest;
import ato.accesschest.repository.SaveHandler;
import ato.accesschest.ui.GuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;

/**
 * 初期化処理を行う
 * サーバー、クライアントサイド両方で実行される
 */
public class ProxyCommon {

    /**
     * 初期化処理
     */
    public void init() {
        registerHooks();
        registerItems();
        registerGuiHandler();
    }

    /**
     * イベントフックの登録
     */
    private void registerHooks() {
        MinecraftForge.EVENT_BUS.register(new SaveHandler());
    }

    /**
     * アイテムの登録
     */
    private void registerItems() {
        GameRegistry.registerBlock(new BlockAccessChest(255), ItemAccessChest.class, "test");
    }

    /**
     * GUI ハンドラの登録
     */
    private void registerGuiHandler() {
        NetworkRegistry.instance().registerGuiHandler(AccessChest.instance, new GuiHandler());
    }
}
