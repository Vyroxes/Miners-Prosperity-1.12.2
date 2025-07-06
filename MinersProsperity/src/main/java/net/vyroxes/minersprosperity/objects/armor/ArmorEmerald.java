package net.vyroxes.minersprosperity.objects.armor;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ArmorEmerald extends ArmorBase
{

	public ArmorEmerald(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
        return repair.getItem() == Items.EMERALD || super.getIsRepairable(toRepair, repair);
	}
}