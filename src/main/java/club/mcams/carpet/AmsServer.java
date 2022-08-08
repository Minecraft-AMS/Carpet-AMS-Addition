
package club.mcams.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.util.math.ChunkPos;
import java.util.Comparator;

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
