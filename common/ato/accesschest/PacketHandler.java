package ato.accesschest;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 受信したカスタムパケットの処理を行う
 */
public abstract class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(packet.data));
        try {
            handleCustomPacket(packet.channel, in, (EntityPlayer) player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void handleCustomPacket(String channel, DataInputStream in, EntityPlayer player) throws IOException;
}
