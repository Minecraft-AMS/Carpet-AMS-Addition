package club.mcams.carpet.mixin.interation;

import club.mcams.carpet.function.Interactions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "tickMovement", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getHealth()F",
            ordinal = 1)
    )
    private float collidesWithEntities(PlayerEntity playerEntity) {
        String playerName = playerEntity.getName().getString();
        if (Interactions.onlinePlayerMap.containsKey(playerName) &&
                Interactions.onlinePlayerMap.get(playerName).contains("entities"))
            return 0.0F;
        return this.getHealth();
    }
}
