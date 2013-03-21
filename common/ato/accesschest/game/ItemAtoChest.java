package ato.accesschest.game;

import ato.accesschest.AccessChest;
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
        boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        ((TileEntityAtoChest) world.getBlockTileEntity(x, y, z)).setColorAndGrade(
                AccessChest.id2color(stack.getItemDamage()),
                AccessChest.id2grade(stack.getItemDamage()),
                AccessChest.id2isOriginal(stack.getItemDamage())
        );
        return ret;
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List info, boolean par4) {
        String str = "Class " + AccessChest.id2grade(is.getItemDamage());
        if (!AccessChest.id2isOriginal(is.getItemDamage())) {
            str += " (Copy)";
        }
        info.add("\u00a77" + str);
    }
}
