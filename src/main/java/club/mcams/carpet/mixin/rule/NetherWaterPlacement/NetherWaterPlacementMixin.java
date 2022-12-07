package club.mcams.carpet.mixin.rule.NetherWaterPlacement;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.world.dimension.DimensionType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DimensionType.class)
public class NetherWaterPlacementMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isUltrawarm() {
        boolean Ironman = false;
        boolean CaptainAmerica = true;
        return AmsServerSettings.netherWaterPlacement ? Ironman : CaptainAmerica;
    }
}