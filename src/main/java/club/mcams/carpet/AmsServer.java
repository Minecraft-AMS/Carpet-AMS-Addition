
package club.mcams.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import net.fabricmc.api.ModInitializer;

public class AmsServer implements CarpetExtension, ModInitializer
{
    @Override
    public String version()
    {
        return "carpet-extra";
    }

    public static void loadExtension()
    {
        CarpetServer.manageExtension(new AmsServer());
    }

    @Override
    public void onInitialize()
    {
        AmsServer.loadExtension();
    }

    @Override
    public void onGameStarted()
    {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(AmsServerSettings.class);
    }
}
