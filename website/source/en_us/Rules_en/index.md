## superBow

Enabling making super bows with both infinite and mending enchants

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## scheduledRandomTickAllPlants

Make all plants accepts scheduled tick as random tick

- `scheduledRandomTickCactus`: Make cactus accepts scheduled tick as random tick

- `scheduledRandomTickBamboo`: Make bamboo accepts scheduled tick as random tick

- `scheduledRandomTickChorusFlower`: Make chorus flower accepts scheduled tick as random tick

- `scheduledRandomTickSugarCane`: Make sugar cane accepts scheduled tick as random tick

- `scheduledRandomTickStem`: Make stems accepts scheduled tick as random tick

> Ported from: [OhMyVanillaMinecraft](https://github.com/hit-mc/OhMyVanillaMinecraft)

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## optimizedDragonRespawn

Optimize dragon respawning

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `OPTIMIZATION`

## commandPlayerChunkLoadController

Control chunk loading for players at any gamemodes

- Command:
- /playerChunkLoading true/false

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `COMMAND`, `AMS_chunkLoader`

## commandNoteblockChunkloader

Use commands to customize a note block chunk loader

- After enabling the rule, use the "/blockChunkloader help" command to view the usage instructions

- Type: `String`
- Default: `false`
- Suggested optionss: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `AMS`, `COMMAND`, `AMS_chunkLoader`

## commandBellChunkloader

Use commands to customize a bell chunk loader

- After enabling the rule, use the "/blockChunkloader help" command to view the usage instructions

- Type: `String`
- Default: `false`
- Suggested options: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `AMS`, `COMMAND`, `AMS_chunkLoader`

## commandPistonChunkloader

Use commands to customize a piston chunk loader (centered on the piston head)

- After enabling the rule, use the "/blockChunkloader help" command to view the usage instructions

- Type: `String`
- Default: `false`
- Suggested options: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `AMS`, `COMMAND`, `AMS_chunkLoader`

## keepWorldTickUpdate

After 300 ticks without any players in the current dimension on the server, Minecraft will stop entities updates, This rule will bypass this restriction

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `AMS_chunkLoader`

## netherWaterPlacement

Players can use water buckets to place water in nether

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## fakePeace

Simulation fake Peace, You can customize the dimensions in which it works, or you can customize the combination and use "," to separate

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## blowUpEverything

Set all blocks BlastResistance to 0

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`

## sharedVillagerDiscounts

Share villagers discount to all players

> Ported from: [totos-carpet-tweaks](https://github.com/totorewa/totos-carpet-tweaks)

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## extinguishedCampfire

The campfire is extinguished when the player places it

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## safeFlight

players don't get hurt by flying into walls

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## infiniteTrades

Prevents villager trades from locking up

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## invulnerable

Players will be protected from all damage except Void Damage

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE`

## creativeOneHitKill

Allows players in Creative mode to kill entities in one hit, If the player is sneaking, other entities around the target get killed too

> Ported from: [lunaar-carpet-addons](https://github.com/Lunaar-SMP/lunaar-carpet-addons)

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE` , `CREATIVE`

## largeEnderChest

Doubles the size of your EnderChest

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE` , `SURVIVAL`

## bambooModelNoOffset

The block model of bamboo will not generate offset

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE` , `OPTIMIZATION`

## bambooCollisionBoxDisabled

Allow players to pass through bamboo

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE`

## commandCustomAntiFireItems

Use command to control whether any item is fireproof or not

- After enabling the rule, use the "/customAntiFireItems help" command to view the usage instructions

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `FEATURE`, `COMMAND`

## itemAntiExplosion

Dropped items will not be destroyed by explosions

- [false] - Disable rule
- [true] - Enable rule
- [no_blast_wave] - Explosion-proof and non-shockwave, the explosion will not push the item

- Type: `String`

- Default: `false`

- Suggested options: `false`, `true`, `no_blast_wave`

- Categories: `AMS`, `FEATURE`, `TNT`

## creativeShulkerBoxDropsDisabled

Breaking a shulker box with items in creative mode will not cause drops

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `CREATIVE`

## shulkerHitLevitationDisabled

When hit by a shulker, the player only takes damage and does not gain the levitation effect

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## immuneShulkerBullet

Players are now completely immune to bullets fired by shulkers

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## blueSkullController

Control the probability of wither and emitting blue skull

- [SURELY] - Always shoot blue skulls
- [NEVER] - Never shoot blue skull
- [VANILLA] - VANILLA

- Type: `enum`

- Default: `VANILLA`

- Suggested options: `SURELY` , `NEVER` , `VANILLA` 

- Categories: `AMS` , `FEATURE` , `EXPERIMENTAL`

## endermanTeleportRandomlyDisabled

Random teleportation by Enderman is prohibited

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE` , `EXPERIMENTAL`

## fasterMovement

Five gears are provided to allow players to move faster

- You can use fasterMovementController rule to control in which dimensions this rule is effective. By default, it is effective in all dimensions.

- Type: `String`

- Default: `VANILLA`

- Suggested options: `Ⅰ`, `Ⅱ`, `Ⅲ`, `Ⅳ`, `Ⅴ`, `VANILLA`

- Categories: `AMS` , `FEATURE` , `EXPERIMENTAL`

## fasterMovementController

Used to control in which dimensions fasterMovement rule is effective.

- [overworld] - Only effective in the Overworld
- [nether] - Only effective in the Nether
- [end] - Only effective in the End
- [all] - Effective in all dimensions

- Type: `enum`

- Default: `all`

- Suggested options: `overworld` , `nether`, `end`, `all`

- Categories: `AMS` , `FEATURE` , `EXPERIMENTAL`

## easyWitherSkeletonSkullDrop

100% Wither Skeleton Skull Drop

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE` , `SURVIVAL`

## commandAnvilInteractionDisabled

Players cannot open the UI of the Anvil

- Command: /anvilInteractionDisabled true/false

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`, `COMMAND`

## preventAdministratorCheat

Disable some command to prevent accidentally cheating

- Affects command list:
- /gamemode, /tp, /teleport, /give, /setblock, /summon, /difficulty, /kill, /time, /weather, /fill, /setblock, /enchant, /experience, /advancement, /effect, /data, /defaultgamemode, /gamerule

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE` , `SURVIVAL` , `COMMAND`

## ghastFireballExplosionDamageSourceFix

> Version: `Minecraft < 1.19.3`

Fix MC-193297 where ghast fireball explosions do not correctly attribute their damage source.

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categroies: `AMS` , `FEATURE` , `SURVIVAL` , `EXPERIMENTAL`, `BUGFIX`

## cakeBlockDropOnBreak

When the cake is destroyed, it can drop cake (the cake will only drop when it is intact)

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE` , `SURVIVAL`

## noCakeEating

Players are not allowed to consume cake

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE` , `SURVIVAL`

## redstoneComponentSound

When players right-click on the redstone component, it will emit a sound

- Affected components:
- daylight detector, redstone dust, repeater

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE`

## largeShulkerBox

Doubles the size of your ShulkerBox

- Type: `boolean`

- Default: `false`

- Suggested options: `false` , `true`

- Categories: `AMS` , `FEATURE`, `EXPERIMENTAL`

## maxPlayerBlockInteractionRange

Change the maximum block interaction distance allowed by the server, set to "-1" to disable this rule

- The range of values: 0 - 512
- When Minecraft >= 1.20.5, you can use the rule "maxPlayerBlockInteractionRangeScope" to modify its scope
- Changing a value in global mode requires the player to re-enter the game

- Type: `double`

- Default: `-1`

- Suggested options: `0 - 512`, `-1`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## maxPlayerEntityInteractionRange

Change the maximum entity interaction distance allowed by the server, set to "-1" to disable this rule

- The range of values: 0 - 512
- When Minecraft >= 1.20.5, you can use the rule "maxPlayerEntityInteractionRangeScope" to modify its scope
- Changing a value in global mode requires the player to re-enter the game

- Type: `double`

- Default: `-1`

- Suggested options: `0 - 512`, `-1`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## maxPlayerBlockInteractionRangeScope

> Version: `Minecraft >= 1.20.5`

Change the scope of the "maxPlayerBlockInteractionRange" rule

- [server] - The client needs to modify the interaction distance on its own, and the server only allows modifications made by the client
- [global] - The distance modification will apply to both the server and the client simultaneously, eliminating the need for separate adjustments by the client

- Type: `String`

- Default: `server`

- Suggested options: `server`, `global`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## maxPlayerEntityInteractionRangeScope

> Version: `Minecraft >= 1.20.5`

Change the scope of the "maxPlayerEntityInteractionRange" rule

- [server] - The client needs to modify the interaction distance on its own, and the server only allows modifications made by the client
- [global] - The distance modification will apply to both the server and the client simultaneously, eliminating the need for separate adjustments by the client

- Type: `String`

- Default: `server`

- Suggested options: `server`, `global`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## maxClientInteractionReachDistance

Change the maximum interaction distance allowed by the client, set to "-1" to disable this rule

- The range of values: 0 - 512
- Need to disable the tweakBlockReachOverride feature in Tweakeroo

- Type: `double`

- Default: `-1`

- Suggested options: `0 - 512`, `-1`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## commandCustomMovableBlock

Customize the non-pushable block to make it pushable (Container blocks are not supported, and if you need to push containers, you can use Carpet's movableBlockEntities rule)

- After enabling the rule, use the "/customMovableBlock help" command to view the usage instructions

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `FEATURE`, `COMMAND`

## easyMaxLevelBeacon

Activate full-level beacon with just one base block

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## commandCustomBlockBlastResistance

Use commands to customize the explosion resistance of any block

- After enabling the rule, use the "/customBlockBlastResistance help" command to view the usage instructions

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`, `COMMAND`

## regeneratingDragonEgg

Every time a player defeats the Ender Dragon, a dragon egg will be generated

- For Dnsolx eggs

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## sneakToEditSign

Players can sneak with empty hands to edit already placed signs by using the interact key

- In Minecraft >= 1.20, the behavior is such that players must sneak in order to edit a sign

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## sneakToEatCake

Players can only eat cake while sneaking

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## fancyFakePlayerName

Add green prefixes and suffixes to the fake player summoned by the carpet mod, the prefix doesn't need to be input when using the command, but the suffix is indeed present

- Note: This rule will create a team named with the value you input
- Example：1024_byteeeee -> [bot] 1024_byteeeee_bot

- Type: `String`

- Default: `false`

- Suggested options: `false`, `bot`, `fake_player`

- Categories: `AMS`, `FEATURE`

## fakePlayerNoScoreboardCounter

The scoreboard will hide the fake player

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## noFamilyPlanning

Allow players to continuously feed animals to breed them

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## hopperSuctionDisabled

The hopper will not suck in items

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## noEnchantedGoldenAppleEating

Prevent players from accidentally eating Enchanted Golden Apples

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## useItemCooldownDisabled

Remove cooldown time for using items

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## flippinCactusSoundEffect

When the flippinCactus rule is enabled in the Carpet Mod, using the cactus will produce sound effects (providing five different sound options, Setting it to 0 disables the sound effects)

- Type: `int`

- Default: `0`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `5`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## undyingCoral

Prevent coral blocks and coral fans from dying

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## enderDragonNoDestroyBlock

Make the Ender Dragon unable to destroy any blocks

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## easyCompost

Make every composting successful

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## easyMineDragonEgg

Makes the dragon egg not teleport, Players can mine dragon eggs to collect them

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## breedableParrots

Customize a food to feed the parrots and breed them (You can also feed them with stones if you really want to)

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## kirinArm

Allowing players to instantly mine all mineable blocks

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## sensibleEnderman

Make Endermen only pick up watermelons and pumpkins

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## endermanPickUpDisabled

Preventing Endermen from picking up blocks

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## mitePearl

Using an Ender Pearl each time will spawn an Endermite

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## enderPearlSoundEffect

> Version: `Minecraft < 1.20`

A sound effect will be played when the player uses an ender pearl for teleportation.

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categroies: `AMS`, `FEATURE`

## commandHere

Use the "/here" command to send the current dimension, coordinates, and corresponding overworld/nether coordinates of your current location and give oneself a 30s glowing effect

- When the client has installed Fuzz Mod, they can click the +H button at the end of the message to highlight the waypoint, and use the \"/coordCompass clear\" command to remove the highlight

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `COMMAND`

## commandWhere

Use the "/where" command to get the specified player's dimension, coordinates, and corresponding Overworld/Nether coordinates and give the target player a 30s glowing effect

- When the client has installed Fuzz Mod, they can click the +H button at the end of the message to highlight the waypoint, and use the \"/coordCompass clear\" command to remove the highlight

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `COMMAND`

## fakePlayerPickUpController

Two modes are provided to control whether fake player can pick up items

- [MainHandOnly] - Only the main hand can pick up items
- [NoPickUp] - Unable to pick up items at all
- [false] - Disable rule

- Type: `String`

- Default: `false`

- Suggested options: `MainHandOnly`, `NoPickUp`, `false`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## commandPlayerLeader

Designate one or more players as guides, granting them a glowing effect

- After enabling the rule, use the "/leader help" command to view the usage instructions

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `COMMAND`

## commandPacketInternetGroper

player can use the "ping" command in the game

- After enabling the rule, use the "/ping help" command to view the usage instructions

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `COMMAND`

## commandPlayerNoNetherPortalTeleport

Players cannot teleport through nether portals

- After enabling the rule, use the "/playerNoNetherPortalTeleport help" command to view the usage instructions

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `FEATURE`, `SURVIVAL` `COMMAND`

## infiniteDurability

Use items without losing durability

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## welcomeMessage

When players join the server, send them a custom message (Use json files to customize messages)

- json location:
- [ save path ]/carpetamsaddition/welcomeMessage.json

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`

## commandGetSaveSize

Use the "/getSaveSize" command to get the current save size

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `COMMAND`

## carpetAlwaysSetDefault

Whenever you set the carpet rule, it will be automatically set to the default value

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## experimentalContentCheckDisabled

> Version: `Minecraft 1.19–1.21.1`

Allow playing Minecraft experimental content in the game by adding data packs (need restart server)

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `EXPERIMENTAL`

## fertilizableSmallFlower

Allow small flowers such as dandelion can also be ripened by bone meal

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## safePointedDripstone

> Version: `Minecraft >= 1.17`

When players land on pointed dripstone, they do not take additional damage from it

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## pointedDripstoneCollisionBoxDisabled

> Version: `Minecraft >= 1.17`

Allow players to pass through pointed dripstone

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## foliageGenerateDisabled

The foliage will not generate in any way

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## commandGetSystemInfo

Use the "/getSystemInfo" command to get the system information of the server

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `COMMAND`

## ironGolemNoDropFlower

Iron golems will not drop flowers

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## easyGetPitcherPod

> Version: `Minecraft >= 1.20`

Players can harvest a random amount of pitcher pods by planting pitcher (The maximum quantity is customizable, while the minimum quantity is fixed at 2)

- The range of values: 2 - 100 or 0

- Type: `int`

- Default: `0`

- Suggested options: `0 or 2 - 100`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## commandGoto

Use the "/goto" command to teleport to the specified dimension and coordinates

- When no coordinates are specified, it will teleport to the corresponding coordinates of the current player location

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `COMMAND`

## sendPlayerDeathLocation

When a player dies, a message with their death coordinates and dimension will be broadcasted

- [false] - Disable rule
- [all] - Always send messages
- [realPlayerOnly] - Messages will only be sent when real players die
- [fakePlayerOnly] - Messages will only be sent when fake players die
- When the client has installed Carpet Org Addition, they can click the +H button at the end of the message to highlight the waypoint, and use the /highlight clear command to remove the highlight

- Type: `String`

- Default: `false`

- Suggested options: `false`, `all`, `realPlayerOnly`, `fakePlayerOnly`

- Categories: `AMS`, `SURVIVAL`

## perfectInvisibility

When the player is invisible, they will be completely hidden from mobs, even when wearing a full set of armor

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## sneakInvisibility

When the player is sneak, they will be completely hidden from mobs, even when wearing a full set of armor

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## commandCustomCommandPermissionLevel

Customize the permission level of any commands

- After enabling the rule, use the "/customCommandPermissionLevel help" command to view the usage instructions

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `SURVIVAL`, `COMMAND`

## shulkerGolem

A shulker can be summoned by placing a carved pumpkin on the shulker box

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## headHunter

When a player dies, they will drop their own skull

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## commandGetPlayerSkull

Use the "/getPlayerSkull <player> <count>" command to get a player skull

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `SURVIVAL`, `COMMAND`

## quickVillagerLevelUp

Each time the trading screen is opened, the villager will level up

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## renewableNetheriteScrap

Customize the drop rate of netherite scrap from zombified piglin

- The range of values: 0 - 1

- Type: `Double`

- Default: `0.0`

- Suggested options: `0.0`, `0.01`, `0.1`, `1.0`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## fakePlayerInteractLikeClient

Fix some cases that fake player interact differently from the client

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `BUGFIX`

## stackableDiscounts

> Version: `Minecraft >= 1.20.2`

Reintroduce the stackable discounts feature in <23w31a

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## preventEndSpikeRespawn

Prevent end spikes (obsidian spikes in the_end) from spawning

- [true] - Prohibited from generating
- [false] - Disable Rule
- [keepEndCrystal] - Preserve crystal generation

- Type: `String`

- Default: `false`

- Suggested options: `false`, `true`, `keepEndCrystal`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## strongLeash

The leash will not break due to excessive distance

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## superZombieDoctor

Players can use a Weakness Potion + Golden Apple to instantly convert a Zombie Villager into a Villager

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## fakePlayerUseOfflinePlayerUUID

Let fake players use offline UUIDs

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## superLeash

Villagers and monsters can be leashed as well

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## fullMoonEveryDay

Full moon every day

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## easyRefreshTrades

When a villager has not made a deal with the player before, each time the trade screen is opened, the content sold by the villager will be refreshed

- When the player is holding an emerald block in their main hand, the refresh will not be triggered

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## jebSheepDropRandomColorWool

When a sheep is named jeb_, shearing its wool will drop wool of a random color

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## cryingObsidianNetherPortal

Nether portals can be constructed using crying obsidian

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## furnaceSmeltingTimeController

The time required to control the smelting of items in the furnace (unit: Tick)

- Type: `int`

- Default: `-1`

- Suggested options: `-1`, `1 - ?`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## fakePlayerDefaultSurvivalMode

When summoning fake players, they are default to survival mode anyway

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## commandGetHeldItemID

Use "/getHeldItemID" command to get item ID of the player main hand item

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `SURVIVAL`, `COMMAND`

## stringDupeReintroduced

> Version: `Minecraft >= 1.21.2`

Reintroduced the string dupe feature, you can use this rule to continue using the string farm

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## witchRedstoneDustDropController

Set the guaranteed minimum amount of Redstone Dust dropped by witches

- Type: `int`

- Default: `-1`

- Suggested options: `-1 - 999999`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## witchGlowstoneDustDropController

Set the guaranteed minimum amount of Glowstone Dust dropped by witches

- Type: `int`

- Default: `-1`

- Suggested options: `-1 - 999999`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## tntPowerController

Controls the explosive power of TNT, setting it to "-1" to disable the rule

- Type: `double`

- Default: `-1.0D`

- Suggested options: `-1.0D - 999999D`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`

## meekEnderman

You can casually glance at endermen

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## customizedNetherPortal

Fully customizable Nether portal. after enabled, the Nether portal blocks can be mined, and mining the Nether portal frame or the portal itself will not cause the nether portal to broken

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## commandCarpetExtensionModWikiHyperlink

Some commonly used Carpet extension wiki links are listed. You only need to use the "/carpetExtensionModWikiHyperlink <ExtensionName>" command to get the hyperlink in the chat box, and you can click the link to jump to it

- As the version is updated, the list of mods will be constantly adjusted

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `SURVIVAL`, `COMMAND`

## commandCustomBlockHardness

Use command to customizing mining hardness for any block

- After enabling the rule, use the "/customBlockBlastResistance helper" command to view the usage instructions
- When the Carpet AMS Addition is not installed on the client, this rule is completely unavailable

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`, `AMS_NETWORK`, `COMMAND`

## onlyOpCanSpawnRealPlayerInWhitelist

Only OP players can spawn players on the whitelist, regardless of whether the whitelist is enabled

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `COMMAND`

## itemEntityCreateNetherPortalDisabled

> Version: `Minecraft >= 1.21`

Prevent item entities from creating portals when passing through Nether portals

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## powerfulExpMending

Allow items to receive the Mending effect simply by being in the backpack, without needing to be held in hand

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## commandAtSomeOnePlayer

Use /@ command to @ some one player

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `COMMAND`

## largeBundle

> Version: `Minecraft >= 1.17`

Let the bundle have a 9x3 or 9x6 UI, and right-click in the hand to open the UI

- This rule does not allow bundle and shulker boxes to be placed in bundle

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## maxChainUpdateDepth

> Version: `Minecraft >= 1.19`

Modify max chain update depth, Set to "-1" to disable the rule

- Type: `int`

- Default: `-1`

- Suggested options: `-1`, `100000`, `200000`, `300000`

- Categories: `AMS`, `FEATURE`, `EXPERIMENTAL`

## phantomSpawnAlert

Broadcast the news of who summoned the phantoms

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `SURVIVAL`

## endPortalChunkLoadDisabled

> Version: `Minecraft >= 1.21`

Entities passing through the End Portal will not cause chunk loading (Available for Minecraft >= 1.20.6)

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## endPortalChunkLoader

> Version: `Minecraft < 1.20.5`

Passing entities through the End Portal causes chunk loading, consistent with Minecraft >= 1.20.5 (Available for Minecraft < 1.20.5)

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `AMS_chunkLoader`

## amsNetworkProtocol

The switch of the AMS network protocol

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `AMS_NETWORK`

## incompatibleProtocolWarningDisabled

Disable warnings sent to clients with an incompatible AMS network protocol. Only applies when amsNetworkProtocol is enabled

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `AMS_NETWORK`

## commandGetClientPlayerFps

Use command to get the FPS of the specified player

- After enabling the rule, use the "/getClientPlayerFps help" command to view the usage instructions

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `AMS_NETWORK`, `COMMAND`

## commandSetPlayerPose

Use command to set player pose

- After enabling the rule, use the "/playerPose help" command to view the usage instructions

- Type: `String`

- Default: `false`

- Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

- Categories: `AMS`, `AMS_NETWORK`, `COMMAND`

## commandAmspDebug

Some commands for network communication testing

* Type: `String`

* Default: `false`

* Suggested options: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`

* Categories: `AMS`, `AMS_NETWORK`, `COMMAND`

## onlyPlayerCanCreateNetherPortal

> Version: `Minecraft >= 1.21`

Only allow new portals to be generated when players pass through the Nether portal

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## preventServerPause

> Version: `Minecraft >= 1.21.2`

Prevent server pause after 60s

* Type: `boolean`

* Default: `false`

* Suggested options: `false`, `true`

* Categories: `AMS`, `FEATURE`

## flippinCactusExtras

Enables flippinCactus rules to work on more blocks

- Affected blocks:
- barrel, chiseled_bookshelf, crafter, shelf

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`

## experimentalMinecartSpeed

> Version: `Minecraft >= 1.21.2`

Set the speed of the experimental minecart

- Enabling this rule will override the game rules

* Type: `int`

* Default: `-1`

* Suggested options: `-1`, `1000`

* Categories: `AMS`, `FEATURE`, `EXPERIMENTAL`

## experimentalMinecartEnabled

> Version: `Minecraft >= 1.21.2`

Enable experimental minecarts

- When the server environment switches the rule setting, the server needs to be restarted
- When the client environment switches the rule settings, the client needs to be restarted

* Type: `boolean`

* Default: `false`

* Suggested options: `false`, `true`

* Categories: `AMS`, `FEATURE`, `EXPERIMENTAL`

## amsTranslationMode

Switch the translation mode of Carpet AMS Addition

- [client] - Prefer sending translated text to the client using the client’s own language
- [server] - Use the language set by the server for all translations

* Type: `enum`

* Default: `CLIENT`

* Suggested options: `CLIENT`, `SERVER`

* Categories: `AMS`

## realPlayerControlDisabled

Prevent Carpet /player commands from controlling any real player

- Type: `boolean`
- Default: `false`
- Suggested options: `false`, `true`
- Categories: `AMS`, `COMMAND`

## destroyBlockPosWarningDisabled

Disable the server-side "Mismatch in destroy block pos" warning

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`

## blockHardnessSyncProtocol

Synchronize the server-side block hardness to the client

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `FEATURE`, `SURVIVAL`, `AMS_NETWORK`

## commandLocationTag

Enable /amsloc to record and query locations across dimensions using signs; defaults to ordinary oak signs with amsloc on the first line

- Type: `String`

- Default: `false`

- Suggested options: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`

- Categories: `AMS`, `COMMAND`, `FEATURE`

## craftableEnchantedGoldenApples

Enchanted Golden Apples can be crafted with 8 Gold Blocks again

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `SURVIVAL`, `CRAFTING`

## craftableBundle

> Version: `Minecraft 1.17–1.21.1`

Crafted bundle in minecraft 1.17/1.18/1.19

- Type: `String`

- Default: `false`

- Suggested options: `false`, `9x3`, `9x6`

- Categories: `AMS`, `SURVIVAL`, `CRAFTING`

## craftableSculkSensor

> Version: `Minecraft 1.17–1.18`

Crafted sculk_sensor in minecraft 1.17/1.18

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `SURVIVAL`, `CRAFTING`

## craftableElytra

Add elytra recipes in minecraft

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `SURVIVAL`, `CRAFTING`

## betterCraftableBoneBlock

Use nine bones to crafted three bone_blocks

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `SURVIVAL`, `CRAFTING`

## betterCraftableDispenser

Better craftable dispenser

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `SURVIVAL`, `CRAFTING`

## betterCraftablePolishedBlackStoneButton

> Version: `Minecraft > 1.17`

Use deepslate to crafted polished_blackstone_button in minecraft

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `SURVIVAL`, `CRAFTING`

## rottenFleshBurnedIntoLeather

Rotten flesh can be burned into leather in a furnace

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `SURVIVAL`, `CRAFTING`

## useNewLodestoneRecipe

> Version: `Minecraft < 1.21.5`

Use iron ingots to craft Lodestone

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `SURVIVAL`, `CRAFTING`

## craftableCarvedPumpkin

Using shears and a pumpkin to craft a carved pumpkin, each crafted carved pumpkin reduces the shears durability by 1 (affected by unbreaking enchantments)

- Type: `boolean`

- Default: `false`

- Suggested options: `false`, `true`

- Categories: `AMS`, `SURVIVAL`, `CRAFTING`
