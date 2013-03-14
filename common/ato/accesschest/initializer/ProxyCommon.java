package ato.accesschest.initializer;

import ato.accesschest.AccessChest;
import ato.accesschest.Properties;
import ato.accesschest.game.BlockAccessChest;
import ato.accesschest.game.ItemAccessChest;
import ato.accesschest.game.RecipeAtoChest;
import ato.accesschest.game.TileEntityAccessChest;
import ato.accesschest.handler.AutoCollect;
import ato.accesschest.repository.SaveHandler;
import ato.accesschest.ui.GuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        registerEntities();
        registerRecipes();
        registerGuiHandler();
    }

    /**
     * イベントフックの登録
     */
    private void registerHooks() {
        MinecraftForge.EVENT_BUS.register(new SaveHandler());
        MinecraftForge.EVENT_BUS.register(new AutoCollect());
    }

    /**
     * アイテムの登録
     */
    private void registerItems() {
        Properties.BLOCK_AC_ID = 255;
        GameRegistry.registerBlock(new BlockAccessChest(Properties.BLOCK_AC_ID), ItemAccessChest.class, "accesschest", Properties.MOD_ID);
    }

    /**
     * エンティティの登録
     */
    private void registerEntities() {
        GameRegistry.registerTileEntity(TileEntityAccessChest.class, "accesschest");
    }

    /**
     * レシピの登録
     */
    private void registerRecipes() {
        int blockIdAC = Properties.BLOCK_AC_ID;
        int blockIdCC = 254; // TODO
        // Access Chest Class-0
        GameRegistry.addShapelessRecipe(new ItemStack(Item.itemsList[blockIdAC], 1, AccessChest.colorgrade2id(15, 0)),
                new Object[]{Block.chest, Item.enderPearl});
        // Access Chest Class-1
        GameRegistry.addRecipe(new RecipeAtoChest(
                new ItemStack(Block.blockLapis),
                new ItemStack(Item.itemsList[blockIdAC], 1, AccessChest.colorgrade2id(15, 0)),
                new ItemStack(Item.itemsList[blockIdAC], 1, AccessChest.colorgrade2id(15, 1))
        ));
        // Access Chest Class-2
        GameRegistry.addRecipe(new RecipeAtoChest(
                new ItemStack(Block.blockGold),
                new ItemStack(Item.itemsList[blockIdAC], 1, AccessChest.colorgrade2id(15, 1)),
                new ItemStack(Item.itemsList[blockIdAC], 1, AccessChest.colorgrade2id(15, 2))
        ));
        // Access Chest Class-3
        GameRegistry.addRecipe(new RecipeAtoChest(
                new ItemStack(Block.blockDiamond),
                new ItemStack(Item.itemsList[blockIdAC], 1, AccessChest.colorgrade2id(15, 2)),
                new ItemStack(Item.itemsList[blockIdAC], 1, AccessChest.colorgrade2id(15, 3))
        ));
//        // Compressed Chest Class-1
//        GameRegistry.addRecipe(new RecipeAtoChest(
//                new ItemStack(Item.diamond),
//                new ItemStack(Block.chest),
//                new ItemStack(Item.itemsList[blockIdCC], 1, AccessChest.colorgrade2id(15, 1))
//        ));
//        // Compressed Chest Class-2
//        GameRegistry.addRecipe(new RecipeAtoChest(
//                new ItemStack(Block.blockDiamond),
//                new ItemStack(Item.itemsList[blockIdCC], 1, AccessChest.colorgrade2id(15, 1)),
//                new ItemStack(Item.itemsList[blockIdCC], 1, AccessChest.colorgrade2id(15, 2))
//        ));
        // TODO
        // Copy of Access Chest Class-1
        // Copy of Access Chest Class-2
    }

    /**
     * GUI ハンドラの登録
     */
    private void registerGuiHandler() {
        NetworkRegistry.instance().registerGuiHandler(AccessChest.instance, new GuiHandler());
    }
}
