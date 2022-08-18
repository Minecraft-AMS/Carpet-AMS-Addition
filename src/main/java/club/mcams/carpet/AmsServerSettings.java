package club.mcams.carpet;

import carpet.settings.Rule;
import static carpet.settings.RuleCategory.*;

/**
 * Here is your example Settings class you can plug to use carpetmod /carpet settings command
 */
public class AmsServerSettings
{
    public static final String AMS="AMS";

    @Rule(
            desc ="音符盒被触发时加载附近区块 3x3 15s",
            extra = {"noteblock chunk loader"},
            category = {AMS,FEATURE}
    )
    public static boolean NoteBlockChunkLoader = false;

    @Rule(
            desc = "让弓可以同时拥有无限与经验修补",
            category = {AMS,FEATURE,SURVIVAL}
    )
    public static boolean InfinityEanchantedBow = false;

    @Rule(
            desc = "恢复0t仙人掌催熟机制，像1.15一样",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean Cactusfarm = false;
}
