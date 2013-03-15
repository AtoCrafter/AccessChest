package ato.accesschest.game;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

public class RecipeAtoChest extends ShapedRecipes {

    private static final RecipeUtil util = new RecipeUtil();
    private ItemStack center;
    private ItemStack round;

    /**
     * コンストラクタ
     *
     * @param center レシピ中央のアイテムスタック
     * @param round  レシピ外周のアイテムスタック
     * @param output レシピの生成物
     */
    public RecipeAtoChest(ItemStack center, ItemStack round, ItemStack output) {
        super(3, 3, new ItemStack[]{
                round, round, round,
                round, center, round,
                round, round, round
        }, output);
        this.center = center;
        this.round = round;
        util.checkIsAtoChest(round);
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        boolean exist = false;
        for (int i = 0; i < 9; ++i) {
            ItemStack target = inventory.getStackInSlot(i);
            if (i == 4) {
                if (!center.isItemEqual(target)) {
                    return false;
                }
            } else {
                if (!util.isTheSameGrade(round, target)) {
                    return false;
                }
            }
            if (target != null) {
                exist = true;
            }
        }
        return exist;
    }
}
