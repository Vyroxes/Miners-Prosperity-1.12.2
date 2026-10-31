package net.vyroxes.minersprosperity.block.machine;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.ItemBlock;
import net.vyroxes.minersprosperity.init.ModItems;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import net.vyroxes.minersprosperity.machine.EnergyStorageTier;
import net.vyroxes.minersprosperity.tile.TileEntityEnergyStorage;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import org.jetbrains.annotations.NotNull;
import java.util.Random;

public class BlockMachineEnergyStorage extends BlockMachine
{
    private final EnergyStorageTier tier;

    public BlockMachineEnergyStorage(String name, EnergyStorageTier tier)
    {
        super(name, Material.IRON, MapColor.STONE, SoundType.METAL, MinersProsperity.blocks_tab, "pickaxe", 0, 5.0F, 10.0F, GuiHandler.GuiTypes.ENERGY_STORAGE);
        this.tier = tier;
        ModItems.ITEMS.removeIf(item -> item instanceof ItemBlock && ((ItemBlock) item).getBlock() == this);
        ModItems.ITEMS.add(new ItemBlockEnergyStorage(this, tier).setRegistryName(name));
    }

    public EnergyStorageTier getTier() { return this.tier; }

    @Override
    public TileEntityMachine getTileEntity(World world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileEntityEnergyStorage ? (TileEntityEnergyStorage) tile : null;
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) { return new TileEntityEnergyStorage(this.tier); }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random random, int fortune) { return Item.getItemFromBlock(this); }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) { return false; }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) { return false; }

    @Override
    public @NotNull BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT; }

    @Override
    public boolean canRenderInLayer(@NotNull IBlockState state, @NotNull BlockRenderLayer layer)
    {
        return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean hasComparatorInputOverride(@NotNull IBlockState state) { return true; }

    @Override
    public int getComparatorInputOverride(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos)
    {
        TileEntityMachine tile = this.getTileEntity(world, pos);
        if (tile == null || tile.getEnergyStored() <= 0) return 0;
        return 1 + (int) ((long) tile.getEnergyStored() * 14 / tile.getMaxEnergyStored());
    }
}