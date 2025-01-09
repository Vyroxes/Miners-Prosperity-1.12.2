package net.vyroxes.minersprosperity.objects.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.vyroxes.minersprosperity.util.annotations.NonnullByDefault;

public class BlockOreBlocks extends BlockBase
{
    public BlockOreBlocks(String name, MapColor mapColor, int harvestLevel, float hardness, float resistance)
    {
        super(name, Material.IRON, mapColor);

        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", harvestLevel);
        setHardness(hardness);
        setResistance(resistance);
    }
}