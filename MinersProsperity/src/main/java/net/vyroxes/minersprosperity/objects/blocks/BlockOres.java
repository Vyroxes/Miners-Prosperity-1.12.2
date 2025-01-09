package net.vyroxes.minersprosperity.objects.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.util.annotations.NonnullByDefault;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockOres extends BlockBase
{
    private final Item item;

    public BlockOres(String name, int harvestLevel, float hardness, float resistance, @Nullable Item item)
    {
        super(name, Material.IRON, MapColor.STONE);

        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", harvestLevel);
        setHardness(hardness);
        setResistance(resistance);
        this.item = item;
    }

    @NonnullByDefault
    @Override
    public @NotNull Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        if (this.item != null)
        {
            return this.item;
        }
        return super.getItemDropped(state, rand, fortune);
    }

    public int quantityDroppedWithBonus(int fortune, @NotNull Random random)
    {
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(this.getBlockState().getValidStates().iterator().next(), random, fortune))
        {
            int i = random.nextInt(fortune + 2) - 1;

            if (i < 0)
            {
                i = 0;
            }

            return this.quantityDropped(random) * (i + 1);
        }
        else
        {
            return this.quantityDropped(random);
        }
    }

    @Override
    public int getExpDrop(@NotNull IBlockState state, net.minecraft.world.@NotNull IBlockAccess world, @NotNull BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
        {
            int i = 0;

            if (this.item != null)
            {
                i = MathHelper.getInt(rand, 3, 7);
            }

            return i;
        }
        return 0;
    }
}