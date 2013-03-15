package ato.accesschest.ui;

import ato.accesschest.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * GUI の同期のためなどに用いられるカスタムパケットの送信を行う
 */
public class PacketSenderClient {

    /**
     * パケット送信のために用いる、Minecraft オブジェクト
     */
    private Minecraft mc;

    /**
     * Minecraft オブジェクトの登録
     */
    public void setMinecraft(Minecraft mc) {
        this.mc = mc;
    }

    /**
     * 現在のスクロール位置をパケットにして送信する
     */
    public void sendScrollIndex(final int index) {
        sendPacket(new IDataWriter() {
            @Override
            public void writeData(DataOutputStream out) throws IOException {
                out.writeInt(Properties.GUI_SCROLL_INDEX);
                out.writeInt(index);
            }
        });
    }

    /**
     * 検索用フィルターの送信
     */
    public void sendFilter(final String filter) {
        sendPacket(new IDataWriter() {
            @Override
            public void writeData(DataOutputStream out) throws IOException {
                out.writeInt(Properties.GUI_FILTER);
                out.writeUTF(filter);
            }
        });
    }

    /**
     * ソートの送信
     */
    public void sendSort() {
        sendPacket(new IDataWriter() {
            @Override
            public void writeData(DataOutputStream out) throws IOException {
                out.writeInt(Properties.GUI_SORT);
            }
        });
    }

    /**
     * 一括排出の送信
     */
    public void sendEject() {
        sendPacket(new IDataWriter() {
            @Override
            public void writeData(DataOutputStream out) throws IOException {
                out.writeInt(Properties.GUI_EJECT);
            }
        });
    }

    /**
     * 一括収納（インベントリ）の送信
     */
    public void sendStoreInventory() {
        sendPacket(new IDataWriter() {
            @Override
            public void writeData(DataOutputStream out) throws IOException {
                out.writeInt(Properties.GUI_STORE_INVENTORY);
            }
        });
    }

    /**
     * 一括収納（アイテム欄）の送信
     */
    public void sendStoreEquipment() {
        sendPacket(new IDataWriter() {
            @Override
            public void writeData(DataOutputStream out) throws IOException {
                out.writeInt(Properties.GUI_STORE_EQUIPMENT);
            }
        });
    }

    /**
     * パケットを生成し、送信する
     *
     * @param writer パケットに何を書き込むか指定するクラス
     */
    private void sendPacket(IDataWriter writer) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(data);
        try {
            writer.writeData(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mc.getSendQueue().addToSendQueue(new Packet250CustomPayload(Properties.CHANNEL_GUI, data.toByteArray()));
    }

    /**
     * パケットに何を書き込むか決める
     */
    private interface IDataWriter {
        /**
         * パケットに書き込む処理
         *
         * @param out 書き込み先
         */
        public void writeData(DataOutputStream out) throws IOException;
    }
}
