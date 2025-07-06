package net.vyroxes.minersprosperity.objects.blocks.machines;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.objects.blocks.BlockBase;
import net.vyroxes.minersprosperity.objects.containers.ContainerAlloyFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.util.annotations.NonnullByDefault;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.SidedIngredientHandler;
import net.vyroxes.minersprosperity.util.handlers.SlotState;
import org.jetbrains.annotations.NotNull;
import java.util.Random;

public abstract class Machine extends BlockBase implements ITileEntityProvider {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    private final GuiHandler.GuiTypes guiTypes;
    private static boolean keepInventory;


    public Machine(String name, Material material, MapColor mapColor, SoundType soundType, CreativeTabs creativeTabs, String harvestTool, int harvestLevel, float hardness, float resistance, GuiHandler.GuiTypes guiTypes) {
        super(name, material, mapColor);
        setSoundType(soundType);
        setCreativeTab(creativeTabs);
        setHarvestLevel(harvestTool, harvestLevel);
        setHardness(hardness);
        setResistance(resistance);
        this.guiTypes = guiTypes;

        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
    }

    public static boolean getStatePowered(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        return state.getValue(POWERED);
    }

    public static void setStatePowered(boolean powered, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;

        worldIn.setBlockState(pos, iblockstate.withProperty(POWERED, powered), 3);

        keepInventory = false;

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    @NonnullByDefault
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(POWERED) ? 13 : 0;
    }

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
                            for (int slot = 0; slot < sidedIngredientHandler.getSlots(); slot++) {
                                sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.DISABLED);
                            }
                        }
                    }
                }
            }
        }

        super.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    @NonnullByDefault
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItemMainhand().isEmpty() && playerIn.getHeldItemOffhand().isEmpty()) {
            if (playerIn.isSneaking()) {
                TileEntityMachine tileEntity = getTileEntity(worldIn, pos);
                if (tileEntity != null) {
                    if (tileEntity.getSidedIngredientHandlers() != null) {
                        EnumFacing side = tileEntity.getRelativeSide(tileEntity.getMachineFacing(), facing);
                        SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(side);
                        for (int slot = 0; slot < sidedIngredientHandler.getSlots(); slot++) {
                            sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.DISABLED);
                        }
                    }
                }
            }
        }

        if (!worldIn.isRemote && this.guiTypes != null)
        {
            if (playerIn.openContainer != playerIn.inventoryContainer)
            {
                return false;
            }

            playerIn.openGui(MinersProsperity.INSTANCE, this.guiTypes.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
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

            for (String key : tileEntityData.getKeySet()) {
                if (key.equals("Energy") || key.equals("FluidName") || key.equals("Amount") || key.equals("States")) {
                    tag.setTag(key, tileEntityData.getTag(key));
                }
            }

            spawnAsEntity(worldIn, pos, blockStack);
        }
    }

    @Override
    @NonnullByDefault
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tag != null && tileEntity instanceof TileEntityMachine tileEntityMachine) {

            if (tag.hasKey("Energy")) {
                if (tileEntityMachine.getEnergyStorage() != null) tileEntityMachine.setEnergyStored(tag.getInteger("Energy"));
            }

            if (tag.hasKey("FluidName") && tag.hasKey("Amount")) {
                if (tileEntityMachine.getFluidTank() != null) tileEntityMachine.setFluidStored(FluidRegistry.getFluid(tag.getString("FluidName")), tag.getInteger("Amount"));
            }

            if (tag.hasKey("States")) {
                NBTTagCompound statesTag = tag.getCompoundTag("States");

                if (statesTag.hasKey("RedstoneControlButtonState")) {
                    tileEntityMachine.setRedstoneControlButtonState(statesTag.getInteger("RedstoneControlButtonState"));
                }

                if (tileEntityMachine.getSidedIngredientHandlers() != null) {
                    for (EnumFacing facing : EnumFacing.values()) {
                        if (statesTag.hasKey(facing.toString() + "SlotStates")) {
                            SidedIngredientHandler sidedHandler = (SidedIngredientHandler) ((TileEntityMachine) tileEntity).getSidedIngredientHandler(facing);

                            sidedHandler.readFromNBT(statesTag);
                        }
                    }
                }
            }
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
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
                        if (!stack.isEmpty()) {
                            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
                        }
                    }
                }
            }

            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @NonnullByDefault
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
    }

    public void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            IBlockState north = worldIn.getBlockState(pos.north());
            IBlockState south = worldIn.getBlockState(pos.south());
            IBlockState west = worldIn.getBlockState(pos.west());
            IBlockState east = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) {
                enumfacing = EnumFacing.SOUTH;
            } else if (enumfacing == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) {
                enumfacing = EnumFacing.NORTH;
            } else if (enumfacing == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            } else if (enumfacing == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing).withProperty(POWERED, state.getValue(POWERED)), 2);
        }
    }

    @SideOnly(Side.CLIENT)
    @NonnullByDefault
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(POWERED)) {
            EnumFacing enumfacing = stateIn.getValue(FACING);
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double) pos.getZ() + 0.5D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

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
    public @NotNull IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    @NonnullByDefault
    @SuppressWarnings("deprecation")
    public @NotNull EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED, FACING);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byIndex(meta & 7);
        boolean powered = (meta & 8) != 0;

        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, facing).withProperty(POWERED, powered);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = (state.getValue(FACING)).getIndex();

        if (state.getValue(POWERED)) {
            meta |= 8;
        }
        return meta;
    }

    public abstract TileEntityMachine getTileEntity(World world, BlockPos pos);
}