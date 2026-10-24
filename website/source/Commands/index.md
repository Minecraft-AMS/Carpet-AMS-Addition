## 方块加载器（commandNoteblockChunkloader | commandPistonChunkloader | commandBellChunkloader）

音符盒、活塞和钟共用 `/blockChunkloader`。先开启对应规则，再设置条件、时长和范围，最后使用 `enabled true` 启用加载器。

| `<type>` | 方块 | 对应规则 |
| --- | --- | --- |
| `noteblock` | 音符盒 | `commandNoteblockChunkloader` |
| `piston` | 活塞 | `commandPistonChunkloader` |
| `bell` | 钟 | `commandBellChunkloader` |

### 指令格式

`<参数>` 需要替换为实际内容，`|` 表示任选其一，输入时不包含这些符号。`/blockChunkloader help` 查看帮助，`/blockChunkloader list` 查看可配置的加载器。

下表中的指令均以 `/blockChunkloader <type>` 开头。

| 子指令 | 用途 |
| --- | --- |
| `show` | 查看设置和条件 |
| `enabled <true\|false>` | 启用或停用加载器 |
| `time <time>` | 设置加载时长 |
| `range <range>` | 设置强加载范围 |
| `test <x> <y> <z>` | 检测指定位置的条件 |
| `reset` | 恢复默认配置并停用加载器 |
| `group create <name>` | 创建条件组 |
| `group list` | 查看全部条件组 |
| `group show <name>` | 查看指定组及其条件 |
| `group mode <name> <all\|any>` | 设置组内全部满足或至少一条满足 |
| `group enabled <name> <true\|false>` | 启用或停用组，保留组内条件 |
| `group remove <name>` | 删除组及其条件 |
| `group clear` | 删除所有组，重建空的 `default` 组 |
| `condition add <group> direction <direction> <distance> <is\|not> <block>` | 按方向和距离添加条件 |
| `condition add <group> offset <dx> <dy> <dz> <is\|not> <block>` | 按坐标偏移添加条件 |
| `condition list <group>` | 查看组内条件和当前编号 |
| `condition remove <group> <id>` | 按编号删除条件 |
| `condition clear <group>` | 清空组内条件 |

### 参数说明

| 参数 | 含义 |
| --- | --- |
| `<time>` | 加载时长，1～300 游戏刻，默认 `300` |
| `<range>` | 强加载区域大小，可选 `1x1`、`3x3`、`5x5`、`7x7`、`9x9`，默认 `3x3` |
| `<name>` | 要创建或管理的条件组名称，例如 `default`、`my_group` |
| `<group>` | 条件所属的已有组名称，默认可填写 `default` |
| `all`、`any` | 组内全部条件满足或至少一条满足，默认为`all`全部条件满足 |
| `<direction>` | `up` 上、`down` 下、`north` 北、`south` 南、`east` 东、`west` 西、`northeast` 东北、`northwest` 西北、`southeast` 东南、`southwest` 西南 |
| `<distance>` | 距离，1～32 格，斜角按各轴计算，例如 `northeast 2` 表示东北方斜角 2 格 |
| `<dx>`、`<dy>`、`<dz>` | 相对加载器的世界坐标偏移，各为 -32～32，正 X 向东、正 Y 向上、正 Z 向南 |
| `is`、`not` | 要求指定位置匹配或不匹配方块条件 |
| `<block>` | 方块 ID，例如 `minecraft:bone_block` |
| `<id>` | 条件编号，编辑条件后会自动重新编号，删除前务必使用 `condition list` 查看并确认 |
| `<x>`、`<y>`、`<z>` | 要测试的加载器位置，例如 `~ ~ ~` |

条件组名允许使用英文字母、数字、下划线 `_` 和连字符 `-`，长度为 1～32 个字符。

多个已启用的非空组必须全部满足，空组和停用组不参与判断，没有有效条件组时不限制触发。检测原点为音符盒、活塞底座或钟，加载中心为音符盒、钟自身的区块，或活塞头到达的区块。

### 示例

音符盒上方有骨块、同高度东北方斜角一格有金块、东北上方一格有钻石块，且西北方顶角上方一格有石头时，触发后强加载 `5x5` 区块，持续 `200` 游戏刻：

