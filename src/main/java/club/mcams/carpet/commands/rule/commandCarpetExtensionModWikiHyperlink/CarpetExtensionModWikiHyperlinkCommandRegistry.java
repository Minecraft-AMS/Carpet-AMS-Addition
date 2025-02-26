/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

package club.mcams.carpet.commands.rule.commandCarpetExtensionModWikiHyperlink;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.commands.suggestionProviders.SetSuggestionProvider;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.MessageTextEventUtils.ClickEventUtil;
import club.mcams.carpet.utils.MessageTextEventUtils.HoverEventUtil;
import club.mcams.carpet.utils.Messenger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashSet;
import java.util.Set;

public class CarpetExtensionModWikiHyperlinkCommandRegistry {
    private static final Translator translator = new Translator("command.carpetExtensionModWikiHyperlink");
    private static final Set<String> EXTENSION_NAMES = new HashSet<>();
    @SuppressWarnings("CodeBlock2Expr")
    private static final SuggestionProvider<ServerCommandSource> getSuggestions = (context, builder) -> {
        return new SetSuggestionProvider<>(EXTENSION_NAMES).getSuggestions(context, builder);
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("carpetExtensionModWikiHyperlink")
                .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandCarpetExtensionModWikiHyperlink))
                .then(CommandManager.argument("extensionName", StringArgumentType.string())
                .suggests(getSuggestions)
                .executes(context -> execute(
                    context.getSource().getPlayer(), StringArgumentType.getString(context, "extensionName")
                ))
            )
        );
    }

    private static int execute(PlayerEntity player, String extensionName) {
        player.sendMessage(
            translator.tr("click_to_jump").formatted(Formatting.AQUA)
            .append(createOpenUrlButton(getUrl(extensionName))), false
        );
        return 1;
    }

    @SuppressWarnings("EnhancedSwitchMigration")
    private static String getUrl(String extensionName) {
        switch (extensionName) {
            case "Carpet-AMS-Addition": return "https://carpet.mcams.club/";
            case "Carpet-ORG-Addition": return "https://github.com/fcsailboat/Carpet-Org-Addition/";
            case "Carpet-TIS-Addition": return "https://carpet.tis.world/";
            case "Carpet-Extra": return "https://github.com/gnembon/carpet-extra/";
            case "Carpet-Fixes": return "https://github.com/fxmorin/carpet-fixes/wiki/";
            case "Carpet-TCTC-Addition": return "https://github.com/The-Cat-Town-Craft/Carpet-TCTC-Addition/";
            case "Carpet-Sky-Addition": return "https://github.com/jsorrell/CarpetSkyAdditions/blob/HEAD/README.md/";
            case "Carpet-PVP": return "https://github.com/TheobaldTheBird/CarpetPVP/";
            case "Carpet-Addons-Not-Found": return "https://github.com/Gilly7CE/Carpet-Addons-Not-Found/wiki/";
            case "Carpet-MCT-Addition": return "https://github.com/MCTown/Carpet-MCT-Addition/";
            case "Carpet-Extra-Extras": return "https://github.com/Thedustbustr/Carpet-Extra-Extras/";
            case "Gugle-Carpet-Addition": return "https://github.com/Gu-ZT/gugle-carpet-addition/";
        }
        return extensionName;
    }

    private static Text createOpenUrlButton(String url) {
        return Messenger.s(getUrl(url)).setStyle(
            Style.EMPTY.withColor(Formatting.GREEN)
            .withClickEvent(ClickEventUtil.event(ClickEventUtil.OPEN_URL, url))
            .withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, getCopyHoverText(url)))
        );
    }

    private static Text getCopyHoverText(String url) {
        return Messenger.s(translator.tr("click_to_jump").getString() + getUrl(url)).formatted(Formatting.YELLOW);
    }

    static {
        EXTENSION_NAMES.add("Carpet-AMS-Addition");
        EXTENSION_NAMES.add("Carpet-ORG-Addition");
        EXTENSION_NAMES.add("Carpet-TIS-Addition");
        EXTENSION_NAMES.add("Carpet-Extra");
        EXTENSION_NAMES.add("Carpet-Fixes");
        EXTENSION_NAMES.add("Carpet-TCTC-Addition");
        EXTENSION_NAMES.add("Carpet-Sky-Addition");
        EXTENSION_NAMES.add("Carpet-PVP");
        EXTENSION_NAMES.add("Carpet-Addons-Not-Found");
        EXTENSION_NAMES.add("Carpet-MCT-Addition");
        EXTENSION_NAMES.add("Carpet-Extra-Extras");
        EXTENSION_NAMES.add("Gugle-Carpet-Addition");
    }
}
