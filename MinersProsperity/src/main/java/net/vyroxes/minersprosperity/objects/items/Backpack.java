package net.vyroxes.minersprosperity.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import org.jetbrains.annotations.NotNull;

public class Backpack extends ItemBase
{
    public Backpack(String name)
    {
        super(name);
        this.addPropertyOverride(new ResourceLocation("minersprosperity", "open_backpack"), ((stack, worldIn, entityIn) -> {
            if (stack.hasTagCompound())
            {
                NBTTagCompound data = stack.getSubCompound("BackpackData");
                if (data != null && data.getBoolean("IsOpen"))
                {
                    return 1.0f;
                }
            }
            return 0.0f;
        }));
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack)
    {
        return 1;
    }
    
    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote)
        {
            playerIn.openGui(MinersProsperity.INSTANCE, GuiHandler.GuiTypes.BACKPACK.ordinal(), worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            itemStack.getOrCreateSubCompound("BackpackData").setBoolean("IsOpen", true);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    public static InventoryBackpack getBackpackInventory(ItemStack itemStack)
    {
        NBTTagCompound compound = itemStack.getTagCompound();

        int size;

        if (itemStack.getItem() instanceof EmeraldBackpack)
        {
            size = 54;
        } else if (itemStack.getItem() instanceof DiamondBackpack) {
            size = 45;
        } else if (itemStack.getItem() instanceof GoldenBackpack) {
            size = 36;
        } else if (itemStack.getItem() instanceof IronBackpack) {
            size = 27;
        } else if (itemStack.getItem() instanceof LeadBackpack) {
            size = 18;
        } else
        {
            size = 9;
        }

        InventoryBackpack inventory = new InventoryBackpack(size);

        if (compound != null && compound.hasKey("BackpackInventory"))
        {
            inventory.readFromNBT(compound.getCompoundTag("BackpackInventory"));
        }

        return inventory;
    }

    public static void saveBackpackInventory(ItemStack itemStack, InventoryBackpack backpackInventory)
    {
        NBTTagCompound compound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound backpackInv = compound.hasKey("BackpackInventory") ? compound.getCompoundTag("BackpackInventory") : new NBTTagCompound();
        backpackInventory.writeToNBT(backpackInv);
        compound.setTag("BackpackInventory", backpackInv);
        
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
            stack.setTagCompound(compound);
        }

        if (!compound.hasKey("BackpackData"))
        {
            NBTTagCompound backpackData = new NBTTagCompound();
            backpackData.setBoolean("AutoCollect", false);
            compound.setTag("BackpackData", backpackData);
        }
        else
        {
            NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");

            if (!backpackData.hasKey("AutoCollect"))
            {
                backpackData.setBoolean("AutoCollect", false);
            }

            compound.setTag("BackpackData", backpackData);
        }

        NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
        boolean autoCollect = backpackData.getBoolean("AutoCollect");
        String statusTooltip = TextFormatting.GRAY + I18n.format("tooltip.backpack.auto_collect") + " " + (autoCollect ? TextFormatting.GREEN + I18n.format("tooltip.backpack.auto_collect.on") : TextFormatting.RED + I18n.format("tooltip.backpack.auto_collect.off"));
        tooltip.add(statusTooltip);
    }
}