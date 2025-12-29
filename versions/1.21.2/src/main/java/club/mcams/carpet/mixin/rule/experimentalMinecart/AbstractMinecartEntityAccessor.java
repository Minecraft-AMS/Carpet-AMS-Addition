package club.mcams.carpet.mixin.rule.experimentalMinecart;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartController;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractMinecartEntity.class)
public interface AbstractMinecartEntityAccessor {
    @Mutable
    @Accessor("controller")
    void setController(MinecartController controller);
}
