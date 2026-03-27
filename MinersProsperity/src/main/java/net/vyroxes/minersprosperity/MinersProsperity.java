package net.vyroxes.minersprosperity;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.vyroxes.minersprosperity.objects.tabs.TabArmor;
import net.vyroxes.minersprosperity.objects.tabs.TabBlocks;
import net.vyroxes.minersprosperity.objects.tabs.TabItems;
import net.vyroxes.minersprosperity.objects.tabs.TabTools;
import net.vyroxes.minersprosperity.proxy.ServerProxy;
import net.vyroxes.minersprosperity.util.handlers.RegistryHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MODID, name = Tags.MODNAME, version = Tags.VERSION, acceptedMinecraftVersions = "[1.12.2]")
public class MinersProsperity
{	
	public static File config;
	
	@Mod.Instance
	public static MinersProsperity INSTANCE;
	
	@SidedProxy(clientSide = Tags.MODGROUP + ".proxy.ClientProxy", serverSide = Tags.MODGROUP + ".proxy.ServerProxy")
	public static ServerProxy proxy;
	
	public static final CreativeTabs tools_tab = new TabTools("tools_tab");
	public static final CreativeTabs armour_tab = new TabArmor("armour_tab");
	public static final CreativeTabs items_tab = new TabItems("items_tab");
	public static final CreativeTabs blocks_tab = new TabBlocks("blocks_tab");

	public static final Logger logger = LogManager.getLogger(Tags.MODID);

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

	@Mod.EventHandler
	public void onInterModEnqueue(FMLInterModComms.IMCEvent event)
	{
		FMLInterModComms.sendFunctionMessage(
				"theoneprobe",
				"getTheOneProbe",
				"net.vyroxes.minersprosperity.util.compat.top.TOPCompat"
		);
	}
}