package net.vyroxes.minersprosperity;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.vyroxes.minersprosperity.objects.tabs.TabArmour;
import net.vyroxes.minersprosperity.objects.tabs.TabBlocks;
import net.vyroxes.minersprosperity.objects.tabs.TabItems;
import net.vyroxes.minersprosperity.objects.tabs.TabTools;
import net.vyroxes.minersprosperity.proxy.ServerProxy;
import net.vyroxes.minersprosperity.util.handlers.RegistryHandler;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class MinersProsperity
{	
	public static File config;
	
	@Instance
	public static MinersProsperity instance;
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.SERVER)
	public static ServerProxy proxy;
	
	public static final CreativeTabs tools_tab = new TabTools("tools_tab");
	public static final CreativeTabs armour_tab = new TabArmour("armour_tab");
	public static final CreativeTabs items_tab = new TabItems("items_tab");
	public static final CreativeTabs blocks_tab = new TabBlocks("blocks_tab");
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		RegistryHandler.preInitRegistries(event);
		RegistryHandler.otherRegistries();
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {RegistryHandler.initRegistries(event);}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {RegistryHandler.postInitRegistries(event);}
	
	@EventHandler
	public static void serverInit(FMLServerStartingEvent event) {RegistryHandler.serverRegistries(event);}
}