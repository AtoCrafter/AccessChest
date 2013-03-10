package ato.accesschest;

import ato.accesschest.ui.PacketReceiverServer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.event.FMLInitializationEvent;

import ato.accesschest.initializer.ProxyCommon;

@Mod(
        modid = Properties.MOD_ID,
        name = Properties.MOD_NAME,
        version = Properties.VERSION
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = true,
        serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(
                channels = {Properties.CHANNEL_SCROLL_INDEX, Properties.CHANNEL_FILTER},
                packetHandler = PacketReceiverServer.class
        )
)
public class AccessChest {

    @Instance(Properties.MOD_ID)
    public static AccessChest instance;
    @SidedProxy(clientSide = "ato.accesschest.initializer.ProxyClient", serverSide = "ato.accesschest.initializer.ProxyCommon")
    public static ProxyCommon initalizer;

    @Init
    public void load(FMLInitializationEvent event) {
        initalizer.init();
    }

    /**
     * Access Chest の色とクラスから GUI の ID を算出する
     */
    public static int colorgrade2id(int color, int grade) {
        return (grade << 4) + color;
    }

    /**
     * Access Chest の色を GUI の ID から算出する
     */
    public static int id2color(int id) {
        int color = id & 0xF;
        return color;
    }

    /**
     * Access Chest のクラスを GUI の ID から算出する
     */
    public static int id2grade(int id) {
        int grade = (id & 0x30) >> 4;
        return grade;
    }
}
