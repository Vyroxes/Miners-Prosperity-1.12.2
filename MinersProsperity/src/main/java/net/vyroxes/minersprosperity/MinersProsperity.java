package net.vyroxes.minersprosperity;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.vyroxes.minersprosperity.objects.tabs.ArmourTab;
import net.vyroxes.minersprosperity.objects.tabs.BlocksTab;
import net.vyroxes.minersprosperity.objects.tabs.ItemsTab;
import net.vyroxes.minersprosperity.objects.tabs.ToolsTab;
import net.vyroxes.minersprosperity.proxy.ServerProxy;
import net.vyroxes.minersprosperity.util.handlers.RegistryHandler;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class MinersProsperity
{	
	@Instance
	public static MinersProsperity instance;
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.SERVER)
	public static ServerProxy proxy;
	
	public static final CreativeTabs tools_tab = new ToolsTab("tools_tab");
	public static final CreativeTabs armour_tab = new ArmourTab("armour_tab");
	public static final CreativeTabs items_tab = new ItemsTab("items_tab");
	public static final CreativeTabs blocks_tab = new BlocksTab("blocks_tab");
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		RegistryHandler.preInitRegistries(event);
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		RegistryHandler.initRegistries(event);
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		RegistryHandler.postInitRegistries(event);
	}
	
	@EventHandler
	public static void serverInit(FMLServerStartingEvent event)
	{
		RegistryHandler.serverRegistries(event);
	}
}
