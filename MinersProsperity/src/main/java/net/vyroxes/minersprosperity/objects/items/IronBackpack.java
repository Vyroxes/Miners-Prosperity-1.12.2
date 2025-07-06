package net.vyroxes.minersprosperity.objects.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import org.jetbrains.annotations.NotNull;

public class IronBackpack extends Backpack
{
    public IronBackpack(String name)
    {
        super(name);
        this.addPropertyOverride(new ResourceLocation("minersprosperity", "open_iron_backpack"), ((stack, worldIn, entityIn) -> {
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
    public @NotNull ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote)
        {
            playerIn.openGui(MinersProsperity.INSTANCE, GuiHandler.GuiTypes.IRON_BACKPACK.ordinal(), worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            itemStack.getOrCreateSubCompound("BackpackData").setBoolean("IsOpen", true);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    public static InventoryBackpack getBackpackInventory(ItemStack itemStack)
    {
        NBTTagCompound compound = itemStack.getTagCompound();
        if (compound != null && compound.hasKey("BackpackInventory"))
        {
            InventoryBackpack inventory = new InventoryBackpack(27, "Iron Backpack");
            inventory.readFromNBT(compound.getCompoundTag("BackpackInventory"));
            return inventory;
        }
        else
        {
            return new InventoryBackpack(27, "Iron Backpack");
        }
    }
}