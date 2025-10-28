/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

//#if MC>=12002
//$$ import club.mcams.carpet.observers.rule.stackableDiscount.StackableDiscountRuleObserver;
//#endif
import club.mcams.carpet.observers.network.NetworkProtocolObserver;
import club.mcams.carpet.observers.rule.largeShulkerBox.LargeShulkerBoxRuleObserver;
import club.mcams.carpet.observers.recipe.RecipeRuleObserver;
import club.mcams.carpet.observers.rule.fancyFakePlayerName.FancyFakePlayerNameRuleObserver;
import club.mcams.carpet.observers.rule.largeEnderChest.LargeEnderChestRuleObserver;
import club.mcams.carpet.observers.network.AmsNetworkProtocolRuleObserver;

import club.mcams.carpet.validators.rule.maxPlayerBlockInteractionRange.MaxPlayerBlockInteractionRangeValidator;
import club.mcams.carpet.validators.rule.maxPlayerEntityInteractionRange.MaxPlayerEntityInteractionRangeValidator;
import club.mcams.carpet.validators.rule.blockChunkLoaderTimeController.MaxTimeValidator;
import club.mcams.carpet.validators.rule.commandPlayerChunkLoadController.MaxRangeValidator;
import club.mcams.carpet.validators.rule.enhancedWorldEater.BlastResistanceValidator;
import club.mcams.carpet.validators.rule.maxClientInteractionReachDistance.MaxClientInteractionReachDistanceValidator;
//#if MC>=12000
//$$ import club.mcams.carpet.validators.rule.easyGetPitcherPod.CountValidator;
//#endif
import club.mcams.carpet.validators.rule.renewableNetherScrap.DropRateValidator;

