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

package club.mcams.carpet.commands.rule.commandCustomAntiFireItems;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.RegexTools;
import club.mcams.carpet.config.rule.commandAntiFireItems.CustomAntiFireItemsConfig;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.argument.ItemStackArgumentType;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CustomAntiFireItemsCommandRegistry {
    private static final Translator tr = new Translator("command.customAntiFireItems");
    public static final List<String> CUSTOM_ANTI_FIRE_ITEMS = new ArrayList<>();

    //#if MC<11900
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    //#else
    //$$ public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
    //#endif
        dispatcher.register(
            CommandManager.literal("customAntiFireItems")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCustomAntiFireItems))
            .then(literal("add")
            //#if MC<11900
            .then(argument("item", ItemStackArgumentType.itemStack())
            //#else
            //$$ .then(argument("item", ItemStackArgumentType.itemStack(commandRegistryAccess))
            //#endif
            .executes(context -> add(
                context.getSource(),
                ItemStackArgumentType.getItemStackArgument(context, "item").createStack(1, false)
            ))))
            .then(literal("remove")
            //#if MC<11900
            .then(argument("item", ItemStackArgumentType.itemStack())
            //#else
            //$$ .then(argument("item", ItemStackArgumentType.itemStack(commandRegistryAccess))
            //#endif
            .executes(context -> remove(
                context.getSource(),
                ItemStackArgumentType.getItemStackArgument(context, "item").createStack(1, false)
            ))))
            .then(literal("removeAll")
            .executes(context -> removeAll(context.getSource())))
            .then(literal("list")
            .executes(context -> list(context.getSource())))
            .then(literal("help")
            .executes(context -> help(context.getSource())))
        );
    }

    private static int add(ServerCommandSource source, ItemStack itemStack) {
        if (!CUSTOM_ANTI_FIRE_ITEMS.contains(getItemName(itemStack))) {
            CUSTOM_ANTI_FIRE_ITEMS.add(getItemName(itemStack));
            saveToJson();
            Messenger.tell(source, Messenger.f(tr.tr("add", getItemName(itemStack)), Layout.GREEN));
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("already_exists", getItemName(itemStack)), Layout.YELLOW));
        }
        return 1;
    }

    private static int remove(ServerCommandSource source, ItemStack itemStack) {
        if (CUSTOM_ANTI_FIRE_ITEMS.contains(getItemName(itemStack))) {
            CUSTOM_ANTI_FIRE_ITEMS.remove(getItemName(itemStack));
            saveToJson();
            Messenger.tell(source, Messenger.f(tr.tr("remove", getItemName(itemStack)), Layout.RED));
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("not_found", getItemName(itemStack)), Layout.RED));
        }
        return 1;
    }

    private static int removeAll(ServerCommandSource source) {
        CUSTOM_ANTI_FIRE_ITEMS.clear();
        saveToJson();
        Messenger.tell(source, Messenger.f(tr.tr("removeAll"), Layout.RED));
        return 1;
    }

    private static int list(ServerCommandSource source) {
        Messenger.tell(source, Messenger.f(Messenger.c(
                tr.tr("list_title"), Messenger.endl(), Messenger.sline()), Layout.GREEN)
        );

        for (String blockName : CUSTOM_ANTI_FIRE_ITEMS) {
            Messenger.tell(source, Messenger.f(Messenger.s(blockName), Layout.GREEN));
        }

        return 1;
    }

    private static int help(ServerCommandSource source) {
        Messenger.tell(
            source, Messenger.f(Messenger.c(
                tr.tr("help.set"), Messenger.endl(),
                tr.tr("help.remove"), Messenger.endl(),
                tr.tr("help.removeAll"), Messenger.endl(),
                tr.tr("help.removeAll"), Messenger.endl(),
                tr.tr("help.list"), Messenger.endl()
            ), Layout.GRAY)
        );

        return 1;
    }

    private static void saveToJson() {
        CustomAntiFireItemsConfig.getInstance().saveToJson(CUSTOM_ANTI_FIRE_ITEMS);
    }

    @NotNull
    private static String getItemName(ItemStack stack) {
        return RegexTools.getItemRegisterName(stack);
    }
}
