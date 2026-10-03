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
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.Arguments;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.config.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceConfig;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.RegexTools;
import carpetamsaddition.utils.messenger.Messenger;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomBlockBlastResistanceCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.customBlockBlastResistance");
    public static final Map<BlockState, Float> CUSTOM_BLOCK_BLAST_RESISTANCE_MAP = new ConcurrentHashMap<>();

    @Override
    public void define(CommandBuilder command) {
        command.name("customBlockBlastResistance")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandCustomBlockBlastResistance)
            .route("set", Arguments.block("block"), Arguments.decimal("resistance"))
            .executes(c -> set(c.source(), c.get(Arguments.block("block")), c.get(Arguments.decimal("resistance"))))

            .route("remove", Arguments.block("block"))
            .executes(c -> remove(c.source(), c.get(Arguments.block("block"))))

            .route("removeAll")
            .executes(c -> removeAll(c.source()))

            .route("list")
            .executes(c -> list(c.source()))

            .route("help")
            .executes(c -> help(c.source()));
    }

    private static int set(CommandSourceStack source, BlockState state, float blastResistance) {
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float oldBlastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.get(state);
            Messenger.tell(source, Messenger.f(
                tr.tr("modify_set", getBlockRegisterName(state), oldBlastResistance, getBlockRegisterName(state), blastResistance),
                Layout.GREEN,
                Layout.BOLD
            ));
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("set", getBlockRegisterName(state), blastResistance), Layout.GREEN, Layout.BOLD));
        }

        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.put(state, blastResistance);
        saveToJson();
        return 1;
    }

    private static int remove(CommandSourceStack source, BlockState state) {
        if (CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.containsKey(state)) {
            float blastResistance = CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.remove(state);
            saveToJson();
            Messenger.tell(source, Messenger.f(tr.tr("remove", getBlockRegisterName(state), blastResistance), Layout.RED, Layout.BOLD));
            return 1;
        }

        Messenger.tell(source, Messenger.f(tr.tr("not_found", getBlockRegisterName(state)), Layout.RED, Layout.BOLD));
        return 0;
    }

    private static int removeAll(CommandSourceStack source) {
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.clear();
        saveToJson();
        Messenger.tell(source, Messenger.f(tr.tr("removeAll"), Layout.RED, Layout.BOLD));
        return 1;
    }

    private static int list(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(Messenger.c(tr.tr("list"), Messenger.endl(), Messenger.sline()), Layout.GREEN, Layout.BOLD));

        for (Map.Entry<BlockState, Float> entry : CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.entrySet()) {
            String blockName = getBlockRegisterName(entry.getKey());
            Messenger.tell(source, Messenger.f(Messenger.s(blockName + " / " + entry.getValue()), Layout.GREEN));
        }

        return 1;
    }

    @SuppressWarnings("DuplicatedCode")
    private static int help(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(Messenger.c(
            tr.tr("help.set"), Messenger.endl(),
            tr.tr("help.remove"), Messenger.endl(),
            tr.tr("help.removeAll"), Messenger.endl(),
            tr.tr("help.list"), Messenger.endl()
        ), Layout.GRAY));
        return 1;
    }

    private static String getBlockRegisterName(BlockState state) {
        return RegexTools.getBlockRegisterName(state);
    }

    private static void saveToJson() {
        CustomBlockBlastResistanceConfig.getInstance().saveBlockStates(CUSTOM_BLOCK_BLAST_RESISTANCE_MAP);
    }
}
