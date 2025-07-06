package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.util.annotations.NonnullByDefault;
import org.jetbrains.annotations.NotNull;

public class ContainerAlloyFurnace extends Container
{
    private final TileEntityMachine tileEntity;
    private int cookTime;
    private int totalCookTime;
    private int energyStored;
    private int maxEnergyStored;
    private int maxReceive;
    private int maxExtract;
    private int energyUsage;
    private int energyGeneration;
    private int fluidStored;

    public ContainerAlloyFurnace(InventoryPlayer playerInventory, TileEntityMachine tileEntity)
    {
        this.tileEntity = tileEntity;
        IItemHandler handler = this.tileEntity.getCustomItemStackHandler();

        this.addSlotToContainer(new SlotItemHandler(handler, 0, 33, 35));
        this.addSlotToContainer(new SlotItemHandler(handler, 1, 53, 35));
        this.addSlotToContainer(new SlotItemHandler(handler, 2, 8, 53));
        this.addSlotToContainer(new SlotItemHandler(handler, 3, 115, 35)
        {
            @Override
            public @NotNull ItemStack onTake(@NotNull EntityPlayer entityPlayer, @NotNull ItemStack itemStack)
            {
                if (!entityPlayer.world.isRemote)
                {
                    if(tileEntity.getFluidStored() >= 10)
                    {
                        giveExperienceToPlayer(entityPlayer);
                    }
                }

                return super.onTake(entityPlayer, itemStack);
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public void giveExperienceToPlayer(EntityPlayer entityPlayer)
    {
        int totalExperience = this.tileEntity.getFluidStored() / 10;
        int drainedExperience = 0;

        while (totalExperience > 0)
        {
            int xpSplit = EntityXPOrb.getXPSplit(totalExperience);
            totalExperience -= xpSplit;
            drainedExperience += xpSplit;
            entityPlayer.world.spawnEntity(new EntityXPOrb(entityPlayer.world, entityPlayer.posX, entityPlayer.posY + 0.5D, entityPlayer.posZ + 0.5D, xpSplit));
        }

        this.tileEntity.getFluidTank().drainInternal(drainedExperience * 10, true);
    }

    @Override
    public void detectAndSendChanges() 
    {
        super.detectAndSendChanges();

        for (IContainerListener icontainerlistener : this.listeners)
        {
            if (this.cookTime != this.tileEntity.getCookTime())
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileEntity.getCookTime());
            }

            if (this.totalCookTime != this.tileEntity.getTotalCookTime())
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileEntity.getTotalCookTime());
            }

            if (this.energyStored != this.tileEntity.getEnergyStored())
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileEntity.getEnergyStored());
            }

            if (this.maxEnergyStored != this.tileEntity.getMaxEnergyStored())
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileEntity.getMaxEnergyStored());
            }

            if (this.maxReceive != this.tileEntity.getMaxReceive())
            {
                icontainerlistener.sendWindowProperty(this, 4, this.tileEntity.getMaxReceive());
            }

            if (this.maxExtract != this.tileEntity.getMaxExtract())
            {
                icontainerlistener.sendWindowProperty(this, 5, this.tileEntity.getMaxExtract());
            }

            if (this.energyUsage != this.tileEntity.getEnergyUsage())
            {
                icontainerlistener.sendWindowProperty(this, 6, this.tileEntity.getEnergyUsage());
            }

            if (this.energyGeneration != this.tileEntity.getEnergyGeneration())
            {
                icontainerlistener.sendWindowProperty(this, 7, this.tileEntity.getEnergyGeneration());
            }

            if (this.fluidStored != this.tileEntity.getFluidStored() || this.tileEntity.getFluidStored() == 0)
            {
                icontainerlistener.sendWindowProperty(this, 8, this.tileEntity.getFluidStored());
            }
        }
        
        this.cookTime = this.tileEntity.getCookTime();
        this.totalCookTime = this.tileEntity.getTotalCookTime();
        this.energyStored = this.tileEntity.getEnergyStored();
        this.maxEnergyStored = this.tileEntity.getMaxEnergyStored();
        this.maxReceive = this.tileEntity.getMaxReceive();
        this.maxExtract = this.tileEntity.getMaxExtract();
        this.energyUsage = this.tileEntity.getEnergyUsage();
        this.energyGeneration = this.tileEntity.getEnergyGeneration();
        this.fluidStored = this.tileEntity.getFluidStored();
    }
    
    @Override
    public void updateProgressBar(int id, int data) 
    {
        this.tileEntity.setField(id, data);
    }
    
    @Override
    public boolean canInteractWith(@NotNull EntityPlayer entityPlayer)
    {
        return this.tileEntity.isUsableByPlayer(entityPlayer);
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer entityPlayer, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        
        if (slot != null && slot.getHasStack()) 
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (index == 3) 
            {
                if (!this.mergeItemStack(itemstack1, 4, 40, true))
                {
                	return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);

                if (!entityPlayer.world.isRemote)
                {
                    if(tileEntity.getFluidStored() > 10)
                    {
                        giveExperienceToPlayer(entityPlayer);
                    }
                }
            }
            else if (index > 3)
            {        
                if (!RecipesAlloyFurnace.getInstance().findRecipes(itemstack1).isEmpty())
                {
                    ItemStack slot0 = this.inventorySlots.get(0).getStack();
                    ItemStack slot1 = this.inventorySlots.get(1).getStack();

                    if (slot0.isEmpty() && slot1.isEmpty())
                    {
                        if (!this.mergeItemStack(itemstack1, 0, 2, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (slot0.isEmpty() && !slot1.isEmpty() && !slot1.getItem().equals(itemstack1.getItem()))
                    {
                        if (!RecipesAlloyFurnace.getInstance().getResult(itemstack1, slot1).isEmpty() || !RecipesAlloyFurnace.getInstance().getResult(slot1, itemstack1).isEmpty())
                        {
                            if (!this.mergeItemStack(itemstack1, 0, 1, false))
                            {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                    else if (!slot0.isEmpty() && slot0.getItem().equals(itemstack1.getItem()))
                    {
                        if (!this.mergeItemStack(itemstack1, 0, 1, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!slot0.isEmpty() && slot1.isEmpty() && !slot0.getItem().equals(itemstack1.getItem()))
                    {
                        if (!RecipesAlloyFurnace.getInstance().getResult(itemstack1, slot0).isEmpty() || !RecipesAlloyFurnace.getInstance().getResult(slot0, itemstack1).isEmpty())
                        {
                            if (!this.mergeItemStack(itemstack1, 1, 2, false))
                            {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                    else if (!slot1.isEmpty() && slot1.getItem().equals(itemstack1.getItem()))
                    {
                        if (!this.mergeItemStack(itemstack1, 1, 2, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                else if (this.tileEntity.getCustomItemStackHandler().isEnergyItemValid(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false))
                    {
                    	return ItemStack.EMPTY;
                    }
                }
                else if (index <= 30)
                {
                	if(!this.mergeItemStack(itemstack1, 31, 40, false))
		            {
		                return ItemStack.EMPTY;
		            }
                }
                else if (index <= 39)
                {
                    if(!this.mergeItemStack(itemstack1, 4, 31, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (!this.mergeItemStack(itemstack1, 4, 40, false))
            {
                return ItemStack.EMPTY;
            }
            
            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
            
            if(itemstack1.getCount() == itemstack.getCount())
            {
            	return ItemStack.EMPTY;
            }
            
            slot.onTake(entityPlayer, itemstack1);
        }
        
        return itemstack;
    }
}