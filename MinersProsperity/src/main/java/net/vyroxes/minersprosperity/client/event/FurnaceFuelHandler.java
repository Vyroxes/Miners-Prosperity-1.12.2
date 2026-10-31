package net.vyroxes.minersprosperity.client.event;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.init.ModArmor;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.init.ModTools;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class FurnaceFuelHandler
{
    private static final Map<Item, Integer> FUEL_MAP = new HashMap<>();

    static
    {
        FUEL_MAP.put(Items.WOODEN_SWORD, 300);
        FUEL_MAP.put(Items.WOODEN_PICKAXE, 300);
        FUEL_MAP.put(Items.WOODEN_AXE, 300);
        FUEL_MAP.put(Items.WOODEN_SHOVEL, 300);
        FUEL_MAP.put(Items.WOODEN_HOE, 300);
        FUEL_MAP.put(ModTools.WOODEN_PAXEL, 300);
        FUEL_MAP.put(ModArmor.WOODEN_BOOTS, 300);
        FUEL_MAP.put(ModArmor.WOODEN_LEGGINGS, 300);
        FUEL_MAP.put(ModArmor.WOODEN_CHESTPLATE, 300);
        FUEL_MAP.put(ModArmor.WOODEN_HELMET, 300);
        FUEL_MAP.put(Items.SHIELD, 300);
        FUEL_MAP.put(ModItems.WOODEN_TREETAP, 300);
        FUEL_MAP.put(ModTools.WOODEN_SHEARS, 300);
        FUEL_MAP.put(ModArmor.WOODEN_HORSE_ARMOR, 300);
        FUEL_MAP.put(ModItems.WOOD_GEAR, 300);
        FUEL_MAP.put(ModItems.WOOD_PLATE, 300);
        FUEL_MAP.put(ModItems.WOOD_DUST, 300);
        FUEL_MAP.put(ModItems.WOOD_TINY_DUST, 30);
        FUEL_MAP.put(Items.CARROT_ON_A_STICK, 300);
        FUEL_MAP.put(Items.BED, 300);
        FUEL_MAP.put(Items.STICK, 150);


        FUEL_MAP.put(ModItems.COAL_CHUNK, 400);
        FUEL_MAP.put(ModItems.COAL_CLUMP, 500);
        FUEL_MAP.put(ModItems.COAL_CONCENTRATE, 800);
        FUEL_MAP.put(ModItems.COAL_NUGGET, 175);
        FUEL_MAP.put(ModItems.CHARCOAL_NUGGET, 175);
        FUEL_MAP.put(ModItems.COAL_DUST, 1600);
        FUEL_MAP.put(ModItems.DIRTY_COAL_DUST, 600);
        FUEL_MAP.put(ModItems.COAL_TINY_DUST, 175);
        FUEL_MAP.put(ModItems.TINY_DIRTY_COAL_DUST, 65);
        FUEL_MAP.put(ModItems.COAL_GEAR, 1600);
        FUEL_MAP.put(ModItems.COAL_PLATE, 1600);
        FUEL_MAP.put(ModItems.COAL_ROD, 800);

        FUEL_MAP.put(ModItems.CHARCOAL_DUST, 1600);
        FUEL_MAP.put(ModItems.CHARCOAL_TINY_DUST, 175);
        FUEL_MAP.put(ModItems.CHARCOAL_GEAR, 1600);
        FUEL_MAP.put(ModItems.CHARCOAL_PLATE, 1600);
        FUEL_MAP.put(ModItems.CHARCOAL_ROD, 800);

        FUEL_MAP.put(ModItems.COKE, 3200);
        FUEL_MAP.put(ModItems.COKE_DUST, 3200);
        FUEL_MAP.put(ModItems.COKE_TINY_DUST, 355);
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