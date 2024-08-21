package club.mcams.carpet.mixin.rule.fakePlayerInteractLikeClient;

import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BoatEntity.class)
public interface BoatEntityInvoker {
    @Accessor("ticksUnderwater")
    float getTicksUnderwater();
}
