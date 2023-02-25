package club.mcams.carpet.mixin;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

public class AmsMixinPlugin extends RestrictiveMixinConfigPlugin {
    private final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void onRestrictionCheckFailed(String mixinClassName, String reason)
    {
        LOGGER.debug("[AMSCM] Disabled mixin {} due to {}", mixinClassName, reason);
    }

    @Override
    public String getRefMapperConfig()
    {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
    {
    }

    @Override
    public List<String> getMixins()
    {
        return null;
    }
}
