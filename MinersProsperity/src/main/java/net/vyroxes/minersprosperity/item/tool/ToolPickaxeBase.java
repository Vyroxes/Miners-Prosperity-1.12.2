package net.vyroxes.minersprosperity.item.tool;

import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModTools;
import org.jetbrains.annotations.NotNull;

public class ToolPickaxeBase extends ItemPickaxe
{
	private final ToolTier tier;

	public ToolPickaxeBase(String name, ToolTier tier)
	{
		super(tier.material);

		this.tier = tier;

		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.tools_tab);
		
		ModTools.TOOLS.add(this);
	}

	@Override
	public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair)
	{
		return tier.isRepairMaterial(repair) || super.getIsRepairable(toRepair, repair);
	}
}