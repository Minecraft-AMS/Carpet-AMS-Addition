package club.mcams.carpet.mixin.rule.netherWaterPlacement;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.world.dimension.DimensionType;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DimensionType.class)
public abstract class NetherWaterPlacementMixin {

    @Final
    @Shadow
    private boolean ultrawarm;

    /**
     * @author
     * @reason
     */

    @Overwrite
    public boolean isUltrawarm() {
        return !AmsServerSettings.netherWaterPlacement && this.ultrawarm;
    }
}