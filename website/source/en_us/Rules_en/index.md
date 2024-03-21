### [ [中文](/carpetamsaddition/Rules) | English ]

# <center>------ Rules ------</center>

&emsp;

## superBow

Let enchantments Infinity and Mending be compatible with each other on bow.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## scheduledRandomTickAllPlants

When enabled, plants will execute codes related to growth in tileTick events planed by finding invaild position. Use to restore 0tick farms/force updates farm in higher version. you can also control specific plants to have force updates.

- `scheduledRandomTickCactus`: Enable 0tick farm of cactus.



- `scheduledRandomTickBamboo`: Enable 0tick farm of bamboo.



- `scheduledRandomTickChorusFlower`: Enable 0tick farm of chorus flower.



- `scheduledRandomTickSugarCane`: Enable 0tick farm of sugar cane.



- `scheduledRandomTickStem`: Enable 0tick farm of twisting vines, weeping vines and kelp.

<Ported from [OhMyVanillaMinecraft](https://github.com/hit-mc/OhMyVanillaMinecraft) >

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## optimizedDragonRespawn

Greatly optimized the code related to dragon fight starting. Provide performance enhancement for endstone farm based on re-summoning of ender dragon. Warning: couldn't ensure same behavior as vanilla Minecraft after enabling this rule.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `OPTIMIZATION`

## commandPlayerChunkLoadController

Control chunkloading of player. Won't remove player detection of deminsion like main island loading in the end. Will be reset after player loging out to avoid [MC-157812](https://bugs.mojang.com/browse/MC-157812).

Command: /playerChunkLoading true/false

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `COMMAND`, `AMS_chunkLoader`

## noteBlockChunkLoader

When a note block is triggered by rising edge of redstone signal, the chunk which the note block located will be added a ticket with level 30 and type `note_block`, which will expire after 300gt. Note blocks that can be used to add ticket must meet the requirement set by the rule option, You can use the rules of `blockChunkLoaderTimeController` and `blockChunkLoaderRangeController` to control their loading time and range.

the option is interpreted as follow:

- `bone_block`: The note block must have a bone block just above it.



- `wither_skeleton_skull` The note block must have a wither skeleton skull just above it. This skull can be either on the note block or attach to another block.



- `note_block`: No special requirement for note blocks to add tickets. Warning: as many high frequency clock will use note block, please take it carefully as it may make some chunks loading permanently in an unexpect way.



- `false`: Disable the rule.



- Type: `String`



- Default: `false`



- Suggested options: `bone_block`, `wither_skeleton_skull`, `note_block`, `false`



- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

Due to after 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, hoppers in chunks loaded by this rule will cease to function whenever there are no players in the current dimension, You can enable the `blockChunkLoaderKeepWorldTickUpdate `or `keepWorldTickUpdate` rules to solve this issue.

## bellBlockChunkLoader

When a bell block is triggered by rising edge of redstone signal, the chunk which the bell block located will be added a ticket with level 30 and type `bell_block`, which will expire after 300gt. Bell blocks have no special requirement to add tickets, You can use the rules of `blockChunkLoaderTimeController` and `blockChunkLoaderRangeController` to control their loading time and range.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

Due to after 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, hoppers in chunks loaded by this rule will cease to function whenever there are no players in the current dimension, You can enable the `blockChunkLoaderKeepWorldTickUpdate `or `keepWorldTickUpdate `rules to solve this issue.

## pistonBlockChunkLoader

When a piston (also available for sticky piston) successfully push or pull its head, the chunk which the piston head (the moving block representing piston head) located will be added a ticket with level 30 and type `piston_block`, which will expire after 300gt. The ticket will be added at the tick when the moving block is created. Pistons that can be used to add ticket must meet the requirement set by the rule option, You can use the rules of `blockChunkLoaderTimeController` and `blockChunkLoaderRangeController` to control their loading time and range.

the option is interpreted as follow:

- `bone_block`: The piston must have a bone block just above it.



- `bedrock`: The piston must have a bedrock just under it.



- `all`: When boe_block is on the piston or bed rock is under the piston.



- `false`: Disable the rule.

Due to after 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, hoppers in chunks loaded by this rule will cease to function whenever there are no players in the current dimension, You can enable the `blockChunkLoaderKeepWorldTickUpdate `or `keepWorldTickUpdate `rules to solve this issue.




- Type: `String`



- Default: `false`



- Suggested options: `bone_block`, `bedrock`, `all`, `false`



- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

## blockChunkLoaderKeepWorldTickUpdate

After 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, This rule will allow the following rules to bypass this restriction when they are loaded.

Affected rules: `noteBlockChunkLoader`, `pistonBlockChunkLoader`, `bellBlockChunkLoader`

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`


## keepWorldTickUpdate

After 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, This rule will bypass this restriction.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

## blockChunkLoaderTimeController

Used to control the loading time of block loader series rules.

Affected rules: 

noteBlockChunkLoader, pistonBlockChunkLoader, bellBlockChunkLoader

- Type: `int`



- Default: `300`



- Suggested options: `1 - 300`



- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

## blockChunkLoaderRangeController

Used to control the loading range of block loader series rules.

Affected rules: 

noteBlockChunkLoader, pistonBlockChunkLoader, bellBlockChunkLoader

- Type: `int`



- Default: `3`



- Suggested options: `1 - 32`



- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

## netherWaterPlacement

Player can place water source in the nether by using water bucket.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## fakePeace

No mob will spawn without change difficulty.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## blowUpEverything

Blast resistence of every block will be set to 0.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`

## sharedVillagerDiscounts

Villagers cured from zombified villager will give trade discounts to every player.

<Ported from [totos-carpet-tweaks](https://github.com/totorewa/totos-carpet-tweaks)>

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## extinguishedCampfire

Campfire will be extinguished by default when being placed.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## safeFlight

Hitting surface will cause no damage to player when flying with elytra.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## customBlockUpdateSuppressor

Customize a block to be an update suppressor (Not all blocks can be set as update suppressor).

Command format:

/carpet customBlockUpdateSuppressor minecraft:BlockName

Use the following command to control whether the "amsUpdateSuppressionCrashFix" rule is automatically enabled when this rule is enabled: 

/amsUpdateSuppressionCrashFixForceMode true/false

- Type: `boolean`



- Default: `false`



- Suggested options: `none`, `minecraft:bone_block`, `minecraft:diamond_ore`, `minecraft:magma_block`



- Categroies: `AMS`, `FEATURE`


## infiniteTrades

When enabled, villager trades will be prevented from locking up.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`


## invulnerable

When enabled, Players will be invulnerable.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE`


## creativeOneHitKill

When enabled, Allows players in Creative mode to kill entities in one hit, If the player is sneaking, other entities around the target get killed too.

<Ported from [lunaar-carpet-addons](https://github.com/Lunaar-SMP/lunaar-carpet-addons)>

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `CREATIVE`


## largeEnderChest

When enabled, Doubles the size of your EnderChest.

- Type: `boolean`

  

- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `SURVIVAL`


## bambooModelNoOffset

When enabled, The block model of bamboo will not generate offset.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `OPTIMIZATION`


## bambooCollisionBoxDisabled

When enabled, players can pass through bamboo.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE`


## campfireSmokeParticleDisabled

When enabled, Campfire will not produce smoke particles.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE`

## *antiFireTotem*

When enabled, Totem will not be destroyed by flames and magma.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## **itemAntiExplosion**

When enabled, Dropped items will not be destroyed by explosions.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `TNT`

## **creativeShulkerBoxDropsDisabled**

When enabled, Breaking a shulker box with items in creative mode will not cause drops.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `CREATIVE`


## **bedRockFlying**

Flight in creative mode is consistent with the bedrock version.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `CREATIVE`


## shulkerHitLevitationDisabled

When enabled, When hit by a shulker, the player only takes damage and does not gain the levitation effect.

- Type `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`


## immuneShulkerBullet

When enabled, Players are now completely immune to bullets fired by shulkers.

- Type `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## blueSkullController

When enabled，You can choose whether to have Withering always shoot blue skulls or never shoot blue skull.
`SURELY`: Always shoot blue skulls。
`NEVER`: Never shoot blue skull。
`VANILLA`: VANILLA。

- Type: `String`



- Default: `VANILLA`



- Suggested options: `SURELY` , `NEVER` , `VANILLA` 



- Categroies: `AMS` , `FEATURE` , `EXPERIMENTAL`


## endermanTeleportRandomlyDisabled

When enabled, Random teleportation by EnderMan is prohibited.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `EXPERIMENTAL`


## fasterMovement

Five gears are provided to allow players to move faster.

> You can use fasterMovementController rule to control in which dimensions this rule is effective. By default, it is effective in all dimensions.

- Type: `String`



- Default: `VANILLA`



- Suggested options: `Ⅰ`, `Ⅱ`, `Ⅲ`, `Ⅳ`, `Ⅴ`, `VANILLA`



- Categroies: `AMS` , `FEATURE` , `EXPERIMENTAL`


## fasterMovementController

Used to control in which dimensions fasterMovement rule is effective.

- Type: `String`



- Default: `all`



- Suggested options: `overworld` , `nether`, `end`, `all`



- Categroies: `AMS` , `FEATURE` , `EXPERIMENTAL`


## easyWitherSkeletonSkullDrop

When enabled, 100% Wither Skeleton Skull Drop.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `SURVIVAL`


## commandAnvilInteractionDisabled

When enabled, Players cannot open the UI of the Anvil.
Command: /anvilInteractionDisabledSwitch true/false

- Type: `String`



- Default: `false`



- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`, `COMMAND`


## preventAdministratorCheat

Disable some command to prevent accidentally cheating.

Affects command list: /gamemode, /tp, /teleport, /give, /setblock, /summon, /difficulty, /kill, /time, /weather, /fill, /setblock, /enchant, /experience, /advancement, /effect, /data, /defaultgamemode, /gamerule

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `SURVIVAL` , `COMMAND`


## amsUpdateSuppressionCrashFix

When enabled, Update suppression does not cause the server to crash, while providing the coordinates and dimensions where the update suppression occurred.

Use the following command to control whether it is forcibly enabled when `customBlockUpdateSuppressor ` is enabled, with a permission level of 2: 

/amsUpdateSuppressionCrashFixForceMode true/false

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `SURVIVAL` , `EXPERIMENTAL`

## ghastFireballExplosionDamageSourceFix

Fix MC-193297 that the large fireball does not create explosion with correct damage source.

<Available for Minecraft < 1.19.3>

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `SURVIVAL` , `EXPERIMENTAL`, `BUGFIX`


## cakeBlockDropOnBreak

When enabled, When the cake is destroyed, it can drop cake (the cake will only drop when it is intact).

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `SURVIVAL`


## noCakeEating

When enabled, Players are not allowed to consume cake.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `SURVIVAL`


## redstoneComponentSound

When enabled, When players right-click on the redstone component, it will emit a sound

Affected components: daylight detector, redstone dust, repeater

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE`


## largeShulkerBox

Doubles the size of your ShulkerBox.
To use this rule, please follow the steps below:

1 - To ensure that no shulker boxes are loaded.

2 - Enter the command: `/carpet setDefault largeShulkerBox true`, to turn on the rule.

3 - Restart the server/single-player world.

4 - Disabling the rule follows the same process.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE`, `EXPERIMENTAL`

## maxPlayerInteractionDistance


Change the maximum interaction distance allowed by the server, set to `-1` to disable this rule

- Type: `double`



- Default: `-1`



- Suggested options: `0 - 512`, `-1`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## maxClientInteractionReachDistance

Change the maximum interaction distance allowed by the client, set to `-1` to disable this rule.


Need to disable the tweakBlockReachOverride feature in Tweakeroo.


- Type: `float`



- Default: `-1`



- Suggested options: `0 - 512`, `-1`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## customMovableBlock

Customize the non-pushable block to make it pushable.
Command format:
/carpet customMovableBlock minecraft:BlockName
You can also set multiple blocks simultaneously by separating them with commas:
/carpet customMovableBlock minecraft:BlockName1,minecraft:BlockName2


- Type: `String`



- Default: `VANILLA`



- Suggested options: `VANILLA`, `minecraft:bedrock`, `minecraft:bedrock,minecraft:obsidian`



- Categroies: `AMS`, `FEATURE`

## eazyMaxLevelBeacon

When enabled, Activate full-level beacon with just one base block.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## commandCustomBlockBlastResistance

Use commands to customize the explosion resistance of any block (this rule will not take effect when the enhancedWorldEater rule is enabled).
After enabling the rules, use the `/customBlockBlastResistance help` command to view the usage instructions.

- Type: `String`



- Default: `false`



- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`, `COMMAND`

## regeneratingDragonEgg

When enabled，Every time a player defeats the Ender Dragon, a dragon egg will be generated.

> For Dnsolx eggs

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## enhancedWorldEater

Make way for the WorldEater by customizing the explosion resistance of the blocks listed below (this rule will override the `customBlowUpBlock ` rule when enabled)

Block list:

Blocks with blast resistance < 17.0F, Bedrock, Anvil, End Portal Frame, End Portal, End Gateway

- Type: `double`



- Default: `-1`



- Suggested options: `-1` , `0 - 16`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL` `TNT`

## sneakToEditSign

When enabled，Players can sneak with empty hands to edit already placed signs by using the interact key, In `Minecraft >= 1.20`, the behavior is such that players must sneak in order to edit a sign.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## fancyFakePlayerName

Add green prefixes and suffixes to the fake player summoned by the carpet mod, the prefix doesn't need to be input when using the command, but the suffix is indeed present.

Example：1024_byteeeee -> [bot] 1024_byteeeee_bot

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## fakePlayerNoScoreboardCounter

The scoreboard will hide the fake player.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## noFamilyPlanning

Allow players to continuously feed animals to breed them.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## hopperSuctionDisabled

When enabled，The hopper will not suck in items.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## noEnchantedGoldenAppleEating

Prevent players from accidentally eating Enchanted Golden Apples.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## useItemCooldownDisabled

Remove cooldown time for using items.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## flippinCactusSoundEffect

When the flippinCactus rule is enabled in the Carpet Mod, using the cactus will produce sound effects (providing five different sound options, Setting it to 0 disables the sound effects).

- Type: `int`



- Default: `0`



- Suggested options: `0`, `1`, `2`, `3`, `4`, `5`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## undyingCoral

Prevent coral blocks and coral fans from dying.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## enderDragonNoDestroyBlock

Make the Ender Dragon unable to destroy any blocks.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## easyCompost

Make every composting successful.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## easyMineDragonEgg

Makes the dragon egg not teleport, Players can mine dragon eggs to collect them.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## breedableParrots

Customize a food to feed the parrots and breed them (You can also feed them with stones if you really want to).

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## sensibleEnderman

Make Endermen only pick up watermelons and pumpkins.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## endermanPickUpDisabled

Preventing Endermen from picking up blocks.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## mitePearl

Using an Ender Pearl each time will spawn an Endermite.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## enderPearlSoundEffect

A sound effect will be played when the player uses an ender pearl for teleportation.

<Available for Minecraft < 1.20>

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## commandHere

Use the "/here" command to send the current dimension, coordinates, and corresponding overworld/nether coordinates of your current location and give oneself a 30s glowing effect.

- Type: `String`



- Default: `false`



- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- Categroies: `AMS`, `COMMAND`

## commandWhere

Use the "/where" command to get the specified player's dimension, coordinates, and corresponding Overworld/Nether coordinates and give the target player a 30s glowing effect.

- Type: `String`



- Default: `false`



- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- Categroies: `AMS`, `COMMAND`

## fakePlayerPickUpController

Two modes are provided to control whether fake player can pick up items.

- [MainHandOnly] - Only the main hand can pick up items



- [NoPickUp] - Unable to pick up items at all



- [false] - Disable rule



- Type: `String`



- Default: `false`



- Suggested options: `MainHandOnly`, `NoPickUp`, `false`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## commandPlayerLeader

Designate one or more players as guides, granting them a glowing effect.

After enabling the rules, use the `/leader help` command to view the usage instructions

- Type: `String`



- Default: `false`



- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- Categroies: `AMS`, `COMMAND`

## commandPacketInternetGroper

player can use the "ping" command in the game.

After enabling the rules, use the `/ping help` command to view the usage instructions

- Type: `String`



- Default: `false`



- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- Categroies: `AMS`, `COMMAND`

## playerNoNetherPortalTeleport

Players cannot teleport through nether portals.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## infiniteDurability

Use items without losing durability.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## welcomeMessage

When players join the server, send them a custom message (Use json files to customize messages).

json location:

[ save path ]/carpetamsaddition/welcomeMessage.json

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`

## commandGetSaveSize

Use the "/getSaveSize" command to get the current save size.

- Type: `String`



- Default: `false`



- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- Categroies: `AMS`, `COMMAND`

## carpetAlwaysSetDefault

Whenever you set the carpet rule, it will be automatically set to the default value.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## experimentalContentCheckDisabled

Allow playing Minecraft experimental content in the game by adding data packs.

< Available for Minecraft >= 1.19 >

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `EXPERIMENTAL`

## craftableEnchantedGoldApple

Enchanted gold apple can be crafted using gold block and apple, which is the original crafting recipe before 15w44a.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `SURVIVAL`, `CRAFTING`

## craftableBundle

Bundle can be crafted using string and rabbit hide.
<Available for Minecraft \>= 1.17>

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `SURVIVAL`, `CRAFTING`

## craftableSculkSensor

Sculk sensor can be crafted using deepslate, redstone and quartz.
<Only available for Minecraft == 1.17 or Minecraft == 1.18>

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `SURVIVAL`, `CRAFTING`

## craftableElytra

Elytra can be crafted using phantom membrane, saddle, stick and string.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `SURVIVAL`, `CRAFTING`

## betterCraftableBoneBlock

Bone block can be crafted better. Directed use 9 bones to craft 3 bone blocks.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `SURVIVAL`, `CRAFTING`

## betterCraftableDispenser

Dispenser can be crafted more flexible. It can be crafted using bow and dropper, or using raw material of bow and dropper.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `SURVIVAL`, `CRAFTING`

## betterCraftablePolishedBlackStoneButton

Use deepslate to crafted polished_blackstone_button in minecraft.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `SURVIVAL`, `CRAFTING`

## enableMinecraft1_21ExperimentalRecipes

Unlock crafting recipes for all items in the 1.21 experimental content.

Using this rule suggests synchronously enabling experimentalContentCheckDisabled to prevent clients from being unable to use blocks.

< Available for Minecraft 1.20.3 - 1.20.4 >

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `SURVIVAL`, `CRAFTING`
