package net.vyroxes.minersprosperity.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockOreBlocks extends BlockBase
{
    public BlockOreBlocks(String name, MapColor mapColor, int harvestLevel, float hardness, float resistance)
    {
        super(name, Material.IRON, mapColor, true);

        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", harvestLevel);
        setHardness(hardness);
        setResistance(resistance);
    }
}