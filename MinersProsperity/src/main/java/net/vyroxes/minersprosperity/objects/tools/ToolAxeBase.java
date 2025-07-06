package net.vyroxes.minersprosperity.objects.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ItemInit;
import net.vyroxes.minersprosperity.util.interfaces.IHasModel;

public class ToolAxeBase extends ItemAxe implements IHasModel
{
	private final Item repairItem;

	public ToolAxeBase(String name, ToolMaterial material, float damage, float speed, Item repairItem)
	{
		super(material, damage, speed);
		this.repairItem = repairItem;
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.tools_tab);
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
        return repair.getItem() == repairItem || super.getIsRepairable(toRepair, repair);
	}
	
	@Override
	public void registerModels()
	{
		MinersProsperity.proxy.registerModel(this, 0);
	}
}