package club.mcams.carpet.mixin.rule.coarseDirtDriedToSand;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;

@Restriction(require = @Condition(value = "minecraft", versionPredicates = "<1.17.0"))
public interface IMixinPointedDripstoneBlock {
}
