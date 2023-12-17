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
        Carpet AMS Addition v2.18.0 更新内容
        1、新增fancyFakePlayerName与fakePlayerNoScoreboardCounter规则
        2、opPlayerNoCheat规则重命名为preventAdministratorCheat且包含Carpet TIS Addition所禁用的指令，不再作为Carpet TIS Addition的拓展
        3、amsUpdateSuppressionCrashFix规则不再默认在customBlockUpdateSuppression开启时强制开启，但依然可以通过/amsUpdateSuppressionCrashFixForceMode true/false命令来控制是否强制开启
        4、commandChunkLoading规则重命名为playerChunkLoadController规则，且指令更改为/playerChunkLoading true/false
        5、MixinExtras v0.3.1 -> v0.3.2
        6、一堆代码上的调整与优化（偷个懒不写了 :<）
     */
}
