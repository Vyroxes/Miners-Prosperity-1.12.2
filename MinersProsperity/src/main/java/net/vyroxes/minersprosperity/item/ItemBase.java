package net.vyroxes.minersprosperity.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModItems;
import org.jetbrains.annotations.NotNull;

public class ItemBase extends Item
{
	private final String repairMaterial;

	public ItemBase(String name, int maxStackSize, int maxDamage)
	{
		this(name, maxStackSize, maxDamage, null);
	}

	public ItemBase(String name, int maxStackSize, int maxDamage, String repairMaterial)
	{
		this.repairMaterial = repairMaterial;

		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.items_tab);
		setMaxStackSize(maxStackSize);

		if (maxDamage > 0) setMaxDamage(maxDamage);

		ModItems.ITEMS.add(this);
	}

	public ItemBase(String name)
	{
		this(name, 64, 0, null);
	}

	@Override
	public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair)
	{
		if (repairMaterial != null && !repair.isEmpty())
		{
			for (ItemStack material : OreDictionary.getOres(repairMaterial, false))
			{
				if (OreDictionary.itemMatches(material, repair, false)) return true;
			}
		}

		return super.getIsRepairable(toRepair, repair);
	}
}