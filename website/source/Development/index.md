- 主开发版本：`Minecraft 1.21.11`

  

- 使用 [preprocessor](https://github.com/ReplayMod/preprocessor) 在单一分支下对所有的Minecraft版本进行维护

  

- 如果你有好的点子可以提交 `feature request` 或在 `dev` 分支下提交pr

  

- 由于英文水平太差，目前所有的英文翻译均来自 [Chat-GPT](https://openai.com/blog/chatgpt)（除了很久很久以前 [nirvanaxiao6](https://github.com/nirvanaxiao6) 撰写的第一篇英文文档）



- 该网站使用 [Hexo]([Hexo](https://hexo.io/zh-cn/index.html)) 构建

&emsp;

## 规则、指令与记录器文档中的版本与来源标注

编辑中文[规则](/Rules/)、[指令](/Commands/)或[记录器](/Loggers/)文档时，使用以下 Markdown 写法：

```md
## 示例规则（exampleRule）

> 版本：`Minecraft >= 1.21`

这里是规则说明。

> 移植自：[项目名称](https://github.com/owner/repo)
```

编辑英文[规则](/en_us/Rules_en/)、[指令](/en_us/Commands_en/)或[记录器](/en_us/Loggers_en/)文档时，使用对应的英文标签：

```md
## exampleRule

> Version: `Minecraft >= 1.21`

Describe the rule here.

> Ported from: [Project name](https://github.com/owner/repo)
```

规则、指令和记录器条目统一使用二级标题（`##`）。版本行必须紧跟在条目标题后面（可以有空行，但不能插入其他内容）。网页会将它显示为标题旁的版本徽标；仅在条目有版本限制时填写。移植来源行可放在条目的说明中，网页会将其显示为来源链接。

请对照实现代码中的预处理条件与实际支持版本填写范围。网站只识别标注格式，不会自动验证版本范围是否正确。

&emsp;

## 版本支持

✔ 正在维护

✖ 停止维护

❓ 通常只修复BUG

|         游戏版本          | 开发状态 |                                                          最后支持版本                                                           |
|:---------------------:|:----:|:-------------------------------------------------------------------------------------------------------------------------:|
|         26.3          |  ✔   |                                                            ---                                                            |
|         26.2          |  ✔   |                                                            ---                                                            |
|        26.1.2         |  ✔   |                                                            ---                                                            |
|        26.1.1         |  ✖   |           [Carpet-AMS-Addition-v26.3](https://github.com/Minecraft-AMS/Carpet-AMS-Addition/releases/tag/v26.3)            |
|         26.1          |  ✖   |           [Carpet-AMS-Addition-v26.3](https://github.com/Minecraft-AMS/Carpet-AMS-Addition/releases/tag/v26.3)            |
| **<u>1.21.11(主)</u>** |  ✔   |                                                            ---                                                            |
|        1.21.8         |  ✔   |                                                            ---                                                            |
|   1.21.9 - 1.21.10    |  ❓   |                                                            ---                                                            |
|     1.21 - 1.21.7     |  ❓   |                                                            ---                                                            |
|        1.20.6         |  ✔   |                                                            ---                                                            |
|     1.20 - 1.20.5     |  ✖   |  [Carpet-AMS-Addition-mc1.20-1.20.5-v2.54.0](https://github.com/Minecraft-AMS/Carpet-AMS-Addition/releases/tag/v2.54.0)   |
|        1.19.4         |  ✔   |                                                            ---                                                            |
|        1.19.3         |  ✖   | [Carpet-AMS-Addition-mc1.19.3-v1.5.3](https://github.com/Minecraft-AMS/Carpet-AMS-Addition/releases/tag/v1.11.2%26v1.5.3) |
|        1.19.2         |  ✖   | [Carpet-AMS-Addition-mc1.19.2-v1.5.3](https://github.com/Minecraft-AMS/Carpet-AMS-Addition/releases/tag/v1.11.2%26v1.5.3) |
|        1.18.2         |  ✔   |                                                            ---                                                            |
|        1.17.1         |  ✔   |                                                            ---                                                            |
|        1.16.5         |  ✔   |                                                            ---                                                            |
|        1.15.2         |  ❓   |                [ Carpet-AMS-Addition-Legacy](https://github.com/1024-byteeeee/Carpet-AMS-Addition-Legacy)                 |
|        1.14.4         |  ❓   |                [ Carpet-AMS-Addition-Legacy](https://github.com/1024-byteeeee/Carpet-AMS-Addition-Legacy)                 |

&emsp;

## 前置
|     前置      |                   链接                   |
| :-----------: | :--------------------------------------: |
| Fabric-Carpet | https://github.com/gnembon/fabric-carpet |

&emsp;

## 许可
此项目在 [ LGPL-v3.0 ](https://choosealicense.com/licenses/lgpl-3.0/) 许可证下可用,您可以随意从中学习并将其纳入您自己的项目中。
