## 超级弓（superBow）

让弓能同时打上无限和经验修补两种附魔

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 计划刻催熟所有作物（scheduledRandomTickAllPlants）

当所有作物方块收到计划刻时仍会给予生长随机刻

- `scheduledRandomTickCactus`: 当仙人掌方块收到计划刻时仍会给予生长随机刻
- `scheduledRandomTickBamboo`: 当竹子方块收到计划刻时仍会给予生长随机刻
- `scheduledRandomTickChorusFlower`: 当紫颂花方块收到计划刻时仍会给予生长随机刻
- `scheduledRandomTickSugarCane`: 当甘蔗方块收到计划刻时仍会给予生长随机刻
- `scheduledRandomTickStem`: 当海带、缠怨藤、垂泪藤方块收到计划刻时仍会给予生长随机刻

> 移植自：[OhMyVanillaMinecraft](https://github.com/hit-mc/OhMyVanillaMinecraft)

- 类型: `boolean`

  

- 默认值: `false`

  

- 参考选项: `false`, `true`

  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 龙战优化（optimizedDragonRespawn）

大幅度优化龙战定位末地门过程，为基于末地门的末地石农场设计

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `OPTIMIZATION`

## 区块加载控制（commandPlayerChunkLoadController）

控制任意游戏模式下玩家对周围区块的加载

- 指令:
- /playerChunkLoading true/false

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `COMMAND`, `AMS_chunkLoader`

## 音符盒方块加载器（commandNoteblockChunkloader）

使用指令自定义音符盒加载器

- 开启规则后，使用"/blockChunkloader help"命令查看使用说明

- 类型: `String`
- 默认值: `false`
- 参考选项: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `AMS`, `COMMAND`, `AMS_chunkLoader`

## 钟方块加载器（commandBellChunkloader）

使用指令自定义钟加载器

- 开启规则后，使用"/blockChunkloader help"命令查看使用说明

- 类型: `String`
- 默认值: `false`
- 参考选项: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `AMS`, `COMMAND`, `AMS_chunkLoader`

## 活塞方块加载器（commandPistonChunkloader）

使用指令自定义活塞加载器（以活塞头到达的区块为中心）

- 开启规则后，使用"/blockChunkloader help"命令查看使用说明

- 类型: `String`
- 默认值: `false`
- 参考选项: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `AMS`, `COMMAND`, `AMS_chunkLoader`

## 保持实体更新（keepWorldTickUpdate）

在服务器当前维度没有玩家的300tick后，Minecraft会停止实体相关的更新，这条规则会绕过这个限制

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `AMS_chunkLoader`

## 地狱可放水（netherWaterPlacement）

玩家可以在地狱使用水桶放置水

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 伪和平（fakePeace）

伪和平，你可以自定义其生效的维度，也可以自定义生效的维度组合，使用“,”隔开即可

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`, `minecraft:overworld`, `minecraft:the_end`, `minecraft:the_nether`, `minecraft:the_end,minecraft:the_nether`
  
  

- 分类: `AMS`, `FEATURE`

## 炸毁所有方块（blowUpEverything）

让所有方块的爆炸抗性为0

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`

## 共享打折（sharedVillagerDiscounts）

玩家将僵尸村民治疗为村民后的获得的折扣将共享给所有玩家

> 移植自：[totos-carpet-tweaks](https://github.com/totorewa/totos-carpet-tweaks)

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 熄灭的篝火（extinguishedCampfire）

当玩家放置篝火时，篝火处于熄灭状态

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 安全飞行（safeFlight）

玩家使用鞘翅飞行时不会因为撞到墙壁而受到伤害

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 自定义方块更新抑制器（customBlockUpdateSuppressor）

自定义一个方块成为更新抑制器（不是所有的方块都能设置为更新抑制器）

- 命令格式：
- /carpet customBlockUpdateSuppressor minecraft:BlockName
- 使用如下指令来控制是否在启用该规则时自动开启"amsUpdateSuppressionCrashFix"规则:
- /amsUpdateSuppressionCrashFixForceMode true/false

- 类型: `String`
  
  

- 默认值: `none`
  
  

- 参考选项: `none`, `minecraft:bone_block`, `minecraft:diamond_ore`, `minecraft:magma_block`
  
  

- 分类: `AMS`, `FEATURE`

## 无限交易（infiniteTrades）

防止村民交易被锁定

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 无懈可击（invulnerable）

玩家将免受除虚空伤害外的一切伤害

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 创造模式一击必杀（creativeOneHitKill）

当玩家在创造模式下时可以做到一击必杀，当玩家处于潜行状态时，周围的实体也会被杀死

> 移植自：[lunaar-carpet-addons](https://github.com/Lunaar-SMP/lunaar-carpet-addons)

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `CREATIVE`

## 大末影箱（largeEnderChest）

让你的末影箱容量增加一倍（与大箱子相同）

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 竹子模型无偏移（bambooModelNoOffset）

让竹子的模型不会产生偏移

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `OPTIMIZATION`

## 禁用竹子碰撞箱（bambooCollisionBoxDisabled）

允许玩家穿过竹子

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 自定义防火物品（commandCustomAntiFireItems）

用命令控制任何物品是否防火

- 开启规则后，使用"/customAntiFireItems help"命令查看使用说明

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `FEATURE`, `COMMAND`

## 掉落物防爆（itemAntiExplosion）

掉落物将不会被爆炸摧毁

- [false] - 禁用规则
- [true] - 启用规则
- [no_blast_wave] - 防爆且无冲击波，爆炸将不会推动掉落物

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`, `no_blast_wave`
  
  

- 分类: `AMS`, `FEATURE`, `TNT`

## 创造模式潜影盒无掉落（creativeShulkerBoxDropsDisabled）

当玩家处于创造模式下时，破坏装有物品的潜影盒也不会产生掉落

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `CREATIVE`

## 禁止潜影贝给予漂浮效果（shulkerHitLevitationDisabled）

玩家被潜影贝击中时只会受到伤害而不会获得漂浮BUFF

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false` , `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 免疫潜影贝飞弹（immuneShulkerBullet）

玩家将完全免疫潜影贝发射的飞弹

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false` , `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 凋零蓝头控制器（blueSkullController）

控制凋零发射蓝色骷髅的概率

- [SURELY] - 总是发射蓝头
- [NEVER] - 从不发射蓝头
- [VANILLA] - 原版表现

- 类型: `String`
  
  

- 默认值: `VANILLA`
  
  

- 参考选项: `SURELY` , `NEVER` , `VANILLA` 
  
  

- 分类: `AMS` , `FEATURE` , `EXPERIMENTAL`

## 禁止末影人随机传送（endermanTeleportRandomlyDisabled）

禁止末影人随机传送

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false` , `true`
  
  

- 分类: `AMS` , `FEATURE` , `EXPERIMENTAL`

## 快速移动（fasterMovement）

提供了五个挡位让玩家移动速度变快

- 你可以使用fasterMovementController规则来控制该规则在哪个维度生效，默认所有维度生效

- 类型: `String`
  
  

- 默认值: `VANILLA`
  
  

- 参考选项: `Ⅰ`, `Ⅱ`, `Ⅲ`, `Ⅳ`, `Ⅴ`, `VANILLA`
  
  

- 分类: `AMS`, `FEATURE` , `EXPERIMENTAL`

## 快速移动规则控制器（fasterMovementController）

用于控制快速移动规则在那个维度生效

- [overworld] - 只在主世界生效
- [nether] - 只在地狱生效
- [end] - 只在末地生效
- [all] - 所有维度生效

- 类型: `String`
  
  

- 默认值: `all`
  
  

- 参考选项: `overworld`, `nether`, `end`, `all`
  
  

- 分类: `AMS`, `FEATURE`, `EXPERIMENTAL`

## 轻松掉落凋零骷髅头（easyWitherSkeletonSkullDrop）

击杀凋零骷髅会百分百掉落凋零骷髅头

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁用铁砧交互（commandAnvilInteractionDisabled）

玩家无法打开铁砧的UI界面

- 指令: /anvilInteractionDisabled true/false

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `COMMAND`

## 防止管理员作弊（preventAdministratorCheat）

禁用部分指令以避免op玩家意外地作弊

- 影响的指令列表:
- /gamemode, /tp, /teleport, /give, /setblock, /summon, /difficulty, /kill, /time, /weather, /fill, /setblock, /enchant, /experience, /advancement, /effect, /data, /defaultgamemode, /gamerule

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `COMMAND`

## 更新抑制崩溃修复（amsUpdateSuppressionCrashFix）

防止更新抑制导致服务器崩溃，同时提供发生更新抑制的坐标与维度

- [false] - 禁用规则
- [true] - 启用规则
- [silence] - 启用但不发送服务器消息
- 使用如下指令来控制其是否在"customBlockUpdateSuppressor"开启时强制启动，权限等级为2:
- /amsUpdateSuppressionCrashFixForceMode true/false

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `EXPERIMENTAL`

## 恶魂火球爆炸伤害源修复（ghastFireballExplosionDamageSourceFix）

> 版本：`Minecraft < 1.19.3`

修复MC-193297中，恶魂火球爆炸时不会正确传递伤害源。


- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `EXPERIMENTAL`, `BUGFIX`

## 可掉落蛋糕（cakeBlockDropOnBreak）

蛋糕在被破坏时可以掉落蛋糕（蛋糕是完整的情况下才会掉落）

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 不准吃蛋糕（noCakeEating）

玩家不可以吃蛋糕

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 红石元件音效（redstoneComponentSound）

玩家右键红石元件时会播放声音

- 影响的元件：
- 阳光传感器、红石粉、中继器

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 大潜影盒（largeShulkerBox）

让潜影盒容量增加一倍（与大箱子相同）

- 服务器环境切换该规则设置时需要重启服务器
- 客户端环境切换该规则设置时需要重启客户端

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `EXPERIMENTAL`

## 最大玩家方块交互距离（maxPlayerBlockInteractionRange）

更改服务器允许玩家与方块的最大交互距离，设置为“-1”禁用该规则

- 允许设定的范围：0 - 512
- Minecraft >= 1.20.5 时可以使用maxPlayerBlockInteractionRangeScope规则来修改其作用域
- global模式下修改值后需要玩家重新进入游戏

- 类型: `double`
  
  

- 默认值: `-1`
  
  

- 参考选项: `0 - 512`, `-1`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 最大玩家实体交互距离（maxPlayerEntityInteractionRange）

更改服务器允许玩家与实体的最大交互距离，设置为“-1”禁用该规则

- 允许设定的范围：0 - 512
- Minecraft >= 1.20.5 时可以使用maxPlayerEntityInteractionRangeScope规则来修改其作用域
- global模式下修改值后需要玩家重新进入游戏

- 类型: `double`
  
  

- 默认值: `-1`
  
  

- 参考选项: `0 - 512`, `-1`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 最大玩家方块交互距离作用域（maxPlayerBlockInteractionRangeScope）

> 版本：`Minecraft >= 1.20.5`

更改maxPlayerBlockInteractionRange规则的作用域

- [server] - 需要客户端自己对交互距离进行修改，服务端仅对客户端修改放行
- [global] - 距离的修改将同时应用于服务端和客户端，客户端无需单独修改交互距离

- 类型: `String`
  
  

- 默认值: `server`
  
  

- 参考选项: `server`, `global`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 最大玩家实体交互距离作用域（maxPlayerEntityInteractionRangeScope）

> 版本：`Minecraft >= 1.20.5`

更改maxPlayerEntityInteractionRange规则的作用域

- [server] - 需要客户端自己对交互距离进行修改，服务端仅对客户端修改放行
- [global] - 距离的修改将同时应用于服务端和客户端，客户端无需单独修改交互距离

- 类型: `String`
  
  

- 默认值: `server`
  
  

- 参考选项: `server`, `global`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 客户端最大交互距离（maxClientInteractionReachDistance）

更改客户端允许的最大交互距离，设置为“-1”禁用该规则

- 允许设定的范围：0 - 512
- 需要关闭Tweakeroo的tweakBlockReachOverride功能

- 类型: `double`
  
  

- 默认值: `-1`
  
  

- 参考选项: `0 - 512`, `-1`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 自定义可移动方块（commandCustomMovableBlock）

可自定义的让不能被推动的方块变得能被推动（不支持容器方块，如果需要推动容器可以使用Carpet的movableBlockEntities规则）

- 开启规则后，使用"/customMovableBlock help"命令查看使用说明

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `FEATURE`, `COMMAND`

## 简易满级信标（easyMaxLevelBeacon）

只需一个底座方块就能激活满级信标

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 自定义方块爆炸抗性（commandCustomBlockBlastResistance）

使用指令自定义任何方块的爆炸抗性（强化版世界吞噬者(enhancedWorldEater)规则开启时该规则将不会生效）

- 开启规则后，使用"/customBlockBlastResistance help"命令查看使用说明

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`, `COMMAND`

## 再生成龙蛋（regeneratingDragonEgg）

玩家每次击败末影龙都会生成一个龙蛋

- 为了Dnsolx的蛋

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 增强型世界吞噬者（enhancedWorldEater）

通过自定义除以下方块的爆炸抗性来让世界吞噬者一路畅通（该规则开启时，自定义炸毁方块(commandCustomBlockBlastResistance)规则将不会生效）

- 方块列表：
- 爆炸抗性 < 17.0F的方块、基岩、铁砧、末地传送门框架、末地传送门、折跃门

- 类型: `double`
  
  

- 默认值: `-1`
  
  

- 参考选项:  `-1`, `0 - 16`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL` `TNT`

## 潜行编辑告示牌（sneakToEditSign）

玩家可以在潜行状态且双手为空时对已经放置的告示牌按使用键来编辑它

- Minecraft >= 1.20时表现为必须潜行

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 潜行吃蛋糕（sneakToEatCake）

玩家只能在潜行状态下吃蛋糕

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 花哨的假人名称（fancyFakePlayerName）

为carpet mod所召唤出来的假人加上绿色的前缀与后缀，输入命令时不用输入前缀，而后缀则是真实存在的

- 注意：该规则会创建一个名为你输入的值的team
- 例：1024_byteeeee -> [bot] 1024_byteeeee_bot

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `bot`, `fake_player`
  
  

- 分类: `AMS`, `FEATURE`

## 假人不计入计分板（fakePlayerNoScoreboardCounter）

计分板将会屏蔽假人

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 没有计划生育（noFamilyPlanning）

允许玩家一直喂食动物来繁殖它们

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁止漏斗吸入（hopperSuctionDisabled）

漏斗将不会吸入物品

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 不准吃附魔金苹果（noEnchantedGoldenAppleEating）

防止玩家意外吃掉附魔金苹果

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁用物品使用冷却（useItemCooldownDisabled）

让使用物品没有冷却时间

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 仙人掌扳手音效（flippinCactusSoundEffect）

在开启carpet mod的仙人掌扳手规则时，使用仙人掌扳手会发出声音（提供了五种不同音效，设置为 0 禁用音效）

- 类型: `int`
  
  

- 默认值: `0`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `5`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁用珊瑚失活（undyingCoral）

让珊瑚扇和珊瑚块不会因缺水失活

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 禁止末影龙破坏方块（enderDragonNoDestroyBlock）

使末影龙不能破坏任何方块

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 轻松堆肥（easyCompost）

让每次堆肥都可以成功

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 易采集龙蛋（easyMineDragonEgg）

让龙蛋不会瞬移，玩家可以直接挖掉龙蛋来收集它

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 可繁殖鹦鹉（breedableParrots）

自定义一种食物来喂食鹦鹉繁殖它们（你也可以用石头来喂它们，如果你真的这么想的话）

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 麒麟臂（kirinArm）

让玩家可以瞬间开采所有可挖掘方块

- 类型: `boolean`

- 默认值: `false`

- 参考选项: `false`, `true`

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 懂事的末影人（sensibleEnderman）

让末影人只会拾起西瓜和南瓜

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁止末影人拾起方块（endermanPickUpDisabled）

禁止末影人拾起方块

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 螨虫珍珠（mitePearl）

每一次使用末影珍珠都会生成一只末影螨

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 末影珍珠音效（enderPearlSoundEffect）

> 版本：`Minecraft < 1.20`

玩家在使用末影珍珠进行传送时会播放音效。


- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 我在哪（commandHere）

使用/here命令发送当前自身所在维度、坐标、对应的主世界/下界坐标，同时给予自身30s的发光效果。

- 当客户端安装了Fuzz模组时，可以单击消息末尾的+H按钮高亮路径点，使用\"/coordCompass clear\"命令来取消高亮

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `COMMAND`

## 你在哪（commandWhere）

使用/where命令获取指定玩家所在维度、坐标、对应的主世界/下界坐标，同时给予目标玩家30s的发光效果。

- 当客户端安装了Fuzz模组时，可以单击消息末尾的+H按钮高亮路径点，使用\"/coordCompass clear\"命令来取消高亮

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `COMMAND`

## 假人拾取控制器（fakePlayerPickUpController）

提供了两种模式来控制假人是否可以拾取物品

- [MainHandOnly] - 仅主手可拾取物品
- [NoPickUp] - 完全无法拾取物品
- [false] - 禁用规则

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `MainHandOnly`, `NoPickUp`, `false`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 玩家导游（commandPlayerLeader）

指定一名或多名玩家成为导游，使他们拥有发光效果

- 开启规则后，使用"/leader help"命令查看使用说明

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `COMMAND`

## ping命令（commandPacketInternetGroper）

玩家可以在游戏中使用ping命令

- 开启规则后，使用"/ping help"命令查看使用说明

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `COMMAND`

## 下界传送门不传送玩家（commandPlayerNoNetherPortalTeleport）

让玩家无法通过下界传送门进行传送

- 开启规则后，使用"/playerNoNetherPortalTeleport help"命令查看使用说明

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL` `COMMAND`

## 无限耐久（infiniteDurability）

使用物品不会丢失耐久

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 欢迎消息（welcomeMessage）

在玩家进入服务器时发送一条自定义消息给玩家（使用json文件来自定义消息）

- json位置：
- [ 存档文件夹 ]/carpetamsaddition/welcomeMessage.json

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`

## 获取存档大小（commandGetSaveSize）

使用 “/getSaveSize” 命令获取当前存档大小

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `COMMAND`

## 地毯规则总是设置为默认（carpetAlwaysSetDefault）

每当你设置carpet规则时，都会自动设置为默认值

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 禁用实验性内容检查（experimentalContentCheckDisabled）

> 版本：`Minecraft 1.19–1.21.1`

允许在游戏中通过添加数据包来游玩Minecraft的实验性内容（需要重启服务器）

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `EXPERIMENTAL`

## 可施肥小花（fertilizableSmallFlower）

使诸如蒲公英的小花也可以被骨粉催熟

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 安全的滴水石锥（safePointedDripstone）

> 版本：`Minecraft >= 1.17`

玩家落在滴水石锥上时，不会受到它的附加伤害

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁用滴水石锥碰撞箱（pointedDripstoneCollisionBoxDisabled）

> 版本：`Minecraft >= 1.17`

允许玩家穿过滴水石锥

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁止树叶生成（foliageGenerateDisabled）

树叶不会以任何方式生成

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 获取系统信息（commandGetSystemInfo）

使用“/getSystemInfo”命令获取服务器的系统信息

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `COMMAND`

## 铁傀儡不掉落花（ironGolemNoDropFlower）

铁傀儡不会掉落花

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 轻松获取瓶子草荚果（easyGetPitcherPod）

> 版本：`Minecraft >= 1.20`

玩家可以通过种植瓶子草来收获随机数量的瓶子草荚果（其最大数量是自定义的，最小数量为固定2个）

- 允许设定的范围：2 - 100 或 0

- 类型: `int`
  
  

- 默认值: `0`
  
  

- 参考选项: `0 或 2 - 100`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## goto命令（commandGoto）

使用 /goto 命令快速传送到指定维度的指定坐标

- 没有指定坐标时会传送至当前玩家所在位置的对应坐标

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `COMMAND`

## 发送玩家死亡位置（sendPlayerDeathLocation）

当玩家死亡时，会广播一条包含该玩家死亡坐标以及维度的消息

- [false] - 禁用规则
- [all] - 总是发送消息
- [realPlayerOnly] - 只有真实玩家死亡时发送消息
- [fakePlayerOnly] - 只有假人死亡时发送消息
- 当客户端安装了Carpet Org Addition时，可以单击消息末尾的H按钮高亮路径点，使用/highlight clear命令来取消高亮

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `all`, `realPlayerOnly`, `fakePlayerOnly`
  
  

- 分类: `AMS`, `SURVIVAL`

## 完美隐身（perfectInvisibility）

当玩家拥有隐身效果时将对生物完全隐身，即使穿着全套盔甲

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 潜行隐身（sneakInvisibility）

当玩家潜行时将对生物完全隐身，即使穿着全套盔甲

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 自定义命令权限等级（commandCustomCommandPermissionLevel）

自定义任何命令的权限等级

- 开启规则后，使用"/customCommandPermissionLevel help"命令查看使用说明

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `SURVIVAL`, `COMMAND`

## 潜影贝傀儡（shulkerGolem）

可以通过在潜影盒上放置一个雕刻南瓜来召唤一只潜影贝

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 猎头（headHunter）

当玩家死亡时他们会掉落一个自己的头颅

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 获取玩家头颅（commandGetPlayerSkull）

使用 “/getPlayerSkull <玩家名> <数量>” 命令来获取一名玩家的头颅

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `SURVIVAL`, `COMMAND`

## 快速升级村民（quickVillagerLevelUp）

每次打开交易屏幕时村民都会升级

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 可再生下界合金碎片（renewableNetheriteScrap）

自定义僵尸猪灵掉落下界合金碎片的概率

- 允许设定的范围：0 - 1

- 类型: `Double`
  
  

- 默认值: `0.0`
  
  

- 参考选项: `0.0`, `0.01`, `0.1`, `1.0`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 村民折扣叠加（stackableDiscounts）

> 版本：`Minecraft >= 1.20.2`

重新引入<23w31a的打骨折机制，让治疗村民的折扣可以叠加

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 禁用末地黑曜石柱重新生成（preventEndSpikeRespawn）

复活龙时末地的黑曜石柱子不会重新生成

- [true] - 禁止生成
- [false] - 禁用规则
- [keepEndCrystal] - 保留水晶生成

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`, `keepEndCrystal`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 假人交互同步客户端行为（fakePlayerInteractLikeClient）

修复几个假人和盔甲架、矿车、船在特定情况下与客户端相应行为结果不一样的问题

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `BUGFIX`

## 强力拴绳（strongLeash）

使拴绳不会因为距离过远而断裂

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 超级僵尸医生（superZombieDoctor）

玩家可以使用虚弱药水 + 金苹果瞬间将僵尸村民转化为村民

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 假玩家使用离线UUID（fakePlayerUseOfflinePlayerUUID）

让假玩家使用离线UUID

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 超级拴绳（superLeash）

让村民与怪物同样可以被拴绳栓住

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 每天都是满月（fullMoonEveryDay）

每天都是满月

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 轻松刷新交易（easyRefreshTrades）

当村民之前没有与玩家达成过交易时，每次打开交易界面都会刷新村民出售的内容

- 当玩家主手拿着绿宝石块时将不会触发刷新

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## jeb羊掉落随机颜色羊毛（jebSheepDropRandomColorWool）

当羊被命名为jeb_后，修剪羊毛会掉落随机颜色的羊毛

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 哭泣的黑曜石下界传送门（cryingObsidianNetherPortal）

可以使用哭泣的黑曜石构建下界传送门

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 熔炉冶炼时间控制器（furnaceSmeltingTimeController）

控制熔炉冶炼物品所需要的时间（单位：Tick）

- 类型: `int`
  
  

- 默认值: `-1`
  
  

- 参考选项: `-1`, `-1 - ?`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 假人默认生存模式（fakePlayerDefaultSurvivalMode）

当召唤假人时，无论如何假人都默认为生存模式

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 获取主手物品ID（commandGetHeldItemID）

使用“/getHeldItemID”命令来获取玩家主手物品的物品ID

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `SURVIVAL`, `COMMAND`

## 重新引入拌线骗特性（stringDupeReintroduced）

> 版本：`Minecraft >= 1.21.2`

重新引入拌线骗特性，可以通过此规则来继续使用刷线机

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 女巫红石粉掉落控制器（witchRedstoneDustDropController）

设置女巫掉落红石粉的保底数量

- 类型: `int`
  
  

- 默认值: `-1`
  
  

- 参考选项: `-1 - 999999`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 女巫萤石粉掉落控制器（witchGlowstoneDustDropController）

设置女巫掉落萤石粉的保底数量

- 类型: `int`
  
  

- 默认值: `-1`
  
  

- 参考选项: `-1 - 999999`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## TNT威力控制器（tntPowerController）

控制TNT的爆炸威力，设置为"-1"禁用规则

- 类型: `double`
  
  

- 默认值: `-1.0D`
  
  

- 参考选项: `-1.0D - 999999D`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `TNT`

## 温顺的末影人（meekEnderman）

你可以随便瞅末影人

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 客制化地狱门（customizedNetherPortal）

完全自定义地狱门，开启后，地狱门方块将可以被挖掘且挖掘地狱门框架和地狱门都不会让整个地狱门破碎

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 地毯拓展模组Wiki超链接（commandCarpetExtensionModWikiHyperlink）

列举了一些常用Carpet拓展的Wiki链接，你只需要使用"/carpetExtensionModWikiHyperlink <ExtensionName>"指令即可在聊天框中获取到跳转链接，点击链接即可跳转

- 随着版本更新该规则的拓展模组列表会不断调整

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `COMMAND`

## 自定义方块硬度（commandCustomBlockHardness）

使用指令来自定义任何方块的挖掘硬度

- 开启规则后，使用"/customBlockHardness help"命令来查看使用说明
- 客户端没有安装Carpet AMS Addition时，该规则完全不可用

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `AMS_NETWORK`, `COMMAND`

## 仅OP可召唤白名单中的玩家（onlyOpCanSpawnRealPlayerInWhitelist）

只有OP玩家可以召唤在白名单中的玩家，无论白名单是否开启

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `COMMAND`

## 真玩家控制保护（realPlayerControlDisabled）

禁止通过Carpet的/player指令操作任何真玩家，包括操作者自己，OP和控制台也不能绕过，不影响假玩家

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AMS`, `COMMAND`

## 禁止物品实体创建下界传送门（itemEntityCreateNetherPortalDisabled）

> 版本：`Minecraft >= 1.21`

让物品实体穿过下界传送门时不会创建传送门

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 强效经验修补（powerfulExpMending）

让物品只需要在背包内即可受到经验修补的效果，无需拿在手上

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## @玩家（commandAtSomeOnePlayer）

使用/@指令去@某位玩家

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `COMMAND`, `SURVIVAL`

## 大收纳袋（largeBundle）

> 版本：`Minecraft >= 1.17`

让收纳袋拥有一个9x3或9x6的UI，手持收纳袋右键即可打开UI

- 该规则不允许在收纳袋中放入收纳袋和潜影盒

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `9x3`, `9x6`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 最大链式更新深度（maxChainUpdateDepth）

> 版本：`Minecraft >= 1.19`

修改最大链式更新深度，设置为“-1”禁用规则

- 类型: `int`
  
  

- 默认值: `-1`
  
  

- 参考选项: `-1`, `100000`, `200000`, `300000`
  
  

- 分类: `AMS`, `FEATURE`, `EXPERIMENTAL`

## 幻翼生成警告（phantomSpawnAlert）

广播谁召唤了幻翼的消息

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `SURVIVAL`

## 禁用末地门加载（endPortalChunkLoadDisabled）

> 版本：`Minecraft >= 1.21`

让实体穿过末地门不会发生区块加载（Minecraft >= 1.20.6可用）

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 末地门加载器（endPortalChunkLoader）

> 版本：`Minecraft < 1.20.5`

让实体穿过末地门时会发生区块加载，与Minecraft >= 1.20.5时保持一致（Minecraft < 1.20.5可用）

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `AMS_CHUNKLOADER`

## AMS网络协议（amsNetworkProtocol）

AMS网络协议的开关

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `AMS_NETWORK`

## 禁用网络协议不兼容警告（incompatibleProtocolWarningDisabled）

禁用发送给使用不兼容AMS网络协议的客户端的警告，仅在amsNetworkProtocol开启时生效

- 类型: `boolean`



- 默认值: `false`



- 参考选项: `false`, `true`



- 分类: `AMS`, `AMS_NETWORK`

## 获取客户端玩家FPS（commandGetClientPlayerFps）

使用指令获取指定客户端玩家的FPS

- 开启规则后，使用"/getClientPlayerFps help"命令查看使用说明

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `AMS_NETWORK`, `COMMAND`

## 设置玩家姿势（commandSetPlayerPose）

使用指令设置玩家姿势

- 开启规则后，使用"/playerPose help"命令查看使用说明

- 类型: `String`
  
  

- 默认值: `false`
  
  

- 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

- 分类: `AMS`, `AMS_NETWORK`, `COMMAND`

## AMSP网络协议Debug指令（commandAmspDebug）

一些用于网络通信测试的指令

* 类型: `String`
  
  

* 默认值: `false`
  
  

* 参考选项: `0`, `1`, `2`, `3`, `4`, `ops`, `true`, `false`
  
  

* 分类: `AMS`, `AMS_NETWORK`, `COMMAND`

## 只有玩家可以生成地狱门（onlyPlayerCanCreateNetherPortal）

> 版本：`Minecraft >= 1.21`

只允许玩家穿过地狱门时生成新的地狱门

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 防止服务器暂停（preventServerPause）

> 版本：`Minecraft >= 1.21.2`

防止服务器60s后暂停

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`

## 仙人掌扳手拓展（flippinCactusExtras）

使仙人掌扳手能适用于更多方块

- 受影响的方块:
- 木桶、雕文书架、合成器、展示架

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`

## 实验性矿车速度（experimentalMinecartSpeed）

> 版本：`Minecraft >= 1.21.2`

设置实验性矿车的速度

- 启用该规则将覆盖游戏规则

- 类型: `int`
  
  

- 默认值: `-1`
  
  

- 参考选项: `-1`, `1000`
  
  

- 分类: `AMS`, `FEATURE`, `EXPERIMENTAL`

## 启用实验性矿车（experimentalMinecartEnabled）

> 版本：`Minecraft >= 1.21.2`

启用实验性矿车

- 服务器环境切换该规则设置时需要重启服务器
- 客户端环境切换该规则设置时需要重启客户端

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `FEATURE`, `EXPERIMENTAL`

## AMS翻译模式（amsTranslationMode）

切换Carpet AMS Addition的翻译模式

- [client] - 优先使用客户端自己的语言向客户端发送翻译文本
- [server] - 统一使用服务端设置的语言进行翻译

- 类型: `enum`
  
  

- 默认值: `CLIENT`
  
  

- 参考选项: `CLIENT`, `SERVER`
  
  

- 分类: `AMS`

## 方块硬度同步协议（blockHardnessSyncProtocol）

将服务端方块硬度同步给客户端

- 类型: `boolean`

  

- 默认值: `false`

  

- 参考选项: `false`, `true`

  

- 分类: `AMS`, `FEATURE`, `SURVIVAL`, `AMS_NETWORK`

## 位置标记（commandLocationTag）

启用/amsloc指令，用告示牌记录并查询跨维度位置；默认识别第一行为amsloc的普通橡木告示牌

- 类型：`String`



- 默认值：`false`



- 参考选项：`false`、`true`、`ops`、`0`、`1`、`2`、`3`、`4`



- 分类：`AMS`、`COMMAND`、`FEATURE`

## 可合成附魔金苹果（craftableEnchantedGoldenApples）

使用老版本的方式来合成附魔金苹果（8金块+1苹果）

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `SURVIVAL`, `CRAFTING`

## 更好的骨块合成（betterCraftableBoneBlock）

使用9个骨头可以合成出3个骨块(一点都不卡！)

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `SURVIVAL`, `CRAFTING`

## 可合成收纳袋（craftableBundle）

> 版本：`Minecraft 1.17–1.21.1`

玩家可以在Minecraft 1.17/1.18/1.19中合成收纳袋

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `SURVIVAL`, `CRAFTING`

## 可合成幽匿感测体（craftableSculkSensor）

> 版本：`Minecraft 1.17–1.18`

玩家可以在Minecraft 1.17/1.18中合成幽匿感测体

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `SURVIVAL`, `CRAFTING`

## 可合成鞘翅（craftableElytra）

添加了鞘翅的合成配方

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false` , `true`
  
  

- 分类: `AMS`, `SURVIVAL`, `CRAFTING`

## 更好的合成发射器（betterCraftableDispenser）

使用更灵活的配方来合成发射器

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `SURVIVAL`, `CRAFTING`

## 更好的合成磨制黑石按钮（betterCraftablePolishedBlackStoneButton）

> 版本：`Minecraft > 1.17`

使用深板岩来合成磨制黑石按钮

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `SURVIVAL`, `CRAFTING`

## 腐肉烧皮革（rottenFleshBurnedIntoLeather）

腐肉可以在熔炉中烧成皮革

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `SURVIVAL`, `CRAFTING`

## 使用新的磁石配方（useNewLodestoneRecipe）

> 版本：`Minecraft < 1.21.5`

使用铁锭来合成磁石

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `SURVIVAL`, `CRAFTING`

## 可合成南瓜头（craftableCarvedPumpkin）

使用剪刀和南瓜合成南瓜头，每合成一个南瓜头剪刀-1耐久（受耐久附魔影响）

- 类型: `boolean`
  
  

- 默认值: `false`
  
  

- 参考选项: `false`, `true`
  
  

- 分类: `AMS`, `SURVIVAL`, `CRAFTING`
