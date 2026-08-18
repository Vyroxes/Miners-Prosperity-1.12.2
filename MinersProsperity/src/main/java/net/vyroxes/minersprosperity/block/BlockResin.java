package net.vyroxes.minersprosperity.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.config.ModConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = "minersprosperity")
public class BlockResin extends BlockBase
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 3);

	private static final double THICK_MIN = 15.9D / 16.0D;
	private static final double THICK_MAX = 1.0D;
	private static final double THICK_INV = 1.0D - THICK_MIN;

	private static final double X_MIN_0 = 6.0D / 16.0D;
	private static final double X_MAX_0 = 10.0D / 16.0D;
	private static final double Y_MIN_0 = 5.0D / 16.0D;
	private static final double Y_MAX_0 = 11.0D / 16.0D;

	protected static final AxisAlignedBB NORTH_AABB_0 = new AxisAlignedBB(X_MIN_0, Y_MIN_0, THICK_MIN, X_MAX_0, Y_MAX_0, THICK_MAX);
	protected static final AxisAlignedBB SOUTH_AABB_0 = new AxisAlignedBB(X_MIN_0, Y_MIN_0, 0.0D, X_MAX_0, Y_MAX_0, THICK_INV);
	protected static final AxisAlignedBB WEST_AABB_0  = new AxisAlignedBB(THICK_MIN, Y_MIN_0, X_MIN_0, THICK_MAX, Y_MAX_0, X_MAX_0);
	protected static final AxisAlignedBB EAST_AABB_0  = new AxisAlignedBB(0.0D, Y_MIN_0, X_MIN_0, THICK_INV, Y_MAX_0, X_MAX_0);

	private static final double X_MIN_1 = 10.0D / 16.0D;
	private static final double X_MAX_1 = 14.0D / 16.0D;
	private static final double X_MIN_1_B = 2.0D / 16.0D;
	private static final double X_MAX_1_B = 6.0D / 16.0D;
	private static final double Y_MIN_1 = 8.0D / 16.0D;
	private static final double Y_MAX_1 = 13.0D / 16.0D;

	protected static final AxisAlignedBB NORTH_AABB_1 = new AxisAlignedBB(X_MIN_1, Y_MIN_1, THICK_MIN, X_MAX_1, Y_MAX_1, THICK_MAX);
	protected static final AxisAlignedBB SOUTH_AABB_1 = new AxisAlignedBB(X_MIN_1_B, Y_MIN_1, 0.0D, X_MAX_1_B, Y_MAX_1, THICK_INV);
	protected static final AxisAlignedBB WEST_AABB_1  = new AxisAlignedBB(THICK_MIN, Y_MIN_1, X_MIN_1_B, THICK_MAX, Y_MAX_1, X_MAX_1_B);
	protected static final AxisAlignedBB EAST_AABB_1  = new AxisAlignedBB(0.0D, Y_MIN_1, X_MIN_1, THICK_INV, Y_MAX_1, X_MAX_1);

	private static final double X_MIN_2 = 3.0D / 16.0D;
	private static final double X_MAX_2 = 7.0D / 16.0D;
	private static final double X_MIN_2_B = 9.0D / 16.0D;
	private static final double X_MAX_2_B = 13.0D / 16.0D;
	private static final double Y_MIN_2 = 2.0D / 16.0D;
	private static final double Y_MAX_2 = 7.0D / 16.0D;

	protected static final AxisAlignedBB NORTH_AABB_2 = new AxisAlignedBB(X_MIN_2, Y_MIN_2, THICK_MIN, X_MAX_2, Y_MAX_2, THICK_MAX);
	protected static final AxisAlignedBB SOUTH_AABB_2 = new AxisAlignedBB(X_MIN_2_B, Y_MIN_2, 0.0D, X_MAX_2_B, Y_MAX_2, THICK_INV);
	protected static final AxisAlignedBB WEST_AABB_2  = new AxisAlignedBB(THICK_MIN, Y_MIN_2, X_MIN_2_B, THICK_MAX, Y_MAX_2, X_MAX_2_B);
	protected static final AxisAlignedBB EAST_AABB_2  = new AxisAlignedBB(0.0D, Y_MIN_2, X_MIN_2, THICK_INV, Y_MAX_2, X_MAX_2);

	private static final double X_MIN_3 = 9.0D / 16.0D;
	private static final double X_MAX_3 = 13.0D / 16.0D;
	private static final double X_MIN_3_B = 3.0D / 16.0D;
	private static final double X_MAX_3_B = 7.0D / 16.0D;
	private static final double Y_MIN_3 = 2.0D / 16.0D;
	private static final double Y_MAX_3 = 7.0D / 16.0D;

	protected static final AxisAlignedBB NORTH_AABB_3 = new AxisAlignedBB(X_MIN_3, Y_MIN_3, THICK_MIN, X_MAX_3, Y_MAX_3, THICK_MAX);
	protected static final AxisAlignedBB SOUTH_AABB_3 = new AxisAlignedBB(X_MIN_3_B, Y_MIN_3, 0.0D, X_MAX_3_B, Y_MAX_3, THICK_INV);
	protected static final AxisAlignedBB WEST_AABB_3  = new AxisAlignedBB(THICK_MIN, Y_MIN_3, X_MIN_3_B, THICK_MAX, Y_MAX_3, X_MAX_3_B);
	protected static final AxisAlignedBB EAST_AABB_3  = new AxisAlignedBB(0.0D, Y_MIN_3, X_MIN_3, THICK_INV, Y_MAX_3, X_MAX_3);

	public BlockResin(String name)
	{
		super(name, Material.PLANTS, MapColor.ORANGE_STAINED_HARDENED_CLAY, true);
		setSoundType(SoundType.SLIME);
		setHarvestLevel("pickaxe", 0);
		setHardness(0.2F);
		setResistance(1.0F);
		setLightLevel(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(VARIANT, 0));
	}

	@Override
	public void getSubBlocks(@NotNull CreativeTabs itemIn, @NotNull NonNullList<ItemStack> items) {}

	@Override
	public @NotNull ItemStack getPickBlock(@NotNull IBlockState state, @NotNull RayTraceResult target, @NotNull World world, @NotNull BlockPos pos, @NotNull EntityPlayer player) {
		return new ItemStack(ModItems.RESIN);
	}

	@Override
	public boolean canSilkHarvest(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public void onBlockExploded(World world, @NotNull BlockPos pos, @NotNull Explosion explosion) {
		world.setBlockToAir(pos);
	}

	@Override
	public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public int quantityDropped(@NotNull IBlockState state, int fortune, @NotNull Random random) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess worldIn, @NotNull IBlockState state, @NotNull BlockPos pos, @NotNull EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull AxisAlignedBB getBoundingBox(IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
		int v = state.getValue(VARIANT);
		EnumFacing f = state.getValue(FACING);

		if (v == 0) return pickFacingAabb(f, NORTH_AABB_0, SOUTH_AABB_0, WEST_AABB_0, EAST_AABB_0);
		if (v == 1) return pickFacingAabb(f, NORTH_AABB_1, SOUTH_AABB_1, WEST_AABB_1, EAST_AABB_1);
		if (v == 2) return pickFacingAabb(f, NORTH_AABB_2, SOUTH_AABB_2, WEST_AABB_2, EAST_AABB_2);
		return pickFacingAabb(f, NORTH_AABB_3, SOUTH_AABB_3, WEST_AABB_3, EAST_AABB_3);
	}

	private static AxisAlignedBB pickFacingAabb(EnumFacing f, AxisAlignedBB n, AxisAlignedBB s, AxisAlignedBB w, AxisAlignedBB e) {
		return switch (f) {
			case SOUTH -> s;
			case WEST -> w;
			case EAST -> e;
			default -> n;
		};
	}

	@SuppressWarnings("deprecation")
	@Override
	public @Nullable AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public @NotNull IBlockState getStateForPlacement(@NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @NotNull EntityLivingBase placer, @NotNull EnumHand hand) {
		if (facing.getAxis().isVertical()) {
			facing = placer.getHorizontalFacing();
		}

		int v = new Random(pos.toLong()).nextInt(4);
		return this.getDefaultState().withProperty(FACING, facing).withProperty(VARIANT, v);
	}

	@Override
	public boolean canPlaceBlockOnSide(@NotNull World worldIn, @NotNull BlockPos pos, EnumFacing side) {
		if (side.getAxis().isVertical()) return false;

		BlockPos attached = pos.offset(side.getOpposite());
		return worldIn.getBlockState(attached).getBlock() instanceof BlockLog;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(@NotNull IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(@NotNull IBlockState state) {
		return false;
	}

	@Override
	public boolean isPassable(@NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
		return true;
	}

	@Override
	public boolean isReplaceable(@NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(@NotNull World worldIn, @NotNull BlockPos pos) {
		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
		{
			BlockPos offset = pos.offset(facing);
			if (worldIn.getBlockState(offset).getBlock() instanceof BlockLog) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, @NotNull World worldIn, BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
		EnumFacing facing = state.getValue(FACING);
		BlockPos attached = pos.offset(facing.getOpposite());

		if (!(worldIn.getBlockState(attached).getBlock() instanceof BlockLog)) {
			worldIn.destroyBlock(pos, false);
		}
	}

	@Override
	public boolean onBlockActivated(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) return true;

		List<Item> treetaps = Arrays.asList(
				ModItems.WOODEN_TREETAP,
				ModItems.IRON_TREETAP,
				ModItems.REFINED_OBSIDIAN_TREETAP
		);

		if (!treetaps.contains(playerIn.getHeldItem(hand).getItem())) return true;

		playerIn.getHeldItem(hand).damageItem(1, playerIn);
		spawnAsEntity(worldIn, pos, new ItemStack(ModItems.RESIN));
		worldIn.playSound(null, pos, SoundEvents.BLOCK_SLIME_BREAK, SoundCategory.BLOCKS, 0.8F, 0.9F + worldIn.rand.nextFloat() * 0.15F);

		if (!ModConfig.WORLD_GENERATION.resinRegrowth)
		{
			worldIn.setBlockToAir(pos);
			return true;
		}

		IBlockState emptyState = ModBlocks.RESIN_BLOCK_EMPTY.getDefaultState().withProperty(BlockResin.FACING, state.getValue(FACING)).withProperty(BlockResin.VARIANT, state.getValue(VARIANT));
		worldIn.setBlockState(pos, emptyState, 2);

		int minTicks = Math.max(20, ModConfig.WORLD_GENERATION.resinRegrowthMinTicks);
		int maxTicks = Math.max(minTicks, ModConfig.WORLD_GENERATION.resinRegrowthMaxTicks);
		int delay = minTicks + worldIn.rand.nextInt(maxTicks - minTicks + 1);

		worldIn.scheduleUpdate(pos, ModBlocks.RESIN_BLOCK_EMPTY, delay);

		return true;
	}

	@Override
	protected @NotNull BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, VARIANT);
	}

	@SuppressWarnings("deprecation")
	@Override
	public @NotNull IBlockState getStateFromMeta(int meta) {
		int facingIndex = meta & 3;
		int variant = (meta >> 2) & 3;
		return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(facingIndex)).withProperty(VARIANT, variant);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int facingIndex = state.getValue(FACING).getHorizontalIndex();
		int variant = state.getValue(VARIANT);
		return facingIndex | (variant << 2);
	}

	@Override
	public @NotNull BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}