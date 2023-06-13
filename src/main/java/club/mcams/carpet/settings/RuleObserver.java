package club.mcams.carpet.settings;


import carpet.settings.ParsedRule;
import carpet.settings.Validator;

import net.minecraft.server.command.ServerCommandSource;

public abstract class RuleObserver<T> extends Validator<T> {
    @Override
    public T validate(ServerCommandSource source, ParsedRule<T> currentRule, T newValue, String string) {
        if (currentRule.get() != newValue) {
            this.onValueChanged(currentRule.get(), newValue);
        }
        return newValue;
    }

    abstract public void onValueChanged(T oldValue, T newValue);
}
