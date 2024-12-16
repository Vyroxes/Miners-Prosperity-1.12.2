package net.vyroxes.minersprosperity.objects.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockOres extends BlockBase
{
    public BlockOres(String name, int harvestLevel, float hardness, float resistance)
    {
        super(name, Material.IRON, MapColor.STONE);

        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", harvestLevel);
        setHardness(hardness);
        setResistance(resistance);
    }
}