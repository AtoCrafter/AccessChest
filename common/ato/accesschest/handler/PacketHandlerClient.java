package ato.accesschest.handler;

import ato.accesschest.Properties;
import ato.accesschest.game.TileEntityAtoChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 受信したカスタムパケットの処理を行う
 */
public class PacketHandlerClient extends PacketHandler {

    @Override
    protected void handleCustomPacket(String channel, DataInputStream in, EntityPlayer player) throws IOException {
        if (Properties.CHANNEL_TILEENTITY.equals(channel)) {
            int x = in.readInt();
            int y = in.readInt();
            int z = in.readInt();
            int color = in.readByte();
            int grade = in.readByte();

            World world = player.worldObj;
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if (tileEntity instanceof TileEntityAtoChest) {
                ((TileEntityAtoChest) tileEntity).setColorAndGrade(color, grade);
            }
        }
    }
}
