package net.vyroxes.minersprosperity.enchantment.event;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.vyroxes.minersprosperity.init.ModEnchantments;

@Mod.EventBusSubscriber
public class GlowHandler
{
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
            return;

        EntityPlayer player = event.player;

        for (ItemStack stack : player.inventory.mainInventory)
        {
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.GLOW, stack) > 0)
            {
                NBTTagCompound tag = stack.getTagCompound();
                if (tag == null)
                {
                    tag = new NBTTagCompound();
                    stack.setTagCompound(tag);
                }

                if (!tag.hasKey("Glow"))
                {
                    tag.setBoolean("Glow", true);
                }
            }
        }

        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if (helmet.isEmpty() || EnchantmentHelper.getEnchantmentLevel(ModEnchantments.GLOW, helmet) <= 0)
            return;

        NBTTagCompound compound = helmet.getTagCompound();
        if (compound == null)
        {
            compound = new NBTTagCompound();
            helmet.setTagCompound(compound);
        }

        if (!compound.hasKey("Glow"))
        {
            compound.setBoolean("Glow", true);
            helmet.setTagCompound(compound);
        }
    }
}