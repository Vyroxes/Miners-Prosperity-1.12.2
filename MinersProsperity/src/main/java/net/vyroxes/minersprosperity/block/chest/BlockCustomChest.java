package net.vyroxes.minersprosperity.block.chest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.block.BlockBase;
import net.vyroxes.minersprosperity.tile.TileEntityCustomChest;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import net.vyroxes.minersprosperity.config.ModConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockCustomChest extends BlockBase {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final AxisAlignedBB CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    private final ChestTier tier;

    public BlockCustomChest(String name, ChestTier tier) {
        super(name, Material.IRON, tier.mapColor, false);
        this.tier = tier;

        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", tier.harvestLevel);
        setHardness(tier.hardness);
        setResistance(tier.resistance);

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return CHEST_AABB;
    }

    @Override
    public @Nullable AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return CHEST_AABB;
    }

    @Override
    public @Nullable RayTraceResult collisionRayTrace(@NotNull IBlockState blockState, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Vec3d start, @NotNull Vec3d end) {
        return super.rayTrace(pos, start, end, CHEST_AABB);
    }

    public int getInventorySize() {
        return tier.inventorySize;
    }

    public int getInventoryColumns() {
        return tier.inventoryColumns;
    }

    public ResourceLocation getGuiTexture() {
        return new ResourceLocation(Tags.MODID, "textures/gui/chest_" + tier.inventorySize + ".png");
    }

    public ResourceLocation getGuiTexture2() {
        return new ResourceLocation(Tags.MODID, "textures/gui/chest_" + tier.inventorySize + "_2.png");
    }

    public ChestTier getTier() {
        return tier;
    }

    public Block getNextBlock() {
        ChestTier next = this.tier.getNext();

        if (next == null) return null;

        return switch (next) {
            case VANILLA -> Blocks.CHEST;
            case LEAD -> ModBlocks.LEAD_CHEST;
            case IRON -> ModBlocks.IRON_CHEST;
            case INCONEL -> ModBlocks.INCONEL_CHEST;
            case GOLD -> ModBlocks.GOLD_CHEST;
            case ELECTRUM -> ModBlocks.ELECTRUM_CHEST;
            case DIAMOND -> ModBlocks.DIAMOND_CHEST;
            case OBSIDIAN_CLUSTER -> ModBlocks.OBSIDIAN_CLUSTER_CHEST;
            case EMERALD -> ModBlocks.EMERALD_CHEST;
            case COBALTSTEEL -> ModBlocks.COBALTSTEEL_CHEST;
            case DARK_STEEL -> ModBlocks.DARK_STEEL_CHEST;
            case BLACK_OPAL -> ModBlocks.BLACK_OPAL_CHEST;
        };
    }

    @Override
    public boolean hasTileEntity(@NotNull IBlockState state) {
        return true;
    }

    @Override
    public @Nullable TileEntity createTileEntity(@NotNull World world, @NotNull IBlockState state) {
        return new TileEntityCustomChest(tier);
    }

    @Override
    public @NotNull IBlockState getStateForPlacement(@NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, @NotNull EnumHand hand) {
        EnumFacing placeFacing = placer.getHorizontalFacing().getOpposite();
        return this.getDefaultState().withProperty(FACING, placeFacing);
    }

    @Override
    public void onBlockPlacedBy(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityCustomChest) {
                ((TileEntityCustomChest) te).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;

        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileEntityCustomChest)) return true;

        if (!ModConfig.BLOCKS.allowOpeningCustomChest) {
            BlockPos above = pos.up();
            IBlockState stateAbove = worldIn.getBlockState(above);
            if (stateAbove.isSideSolid(worldIn, above, EnumFacing.DOWN)) return true;
        }

        playerIn.openGui(MinersProsperity.INSTANCE, GuiHandler.GuiTypes.CHEST.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }

    @Override
    public void breakBlock(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityCustomChest chest) {
            if (!chest.isUpgrading) chest.dropInventory(worldIn, pos);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public @NotNull IBlockState withRotation(@NotNull IBlockState state, @NotNull Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isPassable(@NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return false;
    }

    @Override
    public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess worldIn, @NotNull IBlockState state, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean eventReceived(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);

        TileEntity te = worldIn.getTileEntity(pos);
        return te != null && te.receiveClientEvent(id, param);
    }

    @SideOnly(Side.CLIENT)
    private IBlockState getTierParticleState() {
        return switch (this.tier) {
            case LEAD -> ModBlocks.LEAD_BLOCK.getDefaultState();
            case IRON -> Blocks.IRON_BLOCK.getDefaultState();
            case INCONEL -> ModBlocks.INCONEL_BLOCK.getDefaultState();
            case GOLD -> Blocks.GOLD_BLOCK.getDefaultState();
            case ELECTRUM -> ModBlocks.ELECTRUM_BLOCK.getDefaultState();
            case DIAMOND -> Blocks.DIAMOND_BLOCK.getDefaultState();
            case OBSIDIAN_CLUSTER -> Blocks.OBSIDIAN.getDefaultState();
            case EMERALD -> Blocks.EMERALD_BLOCK.getDefaultState();
            case COBALTSTEEL -> ModBlocks.COBALTSTEEL_BLOCK.getDefaultState();
            case DARK_STEEL -> ModBlocks.DARK_STEEL_BLOCK.getDefaultState();
            case BLACK_OPAL -> ModBlocks.BLACK_OPAL_BLOCK.getDefaultState();
            default -> Blocks.IRON_BLOCK.getDefaultState();
        };
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(@NotNull World world, @NotNull BlockPos pos, ParticleManager manager) {
        IBlockState particleState = getTierParticleState();
        manager.addBlockDestroyEffects(pos, particleState);
        return true;
    }

    public EnumActionResult tryUpgrade(World world, BlockPos pos, ItemStack stack) {
        TileEntity te = world.getTileEntity(pos);

        if (!(te instanceof TileEntityCustomChest chest)) return EnumActionResult.PASS;

        if (chest.numPlayersUsing > 0) return EnumActionResult.FAIL;

        Block nextBlock = getNextBlock();

        if (nextBlock == null) return EnumActionResult.FAIL;

        chest.isUpgrading = true;

        NBTTagCompound nbt = te.writeToNBT(new NBTTagCompound());
        ChestTier nextTier = Objects.requireNonNull(this.tier.getNext());
        nbt.setString("Tier", nextTier.name());

        if (nbt.hasKey("Items")) {
            NBTTagCompound items = nbt.getCompoundTag("Items");
            items.setInteger("Size", nextTier.inventorySize);
            nbt.setTag("Items", items);
        }

        IBlockState oldState = world.getBlockState(pos);
        IBlockState newState = nextBlock.getDefaultState().withProperty(FACING, oldState.getValue(FACING));

        world.setBlockState(pos, newState, 3);

        TileEntity newTe = world.getTileEntity(pos);

        if (newTe != null) {
            newTe.readFromNBT(nbt);
            newTe.markDirty();
        }

        world.notifyBlockUpdate(pos, oldState, newState, 3);

        stack.shrink(1);

        return EnumActionResult.SUCCESS;
    }
}