package ato.accesschest.repository;

import net.minecraft.item.ItemStack;

/**
 * リポジトリのデータ(配列)管理に必要な機能の実装
 */
public class DataManagerArray extends DataManager {

    private ItemStack[] contents;

    public DataManagerArray() {
        contents = new ItemStack[13824];    // 27 * 8^3
        setName("");
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
    public ComparatorAtoChest getComparator() {
        return new ComparatorAtoChest();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
