package net.vyroxes.minersprosperity.block.machine;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.tile.TileEntitySolidFuelGenerator;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import net.vyroxes.minersprosperity.annotation.NonnullByDefault;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import org.jetbrains.annotations.Nullable;
import java.util.Random;

public class BlockMachineSolidFuelGenerator extends BlockMachine {
    public BlockMachineSolidFuelGenerator(String name) {
        super(name, Material.IRON, MapColor.STONE, SoundType.STONE, MinersProsperity.blocks_tab, "pickaxe", 0, 5.0F, 10.0F, GuiHandler.GuiTypes.SOLID_FUEL_GENERATOR);
    }

    @NonnullByDefault
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.SOLID_FUEL_GENERATOR);
    }

    @Override
    public TileEntityMachine getTileEntity(World world, BlockPos pos) {
        return (TileEntitySolidFuelGenerator) world.getTileEntity(pos);
    }

    @Override
    @NonnullByDefault
    public @Nullable TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySolidFuelGenerator();
    }
}