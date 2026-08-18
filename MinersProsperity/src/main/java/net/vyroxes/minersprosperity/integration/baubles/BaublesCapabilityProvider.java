package net.vyroxes.minersprosperity.integration.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(modid = "baubles", iface = "baubles.api.IBauble")
public class BaublesCapabilityProvider implements IBauble
{
    private final BaubleType baubleType;

    public BaublesCapabilityProvider(BaubleType baubleType)
    {
        this.baubleType = baubleType;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack)
    {
        return this.baubleType;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player)
    {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player)
    {
        return true;
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player)
    {
        return false;
    }
}