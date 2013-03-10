package ato.accesschest.ui;

import ato.accesschest.Properties;
import ato.accesschest.repository.RepositoryAccessChest;
import ato.accesschest.repository.RepositoryDammy;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * この MOD で使いされるチェストの GUI を呼び出すハンドラ
 */
public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
//        if (id == Properties.GUI_ID_CHEST) {
//            TileEntityAccessChest chest = (TileEntityAccessChest) world.getBlockTileEntity(x, y, z);
//            InventoryAccessChest inventory = AccessChestManager.inventory.getInventoryAccessChest(player.username, chest.getColor());
//            return new ContainerAccessChest(inventory, player.inventory, chest);
//        } else if (id == ConfigValues.containerCCID) {
//            TileEntityCompressedChest chest = (TileEntityCompressedChest) world.getBlockTileEntity(x, y, z);
//            return new ContainerAccessChest(chest, player.inventory, chest);
//        } else if (id == ConfigValues.containerItemACID) {
//            ItemStack is = player.getCurrentEquippedItem();
//            ItemAbstChest item = (ItemAbstChest) Item.itemsList[ConfigValues.blockAccessChestID];
//            int color = item.getColor(is.getItemDamage());
//            int grade = item.getGrade(is.getItemDamage());
//            return new ContainerAccessChest(AccessChestManager.inventory.getInventoryAccessChest(player.username, color), grade, player.inventory);
//        }
        if (id == Properties.GUI_ID_CHEST) { // test
            return new ContainerAtoChest(new RepositoryAccessChest(0, 2), player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
//        if (id == ConfigValues.containerACID || id == ConfigValues.containerCCID || id == ConfigValues.containerItemACID) {
//            ContainerAccessChestSlave con = new ContainerAccessChestSlave(player.inventory);
//            return new GuiAccessChest(con);
//        }
        if (id == Properties.GUI_ID_CHEST) { // test
            return new GuiAtoChest(new ContainerAtoChest(new RepositoryDammy(2), player.inventory));
        }
        return null;
    }
}
