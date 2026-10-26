/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

package carpetamsaddition.commands.rule.commandLocationTag;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.config.rule.commandLocationTag.LocationTagConfig;
import carpetamsaddition.api.command.*;
import carpetamsaddition.api.command.suggestionProviders.ListSuggestionProvider;
import carpetamsaddition.helpers.rule.commandLocationTag.LocationTagIndex.Entry;
import carpetamsaddition.helpers.rule.commandLocationTag.LocationTagManager;
import carpetamsaddition.helpers.rule.commandLocationTag.LocationTagSignCompat;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.messageTextEvent.ClickEventUtil;
import carpetamsaddition.utils.messenger.Messenger;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.List;

public final class LocationTagCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.amsloc");
    private static final int PAGE_SIZE = 10;

    @Override
    public void define(CommandBuilder command) {
        command.name("amsloc").carpetRule(() -> CarpetAMSAdditionSettings.commandLocationTag);
        command.executes(this::help);
        command.route("help").executes(this::help);
        command.route("list").executes(c -> list(c, null, 1));
        command.route("list", Arguments.integer("page", 1)).executes(c -> list(c, null, c.get(Arguments.integer("page"))));
        CommandPath dimension = CommandPath.path().literal("list").literal("dimension").argument(Arguments.dimension("dimension"));
        command.route(dimension).executes(c -> list(c, LocationTagManager.dimension(c.get(Arguments.dimension("dimension"))), 1));
        command.route(CommandPath.path().literal("list").literal("dimension").argument(Arguments.dimension("dimension"))
            .argument(Arguments.integer("page", 1)))
            .executes(c -> list(c, LocationTagManager.dimension(c.get(Arguments.dimension("dimension"))), c.get(Arguments.integer("page"))));
        command.route("info", Arguments.integer("id", 1)).executes(this::info);
        command.route("config").executes(this::config);
        command.route(CommandPath.path().literal("config").literal("marker").argument(Arguments.greedyString("text")))
            .executes(c -> configure(c, c.get(Arguments.greedyString("text")), LocationTagManager.sign()));
        command.route(CommandPath.path().literal("config").literal("sign")
            .argument(
                Arguments.suggests(Arguments.greedyString("type"),
                (context, builder) -> ListSuggestionProvider.of(LocationTagSignCompat.types()).getSuggestions(context, builder))
            ))
            .executes(c -> configure(c, LocationTagManager.marker(), c.get(Arguments.greedyString("type"))));
        command.route(CommandPath.path().literal("config").literal("reset"))
            .executes(c -> configure(c, "amsloc", "minecraft:oak_sign"));
        command.route("rescan").executes(this::rescan);
    }

    private int help(CommandExecutionContext context) {
        Messenger.tell(context.source(), Messenger.f(tr.tr("title"), Layout.GOLD, Layout.BOLD));

        for (String key : new String[]{"help_list", "help_dimension", "help_info", "help_config", "help_marker", "help_type", "help_reset", "help_rescan", "help_sign"}) {
            Messenger.tell(context.source(), tr.tr(key));
        }

        Messenger.tell(context.source(), tr.tr("recognition", LocationTagManager.marker(), LocationTagManager.sign()));
        String url = "https://carpet.mcams.club/Commands/";
        Messenger.tell(context.source(), Messenger.f(Messenger.style(Messenger.s(url), Style.EMPTY.withClickEvent(ClickEventUtil.event(ClickEventUtil.OPEN_URL, url))), Layout.BLUE, Layout.UNDERLINE));

        return 1;
    }

    private int list(CommandExecutionContext context, String dimension, int page) {
        List<Entry> entries = LocationTagManager.entries(dimension);
        int pages = Math.max(1, (entries.size() + PAGE_SIZE - 1) / PAGE_SIZE);

        if (page > pages) {
            Messenger.tell(context.source(), Messenger.f(tr.tr("page_invalid", pages), Layout.RED));
            return 0;
        }

        Messenger.tell(context.source(), Messenger.f(tr.tr("list", page, pages, entries.size()), Layout.GOLD));

        if (entries.isEmpty()) {
            Messenger.tell(context.source(), Messenger.f(tr.tr("empty"), Layout.GRAY));
        }

        int start = (page - 1) * PAGE_SIZE;

        for (int i = start; i < Math.min(start + PAGE_SIZE, entries.size()); i++) {
            Entry entry = entries.get(i);
            MutableComponent row = Messenger.f(Messenger.s("#" + entry.id + " "), Layout.GRAY);
            row.append(dimensionText(entry.dimension));
            row.append(Messenger.s(" "));
            row.append(Messenger.f(Messenger.s(entry.name + " "), Layout.YELLOW));
            row.append(coordinates(entry));
            row.append(Messenger.s(" "));
            row.append(button("details", "/amsloc info " + entry.id, "details_hover", entry.name));
            Messenger.tell(context.source(), row);
        }

        String base = "/amsloc list" + (dimension == null ? "" : " dimension " + dimension) + " ";
        MutableComponent navigation = Messenger.s("");

        if (page > 1) {
            navigation.append(button("previous", base + (page - 1), "previous_hover", page - 1));
        }

        if (page > 1 && page < pages) {
            navigation.append(Messenger.s(" "));
        }

        if (page < pages) {
            navigation.append(button("next", base + (page + 1), "next_hover", page + 1));
        }

        if (pages > 1) {
            Messenger.tell(context.source(), navigation);
        }

        return entries.size();
    }

    private int info(CommandExecutionContext context) throws CommandSyntaxException {
        Entry entry = LocationTagManager.get(context.get(Arguments.integer("id")));

        if (entry == null) {
            Messenger.tell(context.source(), Messenger.f(tr.tr("missing"), Layout.RED));
            return 0;
        }

        MutableComponent title = Messenger.f(Messenger.s("#" + entry.id + " "), Layout.GOLD);
        title.append(dimensionText(entry.dimension));
        title.append(Messenger.f(Messenger.s(" " + entry.name), Layout.YELLOW));
        Messenger.tell(context.source(), title);
        MutableComponent details = Messenger.s(entry.dimension + " ");
        details.append(coordinates(entry));
        Messenger.tell(context.source(), details);

        return 1;
    }

    private static MutableComponent coordinates(Entry entry) {
        String coordinates = Messenger.formatPos(entry.x, entry.y, entry.z, " ");
        return Messenger.copyButton("[ " + coordinates + " ]", coordinates, Layout.AQUA);
    }

    private static MutableComponent dimensionText(String dimension) {
        switch (dimension) {
            case "minecraft:overworld":
                return Messenger.f(tr.tr("overworld"), Layout.DARK_GREEN);
            case "minecraft:the_nether":
                return Messenger.f(tr.tr("nether"), Layout.DARK_RED);
            case "minecraft:the_end":
                return Messenger.f(tr.tr("end"), Layout.LIGHT_PURPLE);
            default:
                return Messenger.f(Messenger.s("[ " + dimension + " ]"), Layout.AQUA);
        }
    }

    private static MutableComponent button(String labelKey, String command, String hoverKey, Object... hoverArgs) {
        Style style = Messenger.commandButton(command, tr.tr(hoverKey, hoverArgs), Layout.YELLOW);

        return Messenger.f(Messenger.style(tr.tr(labelKey), style), Layout.GREEN);
    }

    private int config(CommandExecutionContext context) {
        Messenger.tell(context.source(), tr.tr("recognition", LocationTagManager.marker(), LocationTagManager.sign()));
        return 1;
    }

    private int configure(CommandExecutionContext context, String marker, String sign) {
        sign = sign.trim();

        if (!sign.contains(":")) {
            sign = "minecraft:" + sign;
        }

        if (!LocationTagSignCompat.types().contains(sign)) {
            Messenger.tell(context.source(), Messenger.f(tr.tr("invalid_sign"), Layout.RED));
            return 0;
        }

        if (!LocationTagConfig.validMarker(marker)) {
            Messenger.tell(context.source(), Messenger.f(tr.tr("invalid_marker"), Layout.RED));
            return 0;
        }

        try {
            LocationTagManager.configure(marker, sign);
            Messenger.tell(context.source(), tr.tr("saved"));
            return config(context);
        } catch (java.io.IOException e) {
            CarpetAMSAdditionServer.LOGGER.error("Failed to save location tag configuration", e);
            Messenger.tell(context.source(), Messenger.f(tr.tr("save_failed"), Layout.RED));
            return 0;
        }
    }

    private int rescan(CommandExecutionContext context) {
        Messenger.tell(context.source(), tr.tr("rescan", LocationTagManager.rescan()));
        return 1;
    }
}
