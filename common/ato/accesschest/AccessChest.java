package ato.accesschest;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.event.FMLInitializationEvent;

import ato.accesschest.initializer.ProxyCommon;

@Mod(
        modid = "AccessChest",
        name = Properties.MOD_NAME,
        version = Properties.VERSION
)
@NetworkMod(
        channels = {Properties.NET_CHANNEL_NAME},
        clientSideRequired = true,
        serverSideRequired = true
)
public class AccessChest {

    @Instance("AccessChest")
    public static AccessChest instance;
    @SidedProxy(clientSide = "ato.accesschest.initializer.ProxyClient", serverSide = "ato.accesschest.initializer.ProxyCommon")
    public static ProxyCommon initalizer;

    @Init
    public void load(FMLInitializationEvent event) {
        initalizer.init();
    }
}
