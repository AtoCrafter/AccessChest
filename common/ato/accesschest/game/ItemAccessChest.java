package ato.accesschest.game;

import ato.accesschest.AccessChest;
import ato.accesschest.Properties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
        if ( !world.isRemote && !player.isSneaking() ) {
            player.openGui(AccessChest.instance, Properties.GUI_ID_CHEST, world, 0, 0, 0);
        }
        return is;
    }

//    @Override
//    public String getItemDisplayName(ItemStack is) {
//        String str = super.getItemDisplayName(is);
//        str += " Class-" + getGrade(is.getItemDamage());
//        if ( Utils.getInstance().isSingleplay() ) {
//            String name = AccessChestManager.name.getChestName("", getColor(is.getItemDamage()));
//            if ( !name.equals("") ) {
//                str += " (" + name + ")";
//            }
//        }
//        return str;
//    }
//
//    @Override
//    protected void linkAbstChest(TileEntityAbstChest tileEntity, EntityPlayer player, int color,
//                                 int grade, boolean isRemote) {
//        ((TileEntityAccessChest)tileEntity).linkAccessChest(player, color, grade, isRemote);
//    }
}
