

### [ 中文 | [English](/carpetamsaddition/en_us/Commands_en) ]

# <center>------ 指 令 ------</center>

&emsp;

### 区块加载控制（commandPlayerChunkLoadController）

- **/playerChunkLoading [true/false]**

  开关玩家自身加载。


### 禁用铁砧交互（commandAnvilInteractionDisabled)

- **/anvilInteractionDisabled [true/false]**

  控制玩家是否可与铁砧交互。

### 更新抑制崩溃修复（amsUpdateSuppressionCrashFix）

- **/amsUpdateSuppressionCrashFixForceMode [true/false]**

  控制是否在开启 `自定义方块更新抑制器（customBlockUpdateSuppressor）` 规则开启时是否强制开启。

### 自定义方块爆炸抗性（commandCustomBlockBlastResistance）

- **/customBlockBlastResistance set <方块> <抗性>**

  添加或修改方块及其爆炸抗性。

  

- **/customBlockBlastResistance remove <方块>**

  从列表中移除方块。

  

- **/customBlockBlastResistance removeAll**

  移除列表中的所有方块。




- **/customBlockBlastResistance list**

  显示所有添加的方块。



- **/customBlockBlastResistance help**

  查看使用说明。

### 我在哪（commandHere）

- **/here**

  发送当前自身所在维度、坐标、对应的主世界/下界坐标，同时给予自身30s的发光效果。

### 你在哪（commandWhere）

- **/where**

  获取指定玩家所在维度、坐标、对应的主世界/下界坐标，同时给予目标玩家30s的发光效果。

### 玩家导游（commandPlayerLeader）

- **/leader add <玩家>**

  添加一名导游。



- **/leader remove <玩家>**

  移除一名导游。



- **/leader removeAll**

  解雇所有导游。



- **/leader broadcastLeaderPos <玩家> interval &lt;tick&gt;**

  定时广播一名导游的坐标，时间设置为负数停止广播。



- **/leader list**

  查看导游列表。



- **/leader help**

  查看使用说明。

### ping命令（commandPacketInternetGroper）

- **/pings <目标IP/域名> <ping的次数>**

  ping某个主机。



- **/pings stop**

  中断ping操作。

### 获取存档大小（commandGetSaveSize）

- **/getSaveSize**

  获取存档大小。

### 获取系统信息（commandGetSystemInfo）

- **/getSystemInfo**

  获取服务器的系统信息。

### goto命令（commandGoto）

- **/goto <维度> <x> <y> <z>**

  传送到指定维度的指定坐标。



- **/goto <维度>**

  传送至当前玩家所在位置的对应坐标。

### 自定义命令权限等级（commandCustomCommandPermissionLevel）

- **/customCommandPermissionLevel set <指令> <权限等级>**

  设置某个指令的权限等级。



- **/customCommandPermissionLevel remove <指令>**

  删除某个被修改的指令。



- **/customCommandPermissionLevel removeAll**

  删除所有被修改的指令。



- **/customCommandPermissionLevel refresh**

  刷新指令树。



- **/customCommandPermissionLevel list**

  查看被修改的指令列表。



- **/customCommandPermissionLevel help**

  查看使用说明。

### 获取玩家头颅（commandGetPlayerSkull）

- **/getPlayerSkull <玩家>**

  获取指定在线玩家的头颅。

### 自定义可移动方块（commandCustomMovableBlock）

- **/customMovableBlock add <方块注册名>**

  增加一个可移动方块。



- **/customMovableBlock remove <方块注册名>**

  删除一个可移动方块。



- **/customMovableBlock removeAll**

  删除所有添加的可移动方块。



- **/customMovableBlock list**

  查看可移动方块列表。



- **/customMovableBlock help**

  查看使用说明。

### 获取主手物品ID（commandGetHeldItemID）

- **/getHeldItemID**

  获取玩家主手物品的物品ID。

### 自定义防火物品（commandCustomAntiFireItems）

- **/customAntiFireItems add <物品注册名>**

  增加一个防火物品。



- **/customAntiFireItems remove <物品注册名>**

  删除一个可防火物品。



- **/customAntiFireItems removeAll**

  删除所有添加的防火物品。



- **/customAntiFireItems list**

  查看防火物品列表。



- **/customAntiFireItems help**

  查看使用说明。

### 地毯拓展模组Wiki超链接（commandCarpetExtensionModWikiHyperlink）

- **/carpetExtensionModWikiHyperlink <拓展模组名>**

  获取某个Carpet拓展模组的Wiki链接。

### 自定义方块硬度（commandCustomBlockHardness）

- **/customBlockHardness set <方块> <硬度>**

  添加或修改方块及其硬度。

  

- **/customBlockHardness remove <方块>**

  从列表中移除方块。

  

- **/customBlockHardness removeAll**

  移除列表中的所有方块。

  


- **/customBlockBlastResistance getDefaultHardness <方块>**

  查询方块的默认硬度。



- **/customBlockHardness list**

  显示所有添加的方块。



- **/customBlockHardness help**

  查看使用说明。