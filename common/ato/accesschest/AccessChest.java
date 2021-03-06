package ato.accesschest;

import ato.accesschest.handler.PacketHandlerClient;
import ato.accesschest.handler.PacketHandlerServer;
import ato.accesschest.initializer.ProxyCommon;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(
        modid = Properties.MOD_ID,
        name = Properties.MOD_NAME,
        version = Properties.VERSION
)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = true,
        serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(
                channels = {Properties.CHANNEL_GUI},
                packetHandler = PacketHandlerServer.class
        ),
        clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(
                channels = {Properties.CHANNEL_TILEENTITY},
                packetHandler = PacketHandlerClient.class
        )
)
public class AccessChest {

    @Instance(Properties.MOD_ID)
    public static AccessChest instance;
    @SidedProxy(clientSide = "ato.accesschest.initializer.ProxyClient", serverSide = "ato.accesschest.initializer.ProxyCommon")
    public static ProxyCommon initalizer;
    public static Config config;

    /**
     * Access Chest の色とクラスから GUI の ID を算出する
     */
    public static int colorgrade2id(int color, int grade) {
        return colorgrade2id(color, grade, true);
    }

    public static int colorgrade2id(int color, int grade, boolean isOriginal) {
        return ((isOriginal ? 0 : 1) << 6) + ((grade & 3) << 4) + (color & 0xF);
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

    /**
     * Access Chest がコピーされたものか GUI の ID から算出する
     */
    public static boolean id2isOriginal(int id) {
        return (id & 0x40) == 0;
    }

    @Mod.EventHandler
    public void preLoad(FMLPreInitializationEvent event) {
        config = new Config(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        initalizer.init();
    }
}
