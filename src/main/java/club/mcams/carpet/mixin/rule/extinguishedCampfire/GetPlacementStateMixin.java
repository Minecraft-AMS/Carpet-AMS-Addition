package club.mcams.carpet.mixin.rule.extinguishedCampfire;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.ItemPlacementContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(CampfireBlock.class)
public abstract class GetPlacementStateMixin extends BlockWithEntity {
    protected GetPlacementStateMixin(Settings builder) {
        super(builder);
    }
    @Inject(method = "<init>*", at = @At("RETURN"))
    protected void initProxy(CallbackInfo info) {
        this.setDefaultState(this.getDefaultState().with(CampfireBlock.LIT, false));
    }
    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    protected void getPlacementStateProxy(ItemPlacementContext context, CallbackInfoReturnable<BlockState> cir) {
        if (cir.getReturnValue() != null && AmsServerSettings.extinguishedCampfire) {
            cir.setReturnValue(cir.getReturnValue().with(CampfireBlock.LIT, false));
            cir.cancel();
        }
    }
}
