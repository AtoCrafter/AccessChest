package ato.accesschest.game;

import ato.accesschest.AccessChest;
import net.minecraft.item.ItemStack;

/**
 * レシピで用いるメソッド群
 */
public class RecipeUtil {

    /**
     * 指定したアイテムスタックがこの MOD のチェストで、それらのクラスが同じかどうか
     */
    public boolean isTheSameGrade(ItemStack is1, ItemStack is2) {
        if (is1 == null || is2 == null) {
            return false;
        } else if (is1.getItem() instanceof ItemAtoChest && is2.getItem() instanceof ItemAtoChest) {
            if (is1.itemID == is2.itemID) {
                return AccessChest.id2grade(is1.getItemDamage()) == AccessChest.id2grade(is2.getItemDamage());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void checkIsAtoChest(ItemStack is) {
        if (!(is.getItem() instanceof ItemAtoChest)) {
            throw new IllegalArgumentException("invalid recipe for AccessChest MOD");
        }
    }

    public boolean isOriginal(ItemStack is) {
        return AccessChest.id2isOriginal(is.getItemDamage());
    }
}
