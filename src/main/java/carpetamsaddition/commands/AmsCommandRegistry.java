/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.commands;

import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.BrigadierCommandCompiler;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.api.command.CommandRegistrationContext;
import carpetamsaddition.commands.debug.network.AmspCommand;
import carpetamsaddition.commands.rule.amsUpdateSuppressionCrashFix.AmsUpdateSuppressionCrashFixCommand;
import carpetamsaddition.commands.rule.commandAtSomeOnePlayer.AtCommand;
import carpetamsaddition.commands.rule.commandAnvilInteractionDisabled.AnvilInteractionDisabledCommand;
import carpetamsaddition.commands.rule.commandCarpetExtensionModWikiHyperlink.CarpetExtensionModWikiHyperlinkCommand;
import carpetamsaddition.commands.rule.commandCustomAntiFireItems.CustomAntiFireItemsCommand;
import carpetamsaddition.commands.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceCommand;
import carpetamsaddition.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommand;
import carpetamsaddition.commands.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelCommand;
import carpetamsaddition.commands.rule.commandCustomMovableBlock.CustomMovableBlockCommand;
import carpetamsaddition.commands.rule.commandGetClientPlayerFps.GetClientPlayerFpsCommand;
import carpetamsaddition.commands.rule.commandGetHeldItemID.GetHeldItemIDCommand;
import carpetamsaddition.commands.rule.commandGetPlayerSkull.GetPlayerSkullCommand;
import carpetamsaddition.commands.rule.commandGetSaveSize.GetSaveSizeCommand;
import carpetamsaddition.commands.rule.commandGetSystemInfo.GetSystemInfoCommand;
import carpetamsaddition.commands.rule.commandGoto.GotoCommand;
import carpetamsaddition.commands.rule.commandHere.HereCommand;
import carpetamsaddition.commands.rule.commandPacketInternetGroper.PingsCommand;
import carpetamsaddition.commands.rule.commandPlayerChunkLoadController.PlayerChunkLoadControllerCommand;
import carpetamsaddition.commands.rule.commandPlayerLeader.LeaderCommand;
import carpetamsaddition.commands.rule.commandPlayerNoNetherPortalTeleport.PlayerNoNetherPortalTeleportCommand;
import carpetamsaddition.commands.rule.commandSetPlayerPose.SetPlayerPoseCommand;
import carpetamsaddition.commands.rule.commandWhere.WhereCommand;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AMS 指令的显式注册表
 *
 * <p>新增 {@link AmsCommand} 实现后，需要将其实例加入 {@link #COMMANDS}
 * 注册时会构建所有指令，并检查重复的根指令名称</p>
 */
public final class AmsCommandRegistry {
    private AmsCommandRegistry() {}

    private static final List<AmsCommand> COMMANDS = Arrays.asList(
        new AmsUpdateSuppressionCrashFixCommand(),
        new AtCommand(),
        new AnvilInteractionDisabledCommand(),
        new CarpetExtensionModWikiHyperlinkCommand(),
        new CustomAntiFireItemsCommand(),
        new CustomBlockBlastResistanceCommand(),
        new CustomBlockHardnessCommand(),
        new CustomCommandPermissionLevelCommand(),
        new CustomMovableBlockCommand(),
        new GetClientPlayerFpsCommand(),
        new GetHeldItemIDCommand(),
        new GetPlayerSkullCommand(),
        new GetSaveSizeCommand(),
        new GetSystemInfoCommand(),
        new GotoCommand(),
        new HereCommand(),
        new PingsCommand(),
        new PlayerChunkLoadControllerCommand(),
        new LeaderCommand(),
        new PlayerNoNetherPortalTeleportCommand(),
        new SetPlayerPoseCommand(),
        new WhereCommand(),
        new AmspCommand()
    );


    /**
     * 将注册表中的全部指令编译并注册到 Brigadier
     *
     * @param context 当前指令注册上下文
     * @throws IllegalStateException 指令未设置名称或出现重复根名称时抛出
     */
    public static void registerAll(CommandRegistrationContext context) {
        Set<String> names = new HashSet<>();

        for (AmsCommand command : COMMANDS) {
            CommandBuilder builder = new CommandBuilder();
            command.define(builder);
            String name = builder.requireName();

            if (!names.add(name)) {
                throw new IllegalStateException("Duplicate command root: /" + name);
            }

            BrigadierCommandCompiler.register(builder, context);
        }
    }
}
