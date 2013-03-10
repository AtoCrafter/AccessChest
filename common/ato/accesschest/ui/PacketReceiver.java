package ato.accesschest.ui;

import ato.accesschest.Properties;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * GUI の同期のためなどに用いられるカスタムパケットの受信を行う
 */
public class PacketReceiver implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(packet.data));
        try {
            Container con = ((EntityPlayer) player).openContainer;
            if (con instanceof ContainerAtoChest) {
                ContainerAtoChest ac = (ContainerAtoChest) con;
                if (Properties.CHANNEL_SCROLL_INDEX.equals(packet.channel)) {
                    receiveScrollIndex(ac, in.readInt());
                } else if (Properties.CHANNEL_FILTER.equals(packet.channel)) {
                    receiveFilter(ac, in.readUTF());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 現在のスクロール位置を受信
     */
    private void receiveScrollIndex(ContainerAtoChest container, int index) {
        container.setScrollIndex(index);
    }

    /**
     * 新しいフィルタを受信
     */
    private void receiveFilter(ContainerAtoChest container, String filter) {
        container.setFilter(filter);
    }
}