```text
# 开启音符盒加载器规则，要求 2 级配置权限，并保存规则设置
/carpet setDefault commandNoteblockChunkloader ops

# 恢复音符盒加载器的默认配置，此时加载器停用
/blockChunkloader noteblock reset

# 将加载时长设为 200 游戏刻
/blockChunkloader noteblock time 200

# 将强加载范围设为 5x5 区块
/blockChunkloader noteblock range 5x5

# 在 default 组添加条件：音符盒上方一格为骨块
/blockChunkloader noteblock condition add default direction up 1 is minecraft:bone_block

# 在 default 组添加条件：音符盒同高度东北方斜角一格为金块
/blockChunkloader noteblock condition add default direction northeast 1 is minecraft:gold_block

# 在 default 组添加条件：音符盒东北上方一格为钻石块
/blockChunkloader noteblock condition add default offset 1 1 -1 is minecraft:diamond_block

# 在 default 组添加条件：音符盒西北方顶角上方一格为石头，组内默认要求全部条件满足
/blockChunkloader noteblock condition add default offset -1 1 -1 is minecraft:stone

# 启用音符盒加载器，触发音符盒且条件满足时加载区块
/blockChunkloader noteblock enabled true
```

## 区块加载控制（commandPlayerChunkLoadController）

| 指令 | 功能 |
| --- | --- |
| `/playerChunkLoading [true/false]` | 开关玩家自身加载 |

## 禁用铁砧交互（commandAnvilInteractionDisabled)

| 指令 | 功能 |
| --- | --- |
| `/anvilInteractionDisabled [true/false]` | 控制玩家是否可与铁砧交互 |

## 更新抑制崩溃修复（amsUpdateSuppressionCrashFix）

| 指令 | 功能 |
| --- | --- |
| `/amsUpdateSuppressionCrashFixForceMode [true/false]` | 控制是否在开启 `自定义方块更新抑制器（customBlockUpdateSuppressor）` 规则开启时是否强制开启 |

## 自定义方块爆炸抗性（commandCustomBlockBlastResistance）

| 指令 | 功能 |
| --- | --- |
| `/customBlockBlastResistance set <方块> <抗性>` | 添加或修改方块及其爆炸抗性 |
| `/customBlockBlastResistance remove <方块>` | 从列表中移除方块 |
| `/customBlockBlastResistance removeAll` | 移除列表中的所有方块 |
| `/customBlockBlastResistance list` | 显示所有添加的方块 |
| `/customBlockBlastResistance help` | 查看使用说明 |

## 我在哪（commandHere）

| 指令 | 功能 |
| --- | --- |
| `/here` | 发送当前自身所在维度、坐标、对应的主世界/下界坐标，同时给予自身30s的发光效果 |

## 你在哪（commandWhere）

| 指令 | 功能 |
| --- | --- |
| `/where` | 获取指定玩家所在维度、坐标、对应的主世界/下界坐标，同时给予目标玩家30s的发光效果 |

## 玩家导游（commandPlayerLeader）

| 指令 | 功能 |
| --- | --- |
| `/leader add <玩家>` | 添加一名导游 |
| `/leader remove <玩家>` | 移除一名导游 |
| `/leader removeAll` | 解雇所有导游 |
| `/leader broadcastLeaderPos <玩家> interval <tick>` | 定时广播一名导游的坐标，时间设置为负数停止广播 |
| `/leader list` | 查看导游列表 |
| `/leader help` | 查看使用说明 |

## ping命令（commandPacketInternetGroper）

| 指令 | 功能 |
| --- | --- |
| `/pings <目标IP/域名> <ping的次数>` | ping某个主机 |
| `/pings stop` | 中断ping操作 |

## 获取存档大小（commandGetSaveSize）

| 指令 | 功能 |
| --- | --- |
| `/getSaveSize` | 获取存档大小 |

## 获取系统信息（commandGetSystemInfo）

| 指令 | 功能 |
| --- | --- |
| `/getSystemInfo` | 获取服务器的系统信息 |

## goto命令（commandGoto）

| 指令 | 功能 |
| --- | --- |
| `/goto <维度> <x> <y> <z>` | 传送到指定维度的指定坐标 |
| `/goto <维度>` | 传送至当前玩家所在位置的对应坐标 |

## 自定义命令权限等级（commandCustomCommandPermissionLevel）

| 指令 | 功能 |
| --- | --- |
| `/customCommandPermissionLevel set <指令> <权限等级>` | 设置某个指令的权限等级 |
| `/customCommandPermissionLevel remove <指令>` | 删除某个被修改的指令 |
| `/customCommandPermissionLevel removeAll` | 删除所有被修改的指令 |
| `/customCommandPermissionLevel refresh` | 刷新指令树 |
| `/customCommandPermissionLevel list` | 查看被修改的指令列表 |
| `/customCommandPermissionLevel help` | 查看使用说明 |

## 获取玩家头颅（commandGetPlayerSkull）

