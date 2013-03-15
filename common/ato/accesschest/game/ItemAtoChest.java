package ato.accesschest.game;

import ato.accesschest.AccessChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * この MOD で追加されるゲーム内のアイテムの抽象クラス
 */
public abstract class ItemAtoChest extends ItemBlock {

    public ItemAtoChest(int id) {
        super(id);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
                                float hitX, float hitY, float hitZ, int metadata) {
        if (player.isSneaking()) {
            boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
            ((TileEntityAtoChest) world.getBlockTileEntity(x, y, z)).setColorAndGrade(
                    AccessChest.id2color(stack.getItemDamage()),
                    AccessChest.id2grade(stack.getItemDamage())
            );
            return ret;
        } else {
            return false;
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
