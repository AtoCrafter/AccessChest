package ato.accesschest.ui;

import net.minecraft.entity.player.EntityPlayer;

import java.io.DataInputStream;
import java.io.IOException;

import static ato.accesschest.Properties.*;

/**
 * GUI の同期のためなどに用いられるカスタムパケットの受信を行う
 */
public class PacketReceiverServer {

    /**
     * データ受信時に呼ばれる
     */
    public void receive(DataInputStream in, ContainerAtoChestServer container, EntityPlayer player) throws IOException {
        switch (in.readInt()) {
            case GUI_SCROLL_INDEX:
                container.setScrollIndex(in.readInt());
                break;
            case GUI_FILTER:
                container.setFilter(in.readUTF());
                break;
            case GUI_SORT:
                container.sort();
                break;
            case GUI_EJECT:
                container.eject(player);
                break;
            case GUI_STORE_INVENTORY:
                container.storeInventory();
                break;
            case GUI_STORE_EQUIPMENT:
                container.storeEquipment();
                break;
            case GUI_RENAME:
                container.setName(in.readUTF());
                break;
            case GUI_CUSTOM_SORT:
                container.setPriorities(in.readInt());
                break;
            default:
                throw new RuntimeException("unexpected value for GUI Packet type");
        }
    }
}
