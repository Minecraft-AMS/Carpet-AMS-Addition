## 龙战优化
实际上这份代码主要优化的是龙战开始时的流程，即放下水晶后检查末地门结构、替换相应方块的过程。
### Vanilla龙战流程介绍
### EnderDragonFight#respawnDragon()
在放下水晶后，服务器会在主线程中调用EnderDragonFight#respawnDragon()，该函数即本代码主要优化目标.
1. 首先，respawnDragon()会检查NBT数据里面有没有存末地门的位置exitPortalLocation，假如没有会用findEndPortal()找一个位置作为门，假如找不到就会按照generatePortal()生成一个
2. 接下来，respawnDragon()会不断调用findEndPortal()刷新一个门的位置，每次找到一个门的位置后会将附近[1]的基岩和末地门方块替换为末地石

[1]具体来说应该是一个6x6x5的空间，也就是末地门基岩的形状.

### EnderDragonFight#findEndPortal()
1. 枚举末地8x8区块，检查其方块实体中所有EndPortalBlockEntity
2. 对于每个EndPortalBlockEntity，使用BlockPattern#searchAround()在附近搜查末地门的**基岩**结构，找到了立即返回
3. 接下来在末地原点(0 0)附近最高的实体方块(#MOTION_BLOCKING?)往下每一层的中心(0,22,0; 0,21,0; etc.)附近都进行searchAround()，找到了立即返回

### BlockPattern#searchAround()
1. 枚举特征的所有旋转
2. 枚举中心附近一个范围的所有坐标作为放置特征的中心
3. 放置特征后尝试匹配

### 优化原理
原则：
- searchAround()非常昂贵，尽量避免
### findEndPortal() step 1
- [x] 实际上枚举重复的区块没有意义，可以缓存
- [x] EndPortalBlockEntity有一个子类EndGatewayBlockEntity，即折跃门方块，去掉对其的检查
### findEndPortal() step 2
- [ ]搜查本身可能还有可优化的空间，枚举大量旋转之后再验证，读取了很多重复信息，实际上假如某些位置方块不是基岩可以大量剪枝
### findEndPortal() step 3
- [x] 逐层下降显然也可以对下降高度进行缓存
- [x] 逐层下降中显然额外枚举的放置点有大量重复
- [x] **(BREAKING)实际上本功能是用于低版本升级，故只需要在convertFromLegacy()或被迫需要找门状结构时才被需要**