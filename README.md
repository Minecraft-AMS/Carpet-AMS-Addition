# Carpet-AMS-Addition

一个简易的基于Carpet mod的拓展

**由于一些功能是低版本模组升级而来，不确定有没有严重问题，由于能力有限该mod不保证能完全解决后续问题**

假如遇到了兼容性问题/破坏了游戏机制/影响了游戏性能/想要特定某个版本可以尽情提issue，我们会尽可能解决w!

## 支持版本/Supported Version

- `22w46a`
- `1.19.2`
- `1.18.2`（主要维护版本）
- `1.17.1`
- `1.16.4/1.16.5`

实际上1.16~22w46a之间的任何版本理论上应该都兼容，若有版本支持问题请提Issue.

## 功能/Feature

### 规则/Rules

`SuperBow`: 开启后，可以让弓同时拥有无限和经验修补附魔。

`scheduledRandomTickAllPlants`: 开启后，使计划刻事件可触发以下所有植物的随机刻生长行为，用于恢复1.16版本的强制催熟特性。亦可通过以下指令单独控制特定植物是否可强制催熟。

- `scheduledRandomTickCactus`: 开启后，使计划刻事件可触发仙人掌的随机刻生长行为。

- `scheduledRandomTickBamboo`: 开启后，使计划刻事件可触发竹子的随机刻生长行为。

- `scheduledRandomTickChorusFlower`: 开启后，使计划刻事件可触发紫颂花的随机刻生长行为。

- `scheduledRandomTickSugarCane`: 开启后，使计划刻事件可触发甘蔗的随机刻生长行为。

- `scheduledRandomTickStem`: 开启后，使计划刻事件可触发海带、缠怨藤、垂泪藤的随机刻生长行为。

`optimizedDragonRespawn`: 大幅度优化了龙战判定代码的性能表现，为基于末地祭坛设计的末地石农场提供性能优化。注意：本选项开启后可能影响原版特性。

`note_block`: 当上边沿红石信号激活音符盒时，为该音符盒所在区块添加类型为"note_block"的加载票。

`bell_block`: 当上边沿红石信号激活钟时，为钟方块所在区块添加类型为"bell_block"的加载票。

`pistonBlockChunkLoader`: 开启后，对于一个正上方放有骨块的普通活塞或黏性活塞，当该活塞产生活塞头的推出/拉回事件时，在创建推出/拉回事件的那一游戏刻为**活塞头方块所在区块**添加类型为"piston_block"，加载等级为30的加载票，持续时间为300gt。注意，黏性活塞的失败收回事件（如尝试拉回超过12个方块时）也可创建加载票。

`CraftableEnchantedGoldenApples`: 开启后，可利用金块和苹果合成附魔金苹果，即恢复到15w44a前的表现。

`BetterCraftableBoneBlock`: 开启后，可更好的合成骨块（可用9个骨头合成三个骨块，大幅降低合成卡顿）。

`netherWaterPlacement`: 开启后，玩家可通过使用水桶的方式在地狱维度中放置水源。

`breakableDeepslate`: 开启后，深板岩的挖掘硬度将与石头相同（均可在急迫二效果下用效率5钻石镐进行瞬间挖掘）。

### 区块加载控制/Chunk Loading Commands

控制玩家的区块加载，有时候会有比较奇怪的情况，可以挪到附近的区块再回来，可能因为某些未知原因所在区块还会加载。不会移除所在维度的玩家检测，例如主世界出生点区块加载和末地主岛加载。

玩家上下线时会将交互状态重置回加载以避免[MC-157812](https://bugs.mojang.com/browse/MC-157812)。

格式：`/chunkloading [true/false]`

### 相关链接

1. `NoteBlockChunkLoader`
   主要功能实现代码来自1.15.x-1.16.x的[NoteBlockChunkLoader](https://github.com/GC-server-CN/NoteBlockChunkLoader)

2. `scheduledRandomTick`
   系列主要功能实现代码来自1.16.4/1.16.5的[OhMyVanillaMinecraf](https://github.com/hit-mc/OhMyVanillaMinecraft)

3. `chunkloading`交互控制功能实现代码来自1.16.5/1.17.1的[Intricarpet](https://github.com/lntricate1/intricarpet)

### 致谢

- 感谢[memorydream](https://github.com/memorydream)，[keuin](https://github.com/keuin)
  ，[lntricate1](https://github.com/lntricate1)的项目代码提供参考
- 感谢gnembon和Fallen_Breath对carpet, carpet-extra和carpet-TIS-addition的实现代码
- 感谢[401Unauthorized](https://github.com/YehowahLiu)在解决开发问题上的无私帮助
- 感谢alpha_hhh和Menggui233帮助测试和提建议
