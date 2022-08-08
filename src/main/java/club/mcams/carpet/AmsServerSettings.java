package club.mcams.carpet;
import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;
import static carpet.settings.RuleCategory.*;

/**
 * Here is your example Settings class you can plug to use carpetmod /carpet settings command
 */
public class AmsServerSettings
{
    public static final String AMS="AMS";


    @Rule(
            desc ="音符盒被触发时加载附近区块 3x3 15s",
            extra = {"noteblock chunk loader",
                    "noteblock chunk loader"
            },
            category = {AMS,EXPERIMENTAL}
    )
    public static boolean NoteBlockChunkLoader = false;
}
