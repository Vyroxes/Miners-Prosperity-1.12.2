package net.vyroxes.minersprosperity.objects.tools;

import net.minecraft.item.ItemAxe;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ToolInit;
import net.vyroxes.minersprosperity.util.interfaces.IHasModel;

public class ToolAxeBase extends ItemAxe implements IHasModel
{
	public ToolAxeBase(String name, ToolMaterial material, float damage, float speed)
	{
		super(material, damage, speed);
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