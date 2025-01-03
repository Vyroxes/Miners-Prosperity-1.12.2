package net.vyroxes.minersprosperity.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IWorldNameable;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.init.ItemInit;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.util.interfaces.IHasModel;

import javax.annotation.Nullable;

public class BlockBase extends Block implements IHasModel
{
	public BlockBase(String name, Material material, MapColor mapColor)
	{
		super(material, mapColor);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.blocks_tab);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(name));
	}

	@Override
	public void registerModels()
	{
		MinersProsperity.proxy.registerModel(Item.getItemFromBlock(this), 0);
	}
}