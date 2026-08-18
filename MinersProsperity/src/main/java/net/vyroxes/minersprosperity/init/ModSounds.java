package net.vyroxes.minersprosperity.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Tags;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class ModSounds {
    public static final SoundEvent MACHINE_POWERED = create("machine.powered");
    public static final SoundEvent ALLOY_FURNACE_ACTIVE = create("machine.alloy_furnace_active");
    public static final SoundEvent PULVERIZER_ACTIVE = create("machine.pulverizer_active");
    public static final SoundEvent ELECTRIC_FURNACE_ACTIVE = create("machine.electric_furnace_active");
    public static final SoundEvent METAL_PRESS_ACTIVE = create("machine.metal_press_active");
    public static final SoundEvent METAL_PRESS_CLANK = create("machine.metal_press_clank");
    public static final SoundEvent SOLAR_PANEL_ACTIVE = create("machine.solar_panel_active");

    private static SoundEvent create(String name)
    {
        ResourceLocation location = new ResourceLocation(Tags.MODID, name);
        return new SoundEvent(location).setRegistryName(location);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        event.getRegistry().registerAll(
                ALLOY_FURNACE_ACTIVE,
                PULVERIZER_ACTIVE,
                ELECTRIC_FURNACE_ACTIVE,
                METAL_PRESS_ACTIVE,
                METAL_PRESS_CLANK,
                SOLAR_PANEL_ACTIVE);
    }
}