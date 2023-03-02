package club.mcams.carpet.mixin.rule.livingEntityBrainLeakFix;

import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.Memory;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Optional;

@Mixin(Brain.class)
public interface IBrainMixin {
    @Accessor("memories")
    Map<MemoryModuleType<?>, Optional<? extends Memory<?>>> getMemories();
}
