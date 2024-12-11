package net.vyroxes.minersprosperity.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;

public class Backpack extends ItemBase
{
    public Backpack(String name)
    {
        super(name);
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return 1;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote)
        {
            playerIn.openGui(MinersProsperity.instance, GuiHandler.BACKPACK_GUI, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    public static BackpackInventory getBackpackInventory(ItemStack itemStack)
    {
        NBTTagCompound compound = itemStack.getTagCompound();
        if (compound != null && compound.hasKey("BackpackInventory"))
        {
            BackpackInventory inventory = new BackpackInventory(27);
            inventory.readFromNBT(compound.getCompoundTag("BackpackInventory"));
            return inventory;
        }
        else
        {
            return new BackpackInventory(27);
        }
    }

    public static void saveBackpackInventory(ItemStack itemStack, BackpackInventory backpackInventory)
    {
        NBTTagCompound compound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound backpackInv = new NBTTagCompound();
        backpackInventory.writeToNBT(backpackInv);
        compound.setTag("BackpackInventory", backpackInv);
        
        NBTTagCompound backpackData = new NBTTagCompound();
        backpackData.setBoolean("AutoCollect", backpackInventory.getAutoCollectState());
        compound.setTag("BackpackData", backpackData);
        
        itemStack.setTagCompound(compound);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
        {
            compound = new NBTTagCompound();
            compound.setBoolean("AutoCollect", false);
            stack.setTagCompound(compound);
        }

        boolean autoCollect = compound.getBoolean("AutoCollect");
        String statusTooltip = TextFormatting.GRAY + "Auto-Collect: "  + (autoCollect ? TextFormatting.GREEN + "On" : TextFormatting.RED + "Off");
        tooltip.add(statusTooltip);
    }
}