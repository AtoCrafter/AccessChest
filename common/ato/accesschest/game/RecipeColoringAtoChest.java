package ato.accesschest.game;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

import java.util.Arrays;

/**
 * 染色レシピ
 */
public class RecipeColoringAtoChest extends ShapelessRecipes {

    private static final RecipeUtil util = new RecipeUtil();
    private ItemStack chest;
    private ItemStack dye;

    public RecipeColoringAtoChest(ItemStack dye, ItemStack atoChest, ItemStack output) {
        super(output, Arrays.asList(new ItemStack[]{dye, atoChest}));
        this.chest = atoChest;
        this.dye = dye;
        util.checkIsAtoChest(atoChest);
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        boolean existChest = false;
        boolean existDye = false;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                ItemStack target = inventory.getStackInRowAndColumn(j, i);
                if (target != null) {
                    if (util.isTheSameGrade(target, chest)) {
                        existChest = true;
                    } else if (dye.isItemEqual(target)) {
                        existDye = true;
                    }
                }
            }
        }
        return existChest && existDye;
    }
}
