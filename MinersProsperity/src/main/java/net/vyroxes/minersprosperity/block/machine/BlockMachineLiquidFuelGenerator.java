package net.vyroxes.minersprosperity.block.machine;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.tile.TileEntityLiquidFuelGenerator;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import net.vyroxes.minersprosperity.annotation.NonnullByDefault;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockMachineLiquidFuelGenerator extends BlockMachine {
    public BlockMachineLiquidFuelGenerator(String name) {
        super(name, Material.IRON, MapColor.STONE, SoundType.STONE, MinersProsperity.blocks_tab, "pickaxe", 0, 5.0F, 10.0F, GuiHandler.GuiTypes.LIQUID_FUEL_GENERATOR);
    }

    @NonnullByDefault
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.LIQUID_FUEL_GENERATOR);
    }

    @Override
    @NonnullByDefault
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!playerIn.isSneaking() && TileEntityLiquidFuelGenerator.isFluidContainer(playerIn.getHeldItem(hand)))
        {
            if (!worldIn.isRemote)
            {
                TileEntityMachine tileEntity = getTileEntity(worldIn, pos);
                if (tileEntity != null && FluidUtil.interactWithFluidHandler(playerIn, hand, ((TileEntityLiquidFuelGenerator) tileEntity).getContainerFluidHandler()))
                {
                    worldIn.notifyBlockUpdate(pos, state, state, 2);
                }
            }
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public TileEntityMachine getTileEntity(World world, BlockPos pos) {
        return (TileEntityLiquidFuelGenerator) world.getTileEntity(pos);
    }

    @Override
    @NonnullByDefault
    public @Nullable TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityLiquidFuelGenerator();
    }
}