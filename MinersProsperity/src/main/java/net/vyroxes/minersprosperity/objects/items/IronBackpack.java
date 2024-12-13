package net.vyroxes.minersprosperity.objects.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;

public class IronBackpack extends ItemBase
{
    public IronBackpack(String name)
    {
        super(name);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote)
        {
            playerIn.openGui(MinersProsperity.instance, GuiHandler.IRON_BACKPACK_GUI, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    public static InventoryIronBackpack getBackpackInventory(ItemStack itemStack)
    {
        NBTTagCompound compound = itemStack.getTagCompound();
        if (compound != null && compound.hasKey("BackpackInventory"))
        {
        	InventoryIronBackpack inventory = new InventoryIronBackpack(54);
            inventory.readFromNBT(compound.getCompoundTag("BackpackInventory"));
            return inventory;
        }
        else
        {
            return new InventoryIronBackpack(54);
        }
    }

    public static void saveBackpackInventory(ItemStack itemStack, InventoryIronBackpack ironBackpackInventory)
    {
        NBTTagCompound compound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound backpackTag = new NBTTagCompound();
        ironBackpackInventory.writeToNBT(backpackTag);
        compound.setTag("BackpackInventory", backpackTag);
        itemStack.setTagCompound(compound);
    }
}