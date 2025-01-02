package net.vyroxes.minersprosperity.objects.blocks.machines;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.util.annotations.NonnullByDefault;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import org.jetbrains.annotations.Nullable;

public class MachineAlloyFurnace extends Machine {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public MachineAlloyFurnace(String name) {
        super(name, Material.IRON, MapColor.STONE, SoundType.STONE, MinersProsperity.blocks_tab, "pickaxe", 0, 5.0F, 10.0F, BlockInit.ALLOY_FURNACE, GuiHandler.GuiTypes.ALLOY_FURNACE);
    }

    @Override
    public TileEntityMachine getTileEntity(World world, BlockPos pos) {
        return (TileEntityAlloyFurnace) world.getTileEntity(pos);
    }

    @Override
    @NonnullByDefault
    public @Nullable TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAlloyFurnace();
    }
}