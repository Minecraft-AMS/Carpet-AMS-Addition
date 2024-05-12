### [ 中文 | [English](/carpetamsaddition/en_us/Rules_en) ]

# <center>------ 规 则 ------</center>

&emsp;

## 超级弓（superBow）

开启后，可以让弓同时拥有无限和经验修补附魔。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`


## 计划刻催熟所有作物（scheduledRandomTickAllPlants）

开启后，使计划刻事件可触发以下所有植物的随机刻生长行为，用于恢复1.15版本的强制催熟特性。亦可通过以下指令单独控制特定植物是否可强制催熟。

- `scheduledRandomTickCactus`: 开启后，使计划刻事件可触发仙人掌的随机刻生长行为。

- `scheduledRandomTickBamboo`: 开启后，使计划刻事件可触发竹子的随机刻生长行为。

- `scheduledRandomTickChorusFlower`: 开启后，使计划刻事件可触发紫颂花的随机刻生长行为。

- `scheduledRandomTickSugarCane`: 开启后，使计划刻事件可触发甘蔗的随机刻生长行为。

- `scheduledRandomTickStem`: 开启后，使计划刻事件可触发海带、缠怨藤、垂泪藤的随机刻生长行为。

  <该规则从 [OhMyVanillaMinecraft](https://github.com/hit-mc/OhMyVanillaMinecraft) 移植>

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 龙战优化（optimizedDragonRespawn）

大幅度优化了龙战判定代码的性能表现，为基于末地祭坛设计的末地石农场提供性能优化。注意：本选项开启后可能影响原版特性。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `OPTIMIZATION`


## 区块加载控制（commandPlayerChunkLoadController）

控制玩家的区块加载，有时候会有比较奇怪的情况，可以挪到附近的区块再回来，可能因为某些未知原因所在区块还会加载。不会移除所在维度的玩家检测，例如主世界出生点区块加载和末地主岛加载。
玩家上下线时会将交互状态重置回加载以避免[MC-157812](https://bugs.mojang.com/browse/MC-157812)。

命令：/playerChunkLoading true/false

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `COMMAND`, `AMS_chunkLoader`


## 音符盒区块加载（noteBlockChunkLoader）

开启后，当上边沿红石信号激活音符盒时，为该音符盒所在区块添加类型为"note_block"，加载等级为30的加载票，持续时间为300gt（15s），你可以使用blockChunkLoaderTimeController与blockChunkLoaderRangeController规则来控制其加载时间与范围。

`bone_block`: 音符盒上有骨块时可以触发加载。

`wither_skeleton_skull`: 音符盒上有凋灵骷髅头（可以是挂在墙上的也可以是放在音符盒上的）时可以触发加载。

`note_block`: 无需条件，只有音符盒即可加载。

`false`: 禁用该规则。

由于在服务器当前维度没有玩家的300tick后，Minecraft会停止实体相关的更新，因此每当当前维度中没有玩家时使用该规则加载的区块中漏斗将会停止工作，你可以启用`blockChunkLoaderKeepWorldTickUpdate`或`keepWorldTickUpdate`规则来解决这个问题。



- 类型: `String`



- 默认值: `false`



- 参考选项: `bone_block`, `wither_skeleton_skull`, `note_block`, `false`



- 分类: `AMS`, `FEATURE`, `AMS_chunkLoader`


## 钟区块加载（bellBlockChunkLoader）

开启后，当上边沿红石信号激活钟时，为钟方块所在区块添加类型为"bell_block"，加载等级为30的加载票，持续时间为300gt（15s），你可以使用blockChunkLoaderTimeController与blockChunkLoaderRangeController规则来控制其加载时间与范围。

由于在服务器当前维度没有玩家的300tick后，Minecraft会停止实体相关的更新，因此每当当前维度中没有玩家时使用该规则加载的区块中漏斗将会停止工作，你可以启用`blockChunkLoaderKeepWorldTickUpdate`或`keepWorldTickUpdate`规则来解决这个问题。



- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `AMS_chunkLoader`


## 活塞头区块加载（pistonBlockChunkLoader）

开启后，当该活塞/黏性活塞产生活塞头的推出/拉回事件时，在创建推出/拉回事件的那一游戏刻为**活塞头方块所在区块**添加类型为"piston_block"，加载等级为30的加载票，持续时间为300gt（15s）。注意，黏性活塞的失败收回事件（如尝试拉回超过12个方块时）也可创建加载票，你可以使用blockChunkLoaderTimeController与blockChunkLoaderRangeController规则来控制其加载时间与范围。

`bone_block`: 活塞\黏性活塞上有骨块时触发加载。

`bedrock`: 活塞\黏性活塞下有基岩时触发加载。

`all`: 活塞\黏性活塞下有骨块或基岩时触发加载。

`false`: 禁用该规则。

由于在服务器当前维度没有玩家的300tick后，Minecraft会停止实体相关的更新，因此每当当前维度中没有玩家时使用该规则加载的区块中漏斗将会停止工作，你可以启用`blockChunkLoaderKeepWorldTickUpdate`或`keepWorldTickUpdate`规则来解决这个问题。



- 类型: `String`



- 默认值: `false`



- 参考选项: `bone_block`, `bedrock`, `all`, `false`



- 分类: `AMS`, `FEATURE`, `AMS_chunkLoader`


## 方块区块加载保持实体更新（blockChunkLoaderKeepWorldTickUpdate）

在服务器当前维度没有玩家的300tick后，Minecraft会停止实体相关的更新，这条规则会让以下规则发生加载时绕过这个限制。
受影响的规则：`noteBlockChunkLoader`、`pistonBlockChunkLoader`、`bellBlockChunkLoader`

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `AMS_chunkLoader`


## 保持实体更新（keepWorldTickUpdate）

在服务器当前维度没有玩家的300tick后，Minecraft会停止实体相关的更新，这条规则会绕过这个限制。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `AMS_chunkLoader`

## 方块加载时长控制器（blockChunkLoaderTimeController）

用于控制方块加载系列规则的加载时长（需要重启服务器）。

受影响的规则：

noteBlockChunkLoader、pistonBlockChunkLoader、bellBlockChunkLoader

## 方块加载范围控制器（blockChunkLoaderRangeController）

用于控制方块加载系列规则的加载范围（需要重启服务器）。

受影响的规则：

noteBlockChunkLoader、pistonBlockChunkLoader、bellBlockChunkLoader


## 地狱可放水（netherWaterPlacement）

开启后，玩家可通过使用水桶的方式在地狱维度中放置水源。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`


