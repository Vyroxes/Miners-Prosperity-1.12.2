package net.vyroxes.minersprosperity.init;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.vyroxes.minersprosperity.objects.blocks.machines.Crusher;

public class BlockInit
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block CRUSHER = new Crusher("crusher");
}