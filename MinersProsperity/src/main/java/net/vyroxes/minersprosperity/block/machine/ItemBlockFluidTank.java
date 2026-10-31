package net.vyroxes.minersprosperity.block.machine;

import net.minecraft.item.ItemBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.block.BlockLiquid;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.vyroxes.minersprosperity.machine.FluidTankTier;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class ItemBlockFluidTank extends ItemBlock
{
    private final FluidTankTier tier;

    public ItemBlockFluidTank(BlockMachineFluidTank block, FluidTankTier tier)
    {
        super(block);
        this.tier = tier;
        this.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        this.setMaxStackSize(1);
    }

    @Override
    public @NotNull EnumActionResult onItemUseFirst(@NotNull EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing side, float hitX, float hitY, float hitZ, @NotNull EnumHand hand)
    {
        if (player.isSneaking()) return this.placeFluid(player, world, pos, side, hand);
        RayTraceResult hit = this.rayTrace(world, player, true);
        if (hit == null || hit.typeOfHit != RayTraceResult.Type.BLOCK) return EnumActionResult.PASS;
        return this.pickUpFluid(player, world, hit.getBlockPos(), hit.sideHit, hand);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, @NotNull EntityPlayer player, @NotNull EnumHand hand)
    {
        RayTraceResult hit = this.rayTrace(world, player, !player.isSneaking());
        if (hit == null || hit.typeOfHit != RayTraceResult.Type.BLOCK) return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
        EnumActionResult result = player.isSneaking() ? this.placeFluid(player, world, hit.getBlockPos(), hit.sideHit, hand)
                : this.pickUpFluid(player, world, hit.getBlockPos(), hit.sideHit, hand);
        return new ActionResult<>(result, player.getHeldItem(hand));
    }

    private EnumActionResult pickUpFluid(EntityPlayer player, World world, BlockPos pos, EnumFacing side, EnumHand hand)
    {
        net.minecraft.block.Block block = world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockLiquid) && !(block instanceof IFluidBlock)) return EnumActionResult.PASS;
        ItemStack held = player.getHeldItem(hand);
        if (!world.isBlockModifiable(player, pos) || !player.canPlayerEdit(pos, side, held)) return EnumActionResult.FAIL;
        if (world.isRemote) return EnumActionResult.SUCCESS;
        net.minecraftforge.fluids.capability.IFluidHandler source = FluidUtil.getFluidHandler(world, pos, side);
        net.minecraftforge.fluids.capability.IFluidHandlerItem container = FluidUtil.getFluidHandler(held.copy());
        FluidStack bucket = source == null ? null : source.drain(Fluid.BUCKET_VOLUME, false);
        if (container == null || bucket == null || bucket.amount != Fluid.BUCKET_VOLUME || container.fill(bucket, false) != bucket.amount) return EnumActionResult.FAIL;
        FluidActionResult result = FluidUtil.tryPickUpFluid(held, player, world, pos, side);
        if (!result.isSuccess()) return EnumActionResult.FAIL;
        player.setHeldItem(hand, result.getResult());
        player.inventory.markDirty();
        return EnumActionResult.SUCCESS;
    }

    private EnumActionResult placeFluid(EntityPlayer player, World world, BlockPos pos, EnumFacing side, EnumHand hand)
    {
        ItemStack held = player.getHeldItem(hand);
        FluidStack fluid = FluidUtil.getFluidContained(held);
        if (fluid == null || fluid.amount <= 0) return EnumActionResult.PASS;
        if (fluid.amount < Fluid.BUCKET_VOLUME || !fluid.getFluid().canBePlacedInWorld()) return EnumActionResult.FAIL;
        BlockPos target = world.getBlockState(pos).getBlock().isReplaceable(world, pos) && side == EnumFacing.UP ? pos : pos.offset(side);
        if (!world.isBlockModifiable(player, target) || !player.canPlayerEdit(target, side, held)) return EnumActionResult.FAIL;
        if (world.isRemote) return EnumActionResult.SUCCESS;
        FluidStack bucket = fluid.copy();
        bucket.amount = Fluid.BUCKET_VOLUME;
        FluidActionResult result = FluidUtil.tryPlaceFluid(null, world, target, held, bucket);
        if (!result.isSuccess()) return EnumActionResult.FAIL;
        if (!player.capabilities.isCreativeMode) player.setHeldItem(hand, result.getResult());
        player.inventory.markDirty();
        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean showDurabilityBar(@NotNull ItemStack stack)
    {
        FluidStack fluid = FluidUtil.getFluidContained(stack);
        return fluid != null && fluid.amount > 0;
    }

    @Override
    public double getDurabilityForDisplay(@NotNull ItemStack stack)
    {
        FluidStack fluid = FluidUtil.getFluidContained(stack);
        return fluid == null ? 1.0D : 1.0D - MathHelper.clamp((double) fluid.amount / this.tier.capacity, 0.0D, 1.0D);
    }

    @Override
    public int getRGBDurabilityForDisplay(@NotNull ItemStack stack) { return 0x3399FF; }

    @Override
    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new FluidHandlerItemStack(stack, this.tier.capacity)
        {
            @Override
            public FluidStack getFluid()
            {
                NBTTagCompound tag = this.container.getTagCompound();
                if (tag == null) return null;
                FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag.getTagList("FluidTanks", 10).getCompoundTagAt(0));
                if (fluid != null) fluid.amount = tier == FluidTankTier.CREATIVE ? this.capacity : Math.min(fluid.amount, this.capacity);
                return fluid;
            }

            @Override
            protected void setFluid(FluidStack fluid)
            {
                if (!this.container.hasTagCompound()) this.container.setTagCompound(new NBTTagCompound());
                NBTTagCompound tankTag = new NBTTagCompound();
                if (fluid != null)
                {
                    fluid = fluid.copy();
                    if (tier == FluidTankTier.CREATIVE) fluid.amount = this.capacity;
                    fluid.writeToNBT(tankTag);
                }
                NBTTagList tanks = new NBTTagList();
                tanks.appendTag(tankTag);
                this.container.getTagCompound().setTag("FluidTanks", tanks);
            }

            @Override
            protected void setContainerToEmpty() { this.setFluid(null); }

            @Override
            public FluidStack drain(int maxDrain, boolean doDrain)
            {
                return super.drain(maxDrain, doDrain && tier != FluidTankTier.CREATIVE);
            }
        };
    }
}