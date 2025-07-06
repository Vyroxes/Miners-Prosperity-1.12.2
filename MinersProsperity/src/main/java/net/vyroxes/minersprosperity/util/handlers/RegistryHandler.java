package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.commands.CommandMPHand;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.init.FluidInit;
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
		event.getRegistry().register(BlockInit.LIQUID_EXPERIENCE_BLOCK);
		TileEntityHandler.registerTileEntities();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
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

		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		MinecraftForge.EVENT_BUS.register(new TooltipHandler());
		KeyInputHandler.registerKeyBindings();
		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
	}

	public static void otherRegistries()
	{
		GameRegistry.registerWorldGenerator(new WorldGenCustomOres(), 0);
	}

	public static void preInitRegistries(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(MinersProsperity.INSTANCE);

		FluidRegistry.registerFluid(FluidInit.LIQUID_EXPERIENCE);
		FluidRegistry.addBucketForFluid(FluidInit.LIQUID_EXPERIENCE);

		MinecraftForge.EVENT_BUS.register(new EnchantmentHandler());
	    MinecraftForge.EVENT_BUS.register(new CraftingHandler());
		
		ConfigHandler.registerConfig(event);
	}
	
	public static void initRegistries(FMLInitializationEvent event)
	{
		OreDictionaryCompat.registerOreDictionary();
		
		NetworkHandler.init(event);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(MinersProsperity.INSTANCE, new GuiHandler());

		MinecraftForge.EVENT_BUS.register(new PickupHandler());

		GameRegistry.addSmelting(new ItemStack(BlockInit.COPPER_ORE), new ItemStack(ItemInit.COPPER_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.TIN_ORE), new ItemStack(ItemInit.TIN_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.SILVER_ORE), new ItemStack(ItemInit.SILVER_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.LEAD_ORE), new ItemStack(ItemInit.LEAD_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.ZINC_ORE), new ItemStack(ItemInit.ZINC_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.ALUMINUM_ORE), new ItemStack(ItemInit.ALUMINUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(Items.IRON_INGOT), new ItemStack(ItemInit.REFINED_IRON_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.NICKEL_ORE), new ItemStack(ItemInit.NICKEL_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.CHROMIUM_ORE), new ItemStack(ItemInit.CHROMIUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.URANIUM_ORE), new ItemStack(ItemInit.URANIUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.TUNGSTEN_ORE), new ItemStack(ItemInit.TUNGSTEN_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.PLATINUM_ORE), new ItemStack(ItemInit.PLATINUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.IRIDIUM_ORE), new ItemStack(ItemInit.IRIDIUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.TITANIUM_ORE), new ItemStack(ItemInit.TITANIUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.RUBY_ORE), new ItemStack(ItemInit.RUBY), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.SAPPHIRE_ORE), new ItemStack(ItemInit.SAPPHIRE), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.AMETHYST_ORE), new ItemStack(ItemInit.AMETHYST), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.TOPAZ_ORE), new ItemStack(ItemInit.TOPAZ), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.PERIDOT_ORE), new ItemStack(ItemInit.PERIDOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(Blocks.OBSIDIAN), new ItemStack(ItemInit.REFINED_OBSIDIAN_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.COBALT_ORE), new ItemStack(ItemInit.COBALT_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.ONYX_ORE), new ItemStack(ItemInit.ONYX), 0.7f);
		GameRegistry.addSmelting(new ItemStack(BlockInit.BLACK_OPAL_ORE), new ItemStack(ItemInit.BLACK_OPAL), 0.7f);

		GameRegistry.addSmelting(new ItemStack(ItemInit.CHARCOAL_DUST), new ItemStack(Items.COAL, 1, 1), 0.1f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.COAL_DUST), new ItemStack(Items.COAL), 0.1f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.COPPER_DUST), new ItemStack(ItemInit.COPPER_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.TIN_DUST), new ItemStack(ItemInit.TIN_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.BRONZE_DUST), new ItemStack(ItemInit.BRONZE_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.SILVER_DUST), new ItemStack(ItemInit.SILVER_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.LEAD_DUST), new ItemStack(ItemInit.LEAD_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.ZINC_DUST), new ItemStack(ItemInit.ZINC_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.BRASS_DUST), new ItemStack(ItemInit.BRASS_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.ALUMINUM_DUST), new ItemStack(ItemInit.ALUMINUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.DURALUMINUM_DUST), new ItemStack(ItemInit.DURALUMINUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.IRON_DUST), new ItemStack(Items.IRON_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.REFINED_IRON_DUST), new ItemStack(ItemInit.REFINED_IRON_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.STEEL_DUST), new ItemStack(ItemInit.STEEL_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.NICKEL_DUST), new ItemStack(ItemInit.NICKEL_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.CHROMIUM_DUST), new ItemStack(ItemInit.CHROMIUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.QUARTZ_DUST), new ItemStack(Items.QUARTZ), 0.2f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.INCONEL_DUST), new ItemStack(ItemInit.INCONEL_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.URANIUM_DUST), new ItemStack(ItemInit.URANIUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.TUNGSTEN_DUST), new ItemStack(ItemInit.TUNGSTEN_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.GOLD_DUST), new ItemStack(Items.GOLD_INGOT), 1.0f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.PLATINUM_DUST), new ItemStack(ItemInit.PLATINUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.WHITE_GOLD_DUST), new ItemStack(ItemInit.WHITE_GOLD_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.ELECTRUM_DUST), new ItemStack(ItemInit.ELECTRUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.IRIDIUM_DUST), new ItemStack(ItemInit.IRIDIUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.TITANIUM_DUST), new ItemStack(ItemInit.TITANIUM_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.RUBY_DUST), new ItemStack(ItemInit.RUBY), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.SAPPHIRE_DUST), new ItemStack(ItemInit.SAPPHIRE), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.AMETHYST_DUST), new ItemStack(ItemInit.AMETHYST), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.TOPAZ_DUST), new ItemStack(ItemInit.TOPAZ), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.PERIDOT_DUST), new ItemStack(ItemInit.PERIDOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.DIAMOND_DUST), new ItemStack(Items.DIAMOND), 1.0f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.REFINED_OBSIDIAN_DUST), new ItemStack(ItemInit.REFINED_OBSIDIAN_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.EMERALD_DUST), new ItemStack(Items.EMERALD), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.COBALT_DUST), new ItemStack(ItemInit.COBALT_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.COBALTSTEEL_DUST), new ItemStack(ItemInit.COBALTSTEEL_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.ONYX_DUST), new ItemStack(ItemInit.ONYX), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.DARK_STEEL_DUST), new ItemStack(ItemInit.DARK_STEEL_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(ItemInit.BLACK_OPAL_DUST), new ItemStack(ItemInit.BLACK_OPAL), 0.7f);

		GameRegistry.addSmelting(new ItemStack(Items.IRON_INGOT), new ItemStack(ItemInit.REFINED_IRON_INGOT), 0.7f);
		GameRegistry.addSmelting(new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(ItemInit.GLOWSTONE_SHARD), 0.7f);
		GameRegistry.addSmelting(new ItemStack(Blocks.OBSIDIAN), new ItemStack(ItemInit.REFINED_OBSIDIAN_INGOT), 0.7f);
	}
	
	public static void postInitRegistries(FMLPostInitializationEvent event) {}
	
	public static void serverRegistries(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandMPHand());
	}
}