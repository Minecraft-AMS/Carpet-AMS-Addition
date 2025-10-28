package club.mcams.carpet.mixin.rule.netherWaterPlacement;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.player.PlayerEntity;
//#if MC >= 12105
//$$ import net.minecraft.entity.LivingEntity;
//#endif
import net.minecraft.item.BucketItem;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin {
    @ModifyExpressionValue(
        method = "placeFluid",
        at = @At(
            value = "INVOKE",
            //#if MC >= 12111
            //$$ target = "Lnet/minecraft/world/attribute/WorldEnvironmentAttributeAccess;getAttributeValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/util/math/BlockPos;)Ljava/lang/Object;"
            //#elseif MC>=11600
            target = "Lnet/minecraft/world/dimension/DimensionType;isUltrawarm()Z"
            //#endif
        )
    )
    //#if MC>=12111
    //$$ private Object netherWaterPlacement(
    //$$ Object original,
    //#else
    private boolean netherWaterPlacement(
        boolean original,
        //#endif
        //#if MC>=12105
        //$$ @Local(argsOnly = true) @Nullable LivingEntity entity
        //#else
        @Local(argsOnly = true) @Nullable PlayerEntity player
        //#endif
    ) {
        if (AmsServerSettings.netherWaterPlacement) {
            //#if MC>=12105
            //$$ if (entity instanceof PlayerEntity player) {
            //#else
            if (player != null) {
            //#endif
                return false;
            }
        }

        return original;
    }
}
