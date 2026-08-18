package net.vyroxes.minersprosperity.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.init.ModItems;

public class BlockBase extends Block
{
	public BlockBase(String name, Material material, MapColor mapColor, boolean hasItemBlock)
	{
		super(material, mapColor);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.blocks_tab);
		
		ModBlocks.BLOCKS.add(this);
		if(hasItemBlock) ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(name));
	}
}