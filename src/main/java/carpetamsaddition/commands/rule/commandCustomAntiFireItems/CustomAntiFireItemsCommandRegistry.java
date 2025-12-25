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

package carpetamsaddition.commands.rule.commandCustomAntiFireItems;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.config.rule.commandAntiFireItems.CustomAntiFireItemsConfig;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.RegexTools;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class CustomAntiFireItemsCommandRegistry {
    private static final Translator tr = new Translator("command.customAntiFireItems");
    public static final List<String> CUSTOM_ANTI_FIRE_ITEMS = new ArrayList<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess) {
        dispatcher.register(
            Commands.literal("customAntiFireItems")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandCustomAntiFireItems))

            // add
            .then(literal("add")
            .then(argument("item", ItemArgument.item(commandRegistryAccess))
            .executes(context -> add(
                context.getSource(),
                ItemArgument.getItem(context, "item").createItemStack(1, false)
            ))))

            // remove
            .then(literal("remove")
            .then(argument("item", ItemArgument.item(commandRegistryAccess))
            .executes(context -> remove(
                context.getSource(),
                ItemArgument.getItem(context, "item").createItemStack(1, false)
            ))))

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

    private static int add(CommandSourceStack source, ItemStack itemStack) {
        if (!CUSTOM_ANTI_FIRE_ITEMS.contains(getItemName(itemStack))) {
            CUSTOM_ANTI_FIRE_ITEMS.add(getItemName(itemStack));
            saveToJson();
            Messenger.tell(source, Messenger.f(tr.tr("add", getItemName(itemStack)), Layout.GREEN));
        } else {
              Messenger.tell(source, Messenger.f(tr.tr("already_exists", getItemName(itemStack)), Layout.YELLOW));
        }
        return 1;
    }

    private static int remove(CommandSourceStack source, ItemStack itemStack) {
        if (CUSTOM_ANTI_FIRE_ITEMS.contains(getItemName(itemStack))) {
            CUSTOM_ANTI_FIRE_ITEMS.remove(getItemName(itemStack));
            saveToJson();
            Messenger.tell(source, Messenger.f(tr.tr("remove", getItemName(itemStack)), Layout.RED));
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("not_found", getItemName(itemStack)), Layout.RED));
        }
        return 1;
    }

    private static int removeAll(CommandSourceStack source) {
        CUSTOM_ANTI_FIRE_ITEMS.clear();
        saveToJson();
        Messenger.tell(source, Messenger.f(tr.tr("removeAll"), Layout.RED));
        return 1;
    }

    private static int list(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(Messenger.c(
            tr.tr("list_title"), Messenger.endl(), Messenger.sline()), Layout.GREEN)
        );

        for (String blockName : CUSTOM_ANTI_FIRE_ITEMS) {
            Messenger.tell(source, Messenger.f(Messenger.s(blockName), Layout.GREEN));
        }

        return 1;
    }

    private static int help(CommandSourceStack source) {
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
