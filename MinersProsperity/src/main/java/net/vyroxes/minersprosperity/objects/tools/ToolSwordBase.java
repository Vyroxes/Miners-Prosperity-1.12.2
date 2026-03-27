package net.vyroxes.minersprosperity.objects.tools;

import net.minecraft.item.ItemSword;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ToolInit;
import net.vyroxes.minersprosperity.util.interfaces.IHasModel;

public class ToolSwordBase extends ItemSword implements IHasModel
{
	public ToolSwordBase(String name, ToolMaterial material)
	{
		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.tools_tab);
		
		ToolInit.TOOLS.add(this);
	}
	
	@Override
	public void registerModels()
	{
		MinersProsperity.proxy.registerModel(this, 0);
	}
}