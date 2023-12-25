/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.utils.compat;

public class DummyClass {
    /*
        Carpet AMS Addition v2.21.0 更新内容

        # 新增规则

        - `禁用物品使用冷却（useItemCooldownDisabled）` 开启后，使用物品没有冷却时间

        - `仙人掌扳手音效（flippinCactusSoundEffect）` 在开启carpet mod的仙人掌扳手规则时，使用仙人掌扳手会发出声音（提供了三种不同音效，设置为0禁用音效）

        - `可合成光源方块（craftableLight）` 添加了光源方块的合成配方，启用时光源方块将不会有挖掘硬度

        - `可合成屏障方块（craftableBarrier）` 添加了屏障方块的合成配方，启用时屏障方块将不会有挖掘硬度

        - `自定义方块爆炸抗性（customBlockBlastResistance）` 可以同时自定义是什么方块这个方块的爆炸抗性是多少，详情见规则文档

        - `自定义方块硬度（customBlockHardness）` 可以同时自定义是什么方块这个方块的挖掘硬度是多少，详情见规则文档

        # 更改

        - 重命名：`自定义炸毁方块（customBlowUpBlock）` -> `自定义方块爆炸抗性（customBlockBlastResistance）`

        - 移除了 `易碎黑曜石（softObsidian）` 与 `易碎深板岩（softDeepslate）` 规则，取而代之的是 `自定义方块硬度（customBlockHardness）` 规则

        ---

        # New Rules

        - `useItemCooldownDisabled` When enabled, there is no cooldown time for using items

        - `flippinCactusSoundEffect` When enabling the `flippinCactus` rule in the Carpet mod, using the cactus wrench will emit a sound (providing three different sound effects, set to 0 to disable sound)

        - `craftableLight` Add light block recipes in minecraft, when enabled, light blocks will have no mining hardness

        - `craftableBarrier` Add barrier recipes in minecraft, when enabled, barrier blocks will have no mining hardness

        - `customBlockBlastResistance` You have the option to customize the block and its explosion resistance simultaneously. For more detailed information, please refer to the rule document

        - `customBlockHardness` You have the option to customize the block and its mining hardness simultaneously. For more detailed information, please refer to the rule document

        # Changes

        - Rename: `customBlowUpBlock` -> `customBlockBlastResistance`

        - The `softObsidian` and `softDeepslate` rules have been removed, and they have been replaced with the `customBlockHardness` rule
     */
}
