package net.vyroxes.minersprosperity.objects.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.init.ItemInit;

public class ArmorObsidianCluster extends ArmorBase
{

	public ArmorObsidianCluster(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
        return repair.getItem() == ItemInit.OBSIDIAN_CLUSTER || super.getIsRepairable(toRepair, repair);
	}
}