package ato.accesschest.repository;

import net.minecraft.item.ItemStack;

/**
 * リポジトリのデータ(配列)管理に必要な機能の実装
 */
public class DataManagerArray extends DataManager {

    private ItemStack[] contents;

    public DataManagerArray() {
        contents = new ItemStack[13824];    // 27 * 8^3
    }

    @Override
    public void setItem(int index, ItemStack is) {
        contents[index] = is;
    }

    @Override
    public ItemStack getItem(int index) {
        return contents[index];
    }

    @Override
    public int getMaxSize() {
        return contents.length;
    }

    @Override
    public ItemStack decreaseItem(int index, int amount) {
        ItemStack is = getItem(index);
        is.stackSize -= amount;
        if (is.stackSize <= 0) {
            is = null;
        }
        setItem(index, is);
        return is;
    }
}
