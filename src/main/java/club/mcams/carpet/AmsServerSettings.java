//#if MC<11900
package club.mcams.carpet;

import club.mcams.carpet.util.recipes.CraftingRule;

import carpet.settings.Rule;
import static carpet.settings.RuleCategory.FEATURE;
import static carpet.settings.RuleCategory.SURVIVAL;
import static carpet.settings.RuleCategory.OPTIMIZATION;
import static carpet.settings.RuleCategory.COMMAND;
import static carpet.settings.RuleCategory.TNT;

/**
 * Here is your example Settings class you can plug to use carpetmod /carpet settings command
 */

@SuppressWarnings({"CanBeFinal", "removal"})
public class AmsServerSettings {
    public static final String AMS = "AMS";
    public static final String CRAFTING = "CRAFTING";

    @Rule(
            desc = "Enabling making super bows with both infinite and mending enchants",
            category = {AMS, FEATURE}
    )
    public static boolean superBow = false;

    @Rule(
            desc = "Make cactus accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean scheduledRandomTickCactus = false;

    @Rule(
            desc = "Make bamboo accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean scheduledRandomTickBamboo = false;

    @Rule(
            desc = "Make chorus flower accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean scheduledRandomTickChorusFlower = false;

    @Rule(
            desc = "Make sugar cane accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean scheduledRandomTickSugarCane = false;

    @Rule(
            desc = "Make stems accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean scheduledRandomTickStem = false;

    @Rule(
            desc = "Make all plants accepts scheduled tick as random tick",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean scheduledRandomTickAllPlants = false;

    @Rule(
            desc = "Optimize dragon respawning",
            extra = {"May slightly affect the vanilla feature"},
            category = {AMS, OPTIMIZATION}
    )
    public static boolean optimizedDragonRespawn = false;

    @Rule(
            desc = "Load nearby 3x3 chunks for 15 seconds when a note block is triggered",
            category = {AMS, FEATURE}
    )
    public static boolean noteBlockChunkLoader = false;

    @Rule(
            desc = "Load nearby 3x3 chunks for 15 seconds when a note_block with a bone block at its top extends",
            category = {AMS, FEATURE}
    )
    public static boolean noteBlockChunkLoaderPro = false;

    @Rule(
            desc = "Load nearby 3x3 chunks for 15 seconds when a bell is triggered",
            category = {AMS, FEATURE}
    )
    public static boolean bellBlockChunkLoader = false;

    @Rule(
            desc = "Load nearby 3x3 chunks for 15 seconds when a piston with a bone block at its top extends",
            category = {AMS, FEATURE}
    )
    public static boolean pistonBlockChunkLoader = false;

    @Rule(
            desc = "Control chunk loading for players at any gamemodes",
            category = {AMS, COMMAND}
    )
    public static String commandChunkLoading = "false";

    @Rule(
            desc = "Players can use water buckets to place water in nether",
            category = {AMS, FEATURE}
    )
    public static boolean netherWaterPlacement = false;

    //#if MC>=11700
    @Rule(
            desc = "Change the hardness of deepslate to stone",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean breakableDeepslate = false;
    //#endif

    @Rule(
            desc = "Players can use diamond_pickaxe or netherite_pickaxe to dig bedrock",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean canBreakBedRock = false;

    @Rule(
            desc = "Players can use diamond_pickaxe or netherite_pickaxe to dig end_portal_frame",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean canBreakEndPortalFrame = false;

    @Rule(
            desc = "Change the hardness of obsdian to deepslate",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean breakableObsidian = false;

    @Rule(
            desc = "Set all blocks BlastResistance to 0",
            category = {AMS, FEATURE, SURVIVAL, TNT}
    )
    public static boolean destroysEverything = false;

    @Rule(
            desc = "anti-explosion",
            category = {AMS, FEATURE, SURVIVAL, TNT}
    )
    public static boolean noBoom = false;

    @Rule(
            desc = "Share villagers discount to all players",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean sharedVillagerDiscounts = false;

    @Rule(
            desc = "Simulation fake Peace",
            category = {AMS, FEATURE}
    )
    public static boolean fakePeace = false;

    @Rule(
            desc = "The campfire is extinguished when the player places it",
            category = {AMS, FEATURE, SURVIVAL}
    )
    public static boolean extinguishedCampfire = false;

    @Rule(
            desc = "Makes ender_chests movable",
            category = {AMS, FEATURE}
    )
    public static boolean movableEnderChest = false;

    @Rule(
            desc = "Makes end_portal_frame movable",
            category = {AMS, FEATURE}
    )
    public static boolean movableEndPortalFrame = false;

    @Rule(
            desc = "Makes obsidian movable",
            category = {AMS, FEATURE}
    )
    public static boolean movableObsidian = false;

    @Rule(
            desc = "Makes crying_obsidian movable",
            category = {AMS, FEATURE}
    )
    public static boolean movableCryingObsidian = false;

    @Rule(
            desc = "Makes bedrock movable",
            category = {AMS, FEATURE}
    )
    public static boolean movableBedRock = false;

    @Rule(
            desc = "Makes enchanting_table movable",
            category = {AMS, FEATURE}
    )
    public static boolean movableEnchantingTable = false;

    @Rule(
            desc = "Makes beacon movable",
            category = {AMS, FEATURE}
    )
    public static boolean movableBeacon = false;

    //#if MC>=11900
    @Rule(
            desc = "Makes reinforced_deepslate movable",
            category = {AMS, FEATURE}
    )
    public static boolean movableReinforcedDeepslate = false;
    //#endif

    @Rule(
            desc = "Makes anvil movable",
            category = {AMS, FEATURE}
    )
    public static boolean movableAnvil = false;

    /**=====================================================================================================
     * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓合成表↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
     * =====================================================================================================*/

    @CraftingRule(recipes = "enchanted_golden_apples.json")
    @Rule(
            desc = "Enchanted Golden Apples can be crafted with 8 Gold Blocks again",
            category = {CRAFTING, SURVIVAL, AMS}
    )

    public static boolean craftableEnchantedGoldenApples = false;

    //#if MC>=11700
    @CraftingRule(recipes = "bundle.json")
    @Rule(
            desc = "crafted bundle in minecraft 1.17/1.18/1.19",
            category = {CRAFTING, SURVIVAL, AMS}
    )
    public static boolean craftableBundle = false;
    //#endif

    //#if MC<11900 && MC>=11700
    @CraftingRule(recipes = "sculk_sensor.json")
    @Rule(
            desc = "crafted sculk_sensor in minecraft 1.17/1.18",
            category = {CRAFTING, SURVIVAL, AMS}
    )
    public static boolean craftableSculkSensor = false;
    //#endif

    @CraftingRule(recipes = "bone_block.json")
    @Rule(
            desc = "Use nine bones to crafted three bone_blocks",
            category = {CRAFTING, SURVIVAL, AMS}
    )
    public static boolean betterCraftableBoneBlock = false;

    @CraftingRule(recipes = "elytra.json")
    @Rule(
            desc = "crafted elytra in minecraft",
            category = {CRAFTING, SURVIVAL, AMS}
    )
    public static boolean craftableElytra = false;

    /**=====================================================================================================
     * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑合成表↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
     * =====================================================================================================*/

}
//#endif

/**=====================================================================================================
 * ---------------------------------------Minecraft Version >= 1.19-------------------------------------
 * =====================================================================================================*/

//#if MC>=11900
//$$ package club.mcams.carpet;
//$$ import club.mcams.carpet.util.recipes.CraftingRule;
//$$ import carpet.api.settings.Rule;
//$$ import static carpet.api.settings.RuleCategory.*;

/**
 * Here is your example Settings class you can plug to use carpetmod /carpet settings command
 */

//$$ @SuppressWarnings({"CanBeFinal"})
//$$ public class AmsServerSettings {
//$$     public static final String AMS = "AMS";
//$$     public static final String CRAFTING = "CRAFTING";

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$    public static boolean superBow = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean scheduledRandomTickCactus = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$   public static boolean scheduledRandomTickBamboo = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean scheduledRandomTickChorusFlower = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean scheduledRandomTickSugarCane = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean scheduledRandomTickStem = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean scheduledRandomTickAllPlants = false;

//$$     @Rule(
//$$             categories = {AMS, OPTIMIZATION}
//$$     )
//$$     public static boolean optimizedDragonRespawn = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean noteBlockChunkLoader = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean noteBlockChunkLoaderPro = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean bellBlockChunkLoader = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean pistonBlockChunkLoader = false;

//$$     @Rule(
//$$             categories = {AMS, COMMAND}
//$$     )
//$$     public static String commandChunkLoading = "false";

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean netherWaterPlacement = false;

         //#if MC>=11700
//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean breakableDeepslate = false;
         //#endif

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean canBreakBedRock = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean canBreakEndPortalFrame = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean breakableObsidian = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL, TNT}
//$$     )
//$$     public static boolean destroysEverything = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL, TNT}
//$$     )
//$$     public static boolean noBoom = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean sharedVillagerDiscounts = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean fakePeace = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE, SURVIVAL}
//$$     )
//$$     public static boolean extinguishedCampfire = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean movableEnderChest = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean movableEndPortalFrame = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean movableObsidian = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean movableCryingObsidian = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean movableBedRock = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean movableEnchantingTable = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean movableBeacon = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean movableReinforcedDeepslate = false;

//$$     @Rule(
//$$             categories = {AMS, FEATURE}
//$$     )
//$$     public static boolean movableAnvil = false;

/**=====================================================================================================
 * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓合成表↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
 * =====================================================================================================*/

//$$     @CraftingRule(recipes = "enchanted_golden_apples.json")
//$$     @Rule(
//$$             categories = {CRAFTING, SURVIVAL, AMS}
//$$    )
//$$     public static boolean craftableEnchantedGoldenApples = false;

         //#if MC>=11700
//$$     @CraftingRule(recipes = "bundle.json")
//$$     @Rule(
//$$             categories = {CRAFTING, SURVIVAL, AMS}
//$$     )
//$$     public static boolean craftableBundle = false;
         //#endif

         //#if MC<11900 && MC>=11700
//$$     @CraftingRule(recipes = "sculk_sensor.json")
//$$     @Rule(
//$$             categories = {CRAFTING, SURVIVAL, AMS}
//$$     )
//$$     public static boolean craftableSculkSensor = false;
         //#endif

//$$     @CraftingRule(recipes = "bone_block.json")
//$$     @Rule(
//$$             categories = {CRAFTING, SURVIVAL, AMS}
//$$     )
//$$     public static boolean betterCraftableBoneBlock = false;

//$$     @CraftingRule(recipes = "elytra.json")
//$$     @Rule(
//$$             categories = {CRAFTING, SURVIVAL, AMS}
//$$     )
//$$     public static boolean craftableElytra = false;

/**=====================================================================================================
 * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑合成表↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
 * =====================================================================================================*/

//$$ }
//#endif