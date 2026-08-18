package net.vyroxes.minersprosperity.block;

import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.config.ModConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BlockResinEmpty extends BlockResin
{
    public BlockResinEmpty(String name) {
        super(name);
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return true;
    }

    @Override
    public void updateTick(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand)
    {
        if (world.isRemote) return;
        if (!ModConfig.WORLD_GENERATION.resinRegrowth) return;

        EnumFacing facing = state.getValue(FACING);
        BlockPos logPos = pos.offset(facing.getOpposite());
        IBlockState logState = world.getBlockState(logPos);

        if (logState.getBlock() != Blocks.LOG || logState.getValue(BlockOldLog.VARIANT) != BlockPlanks.EnumType.SPRUCE)
        {
            world.setBlockToAir(pos);
            return;
        }

        IBlockState resinState = ModBlocks.RESIN_BLOCK.getDefaultState().withProperty(FACING, facing).withProperty(VARIANT, state.getValue(VARIANT));
        world.setBlockState(pos, resinState, 2);
    }

    @Override
    public boolean isReplaceable(@NotNull net.minecraft.world.IBlockAccess world, @NotNull BlockPos pos)
    {
        return false;
    }
}