| 指令 | 功能 |
| --- | --- |
| `/getPlayerSkull <玩家> <数量>` | 获取指定玩家的头颅 |

## 自定义可移动方块（commandCustomMovableBlock）

| 指令 | 功能 |
| --- | --- |
| `/customMovableBlock add <方块注册名>` | 增加一个可移动方块 |
| `/customMovableBlock remove <方块注册名>` | 删除一个可移动方块 |
| `/customMovableBlock removeAll` | 删除所有添加的可移动方块 |
| `/customMovableBlock list` | 查看可移动方块列表 |
| `/customMovableBlock help` | 查看使用说明 |

## 获取主手物品ID（commandGetHeldItemID）

| 指令 | 功能 |
| --- | --- |
| `/getHeldItemID` | 获取玩家主手物品的物品ID |

## 自定义防火物品（commandCustomAntiFireItems）

| 指令 | 功能 |
| --- | --- |
| `/customAntiFireItems add <物品注册名>` | 增加一个防火物品 |
| `/customAntiFireItems remove <物品注册名>` | 删除一个可防火物品 |
| `/customAntiFireItems removeAll` | 删除所有添加的防火物品 |
| `/customAntiFireItems list` | 查看防火物品列表 |
| `/customAntiFireItems help` | 查看使用说明 |

## 地毯拓展模组Wiki超链接（commandCarpetExtensionModWikiHyperlink）

| 指令 | 功能 |
| --- | --- |
| `/carpetExtensionModWikiHyperlink <拓展模组名>` | 获取某个Carpet拓展模组的Wiki链接 |

## 自定义方块硬度（commandCustomBlockHardness）

| 指令 | 功能 |
| --- | --- |
| `/customBlockHardness set <方块> <硬度>` | 添加或修改方块及其硬度 |
| `/customBlockHardness remove <方块>` | 从列表中移除方块 |
| `/customBlockHardness removeAll` | 移除列表中的所有方块 |
| `/customBlockBlastResistance getDefaultHardness <方块>` | 查询方块的默认硬度 |
| `/customBlockHardness list` | 显示所有添加的方块 |
| `/customBlockHardness help` | 查看使用说明 |

## @玩家（commandAtSomeOnePlayer）

| 指令 | 功能 |
| --- | --- |
| `/@ <玩家>` | @某位玩家 |

## 获取客户端玩家FPS（commandGetClientPlayerFps）

| 指令 | 功能 |
| --- | --- |
| `/getClientPlayerFps <玩家>` | 获取指定玩家的FPS |
| `/getClientPlayerFps help` | 查看使用说明 |

## 设置玩家姿势（commandSetPlayerPose）

| 指令 | 功能 |
| --- | --- |
| `/playerPose <玩家> set <姿势>` | 设置玩家姿势 |
| `/playerPose <玩家> stop` | 停止做姿势 |
| `/playerPose help` | 查看使用说明 |

## 下界传送门不传送玩家（commandPlayerNoNetherPortalTeleport）

| 指令 | 功能 |
| --- | --- |
| `/playerNoNetherPortalTeleport globalMode [true/false]` | 设置全局模式 |
| `/playerNoNetherPortalTeleport globalMode` | 查看全局模式状态 |
| `/playerNoNetherPortalTeleport add <player>` | 添加某名玩家到黑名单 |
| `/playerNoNetherPortalTeleport remove <player>` | 将某名玩家从黑名单中移除 |
| `/playerNoNetherPortalTeleport clear` | 清空黑名单 |
| `/playerNoNetherPortalTeleport list` | 查看黑名单 |
| `/playerNoNetherPortalTeleport help` | 查看使用说明 |

## 位置标记（commandLocationTag）

在普通橡木告示牌正面第一行填写 `amsloc`（告示牌种类与标记可通过指令修改），第二至第四行填写名称，每行分别去掉首尾空白后，按顺序直接拼接，不添加空格、换行或其他分隔符。空行不影响拼接，只读取正面。

| 指令 | 功能 |
| --- | --- |
| `/amsloc` 或 `/amsloc help` | 帮助、当前识别条件和文档链接 |
| `/amsloc list [page]` | 每页 10 条，按名称、维度和坐标排序 |
| `/amsloc list dimension <dimension> [page]` | 按维度筛选 |
| `/amsloc info <id>` | 查看名称、完整维度 ID 和坐标 |
| `/amsloc config` | 查看配置 |
| `/amsloc config marker <text>` | 修改第一行识别文本 |
| `/amsloc config sign <type>` | 修改告示牌类型 |
| `/amsloc config reset` | 恢复默认识别条件 |
| `/amsloc rescan` | 分批检查已加载区块 |
