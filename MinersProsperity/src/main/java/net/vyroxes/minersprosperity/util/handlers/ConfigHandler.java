package net.vyroxes.minersprosperity.util.handlers;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Tags;

@Mod.EventBusSubscriber
public class ConfigHandler
{
    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Tags.MODID))
        {
            ConfigManager.sync(Tags.MODID, Config.Type.INSTANCE);
        }
    }
}