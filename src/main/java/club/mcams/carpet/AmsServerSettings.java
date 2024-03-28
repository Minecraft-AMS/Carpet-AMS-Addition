/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import club.mcams.carpet.validators.rule.maxPlayerBlockInteractionRange.MaxPlayerBlockInteractionRangeValidator;
import club.mcams.carpet.validators.rule.maxPlayerEntityInteractionRange.MaxPlayerEntityInteractionRangeValidator;
import club.mcams.carpet.validators.rule.blockChunkLoaderTimeController.MaxTimeValidator;
import club.mcams.carpet.validators.rule.commandPlayerChunkLoadController.MaxRangeValidator;
import club.mcams.carpet.validators.rule.enhancedWorldEater.BlastResistanceValidator;
import club.mcams.carpet.validators.rule.maxClientInteractionReachDistance.MaxClientInteractionReachDistanceValidator;

import club.mcams.carpet.settings.CraftingRule;
import club.mcams.carpet.settings.Rule;

import static carpet.settings.RuleCategory.*;
import static club.mcams.carpet.settings.AmsRuleCategory.*;

public class AmsServerSettings {

    @Rule(categories = {AMS, FEATURE})
    public static boolean superBow = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean scheduledRandomTickCactus = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean scheduledRandomTickBamboo = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean scheduledRandomTickChorusFlower = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean scheduledRandomTickSugarCane = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean scheduledRandomTickStem = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean scheduledRandomTickAllPlants = false;

    @Rule(categories = {AMS, OPTIMIZATION})
    public static boolean optimizedDragonRespawn = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean netherWaterPlacement = false;

    @Rule(categories = {AMS, FEATURE, TNT})
    public static boolean blowUpEverything = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean sharedVillagerDiscounts = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean fakePeace = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean extinguishedCampfire = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean safeFlight = false;

    @Rule(
            options = {"none", "minecraft:bone_block", "minecraft:diamond_ore", "minecraft:magma_block"},
            categories = {AMS, FEATURE},
            strict = false
    )
    public static String customBlockUpdateSuppressor = "none";

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean infiniteTrades = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean invulnerable = false;

    @Rule(categories = {AMS, FEATURE, CREATIVE})
    public static boolean creativeOneHitKill = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean largeEnderChest = false;

    @Rule(categories = {AMS, FEATURE, OPTIMIZATION})
    public static boolean bambooModelNoOffset = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean bambooCollisionBoxDisabled = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean campfireSmokeParticleDisabled = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean antiFireTotem = false;

    @Rule(categories = {AMS, FEATURE, TNT})
    public static boolean itemAntiExplosion = false;

    @Rule(categories = {AMS, FEATURE, CREATIVE})
    public static boolean creativeShulkerBoxDropsDisabled = false;

    @Rule(categories = {AMS, FEATURE, CREATIVE})
    public static boolean bedRockFlying = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean shulkerHitLevitationDisabled = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean immuneShulkerBullet = false;

    @Rule(categories = {AMS, FEATURE, EXPERIMENTAL})
    public static blueSkullProbability blueSkullController = blueSkullProbability.VANILLA;

    @Rule(categories = {AMS, FEATURE, EXPERIMENTAL})
    public static boolean endermanTeleportRandomlyDisabled = false;

    @Rule(
            options = {"VANILLA", "Ⅰ", "Ⅱ", "Ⅲ", "Ⅳ", "Ⅴ"},
            categories = {AMS, FEATURE, EXPERIMENTAL}
    )
    public static String fasterMovement = "VANILLA";

    @Rule(categories = {AMS, FEATURE, EXPERIMENTAL})
    public static fasterMovementDimension fasterMovementController = fasterMovementDimension.ALL;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean easyWitherSkeletonSkullDrop = false;

    @Rule(
            options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
            categories = {AMS, FEATURE, SURVIVAL, COMMAND}
    )
    public static String commandAnvilInteractionDisabled = "false";

