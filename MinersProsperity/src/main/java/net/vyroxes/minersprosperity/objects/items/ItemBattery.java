package net.vyroxes.minersprosperity.objects.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
import net.vyroxes.minersprosperity.objects.energy.CustomEnergyStorage;
import net.vyroxes.minersprosperity.objects.enums.EBatteryTier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemBattery extends ItemBase
{
    private final EBatteryTier tier;

    public ItemBattery(String name, EBatteryTier tier)
    {
        super(name);
        this.tier = tier;
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        return 1;
    }

    public EBatteryTier getTier()
    {
        return tier;
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        CustomEnergyStorage storage = new CustomEnergyStorage(
                tier.capacity,
                tier.maxReceive,
                tier.maxExtract,
                0
        );

        if (nbt != null && nbt.hasKey("Energy"))
            storage.readFromNBT(nbt);

        return new BatteryCapabilityProvider(stack, storage);
    }

    @Override
    public boolean showDurabilityBar(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(@NotNull ItemStack stack)
    {
        IEnergyStorage storage = getEnergyStorage(stack);
        return 1.0 - (double) storage.getEnergyStored() / storage.getMaxEnergyStored();
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flag)
    {
        IEnergyStorage storage = getEnergyStorage(stack);
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

        if (tier == EBatteryTier.CREATIVE)
        {
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + ": " + TextFormatting.GRAY + I18n.format("gui.energy_infinite"));
        }
        else
        {
            if (storage.getMaxEnergyStored() >= 1000000)
            {
                tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + ": " + TextFormatting.GRAY + formattedEnergyStored + "/" + formattedMaxEnergyStored + " MFE");
            }
            else
            {
                tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + ": " + TextFormatting.GRAY + formattedEnergyStored + "/" + formattedMaxEnergyStored + " kFE");
            }
        }
    }

    public static IEnergyStorage getEnergyStorage(ItemStack stack)
    {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        CustomEnergyStorage storage = new CustomEnergyStorage(
                getTierFromStack(stack).capacity,
                getTierFromStack(stack).maxReceive,
                getTierFromStack(stack).maxExtract,
                0
        );

        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null && nbt.hasKey("Energy"))
            storage.readFromNBT(nbt);

        return new EnergyStorageWrapper(stack, storage);
    }

    private static EBatteryTier getTierFromStack(ItemStack stack)
    {
        if (stack.getItem() instanceof ItemBattery)
            return ((ItemBattery) stack.getItem()).getTier();

        return EBatteryTier.BASIC;
    }

    public static void saveEnergyToStack(ItemStack stack, CustomEnergyStorage storage)
    {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        storage.writeToNBT(stack.getTagCompound());
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
            int received = storage.receiveEnergy(maxReceive, simulate);
            if (!simulate)
                saveEnergyToStack(stack, storage);

            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate)
        {
            int extracted = storage.extractEnergy(maxExtract, simulate);
            if (!simulate)
                saveEnergyToStack(stack, storage);

            return extracted;
        }

        @Override
        public int getEnergyStored()
        {
            return storage.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored()
        {
            return storage.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract()
        {
            return storage.canExtract();
        }

        @Override
        public boolean canReceive()
        {
            return storage.canReceive();
        }
    }

    @Override
    public void getSubItems(net.minecraft.creativetab.@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            items.add(new ItemStack(this));

            ItemStack full = new ItemStack(this);
            CustomEnergyStorage storage = new CustomEnergyStorage(tier.capacity, tier.maxReceive, tier.maxExtract, tier.capacity);
            saveEnergyToStack(full, storage);
            items.add(full);
        }
    }
}