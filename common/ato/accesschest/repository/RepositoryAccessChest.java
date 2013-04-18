package ato.accesschest.repository;

import ato.accesschest.game.TileEntityAccessChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Access Chest の実体
 */
public class RepositoryAccessChest extends Repository {

    public RepositoryAccessChest(World world, int color, int grade, boolean isOriginal) {
        super((DataManagerNBT)world.loadItemData(DataManagerNBT.class, "AccessChest-" + color),
                color, grade, isOriginal);
    }

    @Override
    public String getInvName() {
        String str = "Access Chest";
//        String name = AccessChestManager.name.getChestName(ownerName, colorNumber);
//        if ( !name.equals("") ) str += name;
        return str;
    }

    /**
     * 別のインベントリから中身を取り込む
     */
    public void extractInventory(IInventory inv) {
        // Access Chest を対象とした場合、ループが発生するので同じ色の場合は処理しない
        if (isTheSameColoredAccessChest(inv)) return;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack is = inv.getStackInSlot(i);
            if (is != null) {
                int prevSize = is.stackSize;
                storeItem(is);
                if (prevSize == is.stackSize) return;
                if (is.stackSize == 0) inv.setInventorySlotContents(i, null);
            }
        }
    }

    /**
     * 別のインベントリに中身を移す
     */
    public void pourInventory(IInventory inv) {
        // Access Chest を対象とした場合、ループが発生するので同じ色の場合は処理しない
        if (isTheSameColoredAccessChest(inv)) return;

        int index = 0;
        for (int i = 0; i < inv.getSizeInventory() && i < getSizeInventory(); ++i) {
            if (inv.getStackInSlot(i) == null) {
                ItemStack is = null;
                while (is == null) {
                    if (index >= getSizeInventory()) return;
                    is = getStackInSlot(index);
                    setInventorySlotContents(index, null);
                    ++index;
                }
                inv.setInventorySlotContents(i, is);
            }
        }
    }

    /**
     * 対象のインベントリと同じ色か？
     */
    public boolean isTheSameColoredAccessChest(IInventory inv) {
        if (inv instanceof TileEntityAccessChest) {
            return color == ((TileEntityAccessChest) inv).getRepository().getColor();
        } else if (inv instanceof RepositoryAccessChest) {
            return color == ((RepositoryAccessChest) inv).getColor();
        } else {
            return false;
        }
    }
}
