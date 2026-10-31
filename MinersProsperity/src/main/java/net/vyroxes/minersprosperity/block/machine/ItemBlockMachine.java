package net.vyroxes.minersprosperity.block.machine;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.vyroxes.minersprosperity.client.gui.EnergyFormatter;
import net.vyroxes.minersprosperity.client.gui.FluidFormatter;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockMachine extends ItemBlock
{
    private final BlockMachine machineBlock;
    private TileEntityMachine defaultMachine;

    public ItemBlockMachine(BlockMachine block)
    {
        super(block);
        this.machineBlock = block;
    }

    private TileEntityMachine getDefaultMachine()
    {
        if (this.defaultMachine == null) this.defaultMachine = (TileEntityMachine) this.machineBlock.createNewTileEntity(null, 0);
        return this.defaultMachine;
    }

    private int getStoredEnergy(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey("Energy", 3) ? Math.max(0, tag.getInteger("Energy")) : 0;
    }

    private int getEnergyCapacity(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.getInteger("EnergyCapacity") > 0) return tag.getInteger("EnergyCapacity");
        TileEntityMachine machine = this.getDefaultMachine();
        return machine != null && machine.getEnergyStorage() != null ? machine.getMaxEnergyStored() : Math.max(1, this.getStoredEnergy(stack));
    }

    @Override
    public boolean showDurabilityBar(@NotNull ItemStack stack) { return this.getStoredEnergy(stack) > 0; }

    @Override
    public double getDurabilityForDisplay(@NotNull ItemStack stack)
    {
        return 1.0D - MathHelper.clamp((double) this.getStoredEnergy(stack) / this.getEnergyCapacity(stack), 0.0D, 1.0D);
    }

    @Override
    public int getRGBDurabilityForDisplay(@NotNull ItemStack stack)
    {
        return 0xFF0000;
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null && !(this.machineBlock instanceof BlockMachineEnergyStorage)) return;
        int energy = this.getStoredEnergy(stack);
        if (energy > 0 || this.machineBlock instanceof BlockMachineEnergyStorage) tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + TextFormatting.GRAY + ": " + EnergyFormatter.capacity(energy, this.getEnergyCapacity(stack), this.machineBlock instanceof BlockMachineEnergyStorage && ((BlockMachineEnergyStorage) this.machineBlock).getTier() == net.vyroxes.minersprosperity.machine.EnergyStorageTier.CREATIVE));
        if (tag == null || !tag.hasKey("FluidTanks", 9)) return;
        NBTTagList tanks = tag.getTagList("FluidTanks", 10);
        for (int i = 0; i < tanks.tagCount(); i++)
        {
            NBTTagCompound tankTag = tanks.getCompoundTagAt(i);
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tankTag);
            if (fluid == null || fluid.amount <= 0) continue;
            int capacity = tankTag.getInteger("Capacity");
            if (capacity <= 0)
            {
                TileEntityMachine machine = this.getDefaultMachine();
                capacity = machine != null && machine.getFluidTank(i) != null ? machine.getFluidTank(i).getCapacity() : fluid.amount;
            }
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid") + TextFormatting.GRAY + ": " + fluid.getLocalizedName());
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_stored") + TextFormatting.GRAY + ": " + FluidFormatter.capacity(fluid.amount, capacity));
        }
    }
}