package club.mcams.carpet.mixin.interation;

import club.mcams.carpet.function.Interactions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.function.Predicate;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
    @Redirect(method = "expensiveUpdate", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getClosestPlayer(Lnet/minecraft/entity/Entity;D)Lnet/minecraft/entity/player/PlayerEntity;"
    ))
    /* Might have compatibility issue with Lithium: might get more laggy*/
    private PlayerEntity getClosestPlayer(World self, Entity entity, double maxDistance) {
        World world = entity.world;
        Predicate<Entity> predicate = EntityPredicates.EXCEPT_SPECTATOR;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        PlayerEntity playerEntity = null;
        Iterator<? extends PlayerEntity> var13 = world.getPlayers().iterator();
        double lowestDistance = -1.0D;
        while (var13.hasNext()) {
            /* Determine whether the player is in interaction list*/
            PlayerEntity playerEntity1 = (PlayerEntity) var13.next();
            String playerName = playerEntity1.getName().getString();
            if (!predicate.test(playerEntity1) || (
                    Interactions.onlinePlayerMap.containsKey(playerName) &&
                            Interactions.onlinePlayerMap.get(playerName).contains("entities")
            )) continue;
            double distanceSq = playerEntity1.squaredDistanceTo(x, y, z);
            if (distanceSq >= maxDistance * maxDistance) continue;
            if (lowestDistance == -1.0D || distanceSq < lowestDistance) {
                playerEntity = playerEntity1;
                lowestDistance = distanceSq;
            }
        }
        return playerEntity;
    }
}