package club.mcams.carpet.mixin.interation;

import club.mcams.carpet.function.Interactions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Text getName();

    @Shadow
    public abstract boolean isSneaking();

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void pushAwayFrom(Entity entity, CallbackInfo ci) {
        String playerName = "";
        boolean b1 = true;
        if (entity instanceof PlayerEntity)
            playerName = entity.getName().getString();
        else if (((Object) this) instanceof PlayerEntity)
            playerName = ((PlayerEntity) (Object) this).getName().getString();
        else b1 = false;

        if (b1 && Interactions.onlinePlayerMap.containsKey(playerName) &&
                Interactions.onlinePlayerMap.get(playerName).contains("entities"))
            ci.cancel();
    }

    /**
     * @author WenDavid
     * @reason For further compatibility
     */
    @Overwrite
    public boolean bypassesSteppingEffects() {
        if (((Object) this) instanceof PlayerEntity) {
            String playerName = this.getName().getString();
            if (Interactions.onlinePlayerMap.containsKey(playerName) &&
                    Interactions.onlinePlayerMap.get(playerName).contains("blocks"))
                return true;
        }
        return this.isSneaking();
    }

    @Inject(method = "fall", at = @At("HEAD"), cancellable = true)
    private void bypassLanding(double heightDifference, boolean onGround, BlockState landedState, BlockPos blockPos, CallbackInfo ci) {
        if (((Object) this) instanceof PlayerEntity) {
            Block block = landedState.getBlock();
            if (block instanceof FarmlandBlock || block instanceof TurtleEggBlock) {
                String playerName = this.getName().getString();
                if (Interactions.onlinePlayerMap.containsKey(playerName) &&
                        Interactions.onlinePlayerMap.get(playerName).contains("blocks"))
                    ci.cancel();
            }
        }
    }

    @Inject(method = "checkBlockCollision", at = @At("HEAD"), cancellable = true)
    private void bypassBlockCollision(CallbackInfo ci) {
        if (((Object) this) instanceof PlayerEntity) {
            String playerName = this.getName().getString();
            if (Interactions.onlinePlayerMap.containsKey(playerName) &&
                    Interactions.onlinePlayerMap.get(playerName).contains("blocks"))
                ci.cancel();
        }
    }

    @Inject(method = "canAvoidTraps", at = @At("HEAD"), cancellable = true)
    private void canAvoidTraps(CallbackInfoReturnable<Boolean> cir) {
        if (((Object) this) instanceof PlayerEntity) {
            String playerName = this.getName().getString();
            if (Interactions.onlinePlayerMap.containsKey(playerName) &&
                    Interactions.onlinePlayerMap.get(playerName).contains("blocks"))
                cir.setReturnValue(true);
        }
    }
}
