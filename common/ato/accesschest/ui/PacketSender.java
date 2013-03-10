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
public class PacketSender {

    /**
     * パケット送信のために用いる、Minecraft オブジェクト
     */
    private Minecraft mc;

    /**
     * 現在のスクロール位置をパケットにして送信する
     */
    public void sendScrollIndex(Minecraft mc, final int index) {
        this.mc = mc;
        sendPacket(new IDataWriter() {
            @Override
            public void writeData(DataOutputStream out) throws IOException {
                out.writeInt(index);
            }
        });
    }

    /**
     * 検索用フィルターの送信
     */
    public void sendFilter(Minecraft mc, final String filter) {
        this.mc = mc;
        sendPacket(new IDataWriter() {
            @Override
            public void writeData(DataOutputStream out) throws IOException {
                out.writeUTF(filter);
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
        mc.getSendQueue().addToSendQueue(new Packet250CustomPayload(Properties.CHANNEL_SCROLL_INDEX, data.toByteArray()));
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
