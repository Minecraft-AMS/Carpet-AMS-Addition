package club.mcams.carpet.commands.rule.commandGetClientPlayerFPS;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.NetworkUtil;
import club.mcams.carpet.network.payloads.rule.commandGetClientPlayerFPS.ClientPlayerFpsPayload_S2C;

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
    private static final Map<UUID, FpsQueryContext> fpsQueryMap = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("getClientPlayerFps")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetClientPlayerFps))
            .then(CommandManager.argument("player", EntityArgumentType.player())
            .executes(ctx -> {
                ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(ctx, "player");
                return getFps(targetPlayer, ctx.getSource());
            }))
        );
    }

    private static int getFps(ServerPlayerEntity targetPlayer, ServerCommandSource source) {
        NetworkUtil.sendS2CPacket(targetPlayer, ClientPlayerFpsPayload_S2C.create(targetPlayer.getUuid()));
        fpsQueryMap.put(targetPlayer.getUuid(), new FpsQueryContext(source, targetPlayer.getName().getString()));
        return 1;
    }

    public static void onFpsReceived(UUID playerUuid, int fps) {
        FpsQueryContext context = fpsQueryMap.remove(playerUuid);
        if (context != null) {
            System.out.println(context.getPlayerName());
            System.out.println(fps);
        }
    }
}
