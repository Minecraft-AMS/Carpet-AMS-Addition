package club.mcams.carpet;

import club.mcams.carpet.util.recipes.CraftingRule;
import static club.mcams.carpet.settings.AmsRuleCategory.*;
//#if MC<11900
import carpet.settings.Rule;
import com.sun.jna.WString;
//#else
//$$ import carpet.api.settings.Rule;
//#endif

/**
 * Here is your example Settings class you can plug to use carpetmod /carpet settings command
 */

public class AmsServerSettings {

    @Rule(
            //#if MC<11900
            desc = "Enabling making super bows with both infinite and mending enchants",
            category = {AMS, FEATURE}
            //#else
            //$$ categories = {AMS, FEATURE}
            //#endif
    )
    public static boolean superBow = false;

    @Rule(
            //#if MC<11900
            desc = "Make cactus accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean scheduledRandomTickCactus = false;

    @Rule(
            //#if MC<11900
            desc = "Make bamboo accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean scheduledRandomTickBamboo = false;

    @Rule(
            //#if MC<11900
            desc = "Make chorus flower accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean scheduledRandomTickChorusFlower = false;

    @Rule(
            //#if MC<11900
            desc = "Make sugar cane accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean scheduledRandomTickSugarCane = false;

    @Rule(
            //#if MC<11900
            desc = "Make stems accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean scheduledRandomTickStem = false;

    @Rule(
            //#if MC<11900
            desc = "Make all plants accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean scheduledRandomTickAllPlants = false;

    @Rule(
            //#if MC<11900
            desc = "Optimize dragon respawning",
            category = {AMS, OPTIMIZATION}
            //#else
            //$$ categories = {AMS, OPTIMIZATION}
            //#endif
    )
    public static boolean optimizedDragonRespawn = false;

    @Rule(
            options = {"bone_block", "wither_skeleton_skull", "note_block", "OFF"},
            //#if MC<11900
            desc = "Load nearby 3x3 chunks for 15 seconds when a note block is triggered",
            extra = {
                    "[bone_block] - When bone_block is on the note_block",
                    "[wither_skeleton_skull] - When wither_skeleton_skull is on the note_block, either placed on the note block or hanging on the wall",
                    "[note_block] - Only note_block",
                    "[OFF] - Disable the rule"
            },
            category = {AMS, FEATURE, AMS_CHUNKLOADER}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_CHUNKLOADER}
            //#endif
    )
    public static String noteBlockChunkLoader = "OFF";

    @Rule(
            options = {"bone_block", "bedrock", "OFF"},
            //#if MC<11900
            desc = "Load nearby 3x3 chunks for 15 seconds when a piston is triggered",
            extra = {
                    "[bone_block] - When bone_block is on the piston",
                    "[bedrock] - When bedrock is under the piston",
                    "[OFF] - Disable the rule"
            },
            category = {AMS, FEATURE, AMS_CHUNKLOADER}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_CHUNKLOADER}
            //#endif
    )
    public static String pistonBlockChunkLoader = "OFF";

    @Rule(
            //#if MC<11900
            desc = "Load nearby 3x3 chunks for 15 seconds when a bell is triggered",
            category = {AMS, FEATURE, AMS_CHUNKLOADER}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_CHUNKLOADER}
            //#endif
    )
    public static boolean bellBlockChunkLoader = false;

    @Rule(
            //#if MC<11900
            desc = "Control chunk loading for players at any gamemodes",
            category = {AMS, COMMAND, AMS_CHUNKLOADER}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_CHUNKLOADER}
            //#endif
    )
    public static String commandChunkLoading = "false";

    @Rule(
            //#if MC<11900
            desc = "Players can use water buckets to place water in nether",
            category = {AMS, FEATURE}
            //#else
            //$$ categories = {AMS, FEATURE}
            //#endif
    )
    public static boolean netherWaterPlacement = false;

    //#if MC>=11700
    @Rule(
            //#if MC<11900
            desc = "Change the hardness of deepslate to stone",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean breakableDeepslate = false;
    //#endif

    @Rule(
            //#if MC<11900
            desc = "Players can use diamond_pickaxe or netherite_pickaxe to dig bedrock",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean canBreakBedRock = false;

    @Rule(
            //#if MC<11900
            desc = "Players can use diamond_pickaxe or netherite_pickaxe to dig end_portal_frame",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean canBreakEndPortalFrame = false;

    @Rule(
            //#if MC<11900
            desc = "Change the hardness of obsdian to deepslate",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean breakableObsidian = false;

    @Rule(
            //#if MC<11900
            desc = "Set all blocks BlastResistance to 0",
            category = {AMS, FEATURE, SURVIVAL, TNT}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL, TNT}
            //#endif
    )
    public static boolean destroysEverything = false;

    @Rule(
            //#if MC<11900
            desc = "Share villagers discount to all players",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean sharedVillagerDiscounts = false;

    @Rule(
            //#if MC<11900
            desc = "Simulation fake Peace",
            category = {AMS, FEATURE}
            //#else
            //$$ categories = {AMS, FEATURE}
            //#endif
    )
    public static boolean fakePeace = false;

    @Rule(
            //#if MC<11900
            desc = "The campfire is extinguished when the player places it",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean extinguishedCampfire = false;

    @Rule(
            //#if MC<11900
            desc = "players don't get hurt by flying into walls",
            category = {AMS, FEATURE, SURVIVAL}
            //#else
            //$$ categories = {AMS, FEATURE, SURVIVAL}
            //#endif
    )
    public static boolean safeFlight = false;

    //#if MC<11900
    @Rule(
            desc = "Let the bone_block be update suppressor",
            category = {AMS, FEATURE}
    )
    public static boolean boneBlockUpdateSuppressor = false;
    //#endif

    /**
     * 可移动方块规则
     */
    @Rule(
            //#if MC<11900
            desc = "Makes ender_chests movable",
            category = {AMS, FEATURE, AMS_MOVABLE}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_MOVABLE}
            //#endif
    )
    public static boolean movableEnderChest = false;

    @Rule(
            //#if MC<11900
            desc = "Makes end_portal_frame movable",
            category = {AMS, FEATURE, AMS_MOVABLE}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_MOVABLE}
            //#endif
    )
    public static boolean movableEndPortalFrame = false;

    @Rule(
            //#if MC<11900
            desc = "Makes obsidian movable",
            category = {AMS, FEATURE, AMS_MOVABLE}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_MOVABLE}
            //#endif
    )
    public static boolean movableObsidian = false;

    @Rule(
            //#if MC<11900
            desc = "Makes crying_obsidian movable",
            category = {AMS, FEATURE, AMS_MOVABLE}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_MOVABLE}
            //#endif
    )
    public static boolean movableCryingObsidian = false;

    @Rule(
            //#if MC<11900
            desc = "Makes bedrock movable",
            category = {AMS, FEATURE, AMS_MOVABLE}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_MOVABLE}
            //#endif
    )
    public static boolean movableBedRock = false;

    @Rule(
            //#if MC<11900
            desc = "Makes enchanting_table movable",
            category = {AMS, FEATURE, AMS_MOVABLE}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_MOVABLE}
            //#endif
    )
    public static boolean movableEnchantingTable = false;

    @Rule(
            //#if MC<11900
            desc = "Makes beacon movable",
            category = {AMS, FEATURE, AMS_MOVABLE}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_MOVABLE}
            //#endif
    )
    public static boolean movableBeacon = false;

