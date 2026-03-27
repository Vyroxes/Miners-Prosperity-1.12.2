package net.vyroxes.minersprosperity.mixins;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.objects.items.ItemBackpackBase;
import net.vyroxes.minersprosperity.util.compat.baubles.BaublesCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
import java.util.Objects;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase
{
    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void onCheckTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir)
    {
        EntityLivingBase self = (EntityLivingBase)(Object)this;
        if (source.canHarmInCreative() || !(self instanceof EntityPlayer player))
            return;

        ItemStack totem = ItemStack.EMPTY;

        for (EnumHand hand : EnumHand.values())
        {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING)
            {
                totem = stack.copy();
                stack.shrink(1);
                break;
            }
        }

        if (totem.isEmpty())
        {
            for (int i = 0; i < player.inventory.mainInventory.size(); i++)
            {
                ItemStack stack = player.inventory.mainInventory.get(i);
                if (stack.getItem() == Items.TOTEM_OF_UNDYING)
                {
                    totem = stack.copy();
                    stack.shrink(1);
                    break;
                }
            }
        }

        if (Loader.isModLoaded("baubles") && totem.isEmpty())
        {
            totem = BaublesCompat.tryGetTotemFromBaubles(player);
        }

        if (totem.isEmpty())
        {
            List<ItemStack> backpacks = ItemBackpackBase.getAllBackpacksFromPlayer(player);

            for (ItemStack backpack : backpacks)
            {
                ItemStackHandler inv = ItemBackpackBase.getBackpackInventory(backpack);

                for (int i = 0; i < inv.getSlots(); i++)
                {
                    ItemStack slotStack = inv.getStackInSlot(i);
                    if (!slotStack.isEmpty() && slotStack.getItem() == Items.TOTEM_OF_UNDYING)
                    {
                        totem = slotStack.copy();
                        inv.extractItem(i, 1, false);
                        break;
                    }
                }

                if (!totem.isEmpty())
                    break;
            }
        }

        if (!totem.isEmpty())
        {
            if (player instanceof EntityPlayerMP playerMP)
            {
                playerMP.addStat(Objects.requireNonNull(StatList.getObjectUseStats(Items.TOTEM_OF_UNDYING)));
                CriteriaTriggers.USED_TOTEM.trigger(playerMP, totem);
            }

            self.setHealth(1.0F);
            self.clearActivePotions();
            self.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
            self.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
            self.world.setEntityState(self, (byte)35);

            cir.setReturnValue(true);
        }
    }
}