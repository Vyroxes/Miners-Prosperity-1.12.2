package net.vyroxes.minersprosperity.mixins;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.items.ItemBackpackBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(ItemBow.class)
public abstract class MixinItemBow
{
    @Inject(method = "findAmmo", at = @At("RETURN"), cancellable = true)
    private void injectAmmoFromBackpack(EntityPlayer player, CallbackInfoReturnable<ItemStack> cir)
    {
        if (!cir.getReturnValue().isEmpty())
            return;

        if (!player.world.isRemote)
        {
            List<ItemStack> backpacks = ItemBackpackBase.getAllBackpacksFromPlayer(player);
            if (backpacks.isEmpty())
                return;

            for (int b = 0; b < backpacks.size(); b++)
            {
                ItemStack backpack = backpacks.get(b);
                ItemStackHandler inv = ItemBackpackBase.getBackpackInventory(backpack);

                for (int i = 0; i < inv.getSlots(); i++)
                {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemArrow)
                    {
                        cir.setReturnValue(stack.copy());
                        player.getEntityData().setInteger("ArrowSlot", i);
                        player.getEntityData().setInteger("ArrowBackpackIndex", b);
                        return;
                    }
                }
            }
        }
    }

    @Inject(method = "onPlayerStoppedUsing", at = @At("TAIL"))
    private void consumeArrowIfFromBackpack(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft, CallbackInfo ci)
    {
        if (!worldIn.isRemote)
        {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0 || player.capabilities.isCreativeMode)
                return;

            if (!entityLiving.getEntityData().hasKey("ArrowBackpackIndex") || !entityLiving.getEntityData().hasKey("ArrowSlot"))
                return;

            int slot = entityLiving.getEntityData().getInteger("ArrowSlot");
            int backpackIndex = entityLiving.getEntityData().getInteger("ArrowBackpackIndex");

            List<ItemStack> backpacks = ItemBackpackBase.getAllBackpacksFromPlayer(player);
            if (backpackIndex >= backpacks.size())
                return;

            ItemStack backpackStack = backpacks.get(backpackIndex);
            ItemStackHandler inv = ItemBackpackBase.getBackpackInventory(backpackStack);
            ItemStack arrowStack = inv.getStackInSlot(slot);

            if (!arrowStack.isEmpty() && arrowStack.getItem() instanceof ItemArrow) {
                arrowStack.shrink(1);
                inv.setStackInSlot(slot, arrowStack.isEmpty() ? ItemStack.EMPTY : arrowStack);
            }

            entityLiving.getEntityData().removeTag("ArrowSlot");
            entityLiving.getEntityData().removeTag("ArrowBackpackIndex");
        }
    }
}