package ato.accesschest.game;

import net.minecraft.item.ItemStack;

/**
 * 本来 ItemStack クラスで実装されるべき機能を実装
 */
public class ItemStackUtil {

    /**
     * 2 つのアイテムスタックがマージ可能かどうか
     *
     * @see net.minecraft.inventory.Container#mergeItemStack(net.minecraft.item.ItemStack, int, int, boolean)
     */
    public static boolean canMerge(ItemStack is1, ItemStack is2) {
        if (is1 == null || is2 == null) return false;
        if (!is1.isItemEqual(is2)) return false;
        if (!is1.isStackable()) return false;
        if (!ItemStack.areItemStackTagsEqual(is1, is2)) return false;
        return true;
    }
}