## 伪和平（fakePeace）

开启后，所有生物不会生成，但不影响困难难度（类似伪和平）。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`


## 炸毁所有方块（blowUpEverything）

开启后，所有方块的爆炸抗性均为 0。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`


## 共享打折（sharedVillagerDiscounts）

开启后，玩家将僵尸村民治疗为村民后的获得的折扣将共享给所有玩家。

<该规则从 [totos-carpet-tweaks](https://github.com/totorewa/totos-carpet-tweaks) 移植>

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`


## 熄灭的篝火（extinguishedCampfire）

开启后，当玩家放置篝火时，篝火处于熄灭状态。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`


## 安全飞行（safeFlight）

开启后，玩家使用鞘翅飞行时不会因为撞到墙壁而受到伤害。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 自定义方块更新抑制器（customBlockUpdateSuppressor）

自定义一个方块成为更新抑制器（不是所有的方块都能设置为更新抑制器）。

命令格式：

/carpet customBlockUpdateSuppressor minecraft:BlockName

使用如下指令来控制是否在启用该规则时自动开启 `amsUpdateSuppressionCrashFix ` 规则: 

/amsUpdateSuppressionCrashFixForceMode true/false

- 类型: `String`



- 默认值: `none`



- 参考选项: `none`, `minecraft:bone_block`, `minecraft:diamond_ore`, `minecraft:magma_block`



- 分类: `AMS`, `FEATURE`


## 无限交易（infiniteTrades）

开启后，村民交易将不会被锁定。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`


## 无懈可击（invulnerable）

开启后，玩家将免受除虚空外的一切伤害。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`


## 创造模式一击必杀（creativeOneHitKill）

开启后，当玩家在创造模式下时可以做到一击必杀，当玩家处于潜行状态时，周围的实体也会被杀死。

<该规则从 [lunaar-carpet-addons](https://github.com/Lunaar-SMP/lunaar-carpet-addons) 移植>

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `CREATIVE`


## 大末影箱（largeEnderChest）

开启后，让你的末影箱容量增加一倍（与大箱子相同）。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`


## 竹子模型无偏移（bambooModelNoOffset）

开启后，让竹子的模型不会产生偏移。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `OPTIMIZATION`


## 禁用竹子碰撞箱（bambooCollisionBoxDisabled）

开启后，玩家可以穿过竹子。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`


## 禁用篝火烟雾粒子（campfireSmokeParticleDisabled）

开启后，篝火将不会产生烟雾粒子。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`


## 防火图腾（antiFireTotem）

开启后，图腾将不会被火焰或者岩浆烧毁。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`


## 掉落物防爆（itemAntiExplosion）

开启后，掉落物将不会被爆炸摧毁。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `TNT`


## 创造模式潜影盒无掉落（creativeShulkerBoxDropsDisabled）

开启后，当玩家处于创造模式下时，破坏装有物品的潜影盒也不会产生掉落。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `CREATIVE`


## 基岩版飞行（bedRockFlying）

开启后，玩家飞行时的表现与基岩版一致。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `CREATIVE`


## 禁止潜影贝给予漂浮效果（shulkerHitLevitationDisabled）

开启后，玩家被潜影贝击中时只会受到伤害而不会获得漂浮BUFF。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false` , `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`


## 免疫潜影贝飞弹（immuneShulkerBullet）

开启后，玩家将完全免疫潜影贝发射的飞弹。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false` , `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 凋零蓝头控制器（blueSkullController）

开启后，可以选择让凋零总是发射蓝头还是从不发射蓝头。

`SURELY`: 总是发射蓝头

`NEVER`: 从不发射蓝头

`VANILLA`: 原版表现

- 类型: `String`



- 默认值: `VANILLA`



- 参考选项: `SURELY` , `NEVER` , `VANILLA` 



- 分类: `AMS` , `FEATURE` , `EXPERIMENTAL`


## 禁止末影人随机传送（endermanTeleportRandomlyDisabled）

开启后，末影人不会随机传送。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false` , `true`



- 分类: `AMS` , `FEATURE` , `EXPERIMENTAL`


## 更快移动（fasterMovement）

提供了五个挡位，让玩家移动速度更快。

- 类型: `String`



- 默认值: `VANILLA`



- 参考选项: `Ⅰ`, `Ⅱ`, `Ⅲ`, `Ⅳ`, `Ⅴ`, `VANILLA`



- 分类: `AMS`, `FEATURE` , `EXPERIMENTAL`


## 更快移动规则控制器（fasterMovementController）

用于控制更快移动规则在那个维度生效。

- 类型: `String`



- 默认值: `all`



- 参考选项: `overworld`, `nether`, `end`, `all`



- 分类: `AMS`, `FEATURE`, `EXPERIMENTAL`


## 轻松掉落凋零骷髅头（easyWitherSkeletonSkullDrop）

开启后，击杀凋零骷髅会百分百掉落凋零骷髅头。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`


## 禁用铁砧交互（commandAnvilInteractionDisabled）

开启后，玩家无法打开铁砧的UI界面。
命令：/anvilInteractionDisabledSwitch true/false

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `COMMAND`


## 防止管理员作弊（preventAdministratorCheat）

禁用部分指令以避免op玩家意外地作弊。

影响的指令列表: /gamemode, /tp, /teleport, /give, /setblock, /summon, /difficulty, /kill, /time, /weather, /fill, /setblock, /enchant, /experience, /advancement, /effect, /data, /defaultgamemode, /gamerule

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `COMMAND`


## 更新抑制崩溃修复（amsUpdateSuppressionCrashFix）

开启后，更新抑制不会导致服务器崩溃，同时提供发生更新抑制的坐标与维度。

使用如下指令来控制其是否在"customBlockUpdateSuppressor"开启时强制启动，权限等级为2:

/amsUpdateSuppressionCrashFixForceMode true/false

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `EXPERIMENTAL`

## 恶魂火球爆炸伤害源修复（ghastFireballExplosionDamageSourceFix）

修复MC-193297中，恶魂火球爆炸时不会正确传递伤害源。

<该规则 Minecraft < 1.19.3 可用>

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `EXPERIMENTAL`, `BUGFIX`


## 可掉落蛋糕（cakeBlockDropOnBreak）

开启后，蛋糕在被破坏时可以掉落蛋糕（蛋糕是完整的情况下才会掉落）。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`


## 不准吃蛋糕（noCakeEating）

开启后，玩家不可以吃蛋糕

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`


## 红石元件音效（redstoneComponentSound）

开启后，玩家右键红石元件时会播放声音。

影响的元件：阳光传感器、红石粉、中继器。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 大潜影盒（largeShulkerBox）

让潜影盒容量增加一倍（与大箱子相同）。

使用该规则请遵循以下步骤：

1 - 确保没有潜影盒被加载。

2 - 输入指令：`/carpet setDefault largeShulkerBox true` 开启规则。

3 - 重启服务器/单人世界。

4 - 关闭该规则同理。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `EXPERIMENTAL`

## 最大玩家方块交互距离（maxPlayerBlockInteractionRange）

更改服务器允许玩家与方块的最大交互距离，设置为“-1”禁用该规则。

Minecraft >= 1.20.5 时可以使用maxPlayerBlockInteractionRangeScope规则来修改其作用域。

global模式下修改值后需要玩家重新进入游戏。

- 类型: `double`



- 默认值: `-1`



- 参考选项: `0 - 512`, `-1`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 最大玩家实体交互距离（maxPlayerEntityInteractionRange）

更改服务器允许玩家与实体的最大交互距离，设置为“-1”禁用该规则。

Minecraft >= 1.20.5 时可以使用maxPlayerEntityInteractionRangeScope规则来修改其作用域。

global模式下修改值后需要玩家重新进入游戏。

- 类型: `double`



- 默认值: `-1`



- 参考选项: `0 - 512`, `-1`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 最大玩家方块交互距离作用域（maxPlayerBlockInteractionRangeScope）

更改maxPlayerBlockInteractionRange规则的作用域。

[server] - 需要客户端自己对交互距离进行修改，服务端仅对客户端修改放行。

[global] - 距离的修改将同时应用于服务端和客户端，客户端无需单独修改交互距离。

<该规则 Minecraft >= 1.20.5 可用>

- 类型: `String`



- 默认值: `server`



- 参考选项: `server`, `global`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 最大玩家实体交互距离作用域（maxPlayerEntityInteractionRangeScope）

更改maxPlayerEntityInteractionRange规则的作用域。

[server] - 需要客户端自己对交互距离进行修改，服务端仅对客户端修改放行。

[global] - 距离的修改将同时应用于服务端和客户端，客户端无需单独修改交互距离。

<该规则 Minecraft >= 1.20.5 可用>

- 类型: `String`



- 默认值: `server`



- 参考选项: `server`, `global`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 客户端最大方块放置距离（maxClientInteractionReachDistance）

更改客户端允许的最大交互距离，设置为 `-1` 禁用该规则。

需要关闭Tweakeroo的tweakBlockReachOverride功能。

- 类型: `double`



- 默认值: `-1`



- 参考选项: `0 - 512`, `-1`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 自定义可移动方块（customMovableBlock）

可自定义的让不能被推动的方块变得能被推动。
命令格式：
/carpet customMovableBlock minecraft:BlockName
你也可以同时设置多个方块使用 `,` 隔开即可：
/carpet customMovableBlock inecraft:BlockName1,minecraft:BlockName2

- 类型: `String`



- 默认值: `VANILLA`



- 参考选项: `VANILLA`, `minecraft:bedrock`, `minecraft:bedrock,minecraft:obsidian`



- 分类: `AMS`, `FEATURE`

## 简易满级信标（eazyMaxLevelBeacon）

开启后，只需一个底座方块就能激活满级信标。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 自定义方块爆炸抗性（commandCustomBlockBlastResistance）

用指令自定义任何方块的爆炸抗性（强化版世界吞噬者(enhancedWorldEater)规则开启时该规则将不会生效）。

开启规则后使用 `/customBlockBlastResistance help` 命令查看使用说明

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`, `COMMAND`

## 再生成龙蛋（regeneratingDragonEgg）

开启后，玩家每次击败末影龙都会生成一个龙蛋。

> 为了Dnsolx的蛋

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 增强型世界吞噬者（enhancedWorldEater）

通过自定义除以下方块的爆炸抗性来让世界吞噬者一路畅通（该规则开启时，`自定义炸毁方块(customBlowUpBlock)` 规则将不会生效）。

方块列表：

爆炸抗性 < 17.0F的方块、基岩、铁砧、末地传送门框架、末地传送门、折跃门

- 类型: `double`



- 默认值: `-1`



- 参考选项:  `-1`, `0 - 16`



- 分类: `AMS`, `FEATURE`, `SURVIVAL` `TNT`
## 潜行编辑告示牌（sneakToEditSign）

开启后，玩家可以在潜行状态且双手为空时对已经放置的告示牌按使用键来编辑它，Minecraft >= 1.20 时表现为必须潜行。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 花哨的假人名称（fancyFakePlayerName）

为carpet mod所召唤出来的假人加上绿色的前缀与后缀，输入命令时不用输入前缀，而后缀则是真实存在的。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 假人不计入计分板（fakePlayerNoScoreboardCounter）

开启后，计分板将会屏蔽假人。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 没有计划生育（noFamilyPlanning）

允许玩家一直喂食动物来繁殖它们。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁止漏斗吸入（hopperSuctionDisabled）

开启后，漏斗将不会吸入物品。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 不准吃附魔金苹果（noEnchantedGoldenAppleEating）

防止玩家意外吃掉附魔金苹果。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁用物品使用冷却（useItemCooldownDisabled）

让使用物品没有冷却时间。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 仙人掌扳手音效（flippinCactusSoundEffect）

在开启carpet mod的仙人掌扳手规则时，使用仙人掌扳手会发出声音（提供了五种不同音效，设置为0禁用音效）。

- 类型: `int`



- 默认值: `0`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `5`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁用珊瑚失活（undyingCoral）

让珊瑚扇和珊瑚块不会因缺水失活。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 禁止末影龙破坏方块（enderDragonNoDestroyBlock）

使末影龙不能破坏任何方块。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 轻松堆肥（easyCompost）

让每次堆肥都可以成功。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 易采集龙蛋（easyMineDragonEgg）

让龙蛋不会瞬移，玩家可以直接挖掉龙蛋来收集它。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 可繁殖鹦鹉（breedableParrots）

自定义一种食物来喂食鹦鹉繁殖它们（你也可以用石头来喂它们，如果你真的这么想的话）。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 懂事的末影人（sensibleEnderman）

让末影人只会拾起西瓜和南瓜。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁止末影人拾起方块（endermanPickUpDisabled）

禁止末影人拾起方块。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 螨虫珍珠（mitePearl）

每一次使用末影珍珠都会生成一只末影螨。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 末影珍珠音效（enderPearlSoundEffect）

玩家在使用末影珍珠进行传送时会播放音效。

<该规则 Minecraft < 1.20 可用>

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 我在哪（commandHere）

使用 "/here" 命令发送当前自身所在维度、坐标、对应的主世界/下界坐标，同时给予自身30s的发光效果。

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `COMMAND`

## 你在哪（commandWhere）

使用 "/where" 命令获取指定玩家所在维度、坐标、对应的主世界/下界坐标，同时给予目标玩家30s的发光效果。

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `COMMAND`

## 假人拾取控制器（fakePlayerPickUpController）

提供了两种模式来控制假人是否可以拾取物品。

- [MainHandOnly] - 仅主手可拾取物品



- [NoPickUp] - 完全无法拾取物品



- [false] - 禁用规则



- 类型: `String`



- 默认值: `false`



- 参考选项: `MainHandOnly`, `NoPickUp`, `false`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 玩家导游（commandPlayerLeader）

指定一名或多名玩家成为导游，使他们拥有发光效果。

开启规则后使用 `/leader help` 命令查看使用说明

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `COMMAND`

## ping命令（commandPacketInternetGroper）

玩家可以在游戏中使用ping命令。

开启规则后使用 `/ping help` 命令查看使用说明

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `COMMAND`

## 下界传送门不传送玩家（playerNoNetherPortalTeleport）

玩家无法通过下界传送门进行传送。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 无限耐久（infiniteDurability）

使用物品不会丢失耐久。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 欢迎消息（welcomeMessage）

在玩家进入服务器时发送一条自定义消息给玩家（使用json文件来自定义消息）。

json位置：

[ 存档文件夹 ]/carpetamsaddition/welcomeMessage.json

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`

## 获取存档大小（commandGetSaveSize）

使用 “/getSaveSize” 命令获取当前存档大小。

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `COMMAND`

## 地毯规则总是设置为默认（carpetAlwaysSetDefault）

每当你设置carpet规则时，都会自动设置为默认值。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 禁用实验性内容检查（experimentalContentCheckDisabled）

允许在游戏中通过添加数据包来游玩Minecraft的实验性内容（需要重启服务器）。

<该规则 Minecraft >= 1.19 可用>

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `EXPERIMENTAL`

## 可施肥小花（fertilizableSmallFlower）

使诸如蒲公英的小花也可以被骨粉催熟。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 安全的滴水石锥（safePointedDripstone）

玩家落在滴水石锥上时，不会受到它的附加伤害。

<该规则 Minecraft >= 1.17 可用>

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁用滴水石锥碰撞箱（pointedDripstoneCollisionBoxDisabled）

开启后，玩家可以穿过滴水石锥。

<该规则 Minecraft >= 1.17 可用>

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁止树叶生成（foliageGenerateDisabled）

树叶不会以任何方式生成。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`

## 获取系统信息（commandGetSystemInfo）

使用“/getSystemInfo”命令获取服务器的系统信息。

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `COMMAND`

## 铁傀儡不掉落花（ironGolemNoDropFlower）

铁傀儡不会掉落花。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 轻松获取瓶子草荚果（easyGetPitcherPod）

玩家可以通过种植瓶子草来收获随机数量的瓶子草荚果（其最大数量是自定义的，最小数量为固定2个）。

<该规则 Minecraft >= 1.20 可用>

- 类型: `int`



- 默认值: `0`



- 参考选项: `0 或 2 - 100`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## goto命令（commandGoto）

使用 “/goto” 命令快速传送到指定维度的指定坐标，没有指定坐标时会传送至当前玩家所在位置的对应坐标。

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `COMMAND`

## 发送玩家死亡位置（sendPlayerDeathLocation）

当玩家死亡时，会广播一条包含该玩家死亡坐标以及维度的消息。

`false`: 禁用规则。

`all`: 总是发送消息。

`realPlayerOnly`: 只有真实玩家死亡时发送消息。

`fakePlayerOnly`: 只有假人死亡时发送消息。

- 类型: `String`



- 默认值: `false`



- 参考选项: `false`, `all`, `realPlayerOnly`, `fakePlayerOnly`



- 分类: `AMS`, `SURVIVAL`

## 完美隐身（perfectInvisibility）

当玩家拥有隐身效果时将完全隐身，即使穿着全套盔甲。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 潜行隐身（sneakInvisibility）

当玩家潜行时将对生物完全隐身，即使穿着全套盔甲。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 自定义命令权限等级（commandCustomCommandPermissionLevel）

自定义任何命令的权限等级。

开启规则后使用 /customCommandPermissionLevel help 命令查看使用说明。

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `SURVIVAL`, `COMMAND`

## 潜影贝傀儡（shulkerGolem）

可以通过在潜影盒上放置一个雕刻南瓜来召唤一只潜影贝。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 猎头（headHunter）

当玩家死亡时他们会掉落一个自己的头颅。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 获取玩家头颅（commandGetPlayerSkull）

自定义任何命令的权限等级。

使用 “/getPlayerSkull” 命令来获取一名在线玩家的头颅。

- 类型: `String`



- 默认值: `false`



- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`



- 分类: `AMS`, `SURVIVAL`, `COMMAND`



## 可合成附魔金苹果（craftableEnchantedGoldenApples）

开启后，可利用金块和苹果合成附魔金苹果，即恢复到15w44a前的表现。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `SURVIVAL`, `CRAFTING`


## 更好的合成骨块（betterCraftableBoneBlock）

开启后，可更好的合成骨块（可用9个骨头合成三个骨块，大幅降低合成卡顿）。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `SURVIVAL`, `CRAFTING`


## 可合成收纳袋（craftableBundle）

开启后，玩家可以在Minecraft 1.17/1.18/1.19中合成收纳袋。
- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `SURVIVAL`, `CRAFTING`


## 可合成幽匿感测体（craftableSculkSensor）

开启后，玩家可以在Minecraft 1.17/1.18中合成幽匿感测体。
- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `SURVIVAL`, `CRAFTING`


## 可合成鞘翅（craftableElytra）

开启后，玩家可以在Minecraft中合成鞘翅。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false` , `true`



- 分类: `AMS`, `SURVIVAL`, `CRAFTING`


## 更好的合成发射器（betterCraftableDispenser）

开启后，玩家可以使用更灵活的合成配方来合成发射器。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `SURVIVAL`, `CRAFTING`


## 更好的合成磨制黑石按钮（betterCraftablePolishedBlackStoneButton）

使用深板岩来合成磨制黑石按钮。

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `SURVIVAL`, `CRAFTING`
