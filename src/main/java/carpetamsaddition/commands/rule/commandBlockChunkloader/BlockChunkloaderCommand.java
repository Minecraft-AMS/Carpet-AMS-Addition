/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.commands.rule.commandBlockChunkloader;

import carpetamsaddition.api.command.*;
import carpetamsaddition.helpers.rule.blockChunkLoader.LoaderType;
import carpetamsaddition.utils.messenger.Messenger;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.messageTextEvent.ClickEventUtil;
import carpetamsaddition.utils.messageTextEvent.HoverEventUtil;

import net.minecraft.network.chat.Style;

import java.util.Arrays;

import static carpetamsaddition.commands.rule.commandBlockChunkloader.CommandOperations.*;

public class BlockChunkloaderCommand implements AmsCommand {
    @Override
    public void define(CommandBuilder command) {
        command.name("blockChunkloader").requires(source -> Arrays.stream(LoaderType.values()).anyMatch(type -> canUseCommand(source, type)));
        command.route("help").executes(this::help);
        command.route("list").executes(this::list);
        command.executes(this::help);

        for (LoaderType type : LoaderType.values()) {
            SettingsSubcommands.register(command, type);
            GroupSubcommands.register(command, type);
            ConditionSubcommands.register(command, type);
        }
    }

    private int list(CommandExecutionContext context) {
        for (LoaderType type : LoaderType.values()) {
            if (canUseCommand(context.source(), type)) {
                show(context, type);
            }
        }

        return 1;
    }

    private int help(CommandExecutionContext context) {
        String url = "https://carpet.mcams.club/Commands/";
        Messenger.tell(context.source(), tr.tr("help"));
        Messenger.tell(context.source(), Messenger.f(Messenger.style(Messenger.s(url),
            Style.EMPTY.withClickEvent(ClickEventUtil.event(ClickEventUtil.OPEN_URL, url))
            .withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, Messenger.f(tr.tr("help_hover"), Layout.YELLOW)))
        ), Layout.BLUE, Layout.UNDERLINE));
        return 1;
    }
}
