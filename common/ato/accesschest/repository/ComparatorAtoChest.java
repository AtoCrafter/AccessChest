package ato.accesschest.repository;

import ato.accesschest.Properties;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;


public class ComparatorAtoChest implements Comparator {

    protected HashMap priorityMap;

    public ComparatorAtoChest() {
        super();
        priorityMap = new HashMap<KeyPair, Integer>();
    }

    @Override
    public int compare(Object o1, Object o2) {
        int c = compareWithNull(o1, o2);
        if (c == 0 && o1 != null && o2 != null) {
            ItemStack is1 = (ItemStack) o1;
            ItemStack is2 = (ItemStack) o2;
            c = compareWithPriority(is1, is2);
            if (c == 0) {
                c = compareWithIdAndDamage(is1, is2);
                if (c == 0) {
                    c = compareWithNBTId(is1, is2);
                }
            }
        }
        return c;
    }

    /**
     * 片方、もしくはどちらかが null だっとときの定義
     */
    private int compareWithNull(Object o1, Object o2) {
        int i1 = o1 == null ? 1 : 0;
        int i2 = o2 == null ? 1 : 0;
        return i1 - i2;
    }

    /**
     * 優先度に基づく比較の定義
     */
    private int compareWithPriority(ItemStack is1, ItemStack is2) {
        return getPriority(is1) - getPriority(is2);
    }

    public int getPriority(ItemStack is) {
        Object o = priorityMap.get(new KeyPair(is));
        if (o == null) {
            return Properties.DEFAULT_PRIORITY;
        } else {
            return (Integer) o;
        }
    }

    public void setPriority(ItemStack is, int priority) {
        if (is == null) {
            return;
        }
        KeyPair key = new KeyPair(is);
        if (priority == Properties.DEFAULT_PRIORITY || priority < 0) {
            priorityMap.remove(key);
        } else {
            priorityMap.put(key, priority);
        }
    }

    /**
     * アイテムの ID とダメージによる比較の定義
     */
    protected int compareWithIdAndDamage(ItemStack is1, ItemStack is2) {
        if (is1.itemID == is2.itemID) {
            return is1.getItemDamage() - is2.getItemDamage();
        } else {
            return is1.itemID - is2.itemID;
        }
    }

    /**
     * アイテムの NBT データの内容による比較の定義
     */
    protected int compareWithNBTId(ItemStack is1, ItemStack is2) {
        if (is1.stackTagCompound == null && is2.stackTagCompound == null) {
            return 0;
        } else if (is1.stackTagCompound == null) {
            return -1;
        } else if (is2.stackTagCompound == null) {
            return 1;
        } else {
            return is1.stackTagCompound.getId() - is2.stackTagCompound.getId();
        }
    }

    public void readFromNBT(NBTTagCompound nbttc) {
        NBTTagList list = nbttc.getTagList("Priorities");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound nbtpriority = (NBTTagCompound) list.tagAt(i);
            int id = nbtpriority.getInteger("ID");
            int damage = nbtpriority.getInteger("Damage");
            int priority = nbtpriority.getInteger("Priority");
            setPriority(new ItemStack(id, 0, damage), priority);
        }
    }

    public void writeToNBT(NBTTagCompound nbttc) {
        NBTTagList list = new NBTTagList();
        Set set = priorityMap.entrySet();
        Iterator ite = set.iterator();
        while (ite.hasNext()) {
            Map.Entry entry = (Map.Entry) ite.next();
            NBTTagCompound nbtpriority = new NBTTagCompound();
            nbtpriority.setInteger("ID", ((KeyPair) entry.getKey()).id);
            nbtpriority.setInteger("Damage", ((KeyPair) entry.getKey()).damage);
            nbtpriority.setInteger("Priority", (Integer) entry.getValue());
            list.appendTag(nbtpriority);
        }
        nbttc.setTag("Priorities", list);
    }

    /**
     * 同じ種類のアイテムを一意に識別するためにキーとなるオブジェクト
     */
    private class KeyPair {
        private int id;
        private int damage;

        public KeyPair(ItemStack is) {
            if (is == null) {
                this.id = -1;
                this.damage = -1;
            } else {
                this.id = is.itemID;
                // 武器や鎧などの耐久力があるやつは全て同じ種類とする
                if (is.isItemStackDamageable()) {
                    this.damage = 0;
                } else {
                    this.damage = is.getItemDamage();
                }
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + damage;
            result = prime * result + id;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            KeyPair other = (KeyPair) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (damage != other.damage)
                return false;
            if (id != other.id)
                return false;
            return true;
        }

        private ComparatorAtoChest getOuterType() {
            return ComparatorAtoChest.this;
        }
    }
}
