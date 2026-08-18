package net.vyroxes.minersprosperity.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.integration.baubles.BaublesRegistryCompat;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public final class RegistryEvents {
    private RegistryEvents() {}

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : ModBlocks.BLOCKS) if (ModBlocks.isEnabled(block)) event.getRegistry().register(block);
        event.getRegistry().register(ModBlocks.LIQUID_EXPERIENCE_BLOCK);
        event.getRegistry().register(ModBlocks.CRUDE_OIL_BLOCK);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : ModItems.ITEMS) if (ModItems.isEnabled(item)) event.getRegistry().register(item);
        event.getRegistry().registerAll(ModTools.TOOLS.toArray(new Item[0]));
        event.getRegistry().registerAll(ModArmor.ARMORS.toArray(new Item[0]));
        BaublesRegistryCompat.registerItems(event.getRegistry());
    }
}