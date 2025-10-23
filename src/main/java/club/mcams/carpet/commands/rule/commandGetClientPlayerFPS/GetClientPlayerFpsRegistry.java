package club.mcams.carpet.commands.rule.commandGetClientPlayerFPS;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.NetworkUtil;
import club.mcams.carpet.network.payloads.rule.commandGetClientPlayerFPS.ClientPlayerFpsPayload_S2C;
import club.mcams.carpet.utils.PlayerUtil;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GetClientPlayerFpsRegistry {
    private static final Translator tr = new Translator("command.getClientPlayerFps");
    private static final Map<UUID, ServerCommandSource> pendingQueries = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("getClientPlayerFps")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetClientPlayerFps))
            .then(CommandManager.argument("player", EntityArgumentType.player())
            .executes(ctx -> requestFps(EntityArgumentType.getPlayer(ctx, "player"), ctx.getSource())))
        );
    }

    private static int requestFps(ServerPlayerEntity targetPlayer, ServerCommandSource source) {
        pendingQueries.put(targetPlayer.getUuid(), source);
        NetworkUtil.sendS2CPacket(targetPlayer, ClientPlayerFpsPayload_S2C.create(targetPlayer.getUuid()));
        Messenger.tell(source, tr.tr("querying", PlayerUtil.getName(targetPlayer)));
        return 1;
    }

    public static void sendFpsResult(UUID playerUuid, int fps) {
        ServerCommandSource source = pendingQueries.remove(playerUuid);
        if (source != null) {
            ServerPlayerEntity player = PlayerUtil.getServerPlayerEntityFromUuid(playerUuid);
            if (player != null) {
                Messenger.tell(source, tr.tr("feedback", PlayerUtil.getName(player), fps));
            }
        }
    }
}
