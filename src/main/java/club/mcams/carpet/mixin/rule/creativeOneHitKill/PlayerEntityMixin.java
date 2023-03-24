package club.mcams.carpet.mixin.rule.creativeOneHitKill;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.function.Consumer;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements EntityAccessorMixin {

    @Shadow
    @Final
    private PlayerAbilities abilities;

    @Shadow
    public abstract SoundCategory getSoundCategory();

    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;handleAttack(Lnet/minecraft/entity/Entity;)Z",
                    shift = At.Shift.BY,
                    by = -2
            ),
            cancellable = true
    )
    public void creativeOneHitKill(Entity target, CallbackInfo ci) {
        if (AmsServerSettings.creativeOneHitKill
                && !this.accessorGetWorld().isClient
                && this.abilities.creativeMode
                && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) {
            Consumer<Entity> instaKill = (target2) -> {
                if (target2 instanceof EnderDragonPart) {
                    Arrays.stream(((EnderDragonPart) target2).owner.getBodyParts()).forEach(Entity::kill);
                    ((EnderDragonPart) target2).owner.kill();
                } else {
                    target2.kill();
                }
            };
            instaKill.accept(target);
            this.accessorGetWorld().playSound(null, this.invokerGetX(), this.invokerGetY(), this.invokerGetZ(),
                    SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
            if (this.invokerIsSneaking()) {
                this.accessorGetWorld().getNonSpectatingEntities(Entity.class,
                        target.getBoundingBox().expand(2.0D, 0.50D, 2.0D)).stream()
                        .filter(entity -> entity.isAttackable() && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(entity))
                        .forEach(instaKill);
                this.accessorGetWorld().playSound(null, this.invokerGetX(), this.invokerGetY(), this.invokerGetZ(),
                        SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
            }
            ci.cancel();
        }
    }
}
