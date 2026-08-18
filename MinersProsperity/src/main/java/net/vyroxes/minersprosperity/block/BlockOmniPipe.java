package net.vyroxes.minersprosperity.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockOmniPipe extends BlockBase
{

    public BlockOmniPipe(String name, Material material, MapColor mapColor) {
        super(name, material, mapColor, true);

        setSoundType(SoundType.STONE);
//        setHarvestLevel("pickaxe", 0);
        setHardness(0.5f);
        setResistance(1.0f);
    }
}