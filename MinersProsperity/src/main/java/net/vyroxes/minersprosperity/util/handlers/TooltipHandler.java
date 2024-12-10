package net.vyroxes.minersprosperity.util.handlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Reference;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class TooltipHandler
{

    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.Pre event)
    {
    	List<String> lines = new ArrayList<>(event.getLines());
        lines.add(0, "Witam");
    }
}