package club.mcams.carpet.mixin.rule.onlyPlayerCanCreateNetherPortal;

import club.mcams.carpet.AmsServerSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.dimension.PortalForcer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.byteeeee.annotationtoolbox.annotation.GameVersion;
import java.util.Optional;

@GameVersion(version = "Minecraft >= 1.21")
@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {
    @WrapOperation(
        method = "getOrCreateExitPortalTarget",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/dimension/PortalForcer;createPortal(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;"
        )
    )
    private Optional<BlockLocating.Rectangle> itemEntityCreateNetherPortalDisabled(
        PortalForcer forcer, BlockPos pos, Direction.Axis axis, Operation<Optional<BlockLocating.Rectangle>> original,
        ServerWorld world, Entity entity
    ) {
        if (AmsServerSettings.onlyPlayerCanCreateNetherPortal && !(entity instanceof PlayerEntity)) {
            return Optional.empty();
        } else {
            return original.call(forcer, pos, axis);
        }
    }
}