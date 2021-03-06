package ato.accesschest.handler;

import ato.accesschest.Properties;
import ato.accesschest.ui.ContainerAtoChestServer;
import ato.accesschest.ui.PacketReceiverServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 受信したカスタムパケットの処理を行う
 */
public class PacketHandlerServer extends PacketHandler {

    private final PacketReceiverServer ui;

    public PacketHandlerServer() {
        ui = new PacketReceiverServer();
    }

    @Override
    protected void handleCustomPacket(String channel, DataInputStream in, EntityPlayer player) throws IOException {
        Container con = player.openContainer;
        if (con instanceof ContainerAtoChestServer) {
            ContainerAtoChestServer ac = (ContainerAtoChestServer) con;
            if (Properties.CHANNEL_GUI.equals(channel)) {
                ui.receive(in, ac, player);
            }
        }
    }
}
