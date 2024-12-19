package net.vyroxes.minersprosperity.objects.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.BlockInit;

@Mod.EventBusSubscriber(modid = "minersprosperity")
public class BlockCharcoal extends BlockBase
{

	public BlockCharcoal(String name)
	{
		super(name, Material.ROCK, MapColor.BLACK);
		setSoundType(SoundType.STONE);
		setHarvestLevel("pickaxe", 0);
		setHardness(5.0F);
		setResistance(10.0F);
	}

	@SubscribeEvent
	public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event)
	{
		ItemStack fuel = event.getItemStack();

		if (fuel.getItem() == Item.getItemFromBlock(BlockInit.CHARCOAL_BLOCK))
		{
			event.setBurnTime(16000);
		}
	}
}