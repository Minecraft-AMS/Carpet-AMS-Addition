package club.mcams.carpet.mixin.rule.coarseDirtDriedToSand;

import club.mcams.carpet.util.compat.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = "<1.17.0"))
@Mixin(DummyClass.class)
public class MixinPointedDripstoneBlock {
}
