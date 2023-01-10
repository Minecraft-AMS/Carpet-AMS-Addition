package club.mcams.carpet;

import club.mcams.carpet.util.AutoMixinAuditExecutor.AutoMixinAuditExecutor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class AmsServerMod implements ModInitializer {

    private static final String MOD_ID = "carpet-ams-addition";
    private static String version;

    @Override
    public void onInitialize() {
        version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
        AutoMixinAuditExecutor.run();
        AmsServer.init();
    }
    public static String getModId()
    {
        return MOD_ID;
    }

    public static String getVersion()
    {
        return version;
    }
}
