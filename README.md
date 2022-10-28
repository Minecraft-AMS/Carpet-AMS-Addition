# Carpet-AMS-Addition

一个简易的基于Carpet mod的拓展

**由于一些功能是低版本模组升级而来，不确定有没有严重问题，由于能力有限该mod不保证能完全解决后续问题**

假如遇到了兼容性问题/破坏了游戏机制/影响了游戏性能/想要特定某个版本可以尽情提issue，我们会尽可能解决w!

## 支持版本/Supported Version

- `1.18.2`（主要维护版本，因为AMS在这个版本）
- `1.17.1`

## 功能/Feature

### 规则/Rules

`NoteBlockChunkLoader`: 当音符盒被激活时会加载3x3的区块持续15s，无论音符盒是否能够发出声音。

`SuperBow`: 可以让弓同时拥有无限和经验修补附魔。

`zeroTickCactus`: 使计划刻仍可以给予随机刻进行强制催熟仙人掌。

`zeroTickBamboo`: 使计划刻仍可以给予随机刻进行强制催熟竹子。

`zeroTickChorusFlower`: 使计划刻仍可以给予随机刻进行强制催熟紫颂花。

`zeroTickSugarCane`: 使计划刻仍可以给予随机刻进行强制催熟甘蔗。

`zeroTickStem`: 使计划刻仍可以给予随机刻进行强制催熟海带、缠怨藤、垂泪藤。

`zeroTickAllPlants`: 使计划刻仍可以给予随机刻进行强制催熟以上提到的所有作物。

`optimizedDragonRespawn`: 大幅度优化了龙战判定代码的性能表现，可能影响原版特性。

### 区块加载控制/Ghost Commands

控制玩家的区块加载。玩家上下线时会将交互状态重置回默认状态以避免[MC-157812](https://bugs.mojang.com/browse/MC-157812)
的发生
格式：`/ghost [true/false]`

### 相关链接

1. `NoteBlockChunkLoader`
   主要功能实现代码来自1.15.x-1.16.x的[NoteBlockChunkLoader](https://github.com/GC-server-CN/NoteBlockChunkLoader)

2. `zeroTick`
   系列主要功能实现代码来自1.16.4/1.16.5的[OhMyVanillaMinecraf](https://github.com/hit-mc/OhMyVanillaMinecraft)

3. `ghost`交互控制功能实现代码来自1.16.5/1.17.1的[Intricarpet](https://github.com/lntricate1/intricarpet)

### 致谢

- 感谢[memorydream](https://github.com/memorydream)，[keuin](https://github.com/keuin)
  ，[lntricate1](https://github.com/lntricate1)的项目代码提供参考
- 感谢[401Unauthorized](https://github.com/YehowahLiu)在解决开发问题上的无私帮助