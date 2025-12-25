/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package carpetamsaddition.commands.rule.commandCustomMovableBlock;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.config.rule.commandCustomMovableBlock.CustomMovableBlockConfig;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.RegexTools;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CustomMovableBlockCommandRegistry {
    private static final Translator tr = new Translator("command.customMovableBlock");
    public static final List<String> CUSTOM_MOVABLE_BLOCKS = new ArrayList<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess) {
        dispatcher.register(
            Commands.literal("customMovableBlock")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandCustomMovableBlock))

            // add
            .then(literal("add")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .executes(context -> add(
                context.getSource(),
                BlockStateArgument.getBlock(context, "block").getState()
            ))))

            // remove
            .then(literal("remove")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .executes(context -> remove(
                context.getSource(),
                BlockStateArgument.getBlock(context, "block").getState())
            )))

            // removeAll
            .then(literal("removeAll")
            .executes(context -> removeAll(context.getSource())))

            // list
            .then(literal("list")
            .executes(context -> list(context.getSource())))

            // help
            .then(literal("help")
            .executes(context -> help(context.getSource())))
        );
    }

    private static int add(CommandSourceStack source, BlockState blockState) {
        if (!CUSTOM_MOVABLE_BLOCKS.contains(getBlockName(blockState))) {
            CUSTOM_MOVABLE_BLOCKS.add(getBlockName(blockState));
            saveToJson();
            Messenger.tell(source, Messenger.f(tr.tr("add", getBlockName(blockState)), Layout.GREEN));
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("already_exists", getBlockName(blockState)), Layout.YELLOW));
        }

        return 1;
    }

    private static int remove(CommandSourceStack source, BlockState blockState) {
        if (CUSTOM_MOVABLE_BLOCKS.contains(getBlockName(blockState))) {
            CUSTOM_MOVABLE_BLOCKS.remove(getBlockName(blockState));
            saveToJson();
            Messenger.tell(source, Messenger.f(tr.tr("remove", getBlockName(blockState)), Layout.RED));
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("not_found", getBlockName(blockState)), Layout.YELLOW));
        }
        return 1;
    }

    private static int removeAll(CommandSourceStack source) {
        CUSTOM_MOVABLE_BLOCKS.clear();
        saveToJson();
        Messenger.tell(source, Messenger.f(tr.tr("removeAll"), Layout.GREEN));
        return 1;
    }

    private static int list(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(
            Messenger.c(
                tr.tr("list"),
                Messenger.endl(),
                Messenger.dline()
            ), Layout.GREEN
        ));

        for (String blockName : CUSTOM_MOVABLE_BLOCKS) {
            Messenger.tell(source, Messenger.f(Messenger.s(blockName), Layout.GREEN));
        }

        return 1;
    }

    @SuppressWarnings("DuplicatedCode")
    private static int help(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(
            Messenger.c(
                tr.tr("help.set"), Messenger.endl(),
                tr.tr("help.remove"), Messenger.endl(),
                tr.tr("help.removeAll"), Messenger.endl(),
                tr.tr("help.list"), Messenger.endl()
            ), Layout.GRAY
        ));

        return 1;
    }

    private static void saveToJson() {
        CustomMovableBlockConfig.getInstance().saveToJson(CUSTOM_MOVABLE_BLOCKS);
    }

    private static String getBlockName(BlockState blockState) {
        return RegexTools.getBlockRegisterName(blockState);
    }
}