    @Rule(categories = {AMS, FEATURE, SURVIVAL, COMMAND})
    public static boolean preventAdministratorCheat = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL, EXPERIMENTAL})
    public static boolean amsUpdateSuppressionCrashFix = false;

    //#if MC<11900
    @GameVersion(version = "Minecraft < 1.19.3")
    @Rule(categories = {AMS, FEATURE, SURVIVAL, EXPERIMENTAL, BUGFIX})
    public static boolean ghastFireballExplosionDamageSourceFix = false;
    //#endif

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean cakeBlockDropOnBreak = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean noCakeEating = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean redstoneComponentSound = false;

    @Rule(categories = {AMS, FEATURE, EXPERIMENTAL})
    public static boolean largeShulkerBox = false;

    @Rule(
            options = {"-1"},
            categories = {AMS, FEATURE, SURVIVAL},
            validators = MaxPlayerBlockInteractionRangeValidator.class,
            strict = false
    )
    public static double maxPlayerBlockInteractionRange = -1.0D;

    @Rule(
            options = {"-1"},
            categories = {AMS, FEATURE, SURVIVAL},
            validators = MaxPlayerEntityInteractionRangeValidator.class,
            strict = false
    )
    public static double maxPlayerEntityInteractionRange = -1.0D;

    //#if MC<12005
    @GameVersion(version = "Minecraft < 1.20.5", desc = "1.20.5后或许不再需要这个功能？")
    @Rule(
            options = {"-1"},
            categories = {AMS, FEATURE, SURVIVAL},
            validators = MaxClientInteractionReachDistanceValidator.class,
            strict = false
    )
    public static double maxClientInteractionReachDistance = -1.0D;
    //#endif

    @Rule(
            options = {"VANILLA", "minecraft:bedrock", "minecraft:bedrock,minecraft:obsidian"},
            categories = {AMS, FEATURE},
            strict = false
    )
    public static String customMovableBlock = "VANILLA";

    @Rule(categories = {AMS, FEATURE})
    public static boolean easyMaxLevelBeacon = false;

    @Rule(
            options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
            categories = {AMS, FEATURE, SURVIVAL, TNT, COMMAND}
    )
    public static String commandCustomBlockBlastResistance = "false";

    @Rule(categories = {AMS, FEATURE})
    public static boolean regeneratingDragonEgg = false;

    @Rule(
            options = {"-1"},
            categories = {AMS, SURVIVAL, FEATURE, TNT},
            validators = BlastResistanceValidator.class,
            strict = false
    )
    public static double enhancedWorldEater = -1.0D;

    @Rule(categories = {AMS, FEATURE})
    public static boolean sneakToEditSign = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean fancyFakePlayerName = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean fakePlayerNoScoreboardCounter = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean noFamilyPlanning = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean hopperSuctionDisabled = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean noEnchantedGoldenAppleEating = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean useItemCooldownDisabled = false;

    @Rule(
            options = {"0", "1", "2", "3", "4", "5"},
            categories = {AMS, FEATURE, SURVIVAL}
    )
    public static int flippinCactusSoundEffect = 0;

    @Rule(categories = {AMS, FEATURE})
    public static boolean undyingCoral = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean enderDragonNoDestroyBlock = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean easyCompost = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean easyMineDragonEgg = false;

    @Rule(
            options = {"none", "apple", "stone"},
            categories = {AMS, FEATURE},
            strict = false
    )
    public static String breedableParrots = "none";

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean kirinArm = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean sensibleEnderman = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean endermanPickUpDisabled = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean mitePearl = false;

    //#if MC<12000
    @Rule(categories = {AMS, FEATURE})
    public static boolean enderPearlSoundEffect = false;
    //#endif

    @Rule(
            options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
            categories = {AMS, COMMAND}
    )
    public static String commandHere = "false";

    @Rule(
            options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
            categories = {AMS, COMMAND}
    )
    public static String commandWhere = "false";

    @Rule(
            options = {"MainHandOnly", "NoPickUp", "false"},
            categories = {AMS, FEATURE, SURVIVAL}
    )
    public static String fakePlayerPickUpController = "false";

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean sneakToEatCake = false;

    @Rule(
            options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
            categories = {AMS, COMMAND}
    )
    public static String commandPlayerLeader = "false";

    @Rule(
            options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
            categories = {AMS, COMMAND}
    )
    public static String commandPacketInternetGroper = "false";

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean playerNoNetherPortalTeleport = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean infiniteDurability = false;

    @Rule(categories = AMS)
    public static boolean welcomeMessage = false;

    @Rule(
            options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
            categories = {AMS, COMMAND}
    )
    public static String commandGetSaveSize = "false";

    @Rule(categories = {AMS, FEATURE})
    public static boolean carpetAlwaysSetDefault = false;

    //#if MC>=11900
    //$$ @Rule(categories = {AMS, EXPERIMENTAL})
    //$$ public static boolean experimentalContentCheckDisabled = false;
    //#endif

    /*
     * 区块加载规则
     */
    @Rule(
            options = {"bone_block", "wither_skeleton_skull", "note_block", "false"},
            categories = {AMS, FEATURE, AMS_CHUNKLOADER}
    )
    public static String noteBlockChunkLoader = "false";

    @Rule(
            options = {"bone_block", "bedrock", "all", "false"},
            categories = {AMS, FEATURE, AMS_CHUNKLOADER}
    )
    public static String pistonBlockChunkLoader = "false";

    @Rule(categories = {AMS, FEATURE, AMS_CHUNKLOADER})
    public static boolean bellBlockChunkLoader = false;

    @Rule(categories = {AMS, FEATURE, AMS_CHUNKLOADER})
    public static boolean blockChunkLoaderKeepWorldTickUpdate = false;

    @Rule(categories = {AMS, FEATURE, AMS_CHUNKLOADER})
    public static boolean keepWorldTickUpdate = false;

    @Rule(
            options = {"300"},
            categories = {AMS, FEATURE, AMS_CHUNKLOADER},
            validators = MaxTimeValidator.class,
            strict = false
    )
    public static int blockChunkLoaderTimeController = 300;

    @Rule(
            options = {"3"},
            categories = {AMS, FEATURE, AMS_CHUNKLOADER},
            validators = MaxRangeValidator.class,
            strict = false
    )
    public static int blockChunkLoaderRangeController = 3;

    @Rule(categories = {AMS, COMMAND, AMS_CHUNKLOADER})
    public static boolean commandPlayerChunkLoadController = false;

    /*
     * 合成表规则
     */
    @SuppressWarnings("unused")
    @CraftingRule(recipes = "enchanted_golden_apples.json")
    @Rule(categories = {AMS, CRAFTING, SURVIVAL})
    public static boolean craftableEnchantedGoldenApples = false;

    //#if MC>=11700
    @SuppressWarnings("unused")
    @CraftingRule(recipes = "bundle.json")
    @Rule(categories = {AMS, CRAFTING, SURVIVAL})
    public static boolean craftableBundle = false;
    //#endif

    //#if MC<11900 && MC>=11700
    @SuppressWarnings("unused")
    @CraftingRule(recipes = "sculk_sensor.json")
    @Rule(categories = {AMS, CRAFTING, SURVIVAL})
    public static boolean craftableSculkSensor = false;
    //#endif

    @SuppressWarnings("unused")
    @CraftingRule(recipes = "bone_block.json")
    @Rule(categories = {AMS, CRAFTING, SURVIVAL})
    public static boolean betterCraftableBoneBlock = false;

    @SuppressWarnings("unused")
    @CraftingRule(recipes = "elytra.json")
    @Rule(categories = {AMS, CRAFTING, SURVIVAL})
    public static boolean craftableElytra = false;

    @SuppressWarnings("unused")
    @CraftingRule(recipes = {"dispenser1.json", "dispenser2.json"})
    @Rule(categories = {AMS, CRAFTING, SURVIVAL})
    public static boolean betterCraftableDispenser = false;

    //#if MC>=11700
    @SuppressWarnings("unused")
    @CraftingRule(recipes = "polished_blackstone_button.json")
    @Rule(categories = {AMS, CRAFTING, SURVIVAL})
    public static boolean betterCraftablePolishedBlackStoneButton = false;
    //#endif

    //#if MC>=12003
    @GameVersion(version = "Minecraft 1.20.3 - 1.20.5")
    //$$ @SuppressWarnings("unused")
    //$$ @CraftingRule(
    //$$     recipes = {
    //$$     "chiseled_copper.json",
    //$$     "chiseled_copper_from_copper_block_stonecutting.json",
    //$$     "chiseled_copper_from_cut_copper_stonecutting.json",
    //$$     "chiseled_tuff.json",
    //$$     "chiseled_tuff_bricks.json",
    //$$     "chiseled_tuff_bricks_from_polished_tuff_stonecutting.json",
    //$$     "chiseled_tuff_bricks_from_tuff_bricks_stonecutting.json",
    //$$     "chiseled_tuff_bricks_from_tuff_stonecutting.json",
    //$$     "chiseled_tuff_from_tuff_stonecutting.json",
    //$$     "copper_bulb.json",
    //$$     "copper_door.json",
    //$$     "copper_grate.json",
    //$$     "copper_grate_from_copper_block_stonecutting.json",
    //$$     "copper_trapdoor.json",
    //$$     "crafter.json",
    //$$     "exposed_chiseled_copper.json",
    //$$     "exposed_chiseled_copper_from_exposed_copper_stonecutting.json",
    //$$     "exposed_chiseled_copper_from_exposed_cut_copper_stonecutting.json",
    //$$     "exposed_copper_bulb.json",
    //$$     "exposed_copper_door.json",
    //$$     "exposed_copper_grate.json",
    //$$     "exposed_copper_grate_from_exposed_copper_stonecutting.json",
    //$$     "exposed_copper_trapdoor.json",
    //$$     "oxidized_chiseled_copper.json",
    //$$     "oxidized_chiseled_copper_from_oxidized_copper_stonecutting.json",
    //$$     "oxidized_chiseled_copper_from_oxidized_cut_copper_stonecutting.json",
    //$$     "oxidized_copper_bulb.json",
    //$$     "oxidized_copper_door.json",
    //$$     "oxidized_copper_grate.json",
    //$$     "oxidized_copper_grate_from_oxidized_copper_stonecutting.json",
    //$$     "oxidized_copper_trapdoor.json",
    //$$     "polished_tuff.json",
    //$$     "polished_tuff_from_tuff_stonecutting.json",
    //$$     "polished_tuff_slab.json",
    //$$     "polished_tuff_slab_from_polished_tuff_stonecutting.json",
    //$$     "polished_tuff_slab_from_tuff_stonecutting.json",
    //$$     "polished_tuff_stairs.json",
    //$$     "polished_tuff_stairs_from_polished_tuff_stonecutting.json",
    //$$     "polished_tuff_stairs_from_tuff_stonecutting.json",
    //$$     "polished_tuff_wall.json",
    //$$     "polished_tuff_wall_from_polished_tuff_stonecutting.json",
    //$$     "polished_tuff_wall_from_tuff_stonecutting.json",
    //$$     "tuff_bricks.json",
    //$$     "tuff_bricks_from_polished_tuff_stonecutting.json",
    //$$     "tuff_bricks_from_tuff_stonecutting.json",
    //$$     "tuff_brick_slab.json",
    //$$     "tuff_brick_slab_from_polished_tuff_stonecutting.json",
    //$$     "tuff_brick_slab_from_tuff_bricks_stonecutting.json",
    //$$     "tuff_brick_slab_from_tuff_stonecutting.json",
    //$$     "tuff_brick_stairs.json",
    //$$     "tuff_brick_stairs_from_polished_tuff_stonecutting.json",
    //$$     "tuff_brick_stairs_from_tuff_bricks_stonecutting.json",
    //$$     "tuff_brick_stairs_from_tuff_stonecutting.json",
    //$$     "tuff_brick_wall.json",
    //$$     "tuff_brick_wall_from_polished_tuff_stonecutting.json",
    //$$     "tuff_brick_wall_from_tuff_bricks_stonecutting.json",
    //$$     "tuff_brick_wall_from_tuff_stonecutting.json",
    //$$     "tuff_slab.json",
    //$$     "tuff_slab_from_tuff_stonecutting.json",
    //$$     "tuff_stairs.json",
    //$$     "tuff_stairs_from_tuff_stonecutting.json",
    //$$     "tuff_wall.json",
    //$$     "tuff_wall_from_tuff_stonecutting.json",
    //$$     "waxed_chiseled_copper.json",
    //$$     "waxed_chiseled_copper_from_honeycomb.json",
    //$$     "waxed_chiseled_copper_from_waxed_copper_block_stonecutting.json",
    //$$     "waxed_chiseled_copper_from_waxed_cut_copper_stonecutting.json",
    //$$     "waxed_copper_bulb.json",
    //$$     "waxed_copper_bulb_from_honeycomb.json",
    //$$     "waxed_copper_door.json",
    //$$     "waxed_copper_door_from_honeycomb.json",
    //$$     "waxed_copper_grate.json",
    //$$     "waxed_copper_grate_from_honeycomb.json",
    //$$     "waxed_copper_grate_from_waxed_copper_block_stonecutting.json",
    //$$     "waxed_copper_trapdoor.json",
    //$$     "waxed_copper_trapdoor_from_honeycomb.json",
    //$$     "waxed_exposed_chiseled_copper.json",
    //$$     "waxed_exposed_chiseled_copper_from_honeycomb.json",
    //$$     "waxed_exposed_chiseled_copper_from_waxed_exposed_copper_stonecutting.json",
    //$$     "waxed_exposed_chiseled_copper_from_waxed_exposed_cut_copper_stonecutting.json",
    //$$     "waxed_exposed_copper_bulb.json",
    //$$     "waxed_exposed_copper_bulb_from_honeycomb.json",
    //$$     "waxed_exposed_copper_door.json",
    //$$     "waxed_exposed_copper_door_from_honeycomb.json",
    //$$     "waxed_exposed_copper_grate.json",
    //$$     "waxed_exposed_copper_grate_from_honeycomb.json",
    //$$     "waxed_exposed_copper_grate_from_waxed_exposed_copper_stonecutting.json",
    //$$     "waxed_exposed_copper_trapdoor.json",
    //$$     "waxed_exposed_copper_trapdoor_from_honeycomb.json",
    //$$     "waxed_oxidized_chiseled_copper.json",
    //$$     "waxed_oxidized_chiseled_copper_from_honeycomb.json",
    //$$     "waxed_oxidized_chiseled_copper_from_waxed_oxidized_copper_stonecutting.json",
    //$$     "waxed_oxidized_chiseled_copper_from_waxed_oxidized_cut_copper_stonecutting.json",
    //$$     "waxed_oxidized_copper_bulb.json",
    //$$     "waxed_oxidized_copper_bulb_from_honeycomb.json",
    //$$     "waxed_oxidized_copper_door.json",
    //$$     "waxed_oxidized_copper_door_from_honeycomb.json",
    //$$     "waxed_oxidized_copper_grate.json",
    //$$     "waxed_oxidized_copper_grate_from_honeycomb.json",
    //$$     "waxed_oxidized_copper_grate_from_waxed_oxidized_copper_stonecutting.json",
    //$$     "waxed_oxidized_copper_trapdoor.json",
    //$$     "waxed_oxidized_copper_trapdoor_from_honeycomb.json",
    //$$     "waxed_weathered_chiseled_copper.json",
    //$$     "waxed_weathered_chiseled_copper_from_honeycomb.json",
    //$$     "waxed_weathered_chiseled_copper_from_waxed_weathered_copper_stonecutting.json",
    //$$     "waxed_weathered_chiseled_copper_from_waxed_weathered_cut_copper_stonecutting.json",
    //$$     "waxed_weathered_copper_bulb.json",
    //$$     "waxed_weathered_copper_bulb_from_honeycomb.json",
    //$$     "waxed_weathered_copper_door.json",
    //$$     "waxed_weathered_copper_door_from_honeycomb.json",
    //$$     "waxed_weathered_copper_grate.json",
    //$$     "waxed_weathered_copper_grate_from_honeycomb.json",
    //$$     "waxed_weathered_copper_grate_from_waxed_weathered_copper_stonecutting.json",
    //$$     "waxed_weathered_copper_trapdoor.json",
    //$$     "waxed_weathered_copper_trapdoor_from_honeycomb.json",
    //$$     "weathered_chiseled_copper.json",
    //$$     "weathered_chiseled_copper_from_weathered_copper_stonecutting.json",
    //$$     "weathered_chiseled_copper_from_weathered_cut_copper_stonecutting.json",
    //$$     "weathered_copper_bulb.json",
    //$$     "weathered_copper_door.json",
    //$$     "weathered_copper_grate.json",
    //$$     "weathered_copper_grate_from_weathered_copper_stonecutting.json",
    //$$     "weathered_copper_trapdoor.json"
    //$$     }
    //$$ )
    //$$ @Rule(categories = {AMS, CRAFTING, SURVIVAL})
    //$$ public static boolean enableMinecraft1_21ExperimentalRecipes = false;
    //#endif

    public enum blueSkullProbability {
        VANILLA,
        SURELY,
        NEVER
    }

    public enum fasterMovementDimension {
        OVERWORLD,
        NETHER,
        END,
        ALL
    }
}
