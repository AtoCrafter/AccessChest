package ato.accesschest.game;

import ato.accesschest.AccessChest;
import ato.accesschest.repository.RepositoryAccessChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

/**
 * ゲーム内での Access Chest アイテム
 */
public class ItemAccessChest extends ItemAtoChest {

    public ItemAccessChest(int id) {
        super(id);
        setItemName("accesschest");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (!world.isRemote && !player.isSneaking()) {
            player.openGui(
                    AccessChest.instance,
                    is.getItemDamage(),
                    world, 0, 0, 0);
        }
        return is;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world,
                                  int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof IInventory) {
            return true;
        } else {
            return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world,
                             int x, int y, int z, int side, float par8, float par9, float par10) {
        if (player.isSneaking()) {
            return super.onItemUse(is, player, world, x, y, z, side, par8, par9, par10);
        } else {
            return false;
        }
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (player.isSneaking()) {
            return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        } else {
            return false;
        }
    }

    @Override
    public String getItemDisplayName(ItemStack is) {
        String name = (new RepositoryAccessChest(
                AccessChest.id2color(is.getItemDamage()),
                AccessChest.id2grade(is.getItemDamage())
        )).getName();
        if (name != null && !name.equals("")) {
            return name;
        } else {
            return super.getItemDisplayName(is);
        }
    }

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list) {
        list.add(new ItemStack(id, 1, 0x0F));
        list.add(new ItemStack(id, 1, 0x1F));
        list.add(new ItemStack(id, 1, 0x2F));
        list.add(new ItemStack(id, 1, 0x3F));
    }
}
