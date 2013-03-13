package ato.accesschest.ui;

/**
 * GUI の同期のためなどに用いられるカスタムパケットの受信を行う
 */
public class PacketReceiverServer {

    /**
     * 現在のスクロール位置を受信
     */
    public void receiveScrollIndex(ContainerAtoChestServer container, int index) {
        container.setScrollIndex(index);
    }

    /**
     * 新しいフィルタを受信
     */
    public void receiveFilter(ContainerAtoChestServer container, String filter) {
        container.setFilter(filter);
    }

    /**
     * ソートの受信
     */
    public void receiveSort(ContainerAtoChestServer container) {
        container.sort();
    }
}
