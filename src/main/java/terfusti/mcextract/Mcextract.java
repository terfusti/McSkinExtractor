package terfusti.mcextract;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Mcextract.MODID)
public class Mcextract
{
    public static final String MODID = "mcextract";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Mcextract(IEventBus modEventBus, ModContainer modContainer)
    {

    }
}
