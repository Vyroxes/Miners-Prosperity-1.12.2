package net.vyroxes.minersprosperity.item.tool;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModTools;
import org.jetbrains.annotations.NotNull;

public class ToolAxeBase extends ItemAxe
{
	private final ToolTier tier;

	public ToolAxeBase(String name, ToolTier tier, float damage, float speed)
	{
		super(tier.material, damage, speed);

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