package ato.accesschest.repository;

import net.minecraft.item.ItemStack;

/**
 * リポジトリのデータ管理に必要な機能のインターフェースを定義する
 */
public interface IDataManager {

    public void setItem(int index, ItemStack is);

    public ItemStack getItem(int index);

    public int getMaxSize();

    public ComparatorAtoChest getComparator();

    public String getName();

    public void setName(String name);
}
