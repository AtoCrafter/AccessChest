package ato.accesschest.ui;

import ato.accesschest.AccessChest;
import ato.accesschest.Properties;
import ato.accesschest.game.TileEntityAtoChest;
import ato.accesschest.repository.RepositoryAccessChest;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * この MOD で使いされるチェストの GUI を呼び出すハンドラ
 */
public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (0 <= id && id < 16 * 4 * 2) {
            return new ContainerAtoChestServer(new RepositoryAccessChest(
                    AccessChest.id2color(id), AccessChest.id2grade(id), false
            ), player.inventory);
        } else if (id == Properties.GUI_ID_TILEENTITY) {
            TileEntityAtoChest tileEntity = (TileEntityAtoChest) world.getBlockTileEntity(x, y, z);
            return new ContainerAtoChestServer(tileEntity, player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (0 <= id && id < 16 * 4 * 2 || id == Properties.GUI_ID_TILEENTITY) {
            return new GuiAtoChest(new ContainerAtoChestClient(player.inventory));
        }
        return null;
    }
}
