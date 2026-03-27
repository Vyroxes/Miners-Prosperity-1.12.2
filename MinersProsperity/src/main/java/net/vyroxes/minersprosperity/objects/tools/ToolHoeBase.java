package net.vyroxes.minersprosperity.objects.tools;

import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ToolInit;
import net.vyroxes.minersprosperity.util.interfaces.IHasModel;
import org.jetbrains.annotations.NotNull;

public class ToolHoeBase extends ItemHoe implements IHasModel
{
	public ToolHoeBase(String name, ToolMaterial material)
	{
		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.tools_tab);
		
		ToolInit.TOOLS.add(this);
	}

	@Override
	public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair)
	{
		ItemStack mat = this.toolMaterial.getRepairItemStack();
		if (!mat.isEmpty() && OreDictionary.itemMatches(mat, repair, false)) return true;
		return super.getIsRepairable(toRepair, repair);
	}

	@Override
	public void registerModels()
	{
		MinersProsperity.proxy.registerModel(this, 0);
	}
}