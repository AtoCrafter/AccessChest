package ato.accesschest.initializer;

import ato.accesschest.AccessChest;
import ato.accesschest.Properties;
import ato.accesschest.game.*;
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
        if (AccessChest.config.enableAutoCollect) {
            MinecraftForge.EVENT_BUS.register(new AutoCollect());
        }
    }

    /**
     * アイテムの登録
     */
    private void registerItems() {
        GameRegistry.registerBlock(
                new BlockAccessChest(AccessChest.config.blockIDAC).setUnlocalizedName("accesschest:accesschest"),
                ItemAccessChest.class, "accesschest", Properties.MOD_ID
        );
        GameRegistry.registerBlock(
                new BlockCompressedChest(AccessChest.config.blockIDCC).setUnlocalizedName("accesschest:compressedchest"),
                ItemCompressedChest.class, "compressedchest", Properties.MOD_ID
        );
    }

    /**
     * エンティティの登録
     */
    private void registerEntities() {
        GameRegistry.registerTileEntity(TileEntityAccessChest.class, "accesschest");
        GameRegistry.registerTileEntity(TileEntityCompressedChest.class, "compressedchest");
    }

    /**
     * レシピの登録
     */
    private void registerRecipes() {
        Item itemAC = Item.itemsList[AccessChest.config.blockIDAC];
        Item itemCC = Item.itemsList[AccessChest.config.blockIDCC];

        ItemStack ac0 = new ItemStack(itemAC, 1, AccessChest.colorgrade2id(15, 0));
        ItemStack ac1 = new ItemStack(itemAC, 1, AccessChest.colorgrade2id(15, 1));
        ItemStack ac2 = new ItemStack(itemAC, 1, AccessChest.colorgrade2id(15, 2));
        ItemStack ac3 = new ItemStack(itemAC, 1, AccessChest.colorgrade2id(15, 3));
        ItemStack ac1c = new ItemStack(itemAC, 1, AccessChest.colorgrade2id(15, 1, false));
        ItemStack ac2c = new ItemStack(itemAC, 1, AccessChest.colorgrade2id(15, 2, false));
        ItemStack ac3c = new ItemStack(itemAC, 1, AccessChest.colorgrade2id(15, 3, false));
        // Access Chest Class-0
        GameRegistry.addShapelessRecipe(ac0, new Object[]{Block.chest, Item.enderPearl});
        // Access Chest Class-1
        GameRegistry.addRecipe(new RecipeAtoChest(new ItemStack(Block.blockLapis), ac0, ac1));
        // Access Chest Class-2
        GameRegistry.addRecipe(new RecipeAtoChest(new ItemStack(Block.blockGold), ac1, ac2));
        // Access Chest Class-3
        GameRegistry.addRecipe(new RecipeAtoChest(new ItemStack(Block.blockDiamond), ac2, ac3));

        // Compressed Chest Class-1
        GameRegistry.addRecipe(new RecipeAtoChest(
                new ItemStack(Item.diamond),
                new ItemStack(Block.chest),
                new ItemStack(itemCC, 1, AccessChest.colorgrade2id(15, 1))
        ));
        // Compressed Chest Class-2
        GameRegistry.addRecipe(new RecipeAtoChest(
                new ItemStack(Block.blockDiamond),
                new ItemStack(itemCC, 1, AccessChest.colorgrade2id(15, 1)),
                new ItemStack(itemCC, 1, AccessChest.colorgrade2id(15, 2))
        ));

        // Copy of Access Chest Class-1
        GameRegistry.addRecipe(new RecipeCopyAccessChest(new ItemStack(Block.enderChest, 1), ac1, ac1c));
        // Copy of Access Chest Class-2
        GameRegistry.addRecipe(new RecipeCopyAccessChest(new ItemStack(Block.enderChest, 3), ac2, ac2c));
        // Copy of Access Chest Class-3
        GameRegistry.addRecipe(new RecipeCopyAccessChest(new ItemStack(Block.enderChest, 8), ac3, ac3c));

        // Coloring of Access Chest
        for (int color = 0; color < 16; ++color) {
            for (int grade = 0; grade < 4; ++grade) {
                GameRegistry.addRecipe(new RecipeColoringAtoChest(
                        new ItemStack(Item.dyePowder, 1, color),
                        new ItemStack(itemAC, 1, AccessChest.colorgrade2id(15, grade)),
                        new ItemStack(itemAC, 1, AccessChest.colorgrade2id(color, grade))
                ));
            }
        }
        // Coloring of Copied Access Chest
        for (int color = 0; color < 16; ++color) {
            for (int grade = 0; grade < 4; ++grade) {
                GameRegistry.addRecipe(new RecipeColoringAtoChest(
                        new ItemStack(Item.dyePowder, 1, color),
                        new ItemStack(itemAC, 1, AccessChest.colorgrade2id(15, grade, false)),
                        new ItemStack(itemAC, 1, AccessChest.colorgrade2id(color, grade, false))
                ));
            }
        }
        // Coloring of Compressed Chest
        for (int color = 0; color < 16; ++color) {
            for (int grade = 1; grade < 3; ++grade) {
                GameRegistry.addRecipe(new RecipeColoringAtoChest(
                        new ItemStack(Item.dyePowder, 1, color),
                        new ItemStack(itemCC, 1, AccessChest.colorgrade2id(15, grade)),
                        new ItemStack(itemCC, 1, AccessChest.colorgrade2id(color, grade))
                ));
            }
        }
    }

    /**
     * GUI ハンドラの登録
     */
    private void registerGuiHandler() {
        NetworkRegistry.instance().registerGuiHandler(AccessChest.instance, new GuiHandler());
    }
}
