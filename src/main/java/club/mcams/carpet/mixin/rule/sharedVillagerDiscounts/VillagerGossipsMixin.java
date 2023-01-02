package club.mcams.carpet.mixin.rule.sharedVillagerDiscounts;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import static net.minecraft.village.VillageGossipType.MAJOR_POSITIVE;

@Mixin(VillagerGossips.class)
public abstract class VillagerGossipsMixin {
    @Shadow
    @Final
    private Map<UUID, Object> entityReputation;
    @Inject(method = "getReputationFor(Ljava/util/UUID;Ljava/util/function/Predicate;)I", at = @At("HEAD"), cancellable = true)
    public void getReputation(UUID target, Predicate<VillageGossipType> filter, CallbackInfoReturnable<Integer> cir) {
        if (AmsServerSettings.sharedVillagerDiscounts && filter.test(MAJOR_POSITIVE)) {
            GetValueForInvoker targetReputation = (GetValueForInvoker)entityReputation.get(target);
            int otherRepertory = 0;
            if (targetReputation != null) { otherRepertory = targetReputation._getValueFor(vgt -> filter.test(vgt) && !vgt.equals(MAJOR_POSITIVE)); }
            int majorPositiveRepertory = entityReputation.values().stream().mapToInt(r -> ((GetValueForInvoker) r)._getValueFor(vgt -> vgt.equals(MAJOR_POSITIVE))).sum();
            cir.setReturnValue(otherRepertory + Math.min(majorPositiveRepertory, MAJOR_POSITIVE.maxValue * MAJOR_POSITIVE.multiplier));
        }
    }
}
