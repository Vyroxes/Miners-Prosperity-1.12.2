package net.vyroxes.minersprosperity.objects.containers;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesCrusher;
import net.vyroxes.minersprosperity.objects.guis.GuiCrusher;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;
import org.jetbrains.annotations.NotNull;

public class ContainerCrusher extends Container
{
    private final TileEntityCrusher tileCrusher;
    private int cookTime;
    private int totalCookTime;
    private int crusherBurnTime;
    private int currentItemBurnTime;

    public ContainerCrusher(InventoryPlayer playerInventory, TileEntityCrusher tileEntityCrusher) 
    {
        this.tileCrusher = tileEntityCrusher;
        IItemHandler handler = tileEntityCrusher.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        this.addSlotToContainer(new SlotItemHandler(handler, 0, 43, 17) // Slot wejściowy lewy (1)
        {
            @Override
            public boolean isItemValid(@NotNull ItemStack stack)
            {
                return RecipesCrusher.getInstance().isInputInAnyRecipe(stack);
            }
        });


        this.addSlotToContainer(new SlotItemHandler(handler, 1, 69, 17) // Slot wejściowy prawy (2)
        {
            @Override
            public boolean isItemValid(@NotNull ItemStack stack)
            {
                return RecipesCrusher.getInstance().isInputInAnyRecipe(stack);
            }
        });
        this.addSlotToContainer(new SlotItemHandler(handler, 2, 56, 53) // Slot paliwa
        {
            @Override
            public boolean isItemValid(@NotNull ItemStack stack)
            {
                return TileEntityCrusher.isItemFuel(stack);
            }
        });
        this.addSlotToContainer(new SlotItemHandler(handler, 3, 116, 35) // Slot wyjściowy
        {
        	@Override
            public boolean isItemValid(@NotNull ItemStack stack)
            {
                return false;
            }

            @Override
            public @NotNull ItemStack onTake(@NotNull EntityPlayer entityPlayer, @NotNull ItemStack itemStack)
            {
                float experiencePerItem = RecipesCrusher.getInstance().getCrusherExperience(itemStack);
                int totalItemCount = itemStack.getCount();

                if (!entityPlayer.world.isRemote)
                {
                    int totalExperience;

                    if (experiencePerItem == 0.0F)
                    {
                        totalExperience = 0;
                    }
                    else if (experiencePerItem < 1.0F)
                    {
                        int flooredExperience = MathHelper.floor((float) totalItemCount * experiencePerItem);

                        if (flooredExperience < MathHelper.ceil((float) totalItemCount * experiencePerItem) &&
                                Math.random() < (float) totalItemCount * experiencePerItem - (float) flooredExperience)
                        {
                            ++flooredExperience;
                        }

                        totalExperience = flooredExperience;
                    }
                    else
                    {
                        totalExperience = MathHelper.floor((float) totalItemCount * experiencePerItem);
                    }

                    while (totalExperience > 0)
                    {
                        int xpSplit = EntityXPOrb.getXPSplit(totalExperience);
                        totalExperience -= xpSplit;
                        entityPlayer.world.spawnEntity(new EntityXPOrb(entityPlayer.world, entityPlayer.posX, entityPlayer.posY + 0.5D, entityPlayer.posZ + 0.5D, xpSplit));
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

    @Override
    public void detectAndSendChanges() 
    {
        super.detectAndSendChanges();

        for (IContainerListener icontainerlistener : this.listeners)
        {
            if (this.cookTime != this.tileCrusher.cookTime)
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileCrusher.cookTime);
            }

            if (this.crusherBurnTime != this.tileCrusher.crusherBurnTime)
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileCrusher.crusherBurnTime);
            }

            if (this.currentItemBurnTime != this.tileCrusher.currentItemBurnTime)
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileCrusher.currentItemBurnTime);
            }

            if (this.totalCookTime != this.tileCrusher.totalCookTime)
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileCrusher.totalCookTime);
            }
        }
        
        this.cookTime = this.tileCrusher.cookTime;
        this.crusherBurnTime = this.tileCrusher.crusherBurnTime;
        this.currentItemBurnTime = this.tileCrusher.currentItemBurnTime;
        this.totalCookTime = this.tileCrusher.totalCookTime;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) 
    {
        this.tileCrusher.setField(id, data);
    }
    
    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn)
    {
        return this.tileCrusher.isUsableByPlayer(playerIn);
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index)
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

                float experiencePerItem = RecipesCrusher.getInstance().getCrusherExperience(itemstack);
                int totalItemCount = itemstack.getCount();

                if (!playerIn.world.isRemote)
                {
                    int totalExperience;

                    if (experiencePerItem == 0.0F)
                    {
                        totalExperience = 0;
                    }
                    else if (experiencePerItem < 1.0F)
                    {
                        int flooredExperience = MathHelper.floor((float) totalItemCount * experiencePerItem);

                        if (flooredExperience < MathHelper.ceil((float) totalItemCount * experiencePerItem) &&
                                Math.random() < (float) totalItemCount * experiencePerItem - (float) flooredExperience)
                        {
                            ++flooredExperience;
                        }

                        totalExperience = flooredExperience;
                    }
                    else
                    {
                        totalExperience = MathHelper.floor((float) totalItemCount * experiencePerItem);
                    }

                    while (totalExperience > 0)
                    {
                        int xpSplit = EntityXPOrb.getXPSplit(totalExperience);
                        totalExperience -= xpSplit;
                        playerIn.world.spawnEntity(new EntityXPOrb(playerIn.world, playerIn.posX, playerIn.posY + 0.5D, playerIn.posZ + 0.5D, xpSplit));
                    }
                }
            }
            else if (index > 3)
            {        
                if (RecipesCrusher.getInstance().isInputInAnyRecipe(itemstack1))
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
                        if (!RecipesCrusher.getInstance().getCrusherResult(itemstack1, slot1).isEmpty() || !RecipesCrusher.getInstance().getCrusherResult(slot1, itemstack1).isEmpty())
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
                        if (!RecipesCrusher.getInstance().getCrusherResult(itemstack1, slot0).isEmpty() || !RecipesCrusher.getInstance().getCrusherResult(slot0, itemstack1).isEmpty())
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
                else if (TileEntityCrusher.isItemFuel(itemstack1))
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
            
            slot.onTake(playerIn, itemstack1);
        }
        
        return itemstack;
    }
}