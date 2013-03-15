package ato.accesschest.game;

import ato.accesschest.AccessChest;
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
        if (!world.isRemote && !player.isSneaking()) {
            player.openGui(
                    AccessChest.instance,
                    is.getItemDamage(),
                    world, 0, 0, 0);
        }
        return is;
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world,
                             int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (player.isSneaking()) {
            return super.onItemUse(is, player, world, par4, par5, par6, par7, par8, par9, par10);
        } else {
            return false;
        }
    }

    @Override
    public String getItemDisplayName(ItemStack is) {
        String str = super.getItemDisplayName(is);
//        Repository repo = new RepositoryAccessChest(
//                AccessChest.id2color(is.getItemDamage()),
//                AccessChest.id2grade(is.getItemDamage())
//        );
//        if ( Utils.getInstance().isSingleplay() ) {
//            String name = AccessChestManager.name.getChestName("", getColor(is.getItemDamage()));
//            if ( !name.equals("") ) {
//                str += " (" + name + ")";
//            }
//        }
        return str;
    }
}
