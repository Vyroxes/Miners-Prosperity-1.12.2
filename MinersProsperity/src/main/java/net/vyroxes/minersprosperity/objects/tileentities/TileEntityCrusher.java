package net.vyroxes.minersprosperity.objects.tileentities;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.Reference;
import net.vyroxes.minersprosperity.objects.blocks.machines.Crusher;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesCrusher;
import net.vyroxes.minersprosperity.objects.containers.ContainerCrusher;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;

public class TileEntityCrusher extends TileEntity implements ITickable
{
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
    private ItemStackHandler crusherItemStacks = new ItemStackHandler(4);
    private int crusherBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String customName;
    public int buttonState;
    public int northState;
    public int southState;
    public int eastState;
    public int westState;
    public int upState;
    public int downState;
    public int currentGuiId = GuiHandler.GUI_CRUSHER;
    
    public int getButtonState()
    {
    	return this.buttonState;
    }
    
    public void addButtonState()
    {
		if (this.buttonState < 2)
    	{
    		++this.buttonState;
    	}
    	else
    	{
    		this.buttonState = 0;
    	}
		
		this.markDirty();
		
		NetworkHandler.sendButtonStateUpdate(this.buttonState, this.pos);
    }

    public int getFaceState(int face)
    {
        if (face == 0)
        {
            return this.northState;
        }
        else if (face == 1)
        {
            return this.southState;
        }
        else if (face == 2)
        {
            return this.eastState;
        }
        else if (face == 3)
        {
            return this.westState;
        }
        else if (face == 4)
        {
            return this.upState;
        }
        else if (face == 5)
        {
            return this.downState;
        }
        return face;
    }

    public void addFaceState(int face)
    {
        if (face == 0)
        {
            if (this.northState < 4)
            {
                ++this.northState;
            }
            else
            {
                this.northState = 0;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.northState, this.pos);
        }
        else if (face == 1)
        {
            if (this.southState < 4)
            {
                ++this.southState;
            }
            else
            {
                this.southState = 0;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.southState, this.pos);
        }
        else if (face == 2)
        {
            if (this.eastState < 4)
            {
                ++this.eastState;
            }
            else
            {
                this.eastState = 0;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.eastState, this.pos);
        }
        else if (face == 3)
        {
            if (this.westState < 4)
            {
                ++this.westState;
            }
            else
            {
                this.westState = 0;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.westState, this.pos);
        }
        else if (face == 4)
        {
            if (this.upState < 4)
            {
                ++this.upState;
            }
            else
            {
                this.upState = 0;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.upState, this.pos);
        }
        else if (face == 5)
        {
            if (this.downState < 4)
            {
                ++this.downState;
            }
            else
            {
                this.downState = 0;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.downState, this.pos);
        }

        this.markDirty();
    }

