package net.vyroxes.minersprosperity.objects.blocks.machines;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntitySupremeSolarPanel;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityUltimateSolarPanel;
import net.vyroxes.minersprosperity.util.annotations.NonnullByDefault;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class MachineSupremeSolarPanel extends Machine {
    public MachineSupremeSolarPanel(String name) {
        super(name, Material.IRON, MapColor.STONE, SoundType.STONE, MinersProsperity.blocks_tab, "pickaxe", 0, 5.0F, 10.0F, GuiHandler.GuiTypes.SOLAR_PANEL);
    }

    @NonnullByDefault
    public @NotNull Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BlockInit.SUPREME_SOLAR_PANEL);
    }

    @Override
    public TileEntityMachine getTileEntity(World world, BlockPos pos) {
        return (TileEntitySupremeSolarPanel) world.getTileEntity(pos);
    }

    @Override
    @NonnullByDefault
    public @Nullable TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySupremeSolarPanel();
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {}
}