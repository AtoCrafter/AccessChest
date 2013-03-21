package ato.accesschest.game;

import ato.accesschest.AccessChest;
import ato.accesschest.repository.RepositoryAccessChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

import java.util.List;

/**
 * ゲーム内での Access Chest アイテム
 */
public class ItemAccessChest extends ItemAtoChest {

    /**
     * 一括転送を使用しても良いかどうか
     */
    public static boolean canTransfer;

    public ItemAccessChest(int id) {
        super(id);
        setItemName("accesschest");
    }

    // 右クリック関係

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
        if (!world.isRemote && tileEntity instanceof IInventory && canTransfer) {
            RepositoryAccessChest repo = new RepositoryAccessChest(
                    AccessChest.id2color(stack.getItemDamage()),
                    AccessChest.id2grade(stack.getItemDamage()),
                    AccessChest.id2isOriginal(stack.getItemDamage())
            );

            IInventory inv = (IInventory) tileEntity;
            // ラージチェストを考慮
            if (tileEntity instanceof TileEntityChest) {
                for (int d = 1; d <= 7; d += 2) {
                    int nx = d / 3 - 1;
                    int nz = d % 3 - 1;
                    TileEntity chest2 = world.getBlockTileEntity(x + nx, y, z + nz);
                    if (chest2 instanceof TileEntityChest) {
                        inv = new InventoryLargeChest("", inv, (TileEntityChest) chest2);
                        break;
                    }
                }
            }

            if (player.isSneaking()) {
                repo.pourInventory(inv);
            } else {
                repo.extractInventory(inv);
            }
        }
        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
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
        if (player.isSneaking() && !canTransfer) {
            return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        } else {
            return false;
        }
    }

    // 表示関係

    @Override
    public String getItemDisplayName(ItemStack is) {
        String name = (new RepositoryAccessChest(
                AccessChest.id2color(is.getItemDamage()),
                AccessChest.id2grade(is.getItemDamage()),
                AccessChest.id2isOriginal(is.getItemDamage())
        )).getName();
        if (name != null && !name.equals("")) {
            return name;
        } else {
            return super.getItemDisplayName(is);
        }
    }

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list) {
        list.add(new ItemStack(id, 1, AccessChest.colorgrade2id(15, 0, true)));
        list.add(new ItemStack(id, 1, AccessChest.colorgrade2id(15, 1, true)));
        list.add(new ItemStack(id, 1, AccessChest.colorgrade2id(15, 2, true)));
        list.add(new ItemStack(id, 1, AccessChest.colorgrade2id(15, 3, true)));
        list.add(new ItemStack(id, 1, AccessChest.colorgrade2id(15, 0, false)));
        list.add(new ItemStack(id, 1, AccessChest.colorgrade2id(15, 1, false)));
        list.add(new ItemStack(id, 1, AccessChest.colorgrade2id(15, 2, false)));
        list.add(new ItemStack(id, 1, AccessChest.colorgrade2id(15, 3, false)));
    }
}
