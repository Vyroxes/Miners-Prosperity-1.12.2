package net.vyroxes.minersprosperity.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModArmor;
import org.jetbrains.annotations.NotNull;

public class ArmorBase extends ItemArmor
{
	private final ArmorTier tier;

	public ArmorBase(String name, ArmorTier tier, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{
		super(tier.material, renderIndexIn, equipmentSlotIn);

		this.tier = tier;

		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.armour_tab);

		ModArmor.ARMORS.add(this);
	}

	public ArmorTier getTier()
	{
		return tier;
	}

	@Override
	public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
		if (repair.isEmpty()) return false;

		if (OreDictionary.containsMatch(false, OreDictionary.getOres(tier.repairMaterial, false), repair))
		{
			return true;
		}

		return super.getIsRepairable(toRepair, repair);
	}
}