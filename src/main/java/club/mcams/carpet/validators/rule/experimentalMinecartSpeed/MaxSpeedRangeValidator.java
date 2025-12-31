package club.mcams.carpet.validators.rule.experimentalMinecartSpeed;

import carpet.settings.ParsedRule;
import carpet.settings.Validator;

import club.mcams.carpet.translations.Translator;

import net.minecraft.server.command.ServerCommandSource;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.21.2")
@SuppressWarnings("unused")
public class MaxSpeedRangeValidator extends Validator<Integer> {
    private static final Translator translator = new Translator("validator.experimentalMinecartSpeed");

    @Override
    public Integer validate(ServerCommandSource serverCommandSource, ParsedRule<Integer> parsedRule, Integer integer, String s) {
        return integer >= 1 && integer <= 300 ? integer : null;
    }

    @Override
    public String description() {
        return translator.tr("value_range").getString();
    }
}
