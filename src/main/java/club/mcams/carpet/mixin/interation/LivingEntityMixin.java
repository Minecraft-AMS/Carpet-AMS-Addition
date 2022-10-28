package club.mcams.carpet.mixin.interation;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
//    /**
//     * @author WenDavid
//     * @reason
//     */
//    @Overwrite
//    public boolean isAffectedBySplashPotions() {
//        if (((Object) this) instanceof PlayerEntity) {
//            String playerName = ((PlayerEntity) (Object) this).getName().getString();
//            if (Interactions.onlinePlayerMap.containsKey(playerName) &&
//                    Interactions.onlinePlayerMap.get(playerName).contains("entities"))
//                return false;
//        }
//        return true;
//    }
}