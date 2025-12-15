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

package carpetamsaddition.commands.rule.commandCarpetExtensionModWikiHyperlink;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.commands.suggestionProviders.SetSuggestionProvider;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.MessageTextEventUtils.ClickEventUtil;
import carpetamsaddition.utils.MessageTextEventUtils.HoverEventUtil;
import carpetamsaddition.utils.Messenger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import java.util.HashSet;
import java.util.Set;

public class CarpetExtensionModWikiHyperlinkCommandRegistry {
    private static final Translator translator = new Translator("command.carpetExtensionModWikiHyperlink");
    private static final Set<String> EXTENSION_NAMES = new HashSet<>();
    @SuppressWarnings("CodeBlock2Expr")
    private static final SuggestionProvider<CommandSourceStack> getSuggestions = (context, builder) -> {
        return new SetSuggestionProvider<>(EXTENSION_NAMES).getSuggestions(context, builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("carpetExtensionModWikiHyperlink")
                .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandCarpetExtensionModWikiHyperlink))
                .then(Commands.argument("extensionName", StringArgumentType.string())
                .suggests(getSuggestions)
                .executes(context -> execute(
                    context.getSource().getPlayerOrException(), StringArgumentType.getString(context, "extensionName")
                ))
            )
        );
    }

    private static int execute(Player player, String extensionName) {
        player.displayClientMessage(
            translator.tr("click_to_jump").withStyle(ChatFormatting.AQUA)
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

    private static Component createOpenUrlButton(String url) {
        return Messenger.s(getUrl(url)).setStyle(
            Style.EMPTY.withColor(ChatFormatting.GREEN)
            .withClickEvent(ClickEventUtil.event(ClickEventUtil.OPEN_URL, url))
            .withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, getCopyHoverText(url)))
        );
    }

    private static Component getCopyHoverText(String url) {
        return Messenger.s(translator.tr("click_to_jump").getString() + getUrl(url)).withStyle(ChatFormatting.YELLOW);
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
