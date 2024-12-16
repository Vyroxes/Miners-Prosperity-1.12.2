package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.init.ItemInit;
import net.vyroxes.minersprosperity.util.compat.OreDictionaryCompat;
import net.vyroxes.minersprosperity.util.interfaces.IHasModel;
import net.vyroxes.minersprosperity.world.gen.WorldGenCustomOres;

@EventBusSubscriber
public class RegistryHandler
{	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
		TileEntityHandler.registerTileEntities();
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Item item : ItemInit.ITEMS)
		{
			if(item instanceof IHasModel)
			{
				((IHasModel)item).registerModels();
			}
		}
		
		for(Block block : BlockInit.BLOCKS)
		{
			if(block instanceof IHasModel)
			{
				((IHasModel)block).registerModels();
			}
		}
	}

	public static void otherRegistries()
	{
		GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
	}

	public static void preInitRegistries(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(MinersProsperity.instance);
		
	    MinecraftForge.EVENT_BUS.register(new TooltipHandler());
		
	    MinecraftForge.EVENT_BUS.register(new CraftingHandler());
	    
		KeyInputHandler.registerKeyBindings();
		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
		
		ConfigHandler.registerConfig(event);
		MinecraftForge.EVENT_BUS.register(new ConfigHandler());
	}
	
	public static void initRegistries(FMLInitializationEvent event)
	{
		OreDictionaryCompat.registerOreDictionary();
		
		NetworkHandler.init(event);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(MinersProsperity.instance, new GuiHandler());

		MinecraftForge.EVENT_BUS.register(new PickupHandler());
	}
	
	public static void postInitRegistries(FMLPostInitializationEvent event) {}
	
	public static void serverRegistries(FMLServerStartingEvent event) {}
}