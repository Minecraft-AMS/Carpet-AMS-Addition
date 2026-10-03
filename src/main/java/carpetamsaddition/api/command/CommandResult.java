/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.api.command;

/**
 * 常用指令执行结果值
 */
public final class CommandResult {
    private CommandResult() {}

    /** 表示指令成功执行*/
    public static final int SUCCESS = 1;
    /** 表示指令未成功完成*/
    public static final int FAILURE = 0;
}
