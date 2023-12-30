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
        Carpet AMS Addition v2.22.0 更新内容

        # 新增规则

        - `禁用珊瑚失活（undyingCoral）` 让珊瑚扇和珊瑚块不会因缺水失活。

        - `禁止末影龙破坏方块（enderDragonNoDestroyBlock）` 使末影龙不能破坏任何方块。 [#58](https://github.com/Minecraft-AMS/Carpet-AMS-Addition/issues/58)

        # 更改

        - 重构了 `自定义方块爆炸抗性（customBlockBlastResistance）` 与 `自定义方块硬度（customBlockHardness）` 规则，现在使用单独的指令来进行方块的增删改查，详情见文档。

        - 现在 `禁用铁砧交互（anvilInteractionDisabled）` 规则的权限等级是可定义的。

        # 修复

        - 修复了一些规则描述中的错误。

        ---

        # New Rules

        - `undyingCoral` Prevent coral blocks and coral fans from dying.

        - `enderDragonNoDestroyBlock` Make the Ender Dragon unable to destroy any blocks. [#58](https://github.com/Minecraft-AMS/Carpet-AMS-Addition/issues/58)

        # Changes

        - The `customBlockBlastResistance ` and `customBlockHardness ` rules have been refactored. They now use separate commands for adding, removing, modifying, and querying blocks. See the documentation for details.

        - Now the permission level for the `anvilInteractionDisabled ` rule is customizable.

        # Fix

        - Fixed some errors in the descriptions of rules.

     */
}
