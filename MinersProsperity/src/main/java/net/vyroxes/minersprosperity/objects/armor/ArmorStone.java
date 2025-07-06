package net.vyroxes.minersprosperity.objects.armor;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ArmorStone extends ArmorBase
{

	public ArmorStone(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{
		super(name, materialIn, renderIndexIn, equipmentSlotIn);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
        return repair.getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE) || super.getIsRepairable(toRepair, repair);
	}
}