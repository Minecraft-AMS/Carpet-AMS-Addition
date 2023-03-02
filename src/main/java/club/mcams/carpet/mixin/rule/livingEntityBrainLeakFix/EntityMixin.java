package club.mcams.carpet.mixin.rule.livingEntityBrainLeakFix;

import club.mcams.carpet.AmsServerSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "Lnet/minecraft/entity/Entity;remove(Lnet/minecraft/entity/Entity$RemovalReason;)V",
            at = @At("RETURN")
    )
    private void onRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        if (((Entity) (Object) this) instanceof LivingEntity && AmsServerSettings.livingEntityBrainLeakFix) {
            Brain<?> brain = ((LivingEntity) ((Object) this)).getBrain();
            ((IBrainMixin) brain).getMemories().keySet().forEach(brain::forget);
        }
    }
}
