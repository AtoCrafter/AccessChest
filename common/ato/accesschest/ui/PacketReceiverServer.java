package ato.accesschest.ui;

import ato.accesschest.Properties;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * GUI の同期のためなどに用いられるカスタムパケットの受信を行う
 */
public class PacketReceiverServer {

    /**
     * データ受信時に呼ばれる
     */
    public void receive(ContainerAtoChestServer container, DataInputStream in) throws IOException {
        switch (in.readInt()) {
            case Properties.GUI_SCROLL_INDEX:
                container.setScrollIndex(in.readInt());
                break;
            case Properties.GUI_FILTER:
                container.setFilter(in.readUTF());
                break;
            case Properties.GUI_SORT:
                container.sort();
                break;
            default:
                throw new RuntimeException("unexpected value for GUI Packet type");
        }
    }
}
