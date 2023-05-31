\>\>\> [BACK](/README_en.md)

# Rules
NOTE: Rules with 💻 emoji present that correctly executing these rules requires Carpet-AMS-Addition on the client side.
## superBow

Let enchantments Infinity and Mending be compatible with each other on bow.

> Who won't be attracted by a bow without being torn between two enchantments?

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

## bellBlockChunkLoader

When a bell block is triggered by rising edge of redstone signal, the chunk which the bell block located will be added a ticket with level 30 and type 'bell_block', which will expire after 300gt. Bell blocks have no special requirement to add tickets.

- Type: `boolean`
- Default: `false`
- Suggested options: `false`, `true`
- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

## pistonBlockChunkLoader

When a piston (also available for sticky piston) successfully push or pull its head, the chunk which the piston head (the moving block representing piston head) located will be added a ticket with level 30 and type 'piston_block', which will expire after 300gt. The ticket will be added at the tick when the moving block is created. Pistons that can be used to add ticket must meet the requirement set by the rule option.

> A pretty good ~~and opportunistic~~ way to supersede nether portal loading chain for Pearl Cannon.

the option is interpreted as follow:

- `bone_block`: The piston must have a bone block just above it.
- `bedrock`: The piston must have a bedrock just under it.
- `all`: When boe_block is on the piston or bed rock is under the piston.
- `OFF`: Disable the rule.
  

&emsp;

- Type: `String`
- Default: `OFF`
- Suggested options: `bone_block`, `bedrock`, `all`, `OFF`
- Categroies: `AMS`, `FEATURE`, `AMS_chunkLoader`

## netherWaterPlacement

Player can place water source in the nether by using water bucket.

> What an ancient demand.

- Type: `boolean`
- Default: `false`
- Suggested options: `false`, `true`
- Categroies: `AMS`, `FEATURE`

## 💻softDeepslate

Hardness of deepslate will be set to 1.5, which is equal to the hardness of stone. Other words, player with Haste II using an Efficiency V diamond pickaxe can break deepslate instantly.

> Just fxck it using pickaxe in your hands!

- Type: `boolean`
- Default: `false`
- Suggested options: `false`, `true`
- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## 💻softObsidian

Hardness of obsidian will be set to 3, which is equal to the hardness of original deepslate.

> Benefits for portal slicing farm! Wait..What do you want to do with the obsidian platform..?

- Type: `boolean`
- Default: `false`
- Suggested options: `false`, `true`
- Categroies: `AMS`, `FEATURE`, `SURVIVAL`

## fakePeace

No mob will spawn without change difficulty.

> Why not use doMobSpawning provided by Mojang?

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
<Only available for Minecraft \>= 1.19>

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

When enabled, bone block will be a update suppressor.

<Only available for Minecraft \< 1.19 >

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


## 💻bambooModelNoOffset

When enabled, The block model of bamboo will not generate offset.

- Type: `boolean`
- Default: `false`
- Suggested options: `false` , `true`
- Categroies: `AMS` , `FEATURE` , `OPTIMIZATION`


## 💻bambooCollisionBoxDisabled

When enabled, players can pass through bamboo.

- Type: `boolean`
- Default: `false`
- Suggested options: `false` , `true`
- Categroies: `AMS` , `FEATURE`


## 💻campfireSmokeParticleDisabled

When enabled, Campfire will not produce smoke particles.

- Type: `boolean`
- Default: `false`
- Suggested options: `false` , `true`
- Categroies: `AMS` , `FEATURE`


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
>Who can refuse a flat bedrock ground without any defect?

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

>MSPT---/FPS+++

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
