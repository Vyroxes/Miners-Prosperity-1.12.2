package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.item.Item;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.ArmorInit;
import net.vyroxes.minersprosperity.init.ItemInit;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class FurnaceFuelHandler
{
    private static final Map<Item, Integer> FUEL_MAP = new HashMap<>();

    static
    {
        FUEL_MAP.put(ArmorInit.WOODEN_BOOTS, 200);
        FUEL_MAP.put(ArmorInit.WOODEN_LEGGINGS, 200);
        FUEL_MAP.put(ArmorInit.WOODEN_CHESTPLATE, 200);
        FUEL_MAP.put(ArmorInit.WOODEN_HELMET, 200);

        FUEL_MAP.put(ItemInit.WOOD_GEAR, 200);
        FUEL_MAP.put(ItemInit.WOOD_PLATE, 200);

        FUEL_MAP.put(ItemInit.COAL_DUST, 1600);
        FUEL_MAP.put(ItemInit.CHARCOAL_DUST, 1600);

        FUEL_MAP.put(ItemInit.COAL_TINY_DUST, 150);
        FUEL_MAP.put(ItemInit.CHARCOAL_TINY_DUST, 150);

        FUEL_MAP.put(ItemInit.COAL_GEAR, 1600);
        FUEL_MAP.put(ItemInit.CHARCOAL_GEAR, 1600);

        FUEL_MAP.put(ItemInit.COAL_PLATE, 1600);
        FUEL_MAP.put(ItemInit.CHARCOAL_PLATE, 1600);

        FUEL_MAP.put(ItemInit.COAL_ROD, 1600);
        FUEL_MAP.put(ItemInit.CHARCOAL_ROD, 1600);
    }

    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event)
    {
        Item item = event.getItemStack().getItem();

        Integer burnTime = FUEL_MAP.get(item);
        if (burnTime != null)
        {
            event.setBurnTime(burnTime);
        }
    }
}