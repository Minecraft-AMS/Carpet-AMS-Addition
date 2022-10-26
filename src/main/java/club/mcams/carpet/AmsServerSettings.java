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
            category = {AMS,FEATURE}
    )
    public static boolean SuperBow = false;

    @Rule(
            desc = "0t仙人掌催熟",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean zeroTickCactus = false;

    @Rule(
            desc = "0t竹子催熟",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean zeroTickBamboo = false;

    @Rule(
            desc = "0t紫颂花催熟",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean zeroTickChorusFlower = false;

    @Rule(
            desc = "0t甘蔗催熟",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean zeroTickSugarCane = false;

    @Rule(
            desc = "0t海带、缠怨藤、垂泪藤",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean zeroTickStem = false;

    @Rule(
            desc = "0t催熟总开关",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean zeroTickAllPlants = false;

    @Rule(
            desc = "末影龙复活过程检测优化",
            category = {AMS, OPTIMIZATION}
    )
    public static boolean optimizationDragonRespawn = false;
}
