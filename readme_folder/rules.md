\>\>\> [返回索引](/README.md)

# 规则

## 超级弓 (superBow)

开启后，可以让弓同时拥有无限和经验修补附魔。

- 类型: `boolean`

- 默认值: `false`

- 参考选项: `false` , `true`

- 分类: `AMS` , `FEATURE`

  

## 计划刻催熟所有作物 (scheduledRandomTickAllPlants)

开启后，使计划刻事件可触发以下所有植物的随机刻生长行为，用于恢复1.15版本的强制催熟特性。亦可通过以下指令单独控制特定植物是否可强制催熟。

- `scheduledRandomTickCactus`: 开启后，使计划刻事件可触发仙人掌的随机刻生长行为。

- `scheduledRandomTickBamboo`: 开启后，使计划刻事件可触发竹子的随机刻生长行为。

- `scheduledRandomTickChorusFlower`: 开启后，使计划刻事件可触发紫颂花的随机刻生长行为。

- `scheduledRandomTickSugarCane`: 开启后，使计划刻事件可触发甘蔗的随机刻生长行为。

- `scheduledRandomTickStem`: 开启后，使计划刻事件可触发海带、缠怨藤、垂泪藤的随机刻生长行为。

  <该规则从 [OhMyVanillaMinecraf](https://github.com/hit-mc/OhMyVanillaMinecraft) 移植>

- 类型: `boolean`

- 默认值: `false`

- 参考选项: `false` , `true`

- 分类: `AMS` , `FEATURE` , `SURVIVAL`



## 龙战优化 (optimizedDragonRespawn)

大幅度优化了龙战判定代码的性能表现，为基于末地祭坛设计的末地石农场提供性能优化。注意：本选项开启后可能影响原版特性。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `OPTIMIZATION`



### 区块加载控制 (commandChunkLoading)

控制玩家的区块加载，有时候会有比较奇怪的情况，可以挪到附近的区块再回来，可能因为某些未知原因所在区块还会加载。不会移除所在维度的玩家检测，例如主世界出生点区块加载和末地主岛加载。

玩家上下线时会将交互状态重置回加载以避免[MC-157812](https://bugs.mojang.com/browse/MC-157812)。

<该规则从 [Intricarpet](https://github.com/lntricate1/intricarpet) 移植>

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `COMMAND`



## 音符盒区块加载 (noteBlockChunkLoader)

开启后，当上边沿红石信号激活音符盒时，为该音符盒所在区块添加类型为"note_block"，加载等级为30的加载票，持续时间为300gt（15s）。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`

## 音符盒区块加载Pro (noteBlockChunkLoaderPro)

开启后，当上边沿红石信号激活音符盒且音符盒上方放置了一个骨块时，为该音符盒所在区块添加类型为"note_block"，加载等级为30的加载票，持续时间为300gt（15s）。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`

## 钟区块加载 (bellBlockChunkLoader)

开启后，当上边沿红石信号激活钟时，为钟方块所在区块添加类型为"bell_block"，加载等级为30的加载票，持续时间为300gt（15s）。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 活塞头区块加载 (pistonBlockChunkLoader)

开启后，对于一个正上方放有骨块的普通活塞或黏性活塞，当该活塞产生活塞头的推出/拉回事件时，在创建推出/拉回事件的那一游戏刻为**活塞头方块所在区块**添加类型为"piston_block"，加载等级为30的加载票，持续时间为300gt（15s）。注意，黏性活塞的失败收回事件（如尝试拉回超过12个方块时）也可创建加载票。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AMS` , `FEATURE`



## 地狱可放水 (netherWaterPlacement)

开启后，玩家可通过使用水桶的方式在地狱维度中放置水源。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 易碎深板岩 (breakableDeepslate)

开启后，深板岩的挖掘硬度将与石头相同（均可在急迫二效果下用效率5钻石镐进行瞬间挖掘）。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE` , `SURVIVAL`



## 易碎黑曜石 (breakableObsidian)

开启后，黑曜石的挖掘硬度将与深板岩相同。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE` , `SURVIVAL`



## 可开采末地传送门框架 (canBreakEndPortalFrame)

开启后，玩家可以使用钻石镐或下届合金镐来开采末地传送门框架。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE` , `SURVIVAL`



## 可开采基岩(canBreakBedRock)

开启后，玩家将可以使用钻石镐或下届合金镐来开采基岩，使用钻石镐时挖掘硬度为888，使用下届合金镐时挖掘硬度为666。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE` , `SURVIVAL`



## 伪和平(fakePeace)

开启后，所有生物不会生成，但不影响困难难度（类似伪和平）。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE` , `SURVIVAL`



## 炸毁所有方块(destroysEverything)

开启后，所有方块的爆炸抗性均为 0。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE` , `SURVIVAL` , `TNT`



## 共享打折(sharedVillagerDiscounts)

开启后，玩家将僵尸村民治疗为村民后的获得的折扣将共享给所有玩家。

<该规则从 [totos-carpet-tweaks](https://github.com/totorewa/totos-carpet-tweaks) 移植>

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE` , `SURVIVAL`



## 熄灭的篝火(extinguishedCampfire)

开启后，当玩家放置篝火时，篝火处于熄灭状态。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE` , `SURVIVAL`



## 可移动末影箱(movableEnderChest)

开启后，末影箱可以被活塞/黏性活塞推动。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 可移动末地传送门框架(movableEndPortalFrame)

开启后，末地传送门框架可以被活塞/黏性活塞推动。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 可移动黑曜石(movableObsidian)

开启后，黑曜石可以被活塞/黏性活塞推动。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 可移动哭泣的黑曜石(movableCryingObsidian)

开启后，哭泣的黑曜石可以被活塞/黏性活塞推动。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 可移动基岩(movableBedRock)

开启后，基岩可以被活塞/黏性活塞推动。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 可移动附魔台(movableEnchantingTable)

开启后，附魔台可以被活塞/黏性活塞推动。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 可移动信标(movableBeacon)

开启后，信标可以被活塞/黏性活塞推动。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 可移动强化深板岩(movableReinforcedDeepslate)

开启后，强化深板岩可以被活塞/黏性活塞推动。

<该规则 `Minecraft Version >= 1.19` 可用>

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 可移动铁砧(movableAnvil)

开启后，铁砧可以被活塞/黏性活塞推动。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `FEATURE`



## 可合成附魔金苹果(CraftableEnchantedGoldenApples)

开启后，可利用金块和苹果合成附魔金苹果，即恢复到15w44a前的表现。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `SURVIVAL` , `CRAFTING`



## 更好的合成骨块(betterCraftableBoneBlock)

开启后，可更好的合成骨块（可用9个骨头合成三个骨块，大幅降低合成卡顿）。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `SURVIVAL` , `CRAFTING`



## 可合成收纳袋(craftableBundle)

开启后，玩家可以在Minecraft 1.17/1.18/1.19中合成收纳袋。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `SURVIVAL` , `CRAFTING`



## 可合成幽匿感测体(craftableSculkSensor)

开启后，玩家可以在Minecraft 1.17/1.18中合成幽匿感测体。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `SURVIVAL` , `CRAFTING`



## 可合成鞘翅(craftableElytra)

开启后，玩家可以在Minecraft中合成鞘翅。

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false` , `true`
- 分类: `AMS` , `SURVIVAL` , `CRAFTING`
