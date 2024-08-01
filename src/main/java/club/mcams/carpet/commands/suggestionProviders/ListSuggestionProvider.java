package club.mcams.carpet.commands.suggestionProviders;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ListSuggestionProvider<E> implements SuggestionProvider<ServerCommandSource> {
    private final List<E> options;

    public ListSuggestionProvider(List<E> options) {
        this.options = options;
    }

    public static ListSuggestionProvider<?> of(List<?> options) {
        return new ListSuggestionProvider<>(options);
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        options.forEach(option -> builder.suggest(option.toString()));
        return builder.buildFuture();
    }
}
