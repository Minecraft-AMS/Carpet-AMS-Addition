package club.mcams.carpet.mixin.rule.coarseDirtDriedToSand;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.BlockState;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Restriction(require=@Condition(value = "minecraft", versionPredicates = ">=1.17.0"))
@Mixin(PointedDripstoneBlock.class)
public interface IMixinPointedDripstoneBlock {
    @Invoker("getSupportingPos")
    public static Optional<BlockPos> invoke_getSupportingPos(World world, BlockPos pos2, BlockState state2, int range){
        throw new AssertionError();
    }
}
