package ato.accesschest.ui;

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
    public void receive(ContainerAtoChestServer container, DataInputStream in) throws IOException {
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
                // TODO
                break;
            default:
                throw new RuntimeException("unexpected value for GUI Packet type");
        }
    }
}
