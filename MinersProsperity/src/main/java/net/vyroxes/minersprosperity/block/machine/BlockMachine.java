package net.vyroxes.minersprosperity.block.machine;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.block.BlockBase;
import net.vyroxes.minersprosperity.fluid.CustomFluidTank;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import net.vyroxes.minersprosperity.annotation.NonnullByDefault;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import org.jetbrains.annotations.NotNull;
import java.util.Random;

public abstract class BlockMachine extends BlockBase implements ITileEntityProvider {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<MachineState> STATE = PropertyEnum.create("state", MachineState.class);

    private final GuiHandler.GuiTypes guiTypes;
    private static boolean keepInventory;

    public enum MachineState implements IStringSerializable
    {
        OFF("off"),
        POWERED("powered"),
        ACTIVE("active");

        private final String name;

        MachineState(String name) { this.name = name; }

        @Override
        public @NotNull String getName() { return this.name; }

        public static MachineState byMeta(int meta)
        {
            return switch (meta)
            {
                case 1 -> POWERED;
                case 2 -> ACTIVE;
                default -> OFF;
            };
        }
    }

    public BlockMachine(String name, Material material, MapColor mapColor, SoundType soundType, CreativeTabs creativeTabs, String harvestTool, int harvestLevel, float hardness, float resistance, GuiHandler.GuiTypes guiTypes) {
        super(name, material, mapColor, true);
        setSoundType(soundType);
        setCreativeTab(creativeTabs);
        setHarvestLevel(harvestTool, harvestLevel);
        setHardness(hardness);
        setResistance(resistance);
        this.guiTypes = guiTypes;

        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(STATE, MachineState.OFF));
    }

    public static MachineState getMachineState(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);

        if (!(state.getBlock() instanceof BlockMachine)) return MachineState.OFF;

        return state.getValue(STATE);
    }

    public static void setMachineState(MachineState machineState, World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);

        if (!(state.getBlock() instanceof BlockMachine)) return;

        if (state.getValue(STATE) == machineState) return;

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        keepInventory = true;

        worldIn.setBlockState(pos, state.withProperty(STATE, machineState), 3);

        keepInventory = false;

        if (tileEntity != null)
        {
            tileEntity.validate();
            worldIn.setTileEntity(pos, tileEntity);
        }
    }

    @Override
    @NonnullByDefault
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) { return state.getValue(STATE) == MachineState.ACTIVE ? 13 : 0; }

    @Override
    @NonnullByDefault
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if (playerIn.getHeldItemMainhand().isEmpty() && playerIn.getHeldItemOffhand().isEmpty()) {
            if (playerIn.isSneaking()) {
                TileEntityMachine tileEntity = getTileEntity(worldIn, pos);
                if (tileEntity != null) {
                    if (tileEntity.getSidedIngredientHandlers() != null) {
                        for (EnumFacing face : EnumFacing.values()) {
                            SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
                            for (int slot = 0; slot < sidedIngredientHandler.getStateCount(); slot++) sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.DISABLED);
                        }
                    }
                }
            }
        }
        else if (playerIn.getHeldItemMainhand().getItem() == ModItems.WRENCH && playerIn.getHeldItemOffhand().isEmpty())
        {
            if (!worldIn.isRemote)
            {
                ItemStack wrench = playerIn.getHeldItemMainhand();
                breakBlock(worldIn, pos, worldIn.getBlockState(pos));
                harvestBlock(worldIn, playerIn, pos, worldIn.getBlockState(pos), worldIn.getTileEntity(pos), playerIn.getHeldItemMainhand());
                worldIn.setBlockToAir(pos);
                wrench.damageItem(1, playerIn);
                worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }

        super.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    @NonnullByDefault
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!playerIn.isSneaking() && !playerIn.getHeldItemMainhand().isEmpty() && playerIn.getHeldItemMainhand().getItem() != ModItems.WRENCH && playerIn.getHeldItemOffhand().isEmpty())
        {
            TileEntityMachine tileEntity = getTileEntity(worldIn, pos);

            if (tileEntity != null && tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
            {
                ItemStack held = playerIn.getHeldItem(hand);
                if (!held.isEmpty()) if (tryFillContainer(playerIn, hand, tileEntity)) return true;
            }
        }
        else if (playerIn.isSneaking() && playerIn.getHeldItemMainhand().isEmpty() && playerIn.getHeldItemOffhand().isEmpty())
        {
            TileEntityMachine tileEntity = getTileEntity(worldIn, pos);
            if (tileEntity != null) {
                if (tileEntity.getSidedIngredientHandlers() != null) {
                    EnumFacing side = tileEntity.getRelativeSide(tileEntity.getMachineFacing(), facing);
                    SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(side);
                    for (int slot = 0; slot < sidedIngredientHandler.getStateCount(); slot++) sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.DISABLED);

                    return true;
                }
            }
        }
        else if (playerIn.getHeldItemMainhand().getItem() == ModItems.WRENCH && playerIn.getHeldItemOffhand().isEmpty())
        {
            ItemStack wrench = playerIn.getHeldItemMainhand();
            if (facing.getAxis().isHorizontal())
            {
                IBlockState newState = state.withProperty(FACING, facing);
                worldIn.setBlockState(pos, newState, 3);
                worldIn.notifyBlockUpdate(pos, state, newState, 3);
                wrench.damageItem(1, playerIn);
            }
            else
            {
                EnumFacing currentFacing = state.getValue(FACING);
                EnumFacing nextFacing = currentFacing.rotateY();
                IBlockState newState = state.withProperty(FACING, nextFacing);
                worldIn.setBlockState(pos, newState, 3);
                worldIn.notifyBlockUpdate(pos, state, newState, 3);
                wrench.damageItem(1, playerIn);
            }
            return true;
        }

        if (!worldIn.isRemote && this.guiTypes != null)
        {
            if (playerIn.openContainer != playerIn.inventoryContainer) return false;
            playerIn.openGui(MinersProsperity.INSTANCE, this.guiTypes.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    private boolean tryFillContainer(EntityPlayer player, EnumHand hand, TileEntityMachine tileEntity)
    {
        ItemStack held = player.getHeldItem(hand);

        IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

        if (fluidHandler == null) return false;

        FluidActionResult result = FluidUtil.tryFillContainer(held, fluidHandler, Integer.MAX_VALUE, player, true);

        if (result.isSuccess())
        {
            player.setHeldItem(hand, result.getResult());
            tileEntity.markDirty();
            return true;
        }

        return false;
    }

    @Override
    @NonnullByDefault
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tag != null && tileEntity instanceof TileEntityMachine tileEntityMachine) {

            if (tag.hasKey("Energy")) if (tileEntityMachine.getEnergyStorage() != null) tileEntityMachine.setEnergyStored(tag.getInteger("Energy"));

            if (tag.hasKey("FluidTanks", 9))
            {
                NBTTagList tanksTag = tag.getTagList("FluidTanks", 10);

                CustomFluidTank[] fluidTanks = tileEntityMachine.getFluidTanks();

                int count = Math.min(tanksTag.tagCount(), fluidTanks.length);

                for (int i = 0; i < count; i++) fluidTanks[i].readFromNBT(tanksTag.getCompoundTagAt(i));
            }

            if (tag.hasKey("States")) {
                NBTTagCompound statesTag = tag.getCompoundTag("States");

                if (statesTag.hasKey("RedstoneControlButtonState")) tileEntityMachine.setRedstoneControlButtonState(statesTag.getInteger("RedstoneControlButtonState"));

                if (tileEntityMachine.getSidedIngredientHandlers() != null) {
                    for (EnumFacing facing : EnumFacing.values()) {
                        if (statesTag.hasKey(facing.toString() + "SlotStates")) {
                            SidedIngredientHandler sidedHandler = (SidedIngredientHandler) tileEntityMachine.getSidedIngredientHandler(facing);

                            sidedHandler.readFromNBT(statesTag);
                        }
                    }
                }
            }
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void harvestBlock(@NotNull World worldIn, @NotNull EntityPlayer player, @NotNull BlockPos pos, @NotNull IBlockState state, TileEntity tileEntity, @NotNull ItemStack stack) {
        if (tileEntity != null) {
            NBTTagCompound tileEntityData = new NBTTagCompound();
            tileEntity.writeToNBT(tileEntityData);
            worldIn.removeTileEntity(pos);
            Item itemDropped = this.getItemDropped(state, worldIn.rand, 0);
            ItemStack blockStack = new ItemStack(itemDropped, 1, this.damageDropped(state));
            NBTTagCompound tag = blockStack.getTagCompound();

            if (tag == null) {
                tag = new NBTTagCompound();
                blockStack.setTagCompound(tag);
            }

            for (String key : tileEntityData.getKeySet()) if (key.equals("Energy") || key.equals("FluidTanks") || key.equals("States")) tag.setTag(key, tileEntityData.getTag(key));

            spawnAsEntity(worldIn, pos, blockStack);
        }
    }

    @Override
    @NonnullByDefault
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!keepInventory) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity != null && tileentity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                IItemHandler itemHandler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (itemHandler != null) {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack stack = itemHandler.getStackInSlot(i);
                        if (!stack.isEmpty()) InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                    }
                }
            }

            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @NonnullByDefault
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) { this.setDefaultFacing(worldIn, pos, state); }

    public void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            IBlockState north = worldIn.getBlockState(pos.north());
            IBlockState south = worldIn.getBlockState(pos.south());
            IBlockState west = worldIn.getBlockState(pos.west());
            IBlockState east = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) enumfacing = EnumFacing.SOUTH;
            else if (enumfacing == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) enumfacing = EnumFacing.NORTH;
            else if (enumfacing == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) enumfacing = EnumFacing.EAST;
            else if (enumfacing == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) enumfacing = EnumFacing.WEST;

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }

    @SideOnly(Side.CLIENT)
    @NonnullByDefault
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(STATE) == MachineState.ACTIVE) {
            EnumFacing enumfacing = stateIn.getValue(FACING);
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double) pos.getZ() + 0.5D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            switch (enumfacing) {
                case WEST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case EAST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    @NonnullByDefault
    public @NotNull IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) { return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()); }

    @Override
    @NonnullByDefault
    @SuppressWarnings("deprecation")
    public @NotNull EnumBlockRenderType getRenderType(IBlockState state) { return EnumBlockRenderType.MODEL; }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull IBlockState withRotation(IBlockState state, Rotation rot) { return state.withProperty(FACING, rot.rotate(state.getValue(FACING))); }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull IBlockState withMirror(IBlockState state, Mirror mirrorIn) { return state.withRotation(mirrorIn.toRotation(state.getValue(FACING))); }

    @Override
    protected @NotNull BlockStateContainer createBlockState() { return new BlockStateContainer(this, STATE, FACING); }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3);
        MachineState machineState = MachineState.byMeta((meta >> 2) & 3);
        if (facing.getAxis() == EnumFacing.Axis.Y) facing = EnumFacing.NORTH;
        return this.getDefaultState().withProperty(FACING, facing).withProperty(STATE, machineState);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getHorizontalIndex();
        meta |= state.getValue(STATE).ordinal() << 2;
        return meta;
    }

    public abstract TileEntityMachine getTileEntity(World world, BlockPos pos);
}