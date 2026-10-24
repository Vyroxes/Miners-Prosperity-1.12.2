package net.vyroxes.minersprosperity.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.util.MachineRelocatorUtils;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemMachineRelocator extends ItemBase {
    public ItemMachineRelocator(String name)
    {
        super(name);
        this.setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation(Tags.MODID, "filled"), (stack, world, entity) -> MachineRelocatorUtils.hasMachine(stack) ? 1.0F : 0.0F);
    }

    @Override
    public @NotNull String getItemStackDisplayName(@NotNull ItemStack stack)
    {
        String name = super.getItemStackDisplayName(stack);
        if (!MachineRelocatorUtils.hasMachine(stack)) return name;

        String machineName = MachineRelocatorUtils.getStoredMachineName(stack);

        return machineName.isEmpty() ? name : name + " (" + machineName + ")";
    }

    @Override
    public boolean hasEffect(@NotNull ItemStack stack)
    {
        return MachineRelocatorUtils.hasMachine(stack) || super.hasEffect(stack);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);

        if (!MachineRelocatorUtils.hasMachine(stack))
        {
            tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.machine_relocator.empty"));
            return;
        }

        String name = MachineRelocatorUtils.getStoredMachineName(stack);

        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.machine_relocator.machine") + ": " + TextFormatting.AQUA + name);
        if (flag.isAdvanced()) tooltip.add(TextFormatting.DARK_GRAY + MachineRelocatorUtils.getStoredBlockId(stack));
    }
}