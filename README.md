# Carpet-AMS-Addition

[![Dev Builds](https://github.com/Minecraft-AMS/Carpet-AMS-Addition/actions/workflows/gradle.yml/badge.svg)](https://github.com/Minecraft-AMS/Carpet-AMS-Addition/actions/workflows/gradle.yml)

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

`SuperBow`: 可以让弓同时拥有无限和经验修补附魔。

`scheduledRandomTickCactus`: 使计划刻仍可以给予随机刻进行强制催熟仙人掌。

`scheduledRandomTickBamboo`: 使计划刻仍可以给予随机刻进行强制催熟竹子。

`scheduledRandomTickChorusFlower`: 使计划刻仍可以给予随机刻进行强制催熟紫颂花。

`scheduledRandomTickSugarCane`: 使计划刻仍可以给予随机刻进行强制催熟甘蔗。

`scheduledRandomTickStem`: 使计划刻仍可以给予随机刻进行强制催熟海带、缠怨藤、垂泪藤。

`scheduledRandomTickAllPlants`: 使计划刻仍可以给予随机刻进行强制催熟以上提到的所有作物。

`optimizedDragonRespawn`: 大幅度优化了龙战判定代码的性能表现，可能影响原版特性。

`blockChunkLoader [block_type]`: 当红石激活特定方块(音符盒/钟)时强加载所在区块300gt，可选项包括`false`(不使用加载)
,`note_block`(音符盒)和`bell_block`(钟)

`CraftableEnchantedGoldenApples`: 可制作附魔金苹果。

`BetterCraftableBoneBlock`: 更好的合成骨块（9个骨头合成三个骨块，主要为了降低卡顿）。

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
