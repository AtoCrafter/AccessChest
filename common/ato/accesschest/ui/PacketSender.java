package ato.accesschest.ui;

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

    public PacketSender(Minecraft mc) {
        this.mc = mc;
    }

    /**
     * 現在のスクロール位置をパケットにして送信する
     */
    public void sendScrollIndex(final int index) {
        sendPacket(new IDataWriter() {
            @Override
            public void writeData(DataOutputStream out) throws IOException {
                out.writeInt(index);
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
        mc.getSendQueue().addToSendQueue(new Packet250CustomPayload("ato.accesschest|scrollindex", data.toByteArray()));
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
