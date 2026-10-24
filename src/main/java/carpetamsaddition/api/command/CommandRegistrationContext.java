/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.api.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
//#if MC>=11904
import net.minecraft.commands.CommandBuildContext;
//#endif

/**
 * 指令注册阶段的跨版本上下文
 *
 * <p>统一保存 Brigadier dispatcher，并在支持的游戏版本中携带</p>
 */
public final class CommandRegistrationContext {
    private final CommandDispatcher<CommandSourceStack> dispatcher;
    //#if MC>=11904
    private final CommandBuildContext buildContext;
    //#endif

    /**
     * 创建注册上下文
     *
     * @param dispatcher 当前服务端指令分发器
     //#if MC>=11904
     * @param buildContext Minecraft 指令参数构建上下文
     //#endif
     */
    public CommandRegistrationContext(
        CommandDispatcher<CommandSourceStack> dispatcher
        //#if MC>=11904
        , CommandBuildContext buildContext
        //#endif
    ) {
        this.dispatcher = dispatcher;
        //#if MC>=11904
        this.buildContext = buildContext;
        //#endif
    }

    /** @return 当前服务端指令分发器 */
    public CommandDispatcher<CommandSourceStack> dispatcher() {
        return this.dispatcher;
    }

    //#if MC>=11904
    /** @return Minecraft 指令参数构建上下文 */
    public CommandBuildContext buildContext() {
        return this.buildContext;
    }
    //#endif
}
