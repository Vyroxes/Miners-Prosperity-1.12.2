package net.vyroxes.minersprosperity.objects.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ArmorInit;
import net.vyroxes.minersprosperity.util.interfaces.IHasModel;

public class ArmorBase extends ItemArmor implements IHasModel
{
	public ArmorBase(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
	{
		super(materialIn, renderIndexIn, equipmentSlotIn);

		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.armour_tab);

		ArmorInit.ARMORS.add(this);
	}

	@Override
	public void registerModels()
	{
		MinersProsperity.proxy.registerModel(this, 0);
	}
}