    public void subFaceState(int face)
    {
        if (face == 0)
        {
            if (this.northState > 0)
            {
                --this.northState;
            }
            else
            {
                this.northState = 4;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.northState, this.pos);
        }
        else if (face == 1)
        {
            if (this.southState > 0)
            {
                --this.southState;
            }
            else
            {
                this.southState = 4;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.southState, this.pos);
        }
        else if (face == 2)
        {
            if (this.eastState > 0)
            {
                --this.eastState;
            }
            else
            {
                this.eastState = 4;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.eastState, this.pos);
        }
        else if (face == 3)
        {
            if (this.westState > 0)
            {
                --this.westState;
            }
            else
            {
                this.westState = 4;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.westState, this.pos);
        }
        else if (face == 4)
        {
            if (this.upState > 0)
            {
                --this.upState;
            }
            else
            {
                this.upState = 4;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.upState, this.pos);
        }
        else if (face == 5)
        {
            if (this.downState > 0)
            {
                --this.downState;
            }
            else
            {
                this.downState = 4;
            }
            NetworkHandler.sendFaceStateUpdate(face, this.downState, this.pos);
        }

        this.markDirty();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
        {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.crusherItemStacks);
        }
        return super.getCapability(capability, facing);
    }
    
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.crusher");
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
    {
        this.readFromNBT(pkt.getNbtCompound());
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() 
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.pos, 1, tag);
    }
    
    @Override
    public NBTTagCompound getUpdateTag() 
    {
        NBTTagCompound tag = super.writeToNBT(new NBTTagCompound());
        tag.setTag("Inventory", this.crusherItemStacks.serializeNBT());
        tag.setInteger("BurnTime", this.crusherBurnTime);
        tag.setInteger("CookTime", this.cookTime);
        tag.setInteger("TotalCookTime", this.totalCookTime);
        tag.setInteger("CurrentItemBurnTime", this.currentItemBurnTime);
        tag.setInteger("ButtonState", this.buttonState);
        tag.setInteger("CurrentGuiId", this.currentGuiId);
        tag.setInteger("NorthState", this.northState);
        tag.setInteger("SouthState", this.southState);
        tag.setInteger("EastState", this.eastState);
        tag.setInteger("WestState", this.westState);
        tag.setInteger("UpState", this.upState);
        tag.setInteger("DownState", this.downState);
        return tag;
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setTag("Inventory", this.crusherItemStacks.serializeNBT());
        tag.setInteger("BurnTime", this.crusherBurnTime);
        tag.setInteger("CookTime", this.cookTime);
        tag.setInteger("TotalCookTime", this.totalCookTime);
        tag.setInteger("CurrentItemBurnTime", this.currentItemBurnTime);
        tag.setInteger("ButtonState", this.buttonState);
        tag.setInteger("CurrentGuiId", this.currentGuiId);
        tag.setInteger("NorthState", this.northState);
        tag.setInteger("SouthState", this.southState);
        tag.setInteger("EastState", this.eastState);
        tag.setInteger("WestState", this.westState);
        tag.setInteger("UpState", this.upState);
        tag.setInteger("DownState", this.downState);
        
        if (this.hasCustomName()) 
        {
            tag.setString("CustomName", this.customName);
        }
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.crusherItemStacks.deserializeNBT(compound.getCompoundTag("Inventory"));
        this.crusherBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("TotalCookTime");
        this.currentItemBurnTime = compound.getInteger("CurrentItemBurnTime");
        this.buttonState = compound.getInteger("ButtonState");
        this.currentGuiId = compound.getInteger("CurrentGuiId");
        this.northState = compound.getInteger("NorthState");
        this.southState = compound.getInteger("SouthState");
        this.eastState = compound.getInteger("EastState");
        this.westState = compound.getInteger("WestState");
        this.upState = compound.getInteger("UpState");
        this.downState = compound.getInteger("DownState");
        
        if (compound.hasKey("CustomName", 8)) 
        {
            this.setCustomName(compound.getString("CustomName"));
        }
        
        this.markDirty();
    }
    
    public boolean isActive()
    {
    	boolean active = this.crusherBurnTime > 0;
    	
        return active;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isActive(TileEntityCrusher tileentity)
    {
        return tileentity.getField(0) > 0;
    }

    public static int getCookTime(ItemStack input1, ItemStack input2)
    {
        int cookTime = RecipesCrusher.getInstance().getCookTime(input1, input2);

        return cookTime;
    }
    
    @Override
    public void update() 
    {
        boolean flag = this.isActive();
        boolean flag1 = false;
        
        if (this.crusherBurnTime > 0)
        {
            if (!Crusher.getStateActive(world, pos))
            {
            	Crusher.setStateActive(true, world, pos);
            }
        	--this.crusherBurnTime;
        	this.markDirty();
        }

        if (!this.world.isRemote)
        {
        	boolean hasRedstoneSignal = this.world.isBlockPowered(this.pos);
            boolean canBurn = false;

            switch (this.buttonState)
            {
                case 0:
                    canBurn = true;
                    break;
                case 1:
                    canBurn = !hasRedstoneSignal;
                    break;
                case 2:
                    canBurn = hasRedstoneSignal;
                    break;
            }
        	
            ItemStack input1 = crusherItemStacks.getStackInSlot(0);
            ItemStack input2 = crusherItemStacks.getStackInSlot(1);
            ItemStack fuel = crusherItemStacks.getStackInSlot(2);

            if (!canBurn && this.cookTime > 0)
            {
            	this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }
            
            if (canBurn && (this.isActive() || (!fuel.isEmpty() && (!input1.isEmpty() || !input2.isEmpty()))))
            {
                if (!this.isActive() && this.canSmelt())
                {
                    this.crusherBurnTime = getItemBurnTime(fuel);
                    this.currentItemBurnTime = crusherBurnTime;

                    if (this.isActive())
                    {
                        flag1 = true;

                        if (!fuel.isEmpty())
                        {
                            Item item = fuel.getItem();
                            fuel.shrink(1);

                            if (fuel.isEmpty())
                            {
                                ItemStack item1 = item.getContainerItem(fuel);
                                crusherItemStacks.setStackInSlot(2, item1);
                                this.markDirty();
                            }
                        }
                    }
                }
                
                if (this.isActive() && this.canSmelt())
                {
                    if (cookTime == 0)
                    {
                        totalCookTime = RecipesCrusher.getInstance().getCookTime(input1, input2);
                    }

                    ++this.cookTime;

                    if (this.cookTime >= this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = TileEntityCrusher.getCookTime(input1, input2);
                        this.smeltItem();
                        flag1 = true;
                        this.markDirty();
                    }
                }
                else
                {
                    //this.cookTime = 0;
                    this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
                }
            }
            else if (!this.isActive() && this.cookTime > 0)
            {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (flag != this.isActive())
            {
                flag1 = true;
                Crusher.setStateActive(this.isActive(), world, pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    private boolean canSmelt() 
    {
        ItemStack input1 = crusherItemStacks.getStackInSlot(0);
        ItemStack input2 = crusherItemStacks.getStackInSlot(1);

        if (input1.isEmpty() || input2.isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack result = RecipesCrusher.getInstance().getCrusherResult(input1, input2);
            
            if (result.isEmpty())
            {
                result = RecipesCrusher.getInstance().getCrusherResult(input2, input1);
            }

            if (result.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack output = crusherItemStacks.getStackInSlot(3);
                
                if (output.isEmpty())
                {
                    return true;
                }
                if (!output.isItemEqual(result))
                {
                    return false;
                }
                
                int res = output.getCount() + result.getCount();
                return res <= 64 && res <= output.getMaxStackSize();
            }
        }
    }
    
    private void smeltItem()
    {
        ItemStack input1 = crusherItemStacks.getStackInSlot(0);
        ItemStack input2 = crusherItemStacks.getStackInSlot(1);

        if (this.canSmelt())
        {
            ItemStack result = RecipesCrusher.getInstance().getCrusherResult(input1, input2);
            ItemStack output = crusherItemStacks.getStackInSlot(3);

            if (output.isEmpty())
            {
                crusherItemStacks.setStackInSlot(3, result.copy());
                this.markDirty();
            }
            else if (output.isItemEqual(result))
            {
                output.grow(result.getCount());
                this.markDirty();
            }

            input1.shrink(1);
            input2.shrink(1);

            if (input1.isEmpty()) crusherItemStacks.setStackInSlot(0, ItemStack.EMPTY);
            if (input2.isEmpty()) crusherItemStacks.setStackInSlot(1, ItemStack.EMPTY);
            
            this.markDirty();
        }
    }

    public static int getItemBurnTime(ItemStack fuel)
    {
        return TileEntityFurnace.getItemBurnTime(fuel);
    }

    public static boolean isItemFuel(ItemStack fuel)
    {
        return getItemBurnTime(fuel) > 0;
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
    	if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if (index == 2)
        {
            return false;
        }
        else if (index != 1)
        {
            return true;
        }
        else
        {
            ItemStack itemstack = crusherItemStacks.getStackInSlot(1);
            return isItemFuel(stack) || (SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET);
        }
    }
    
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }
    
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (direction == EnumFacing.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET)
            {
                return false;
            }
        }

        return true;
    }

    public void setCurrentGuiId(int guiId)
    {
        this.currentGuiId = guiId;

        NetworkHandler.sendCurrentGuiIdUpdate(this.currentGuiId, this.pos);
    }

    public int getCurrentGuiId()
    {
        return this.currentGuiId;
    }

    public String getGuiID()
    {
        if (currentGuiId == GuiHandler.GUI_CRUSHER_SLOTS_CONFIGURATION)
        {
            return Reference.MODID + ":crusher_slots_configuration";
        }
        return Reference.MODID + ":crusher";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerCrusher(playerInventory, this);
    }
    
    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.crusherBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.crusherBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }
    
    public int getFieldCount()
    {
        return 4;
    }
}