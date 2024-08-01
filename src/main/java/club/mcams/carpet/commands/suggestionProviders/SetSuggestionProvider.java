package club.mcams.carpet.commands.suggestionProviders;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.server.command.ServerCommandSource;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SetSuggestionProvider<E> implements SuggestionProvider<ServerCommandSource> {
    private final Set<E> options;

    public SetSuggestionProvider(Set<E> options) {
        this.options = options;
    }

    public static SetSuggestionProvider<?> of(Set<?> options) {
        return new SetSuggestionProvider<>(options);
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        options.forEach(option -> builder.suggest(option.toString()));
        return builder.buildFuture();
    }
}
