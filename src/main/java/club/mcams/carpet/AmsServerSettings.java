package club.mcams.carpet;

import carpet.settings.Rule;
import club.mcams.carpet.util.recipes.CraftingRule;

import static carpet.settings.RuleCategory.*;

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
            desc = "Load nearby 3x3 chunks for 15 seconds when a bell is triggered",
            category = {AMS, FEATURE}
    )
    public static boolean bellBlockChunkLoader = false;
    @Rule(
            desc = "Load nearby 3x3 chunks for 15 seconds when a piston with a bone block at its top extends",
            category = {AMS, FEATURE}
    )
    public static boolean pistonBlockChunkLoader = false;

    @CraftingRule(recipes = "enchanted_golden_apples.json")
    @Rule(
            desc = "Enchanted Golden Apples can be crafted with 8 Gold Blocks again",
            category = {CRAFTING, SURVIVAL, AMS}
    )
    public static boolean craftableEnchantedGoldenApples = false;

    @CraftingRule(recipes = "bone_block.json")
    @Rule(
            desc = "Use nine bones to crafted three bone_blocks",
            category = {CRAFTING, SURVIVAL, AMS}
    )
    public static boolean betterCraftableBoneBlock = false;

    @Rule(
            desc = "Control chunk loading for players at any gamemodes",
            category = {AMS, COMMAND}
    )
    public static String commandChunkLoading = "false";

}
