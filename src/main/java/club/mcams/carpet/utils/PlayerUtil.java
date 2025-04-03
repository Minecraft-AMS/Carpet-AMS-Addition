package club.mcams.carpet.utils;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerUtil {
    public static String getName(PlayerEntity player) {
        return player.getGameProfile().getName();
    }
}
