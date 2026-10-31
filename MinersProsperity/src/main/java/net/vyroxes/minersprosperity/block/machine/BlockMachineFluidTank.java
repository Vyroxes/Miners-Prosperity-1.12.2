package net.vyroxes.minersprosperity.block.machine;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.fluids.FluidUtil;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.machine.FluidTankTier;
import net.vyroxes.minersprosperity.client.gui.FluidFormatter;
import net.vyroxes.minersprosperity.tile.TileEntityFluidTank;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BlockMachineFluidTank extends BlockMachine
{
    private final FluidTankTier tier;

    public BlockMachineFluidTank(String name, FluidTankTier tier)
    {
        super(name, Material.IRON, MapColor.STONE, SoundType.METAL, MinersProsperity.blocks_tab, "pickaxe", 0, 5.0F, 10.0F, GuiHandler.GuiTypes.FLUID_TANK);
        this.tier = tier;
        ModItems.ITEMS.removeIf(item -> item instanceof ItemBlock && ((ItemBlock) item).getBlock() == this);
        ModItems.ITEMS.add(new ItemBlockFluidTank(this, tier));
    }

    public FluidTankTier getTier() { return this.tier; }

    @Override
    public TileEntityMachine getTileEntity(World world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileEntityFluidTank ? (TileEntityFluidTank) tile : null;
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World worldIn, int meta) { return new TileEntityFluidTank(this.tier); }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) { return Item.getItemFromBlock(this); }

    @Override
    public boolean onBlockActivated(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!player.isSneaking() && !player.getHeldItem(hand).isEmpty() && FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null)
        {
            if (!world.isRemote)
            {
                TileEntityMachine tile = this.getTileEntity(world, pos);
                if (tile != null)
                {
                    if (FluidUtil.getFluidContained(player.getHeldItem(hand)) != null)
                    {
                        IItemHandler inventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                        if (inventory != null)
                        {
                            FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(player.getHeldItem(hand), tile.getFluidTank(0), inventory, Integer.MAX_VALUE, player, true);
                            if (result.isSuccess()) player.setHeldItem(hand, result.getResult());
                        }
                    }
                    else FluidUtil.interactWithFluidHandler(player, hand, Objects.requireNonNull(tile.getFluidTank(0)));
                }
            }
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockClicked(@NotNull World world, @NotNull BlockPos pos, EntityPlayer player)
    {
        if (player.getHeldItemMainhand().getItem() == ModItems.WRENCH && player.getHeldItemOffhand().isEmpty())
        {
            if (!world.isRemote)
            {
                ItemStack wrench = player.getHeldItemMainhand();
                this.harvestBlock(world, player, pos, world.getBlockState(pos), world.getTileEntity(pos), wrench);
                wrench.damageItem(1, player);
                world.playSound(null, pos, net.minecraft.init.SoundEvents.BLOCK_METAL_BREAK, net.minecraft.util.SoundCategory.BLOCKS, 1, 1);
            }
            return;
        }
        super.onBlockClicked(world, pos, player);
    }

    @Override
    public boolean removedByPlayer(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull EntityPlayer player, boolean willHarvest)
    {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(@NotNull World world, @NotNull EntityPlayer player, @NotNull BlockPos pos, @NotNull IBlockState state, TileEntity tile, @NotNull ItemStack tool)
    {
        this.breakBlock(world, pos, state);
        super.harvestBlock(world, player, pos, state, tile, tool);
        world.setBlockToAir(pos);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flag)
    {
        FluidStack fluid = FluidUtil.getFluidContained(stack);
        if (fluid != null && fluid.amount > 0) tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid") + TextFormatting.GRAY + ": " + fluid.getLocalizedName());
        if (tier == FluidTankTier.CREATIVE)
        {
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_stored") + TextFormatting.GRAY + ": " + I18n.format("gui.energy_infinite"));
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_transfer") + TextFormatting.GRAY + ": " + I18n.format("gui.energy_infinite"));
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.fluid_tank.creative"));
        }
        else
        {
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_stored") + TextFormatting.GRAY + ": " + FluidFormatter.capacity(fluid == null ? 0 : fluid.amount, this.tier.capacity));
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_transfer") + TextFormatting.GRAY + ": " + FluidFormatter.transfer(this.tier.transfer));
        }
    }

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
    public int getLightValue(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) { return 0; }

    @Override
    public boolean hasComparatorInputOverride(@NotNull IBlockState state) { return true; }

    @Override
    public int getComparatorInputOverride(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos)
    {
        TileEntityMachine tile = this.getTileEntity(world, pos);
        if (tile == null || Objects.requireNonNull(tile.getFluidTank(0)).getFluidAmount() == 0) return 0;
        return 1 + (int) ((long) Objects.requireNonNull(tile.getFluidTank(0)).getFluidAmount() * 14 / Objects.requireNonNull(tile.getFluidTank(0)).getCapacity());
    }
}