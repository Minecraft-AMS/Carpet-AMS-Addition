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

## commandChunkLoading

Control chunkloading of player. Won't remove player detection of deminsion like main island loading in the end. Will be reset after player loging out to avoid [MC-157812](https://bugs.mojang.com/browse/MC-157812).

Command: /chunkloading

<Ported from [Intricarpet](https://github.com/lntricate1/intricarpet)>

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `COMMAND`

## noteBlockChunkLoader

When a note block is triggered by rising edge of redstone signal, the chunk which the note block located will be added a ticket with level 30 and type 'note_block', which will expire after 300gt. Note blocks that can be used to add ticket must meet the requirement set by the rule option.

the option is interpreted as follow:

- `bone_block`: The note block must have a bone block just above it.



- `wither_skeleton_skull` The note block must have a wither skeleton skull just above it. This skull can be either on the note block or attach to another block.



- `note_block`: No special requirement for note blocks to add tickets. Warning: as many high frequency clock will use note block, please take it carefully as it may make some chunks loading permanently in an unexpect way.



- `OFF`: Disable the rule.

&emsp;

- Type: `String`



- Default: `OFF`



- Suggested options: `bone_block`, `wither_skeleton_skull`, `note_block`, `OFF`



- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

&emsp;

Due to after 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, hoppers in chunks loaded by this rule will cease to function whenever there are no players in the current dimension, You can enable the blockChunkLoaderKeepTickEntities or keepEntityUpdate rules to solve this issue, but I personally recommend using the method of placing fake players to work around this problem.

## bellBlockChunkLoader

When a bell block is triggered by rising edge of redstone signal, the chunk which the bell block located will be added a ticket with level 30 and type 'bell_block', which will expire after 300gt. Bell blocks have no special requirement to add tickets.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

&emsp;

Due to after 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, hoppers in chunks loaded by this rule will cease to function whenever there are no players in the current dimension, You can enable the blockChunkLoaderKeepTickEntities or keepEntityUpdate rules to solve this issue, but I personally recommend using the method of placing fake players to work around this problem.

## pistonBlockChunkLoader

When a piston (also available for sticky piston) successfully push or pull its head, the chunk which the piston head (the moving block representing piston head) located will be added a ticket with level 30 and type 'piston_block', which will expire after 300gt. The ticket will be added at the tick when the moving block is created. Pistons that can be used to add ticket must meet the requirement set by the rule option.



the option is interpreted as follow:

- `bone_block`: The piston must have a bone block just above it.



- `bedrock`: The piston must have a bedrock just under it.



- `all`: When boe_block is on the piston or bed rock is under the piston.



- `OFF`: Disable the rule.



Due to after 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, hoppers in chunks loaded by this rule will cease to function whenever there are no players in the current dimension, You can enable the blockChunkLoaderKeepTickEntities or keepEntityUpdate rules to solve this issue, but I personally recommend using the method of placing fake players to work around this problem.




- Type: `String`



- Default: `OFF`



- Suggested options: `bone_block`, `bedrock`, `all`, `OFF`



- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

## netherWaterPlacement

Player can place water source in the nether by using water bucket.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`

## softDeepslate

Hardness of deepslate will be set to 1.5, which is equal to the hardness of stone. Other words, player with Haste II using an Efficiency V diamond pickaxe can break deepslate instantly.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## softObsidian

Hardness of obsidian will be set to 3, which is equal to the hardness of original deepslate.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

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

## weakObsidian

Explosion can destroy obsidian. 
Warning: This function (also for other similar rules) will slightly reduce performance when calculating explosion.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`

## weakCryingObsidian

Explosion can destroy crying obsidian.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`

## weakBedRock

Explosion can destroy bedrock.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`

## enhancedWorldEater

Explosion can destroy all blocks except anvil and bedrock.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`

## weakReinforcedDeepslate

Explosion can destroy reinforced deepslate.
<Only available for Minecraft >= 1.19>

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

## boneBlockUpdateSuppressor

When enabled, bone block will be a update suppressor (When using it, it is recommended to start the Update Suppression Crash Fix feature to prevent crashes).

<Only available for Minecraft < 1.19 >

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



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


## kirinArm

When enabled, Players can instantly break any block except bedrock with bare hands.

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


## enderManTeleportRandomlyDisabled

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



- Suggested options: `Ⅰ` , `Ⅱ`, `Ⅲ`, `Ⅳ`, `Ⅴ`, `VANILLA`



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


## anvilInteractionDisabled

When enabled, Players cannot open the UI of the Anvil.
Command: /anvilInteractionDisabledSwitch

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `SURVIVAL`


## opPlayerNoCheatExtra

When enabled, Disable some command to prevent accidentally cheating, It is recommended to use it with opPlayerNoCheat from Carpet TIS Addition.

Affects command list: /difficulty, /kill, /time, /weather, /fill, /setblock, /enchant, /experience, /advancement, /effect, /data, /defaultgamemode

(Carpet TIS Addition)Affects command list: /gamemode, /tp, /teleport, /give, /setblock, /summon

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `SURVIVAL` , `COMMAND`


## amsUpdateSuppressionCrashFix

When enabled, Update suppression does not cause the server to crash.

<Ported from [carpet-extra](https://github.com/gnembon/carpet-extra)>

<Only available for Minecraft < 1.19 >

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `SURVIVAL` , `EXPERIMENTAL`


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


## blockChunkLoaderKeepTickUpdate

After 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, This rule will allow the following rules to bypass this restriction when they are loaded.

Affected rules: `noteBlockChunkLoader`, `pistonBlockChunkLoader`, `bellBlockChunkLoader`

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE`


## keepEntityUpdate

After 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, This rule will bypass this restriction.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE`


## largeShulkerBox

Doubles the size of your ShulkerBox.
The current rule is not yet perfect. Please follow the steps below when using this rule:

1 - To ensure that no shulker boxes are loaded.

2 - Enter the command: `/carpet setDefault largeShulkerBox true`, to turn on the rule.

3 - Restart the server/single-player world.

4 - Disabling the rule follows the same process.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE`, `EXPERIMENTAL`


## movableEnderChest

Ender chest can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableEndPortalFrame

End portal frame can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableObsidian

Obsidian can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableCryingObsidian

Crying obsidian can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableBedRock

Bedrock can be moved by piston or sticky piston.
- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableEnchantingTable

Enchanting table can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableBeacon

Beacon can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableReinforcedDeepslate

Reinforced deepslate can be moved by piston or sticky piston.
<Only available for Minecraft \>= 1.19>

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableAnvil

Anvil can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableSculkCatalyst

Sculk catalyst can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableSculkSensor

Sculk sensor can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableSculkShrieker

Sculk shrieker can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false`, `true`



- Categroies: `AMS`, `FEATURE`, `AMS_movable`

## movableCalibratedSculkSensor

Calibrated Sculk Sensor can be moved by piston or sticky piston.

- Type: `boolean`



- Default: `false`



- Suggested options: `false` , `true`



- Categroies: `AMS` , `FEATURE` , `AMS_movable`

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
