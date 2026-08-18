package net.vyroxes.minersprosperity.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ModBlocks;
import org.jetbrains.annotations.NotNull;

public class TabBlocks extends CreativeTabs
{
	public TabBlocks(String label)
	{
		super("blocks_tab");
	}

	@Override
	public @NotNull ItemStack createIcon()
	{
		return new ItemStack(ModBlocks.ALLOY_FURNACE);
	}
}