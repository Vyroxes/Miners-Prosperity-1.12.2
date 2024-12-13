package net.vyroxes.minersprosperity.objects.tools;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ItemInit;
import net.vyroxes.minersprosperity.util.interfaces.IHasModel;

public class SwordEmerald extends ItemSword implements IHasModel
{
	public SwordEmerald(String name, ToolMaterial material)
	{
		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.tools_tab);
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
        return repair.getItem() == Items.EMERALD || super.getIsRepairable(toRepair, repair);
	}
	
	@Override
	public void registerModels()
	{
		MinersProsperity.proxy.registerModel(this, 0);
	}
}