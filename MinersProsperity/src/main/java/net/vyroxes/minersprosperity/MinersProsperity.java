package net.vyroxes.minersprosperity;

import java.io.File;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.vyroxes.minersprosperity.client.registry.ClientModelRegistry;
import net.vyroxes.minersprosperity.command.CommandMP;
import net.vyroxes.minersprosperity.config.ConfigOrderHandler;
import net.vyroxes.minersprosperity.creative.TabArmor;
import net.vyroxes.minersprosperity.creative.TabBlocks;
import net.vyroxes.minersprosperity.creative.TabItems;
import net.vyroxes.minersprosperity.creative.TabTools;
import net.vyroxes.minersprosperity.enchantment.QuickMiningLoader;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import net.vyroxes.minersprosperity.init.ModEntities;
import net.vyroxes.minersprosperity.init.ModFluids;
import net.vyroxes.minersprosperity.init.ModTileEntities;
import net.vyroxes.minersprosperity.integration.OreDictionaryCompat;
import net.vyroxes.minersprosperity.integration.baubles.BaublesCompatHandler;
import net.vyroxes.minersprosperity.machine.recipe.FurnaceRecipes;
import net.vyroxes.minersprosperity.machine.recipe.MachineRecipes;
import net.vyroxes.minersprosperity.network.NetworkHandler;
import net.vyroxes.minersprosperity.proxy.CommonProxy;
import net.vyroxes.minersprosperity.world.event.ResinHandler;
import net.vyroxes.minersprosperity.world.gen.WorldGeneration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MODID, name = Tags.MODNAME, version = Tags.VERSION, acceptedMinecraftVersions = "[1.12.2]", guiFactory = Tags.MODGROUP + ".client.gui.ConfigGuiFactory")
public class MinersProsperity
{
    static
    {
        FluidRegistry.enableUniversalBucket();
    }

	public static File config;
	
	@Mod.Instance(Tags.MODID)
	public static MinersProsperity INSTANCE;
	
	@SidedProxy(clientSide = Tags.MODGROUP + ".proxy.ClientProxy", serverSide = Tags.MODGROUP + ".proxy.CommonProxy")
	public static CommonProxy proxy;
	
	public static final CreativeTabs tools_tab = new TabTools("tools_tab");
	public static final CreativeTabs armour_tab = new TabArmor("armour_tab");
	public static final CreativeTabs items_tab = new TabItems("items_tab");
	public static final CreativeTabs blocks_tab = new TabBlocks("blocks_tab");

	public static final Logger logger = LogManager.getLogger(Tags.MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModFluids.register();
        ModTileEntities.register();
        ModEntities.register();
        WorldGeneration.register();

        ClientModelRegistry.registerEntityRenderers();

        MinecraftForge.TERRAIN_GEN_BUS.register(ResinHandler.class);

        if (Loader.isModLoaded("baubles")) MinecraftForge.EVENT_BUS.register(new BaublesCompatHandler());

        proxy.preInit(event);
    }

    @Mod.EventHandler
	public static void init(FMLInitializationEvent event) {
        OreDictionaryCompat.registerOreDictionary();

        QuickMiningLoader.loadFromJSON();
        MachineRecipes.load();

        NetworkHandler.init(event);

        ConfigOrderHandler.apply();

        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());

        FurnaceRecipes.register();

        proxy.init(event);
	}

    @Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
	}

    @Mod.EventHandler
	public static void serverInit(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandMP());
	}

	@Mod.EventHandler
	public void onInterModEnqueue(FMLInterModComms.IMCEvent event) {
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "net.vyroxes.minersprosperity.integration.top.TOPCompat");
	}
}