    //#if MC>=11900
    //$$ @Rule(categories = {AMS, FEATURE, AMS_MOVABLE})
    //$$ public static boolean movableReinforcedDeepslate = false;
    //#endif

    @Rule(
            //#if MC<11900
            desc = "Makes anvil movable",
            category = {AMS, FEATURE, AMS_MOVABLE}
            //#else
            //$$ categories = {AMS, FEATURE, AMS_MOVABLE}
            //#endif
    )
    public static boolean movableAnvil = false;

    /**
     * 合成表规则
     */
    @CraftingRule(recipes = "enchanted_golden_apples.json")
    @Rule(
            //#if MC<11900
            desc = "Enchanted Golden Apples can be crafted with 8 Gold Blocks again",
            category = {CRAFTING, SURVIVAL, AMS}
            //#else
            //$$ categories = {CRAFTING, SURVIVAL, AMS}
            //#endif
    )

    public static boolean craftableEnchantedGoldenApples = false;

    //#if MC>=11700
    @CraftingRule(recipes = "bundle.json")
    @Rule(
            //#if MC<11900
            desc = "crafted bundle in minecraft 1.17/1.18/1.19",
            category = {CRAFTING, SURVIVAL, AMS}
            //#else
            //$$ categories = {CRAFTING, SURVIVAL, AMS}
            //#endif
    )
    public static boolean craftableBundle = false;
    //#endif

    //#if MC<11900 && MC>=11700
    @CraftingRule(recipes = "sculk_sensor.json")
    @Rule(
            //#if MC<11900
            desc = "crafted sculk_sensor in minecraft 1.17/1.18",
            category = {CRAFTING, SURVIVAL, AMS}
            //#else
            //$$ categories = {CRAFTING, SURVIVAL, AMS}
            //#endif
    )
    public static boolean craftableSculkSensor = false;
    //#endif

    @CraftingRule(recipes = "bone_block.json")
    @Rule(
            //#if MC<11900
            desc = "Use nine bones to crafted three bone_blocks",
            category = {CRAFTING, SURVIVAL, AMS}
            //#else
            //$$ categories = {CRAFTING, SURVIVAL, AMS}
            //#endif
    )
    public static boolean betterCraftableBoneBlock = false;

    @CraftingRule(recipes = "elytra.json")
    @Rule(
            //#if MC<11900
            desc = "crafted elytra in minecraft",
            category = {CRAFTING, SURVIVAL, AMS}
            //#else
            //$$ categories = {CRAFTING, SURVIVAL, AMS}
            //#endif
    )
    public static boolean craftableElytra = false;
}
