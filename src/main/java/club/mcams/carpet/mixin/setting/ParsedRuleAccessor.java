package club.mcams.carpet.mixin.setting;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.lang.reflect.Field;

@Mixin(ParsedRule.class)
public interface ParsedRuleAccessor {
    //#if MC<=11900
    @SuppressWarnings("rawtypes")
    @Invoker(value = "<init>", remap = false)
    static ParsedRule invokeConstructor(Field field, Rule rule, carpet.settings.SettingsManager settingsManager) {
        throw new RuntimeException();
    }
    //#endif
}
