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

package carpetamsaddition.commands.rule.commandCustomBlockBlastResistance;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Colors;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.RegexTools;
import carpetamsaddition.config.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceConfig;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.ChatFormatting;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CustomBlockBlastResistanceCommandRegistry {
    private static final Translator tr = new Translator("command.customBlockBlastResistance");
    public static final Map<BlockState, Float> CUSTOM_BLOCK_BLAST_RESISTANCE_MAP = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess) {
        dispatcher.register(
            Commands.literal("customBlockBlastResistance")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandCustomBlockBlastResistance))

            // set
            .then(literal("set")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .then(argument("resistance", FloatArgumentType.floatArg())
            .executes(context -> set(
                context.getSource(),
                BlockStateArgument.getBlock(context, "block").getState(),
                FloatArgumentType.getFloat(context, "resistance")
            )))))

            // remove
            .then(literal("remove")
            .then(argument("block", BlockStateArgument.block(commandRegistryAccess))
            .executes(context -> remove(
                context.getSource(),
                BlockStateArgument.getBlock(context, "block").getState()
            ))))

            // remove all
            .then(literal("removeAll").executes(context -> removeAll(context.getSource())))

            // list
            .then(literal("list").executes(context -> list(context.getSource())))

            // help
            .then(literal("help").executes(context -> help(context.getSource())))
        );
    }

    private static int set(CommandSourceStack source, BlockState state, float blastResistance) {
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float oldBlastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            Messenger.tell(
                source, tr.tr(
                    "modify_set",
                    getBlockRegisterName(state),
                    oldBlastResistance,
                    getBlockRegisterName(state),
                    blastResistance
                ).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
            );
        } else {
            Messenger.tell(
                source, tr.tr(
                    "set",
                    getBlockRegisterName(state),
                    blastResistance
                ).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
            );
        }

        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.put(state, blastResistance);
        saveToJson();
        return 1;
    }

    private static int remove(CommandSourceStack source, BlockState state) {
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float blastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.remove(state);
            saveToJson();
            Messenger.tell(
                source, tr.tr(
                    "remove",
                    getBlockRegisterName(state),
                    blastResistance
                ).withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
            );
            return 1;
        } else {
            Messenger.tell(
                source, tr.tr(
                    "not_found",
                    getBlockRegisterName(state)
                ).withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
            );
            return 0;
        }
    }

    private static int removeAll(CommandSourceStack source) {
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.clear();
        saveToJson();
        Messenger.tell(source, tr.tr("removeAll").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        return 1;
    }

    private static int list(CommandSourceStack source) {
        Messenger.tell(
            source,
            Messenger.c(
                tr.tr("list"),
                Messenger.endl(),
                Messenger.sline()
            ).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
        );

        for (Map.Entry<BlockState, Float> entry : CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.entrySet()) {
            BlockState state = entry.getKey();
            float blastResistance = entry.getValue();
            String blockName = getBlockRegisterName(state);
            Messenger.tell(source, Messenger.s(blockName + " / " + blastResistance).withColor(Colors.GREEN));
        }

        return 1;
    }

    @SuppressWarnings("DuplicatedCode")
    private static int help(CommandSourceStack source) {
        Messenger.tell(
            source,
            Messenger.c(
                tr.tr("help.set"), Messenger.endl(),
                tr.tr("help.remove"), Messenger.endl(),
                tr.tr("help.removeAll"), Messenger.endl(),
                tr.tr("help.list"), Messenger.endl()
            ).withColor(Colors.GRAY)
        );
        return 1;
    }

    private static String getBlockRegisterName(BlockState state) {
        return RegexTools.getBlockRegisterName(state.getBlock().toString());
    }

    private static void saveToJson() {
        CustomBlockBlastResistanceConfig.getInstance().saveBlockStates(CUSTOM_BLOCK_BLAST_RESISTANCE_MAP);
    }
}
