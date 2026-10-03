/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.api.command;

import carpetamsaddition.commands.AmsCommandRegistry;

/**
 * AMS 指令的统一声明接口
 *
 * <p>实现类只负责通过 {@link CommandBuilder} 描述指令名称、权限、路由和执行逻辑，
 * 无需直接创建或注册 Brigadier 节点。实现类需要在 {@link AmsCommandRegistry} 的显式列表中注册。</p>
 */
public interface AmsCommand {
    /**
     * 定义当前指令。
     *
     * @param command 用于声明指令结构的构建器
     */
    void define(CommandBuilder command);
}
