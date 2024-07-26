package club.mcams.carpet.validators.rule.fancyFakePlayerName;

import carpet.patches.EntityPlayerMPFake;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.helpers.rule.fancyFakePlayerName.FancyFakePlayerNameTeamController;
import club.mcams.carpet.helpers.rule.fancyFakePlayerName.FancyNameHelper;
import club.mcams.carpet.settings.RuleObserver;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Objects;

public class FancyFakePlayerNameRuleObserver extends RuleObserver<String> {
    public void onValueChange(String oldValue, String newValue) {
        MinecraftServer server = AmsServer.minecraftServer;
        if (server != null) {
            FancyFakePlayerNameTeamController.removeBotTeam(server, oldValue);
            if (!Objects.equals(newValue, "false")) {
                List<ServerPlayerEntity> playerEntities = server.getPlayerManager().getPlayerList();
                for (ServerPlayerEntity player : playerEntities) {
                    if (player instanceof EntityPlayerMPFake && !((EntityPlayerMPFake) player).isAShadow) {
                        FancyNameHelper.addBotTeamNamePrefix(player, newValue);
                    }
                }
            }
        }
    }
}
