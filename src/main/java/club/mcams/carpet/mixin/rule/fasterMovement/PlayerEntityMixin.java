package club.mcams.carpet.mixin.rule.fasterMovement;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    public void getMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (Objects.equals(AmsServerSettings.fasterMovement, "Ⅰ")) {
            cir.setReturnValue(0.2F);
        } else if (Objects.equals(AmsServerSettings.fasterMovement, "Ⅱ")) {
            cir.setReturnValue(0.3F);
        } else if (Objects.equals(AmsServerSettings.fasterMovement, "Ⅲ")) {
            cir.setReturnValue(0.4F);
        }
    }
}
