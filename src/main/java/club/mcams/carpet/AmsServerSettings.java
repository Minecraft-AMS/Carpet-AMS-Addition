package club.mcams.carpet;

import carpet.settings.Rule;
import static carpet.settings.RuleCategory.*;

/**
 * Here is your example Settings class you can plug to use carpetmod /carpet settings command
 */
public class AmsServerSettings
{
    public static final String AMS="AMS";

    @Rule(
            desc = "末影龙复活过程检测优化",
            category = {AMS, OPTIMIZATION}
    )
    public static boolean optimizedDragonRespawn = false;
}
