package ato.accesschest.game;

import ato.accesschest.AccessChest;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

public class RecipeAtoChest extends ShapedRecipes {

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
                center, center, center,
                center, round, center,
                center, center, center
        }, output);
        this.center = center;
        this.round = round;
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        boolean exist = false;
        for (int i = 0; i < 9; ++i) {
            ItemStack target = inventory.getStackInSlot(i);
            if (i == 4) {
                if (!match(center, target)) {
                    return false;
                }
            } else {
                if (!match(round, target)) {
                    return false;
                }
            }
            if (target != null) {
                exist = true;
            }
        }
        return exist;
    }

    /**
     * レシピに使えるアイテムかどうか調べる
     *
     * @param is1 レシピのアイテム
     * @param is2 チェックするアイテム
     */
    private boolean match(ItemStack is1, ItemStack is2) {
        if (is2 == null) {
            return false;
        }
        if (is1.itemID == is2.itemID) {
            return AccessChest.id2grade(is1.getItemDamage()) == AccessChest.id2grade(is2.getItemDamage());
        }
        return false;
    }

}
