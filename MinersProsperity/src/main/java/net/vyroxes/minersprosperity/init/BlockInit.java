package net.vyroxes.minersprosperity.init;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.vyroxes.minersprosperity.objects.blocks.BlockCharcoal;
import net.vyroxes.minersprosperity.objects.blocks.BlockOreBlocks;
import net.vyroxes.minersprosperity.objects.blocks.BlockOres;
import net.vyroxes.minersprosperity.objects.blocks.machines.Crusher;

public class BlockInit
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();

	//BLOCKS
	//MACHINES
	public static final Block CRUSHER = new Crusher("crusher");

	//ORES
	public static final Block COPPER_ORE = new BlockOres("copper_ore", 1, 5.0F, 10.0F);
	public static final Block TIN_ORE = new BlockOres("tin_ore", 1, 5.0F, 10.0F);
	public static final Block LEAD_ORE = new BlockOres("lead_ore", 1, 5.0F, 10.0F);
	public static final Block SILVER_ORE = new BlockOres("silver_ore", 1, 5.0F, 10.0F);

	//OREBLOCKS
	public static final Block CHARCOAL_BLOCK = new BlockCharcoal("charcoal_block");
	public static final Block COPPER_BLOCK = new BlockOreBlocks("copper_block", MapColor.ORANGE_STAINED_HARDENED_CLAY, 1, 5.0F, 10.0F);
	public static final Block TIN_BLOCK = new BlockOreBlocks("tin_block", MapColor.IRON, 1, 5.0F, 10.0F);
	public static final Block LEAD_BLOCK = new BlockOreBlocks("lead_block", MapColor.LIGHT_BLUE, 1, 5.0F, 10.0F);
	public static final Block SILVER_BLOCK = new BlockOreBlocks("silver_block", MapColor.SILVER, 1, 5.0F, 10.0F);
}