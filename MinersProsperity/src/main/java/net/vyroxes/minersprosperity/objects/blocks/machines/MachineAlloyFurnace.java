package net.vyroxes.minersprosperity.objects.blocks.machines;

import java.util.Random;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.objects.blocks.BlockBase;
import net.vyroxes.minersprosperity.objects.containers.ContainerAlloyFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.SidedItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MachineAlloyFurnace extends BlockBase implements ITileEntityProvider
{	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	private static boolean keepInventory;
	
    public MachineAlloyFurnace(String name)
    {
        super(name, Material.IRON, MapColor.STONE);
        setSoundType(SoundType.STONE);
		setCreativeTab(MinersProsperity.blocks_tab);
		setHarvestLevel("pickaxe", 0);
		setHardness(5.0F);
		setResistance(10.0F);
		
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
    }

	@Override
	public int getLightValue(IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos)
	{
		return state.getValue(POWERED) ? 13 : 0;
	}

	@Override
	public void harvestBlock(@NotNull World worldIn, @NotNull EntityPlayer player, @NotNull BlockPos pos, @NotNull IBlockState state, @Nullable TileEntity tileEntity, @NotNull ItemStack stack)
	{
		if (tileEntity != null)
		{
			NBTTagCompound tileEntityData = new NBTTagCompound();
			tileEntity.writeToNBT(tileEntityData);
			worldIn.removeTileEntity(pos);

			Item itemDropped = this.getItemDropped(state, worldIn.rand, 0);
            ItemStack blockStack = new ItemStack(itemDropped, 1, this.damageDropped(state));
            NBTTagCompound tag = blockStack.getTagCompound();
            if (tag == null)
            {
                tag = new NBTTagCompound();
                blockStack.setTagCompound(tag);
            }

			for (String key : tileEntityData.getKeySet())
			{
				if (key.equals("Energy") || key.equals("States"))
				{
					tag.setTag(key, tileEntityData.getTag(key));
				}
			}

            spawnAsEntity(worldIn, pos, blockStack);
		}
	}

	@Override
	public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune)
	{
		return Item.getItemFromBlock(BlockInit.ALLOY_FURNACE);
	}

	@Override
	public void onBlockPlacedBy(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityLivingBase placer, @NotNull ItemStack stack)
	{
		NBTTagCompound tag = stack.getTagCompound();

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tag != null && tileEntity instanceof TileEntityAlloyFurnace alloyFurnace)
		{
			if (tag.hasKey("Energy"))
			{
				alloyFurnace.setEnergyStored(tag.getInteger("Energy"));
			}

			if (tag.hasKey("States"))
			{
				NBTTagCompound statesTag = tag.getCompoundTag("States");

				if (statesTag.hasKey("RedstoneControlButtonState"))
				{
					alloyFurnace.setRedstoneControlButtonState(statesTag.getInteger("RedstoneControlButtonState"));
				}

				for (EnumFacing facing : EnumFacing.values())
				{
					String facingName = facing.getName();

					SidedItemHandler sidedHandler = (SidedItemHandler) alloyFurnace.getSidedItemHandler(facing);

					if (statesTag.hasKey(facingName + "Inputs"))
					{
						NBTTagList inputList = statesTag.getTagList(facingName + "Inputs", Constants.NBT.TAG_COMPOUND);
						for (int i = 0; i < inputList.tagCount(); i++)
						{
							NBTTagCompound slotsTag = inputList.getCompoundTagAt(i);
							sidedHandler.getInputs()[i] = SidedItemHandler.SlotState.valueOf(slotsTag.getString("state"));
						}
					}

					if (statesTag.hasKey(facingName + "Outputs"))
					{
						NBTTagList outputList = statesTag.getTagList(facingName + "Outputs", Constants.NBT.TAG_COMPOUND);
						for (int i = 0; i < outputList.tagCount(); i++)
						{
							NBTTagCompound slotsTag = outputList.getCompoundTagAt(i);
							sidedHandler.getOutputs()[i] = SidedItemHandler.SlotState.valueOf(slotsTag.getString("state"));
						}
					}
				}
			}
		}

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public void breakBlock(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state)
	{
		if (!keepInventory)
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity != null && tileentity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
			{
				IItemHandler itemHandler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				if (itemHandler != null)
				{
					for (int i = 0; i < itemHandler.getSlots(); i++)
					{
						ItemStack stack = itemHandler.getStackInSlot(i);
						if (!stack.isEmpty())
						{
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
	public void onBlockAdded(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state)
	{
		this.setDefaultFacing(worldIn, pos, state);
	}
	
	public void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) 
	{
		if (!worldIn.isRemote) 
        {
            IBlockState north = worldIn.getBlockState(pos.north());
            IBlockState south = worldIn.getBlockState(pos.south());
            IBlockState west = worldIn.getBlockState(pos.west());
            IBlockState east = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock())
        	{
        		enumfacing = EnumFacing.SOUTH;
        	}
            else if (enumfacing == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock())
            {
            	enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock())
            {
            	enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock())
            {
            	enumfacing = EnumFacing.WEST;
            }
            
            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing).withProperty(POWERED, state.getValue(POWERED)), 2);
        }
	}
	
	@SideOnly(Side.CLIENT)
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(IBlockState stateIn, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Random rand)
    {
		if (stateIn.getValue(POWERED))
        {
            EnumFacing enumfacing = stateIn.getValue(FACING);
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D)
            {
                worldIn.playSound((double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch (enumfacing)
            {
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
	public boolean onBlockActivated(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			if (playerIn.openContainer instanceof ContainerAlloyFurnace)
			{
				return false;
			}

			playerIn.openGui(MinersProsperity.instance, GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}
	
	public static boolean getStatePowered(World worldIn, BlockPos pos)
	{
	    IBlockState state = worldIn.getBlockState(pos);
	    return state.getValue(POWERED);
	}
	
	public static void setStatePowered(boolean powered, World worldIn, BlockPos pos)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;
		
		worldIn.setBlockState(pos, BlockInit.ALLOY_FURNACE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(POWERED, powered), 3);
		
        keepInventory = false;
		
		if(tileentity != null) 
		{
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}
	
	@Override
	public boolean hasTileEntity(@NotNull IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(@NotNull World worldIn, int meta)
	{
		return new TileEntityAlloyFurnace();
	}

	private TileEntityAlloyFurnace getTileEntity(World world, BlockPos pos)
	{
		return (TileEntityAlloyFurnace) world.getTileEntity(pos);
	}

	@Override
	public @NotNull IBlockState getStateForPlacement(@NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, @NotNull EnumHand hand)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public @NotNull IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}
	
	@Override
	protected @NotNull BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, POWERED, FACING);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public @NotNull IBlockState getStateFromMeta(int meta)
	{
		EnumFacing facing = EnumFacing.byIndex(meta & 7);
	    boolean powered = (meta & 8) != 0;

	    if (facing.getAxis() == EnumFacing.Axis.Y) 
	    {
	        facing = EnumFacing.NORTH;
	    }

	    return this.getDefaultState().withProperty(FACING, facing).withProperty(POWERED, powered);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
	    int meta = (state.getValue(FACING)).getIndex();
	    
		if (state.getValue(POWERED))
	    {
	        meta |= 8;
	    }
	    return meta;
	}
}