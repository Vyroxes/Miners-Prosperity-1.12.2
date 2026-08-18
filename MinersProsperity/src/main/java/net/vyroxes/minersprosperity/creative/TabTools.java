package net.vyroxes.minersprosperity.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ModTools;
import org.jetbrains.annotations.NotNull;

public class TabTools extends CreativeTabs
{
	public TabTools(String label)
	{
		super("tools_tab");
	}

	@Override
	public @NotNull ItemStack createIcon()
	{
		return new ItemStack(ModTools.EMERALD_PICKAXE);
	}
}