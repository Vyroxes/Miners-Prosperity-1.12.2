package net.vyroxes.minersprosperity.integration.jei.liquid_fuel_generator;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.vyroxes.minersprosperity.inventory.container.ContainerLiquidFuelGenerator;
import org.jetbrains.annotations.NotNull;

public class LiquidFuelGeneratorRecipeTransferHandler implements IRecipeTransferHandler<ContainerLiquidFuelGenerator>
{
    private final IRecipeTransferHandlerHelper helper;

    public LiquidFuelGeneratorRecipeTransferHandler(IRecipeTransferHandlerHelper helper)
    {
        this.helper = helper;
    }

    @Override
    public @NotNull Class<ContainerLiquidFuelGenerator> getContainerClass()
    {
        return ContainerLiquidFuelGenerator.class;
    }

    @Override
    public IRecipeTransferError transferRecipe(@NotNull ContainerLiquidFuelGenerator container, IRecipeLayout layout, @NotNull EntityPlayer player, boolean maxTransfer, boolean doTransfer)
    {
        FluidStack required = layout.getFluidStacks().getGuiIngredients().get(0).getDisplayedIngredient();
        if (required == null) return this.helper.createInternalError();

        ItemStack input = container.getSlot(0).getStack();
        if (!input.isEmpty())
        {
            FluidStack fluid = FluidUtil.getFluidContained(input);
            if (fluid != null && fluid.amount > 0 && required.isFluidEqual(fluid)) return null;
            return this.helper.createUserErrorWithTooltip(I18n.format("jei.liquid_fuel_generator.input_occupied"));
        }

        for (int slot = 3; slot < 39; slot++)
        {
            ItemStack stack = container.getSlot(slot).getStack();
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            if (fluid == null || fluid.amount <= 0 || !required.isFluidEqual(fluid) || !container.getSlot(0).isItemValid(stack)) continue;

            if (doTransfer)
            {
                Minecraft.getMinecraft().playerController.windowClick(container.windowId, slot, 0, ClickType.QUICK_MOVE, player);
            }
            return null;
        }

        return this.helper.createUserErrorWithTooltip(I18n.format("jei.liquid_fuel_generator.missing_container"));
    }
}