import club.mcams.carpet.settings.Rule;
import club.mcams.carpet.settings.RecipeRule;

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

    @Rule(
        options = {"false", "true", "minecraft:overworld", "minecraft:the_end", "minecraft:the_nether", "minecraft:the_end,minecraft:the_nether"},
        categories = {AMS, FEATURE},
        strict = false
    )
    public static String fakePeace = "false";

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

    @Rule(categories = {AMS, FEATURE, SURVIVAL}, validators = LargeEnderChestRuleObserver.class)
    public static boolean largeEnderChest = false;

    @Rule(categories = {AMS, FEATURE, OPTIMIZATION})
    public static boolean bambooModelNoOffset = false;

    @Rule(categories = {AMS, FEATURE})
    public static boolean bambooCollisionBoxDisabled = false;

    @Rule(
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, FEATURE, COMMAND}
    )
    public static String commandCustomAntiFireItems = "false";

    @Rule(
        options = {"true", "false", "no_blast_wave"},
        categories = {AMS, FEATURE, TNT}
    )
    public static String itemAntiExplosion = "false";

    @Rule(categories = {AMS, FEATURE, CREATIVE})
    public static boolean creativeShulkerBoxDropsDisabled = false;

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

    @Rule(
        options = {"false", "true", "silence"},
        categories = {AMS, FEATURE, SURVIVAL, EXPERIMENTAL}
    )
    public static String amsUpdateSuppressionCrashFix = "false";

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

    @Rule(
        categories = {AMS, FEATURE, EXPERIMENTAL},
        validators = LargeShulkerBoxRuleObserver.class
    )
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

    //#if MC>=12005
    //$$ @GameVersion(version = "Minecraft >= 1.20.5")
    //$$ @Rule(
    //$$     options = {"server", "global"},
    //$$     categories = {AMS, FEATURE, SURVIVAL}
    //$$ )
    //$$ public static String maxPlayerBlockInteractionRangeScope = "server";
    //#endif

    //#if MC>=12005
    //$$ @GameVersion(version = "Minecraft >= 1.20.5")
    //$$ @Rule(
    //$$     options = {"server", "global"},
    //$$     categories = {AMS, FEATURE, SURVIVAL}
    //$$ )
    //$$ public static String maxPlayerEntityInteractionRangeScope = "server";
    //#endif

    @Rule(
        options = {"-1"},
        categories = {AMS, FEATURE, SURVIVAL},
        validators = MaxClientInteractionReachDistanceValidator.class,
        strict = false
    )
    public static double maxClientInteractionReachDistance = -1.0D;

    @Rule(
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, FEATURE, COMMAND}
    )
    public static String commandCustomMovableBlock = "false";

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

    @Rule(
        options = {"false", "bot", "fake_player"},
        categories = {AMS, FEATURE},
        validators = FancyFakePlayerNameRuleObserver.class,
        strict = false
    )
    public static String fancyFakePlayerName = "false";

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
    @GameVersion(version = "Minecraft < 1.20")
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

    @Rule(
        options = {"true", "false", "keepEndCrystal"},
        categories = {AMS, FEATURE, SURVIVAL}
    )
    public static String preventEndSpikeRespawn = "false";

    @Rule(categories = AMS)
    public static boolean welcomeMessage = false;

    @Rule(
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, COMMAND}
    )
    public static String commandGetSaveSize = "false";

    @Rule(categories = {AMS, FEATURE})
    public static boolean carpetAlwaysSetDefault = false;

    //#if MC>=11900 && MC<=12101
    //$$ @GameVersion(version = "Minecraft 1.19 - 1.21.1")
    //$$ @Rule(categories = {AMS, EXPERIMENTAL})
    //$$ public static boolean experimentalContentCheckDisabled = false;
    //#endif

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean fertilizableSmallFlower = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean safePointedDripstone = false;

    //#if MC>=11700
    @GameVersion(version = "Minecraft >= 1.17")
    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean pointedDripstoneCollisionBoxDisabled = false;
    //#endif

    @Rule(categories = {AMS, FEATURE})
    public static boolean foliageGenerateDisabled = false;

    @Rule(
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, COMMAND}
    )
    public static String commandGetSystemInfo = "false";

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean ironGolemNoDropFlower = false;

    //#if MC>=12000
    //$$ @GameVersion(version = "Minecraft >= 1.20")
    //$$ @Rule(
    //$$     options = {"0"},
    //$$     categories = {AMS, FEATURE, SURVIVAL},
    //$$     validators = CountValidator.class,
    //$$     strict = false
    //$$ )
    //$$ public static int easyGetPitcherPod = 0;
    //#endif

    @Rule(
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, COMMAND}
    )
    public static String commandGoto = "false";

    @Rule(
        options = {"false", "all", "realPlayerOnly", "fakePlayerOnly"},
        categories = {AMS, SURVIVAL}
    )
    public static String sendPlayerDeathLocation = "false";

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean perfectInvisibility = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean sneakInvisibility = false;

    @Rule(
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, SURVIVAL, COMMAND}
    )
    public static String commandCustomCommandPermissionLevel = "false";

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean shulkerGolem = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean headHunter = false;

    //#if MC>=12002
    //$$ @GameVersion(version = "Minecraft >= 1.20.2")
    //$$ @Rule(
    //$$     categories = {AMS, FEATURE, SURVIVAL},
    //$$     validators = StackableDiscountRuleObserver.class
    //$$ )
    //$$ public static boolean stackableDiscounts = false;
    //#endif

    @Rule(
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, SURVIVAL, COMMAND}
    )
    public static String commandGetPlayerSkull = "false";

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean quickVillagerLevelUp = false;

    @Rule(
        options = {"0.0", "0.01", "0.1", "1.0"},
        categories = {AMS, FEATURE, SURVIVAL},
        validators = DropRateValidator.class,
        strict = false
    )
    public static double renewableNetheriteScrap = 0.0D;

    @Rule(categories = {AMS, FEATURE, BUGFIX})
    public static boolean fakePlayerInteractLikeClient = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean strongLeash = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean superZombieDoctor = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean fakePlayerUseOfflinePlayerUUID = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean superLeash = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean fullMoonEveryDay = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean easyRefreshTrades = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean jebSheepDropRandomColorWool = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean cryingObsidianNetherPortal = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL}, strict = false)
    public static int furnaceSmeltingTimeController = -1;

    @Rule(categories = {AMS, FEATURE})
    public static boolean fakePlayerDefaultSurvivalMode = false;

    @Rule(
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, SURVIVAL, COMMAND}
    )
    public static String commandGetHeldItemID = "false";

    //#if MC>=12102
    //$$ @Rule(categories = {AMS, FEATURE, SURVIVAL})
    //$$ public static boolean stringDupeReintroduced = false;
    //#endif

    @Rule(
        options = "-1",
        categories = {AMS, FEATURE, SURVIVAL},
        strict = false
    )
    public static int witchRedstoneDustDropController = -1;

    @Rule(
        options = "-1",
        categories = {AMS, FEATURE, SURVIVAL},
        strict = false
    )
    public static int witchGlowstoneDustDropController = -1;

    @Rule(
        options = "-1.0",
        categories = {AMS, FEATURE, SURVIVAL, TNT},
        strict = false
    )
    public static double tntPowerController = -1.0D;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean meekEnderman = false;

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean customizedNetherPortal = false;

    @Rule(
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, COMMAND}
    )
    public static String commandCarpetExtensionModWikiHyperlink = "false";

    @Rule(categories = {AMS, COMMAND})
    public static boolean onlyOpCanSpawnRealPlayerInWhitelist = false;

    //#if MC>=12100
    //$$ @GameVersion(version = "Minecraft >= 1.21")
    //$$ @Rule(categories = {AMS, FEATURE})
    //$$ public static boolean itemEntityCreateNetherPortalDisabled = false;
    //#endif

    @Rule(categories = {AMS, FEATURE, SURVIVAL})
    public static boolean powerfulExpMending = false;

    @Rule(
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, COMMAND}
    )
    public static String commandAtSomeOnePlayer = "false";

    //#if MC>=11700
    @GameVersion(version = "Minecraft >= 1.17")
    @Rule(
        options = {"false", "9x3", "9x6"},
        categories = {AMS, FEATURE, SURVIVAL}
    )
    public static String largeBundle = "false";
    //#endif

    //#if MC>=11900
    //$$ @GameVersion(version = "Minecraft >= 1.19")
    //$$ @Rule(
    //$$     options = {"-1", "100000", "200000", "300000"},
    //$$     categories = {AMS, FEATURE, EXPERIMENTAL},
    //$$     strict = false
    //$$ )
    //$$ public static int maxChainUpdateDepth = -1;
    //#endif

    @Rule(categories = {AMS, SURVIVAL})
    public static boolean phantomSpawnAlert = false;

    //#if MC>=12005
    //$$ @Rule(categories = {AMS, FEATURE, SURVIVAL})
    //$$ public static boolean endPortalChunkLoadDisabled = false;
    //#endif

    /*
     * AMS网络协议规则
     */
    @Rule(
        validators = AmsNetworkProtocolRuleObserver.class,
        categories = {AMS, AMS_NETWORK}
    )
    public static boolean amsNetworkProtocol = false;

    @Rule(
        validators = NetworkProtocolObserver.class,
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, AMS_NETWORK, COMMAND}
    )
    public static boolean commandAmspDebug = false;

    @Rule(
        validators = NetworkProtocolObserver.class,
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, FEATURE, SURVIVAL, AMS_NETWORK, COMMAND}
    )
    public static String commandCustomBlockHardness = "false";

    @Rule(
        validators = NetworkProtocolObserver.class,
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, AMS_NETWORK, COMMAND}
    )
    public static String commandGetClientPlayerFps = "false";

    @Rule(
        validators = NetworkProtocolObserver.class,
        options = {"0", "1", "2", "3", "4", "ops", "true", "false"},
        categories = {AMS, AMS_NETWORK, COMMAND}
    )
    public static String commandSetPlayerPose = "false";

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

    //#if MC<12005
    @GameVersion(version = "Minecraft < 1.20.5")
    @Rule(categories = {AMS, FEATURE, AMS_CHUNKLOADER})
    public static boolean endPortalChunkLoader = false;
    //#endif

    /*
     * 合成表规则
     */
    @RecipeRule
    @Rule(categories = {AMS, CRAFTING, SURVIVAL}, validators = RecipeRuleObserver.class)
    public static boolean craftableEnchantedGoldenApples = false;

    //#if MC>=11700 && MC<12102
    @GameVersion(version = "Minecraft 1.17 - 1.21.1")
    @RecipeRule
    @Rule(categories = {AMS, CRAFTING, SURVIVAL}, validators = RecipeRuleObserver.class)
    public static boolean craftableBundle = false;
    //#endif

    //#if MC<11900 && MC>=11700
    @GameVersion(version = "Minecraft 1.17 - 1.18")
    @RecipeRule
    @Rule(categories = {AMS, CRAFTING, SURVIVAL}, validators = RecipeRuleObserver.class)
    public static boolean craftableSculkSensor = false;
    //#endif

    @RecipeRule
    @Rule(categories = {AMS, CRAFTING, SURVIVAL}, validators = RecipeRuleObserver.class)
    public static boolean betterCraftableBoneBlock = false;

    @RecipeRule
    @Rule(categories = {AMS, CRAFTING, SURVIVAL}, validators = RecipeRuleObserver.class)
    public static boolean craftableElytra = false;

    @RecipeRule
    @Rule(categories = {AMS, CRAFTING, SURVIVAL}, validators = RecipeRuleObserver.class)
    public static boolean betterCraftableDispenser = false;

    //#if MC>=11700
    @GameVersion(version = "Minecraft >= 1.17")
    @RecipeRule
    @Rule(categories = {AMS, CRAFTING, SURVIVAL}, validators = RecipeRuleObserver.class)
    public static boolean betterCraftablePolishedBlackStoneButton = false;
    //#endif

    @RecipeRule
    @Rule(categories = {AMS, CRAFTING, SURVIVAL}, validators = RecipeRuleObserver.class)
    public static boolean rottenFleshBurnedIntoLeather = false;

    //#if MC<12105
    @GameVersion(version = "Minecraft < 1.21.5")
    @RecipeRule
    @Rule(categories = {AMS, CRAFTING, SURVIVAL}, validators = RecipeRuleObserver.class)
    public static boolean useNewLodestoneRecipe = false;
    //#endif

    @RecipeRule
    @Rule(categories = {AMS, CRAFTING, SURVIVAL}, validators = RecipeRuleObserver.class)
    public static boolean craftableCarvedPumpkin = false;

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

    @SuppressWarnings("unused")
    @Rule(categories = AMS)
    public static boolean testRule = false;
}
