package club.mcams.carpet.mixin.rule.canBreakBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

/**
 * 该规则会影响原版获取基岩的探索，因此该规则可能会在未来移除
 */
@Mixin(Blocks.class)
public abstract class BlocksRedirect {
    @Redirect(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=bedrock")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;dropsNothing()Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 0)
    )
    private static AbstractBlock.Settings bedrockDrop(AbstractBlock.Settings instance) {
        return instance;
    }

    @Redirect(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=end_portal_frame")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;dropsNothing()Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 0)
    )
    private static AbstractBlock.Settings endPortalFrameDrop(AbstractBlock.Settings instance) {
        return instance;
    }
}