package net.vyroxes.minersprosperity.item.battery;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.vyroxes.minersprosperity.energy.CustomEnergyStorage;
import net.vyroxes.minersprosperity.item.ItemBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class ItemBattery extends ItemBase
{
    private final BatteryTier tier;

    public ItemBattery(String name, BatteryTier tier)
    {
        super(name);
        this.tier = tier;
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        return 1;
    }

    public BatteryTier getTier()
    {
        return tier;
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        CustomEnergyStorage storage = new CustomEnergyStorage(tier.capacity, tier.maxReceive, tier.maxExtract, 0);

        BatteryCapabilityProvider provider = new BatteryCapabilityProvider(stack, storage);

        if (nbt != null) provider.deserializeNBT(nbt);

        return provider;
    }

    @Override
    public boolean showDurabilityBar(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(@NotNull ItemStack stack)
    {
        IEnergyStorage storage = getEnergyStorage(stack);
        if (storage == null || storage.getMaxEnergyStored() <= 0) return 1.0;

        return 1.0 - (double) storage.getEnergyStored() / storage.getMaxEnergyStored();
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flag)
    {
        IEnergyStorage storage = getEnergyStorage(stack);
        if (storage == null) return;

        float energyStored, maxEnergyStored;
        String formattedEnergyStored, formattedMaxEnergyStored;
        if (storage.getMaxEnergyStored() >= 1000000)
        {
            energyStored = (float) storage.getEnergyStored() / 1000000;
            formattedEnergyStored = String.format("%.2f", energyStored);
            maxEnergyStored = (float) storage.getMaxEnergyStored() / 1000000;
            formattedMaxEnergyStored = String.format("%.2f", maxEnergyStored);
        }
        else
        {
            energyStored = (float) storage.getEnergyStored() / 1000;
            formattedEnergyStored = String.format("%.1f", energyStored);
            maxEnergyStored = (float) storage.getMaxEnergyStored() / 1000;
            formattedMaxEnergyStored = String.format("%.1f", maxEnergyStored);
        }

        if (tier == BatteryTier.CREATIVE) tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + ": " + TextFormatting.GRAY + I18n.format("gui.energy_infinite"));
        else
        {
            if (storage.getMaxEnergyStored() >= 1000000) tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + ": " + TextFormatting.GRAY + formattedEnergyStored + "/" + formattedMaxEnergyStored + " MFE");
            else tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + ": " + TextFormatting.GRAY + formattedEnergyStored + "/" + formattedMaxEnergyStored + " kFE");
        }
    }

    @Nullable
    public static IEnergyStorage getEnergyStorage(ItemStack stack)
    {
        if (stack.isEmpty()) return null;

        return stack.getCapability(CapabilityEnergy.ENERGY, null);
    }

    private static BatteryTier getTierFromStack(ItemStack stack)
    {
        if (stack.getItem() instanceof ItemBattery) return ((ItemBattery) stack.getItem()).getTier();

        return BatteryTier.BASIC;
    }

    private static void syncEnergyToStack(ItemStack stack, CustomEnergyStorage storage)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null)
        {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        storage.writeToNBT(tag);
    }

    public static class EnergyStorageWrapper implements IEnergyStorage
    {
        private final ItemStack stack;
        private final CustomEnergyStorage storage;

        public EnergyStorageWrapper(ItemStack stack, CustomEnergyStorage storage)
        {
            this.stack = stack;
            this.storage = storage;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            if (getTierFromStack(stack) == BatteryTier.CREATIVE)
            {
                int missing = storage.getMaxEnergyStored() - storage.getEnergyStored();

                if (simulate) return missing;

                int receivedTotal = 0;

                while (missing > 0)
                {
                    int received = storage.receiveEnergy(missing, false);

                    if (received <= 0) break;

                    receivedTotal += received;
                    missing -= received;
                }

                if (receivedTotal > 0) syncEnergyToStack(stack, storage);

                return receivedTotal;
            }

            int received = storage.receiveEnergy(maxReceive, simulate);

            if (!simulate && received > 0) syncEnergyToStack(stack, storage);

            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate)
        {
            int extracted = storage.extractEnergy(maxExtract, simulate);

            if (!simulate && extracted > 0) syncEnergyToStack(stack, storage);

            return extracted;
        }

        @Override
        public int getEnergyStored() { return storage.getEnergyStored(); }

        @Override
        public int getMaxEnergyStored() { return storage.getMaxEnergyStored(); }

        @Override
        public boolean canExtract() { return storage.canExtract(); }

        @Override
        public boolean canReceive() { return storage.canReceive(); }
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            ItemStack empty = new ItemStack(this);
            items.add(empty);

            ItemStack full = new ItemStack(this);
            IEnergyStorage energy = full.getCapability(CapabilityEnergy.ENERGY, null);

            if (energy != null)
            {
                int missing = energy.getMaxEnergyStored() - energy.getEnergyStored();

                while (missing > 0)
                {
                    int received = energy.receiveEnergy(missing, false);

                    if (received <= 0) break;

                    missing -= received;
                }
            }

            items.add(full);
        }
    }
}