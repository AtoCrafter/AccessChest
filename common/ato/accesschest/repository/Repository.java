package ato.accesschest.repository;

import ato.accesschest.AccessChest;
import ato.accesschest.game.ItemStackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

/**
 * この MOD で追加されるチェストの実体
 * リポジトリに関する機能の実装
 */
public abstract class Repository implements IInventory {

    /**
     * データ管理を行うオブジェクト
     */
    protected DataManager data;
    /**
     * チェストの色
     */
    protected int color;
    /**
     * チェストのランク。"** Chest Class-?" と表現される
     */
    protected int grade;

    public Repository(DataManager data) {
        this.data = data;
    }

    public int getColor() {
        return color;
    }

    public int getGrade() {
        return grade;
    }

    // IInventory の実装

    @Override
    public int getSizeInventory() {
        return (int) (27 * Math.pow(8, grade));
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return data.getItem(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        ItemStack is = data.getItem(index);
        if (is == null) {
            return null;
        } else {
            if (is.stackSize <= amount) {
                data.setItem(index, null);
                return is;
            } else {
                return is.splitStack(amount);
            }
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack is) {
        data.setItem(index, is);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    /**
     * リポジトリ内にアイテムを入れる
     * 引数の ItemStack は関数内部で収納した分だけ減らされる
     */
    public void storeItem(ItemStack itemStack) {
        for (int i = 0; i < getSizeInventory(); ++i) {
            ItemStack stored = data.getItem(i);
            if (stored == null) {
                data.setItem(i, itemStack.splitStack(Math.min(getInventoryStackLimit(), itemStack.stackSize)));
            } else if (ItemStackUtil.canMerge(stored, itemStack)) {
                int trans = Math.max(0,
                        Math.min(Math.min(stored.getMaxStackSize(), getInventoryStackLimit()) - stored.stackSize,
                                itemStack.stackSize));
                stored.stackSize += trans;
                itemStack.stackSize -= trans;
            }
            if (itemStack.stackSize <= 0) return;
        }
    }

    // レポジトリの機能

    /**
     * リポジトリに名前をつける
     */
    public void setName(String name) {
        data.setName(name);
    }

    /**
     * アイテムの ID, ダメージ、優先度などを用いて並び替え
     */
    public void sort() {
        // java.util.Arrays を用いたソートのために、一度すべてのデータを ItemStack[] に変換
        ItemStack[] array = new ItemStack[data.getMaxSize()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = data.getItem(i);
        }
        // ソート＆コンパクトの実行
        Arrays.sort(array, data.getComparator());
        array = compact(array);
        // ソート済み配列を元のデータに格納
        for (int i = 0; i < array.length; ++i) {
            data.setItem(i, array[i]);
        }
    }

    /**
     * 可能な限りスタックして、アイテムスタック数が少なくなるように前に詰める
     * 事前にソートされている必要がある
     */
    private ItemStack[] compact(ItemStack[] array) {
        int count = 0;
        ItemStack[] compact = new ItemStack[array.length];
        for (int i = 0; i < array.length && array[i] != null; ++i) {
            while (array[i] != null) {
                if (compact[count] == null) {
                    compact[count] = array[i];
                    array[i] = null;
                } else if (ItemStackUtil.canMerge(compact[count], array[i])) {
                    int space = compact[count].getMaxStackSize() - compact[count].stackSize;
                    int trans = Math.min(space, array[i].stackSize);
                    compact[count].stackSize += trans;
                    array[i].stackSize -= trans;
                    if (array[i].stackSize == 0) {
                        array[i] = null;
                    }
                    if (compact[count].stackSize == compact[count].getMaxStackSize()) {
                        count++;
                    }
                } else {
                    count++;
                }
            }
        }
        return compact;
    }

    /**
     * 一括排出
     */
    public ItemStack[] eject() {
        ItemStack[] list = new ItemStack[AccessChest.config.ejectStackMaxNum];
        int index = -1;
        for (int i = 0; i < list.length; ++i) {
            while (list[i] == null) {
                list[i] = getStackInSlot(++index);
                if (index >= getSizeInventory()) {
                    return list;
                }
            }
            setInventorySlotContents(index, null);
        }
        return list;
    }
}
