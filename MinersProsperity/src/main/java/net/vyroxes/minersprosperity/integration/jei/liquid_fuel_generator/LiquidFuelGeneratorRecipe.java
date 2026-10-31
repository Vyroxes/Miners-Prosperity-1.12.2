package net.vyroxes.minersprosperity.integration.jei.liquid_fuel_generator;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;

public class LiquidFuelGeneratorRecipe implements IRecipeWrapper
{
    private final net.vyroxes.minersprosperity.machine.recipe.LiquidFuelGeneratorRecipe.Recipe recipe;

    public LiquidFuelGeneratorRecipe(net.vyroxes.minersprosperity.machine.recipe.LiquidFuelGeneratorRecipe.Recipe recipe)
    {
        this.recipe = recipe;
    }

    public int getFuelAmount() { return this.recipe.input.amount; }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInput(FluidStack.class, this.recipe.input.copy());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int width, int height, int mouseX, int mouseY)
    {
        minecraft.fontRenderer.drawString(I18n.format("gui.liquid_fuel_generator.amount", this.recipe.input.amount), 26, 3, 0x808080);
        minecraft.fontRenderer.drawString(I18n.format("gui.solid_fuel_generator.burn_time", this.recipe.totalCookTime / 20.0), 26, 15, 0x808080);
        minecraft.fontRenderer.drawString(this.recipe.energyGeneration + " FE/t", 26, 27, 0x808080);
        minecraft.fontRenderer.drawString((long) this.recipe.energyGeneration * this.recipe.totalCookTime + " FE", 26, 39, 0x808080);
    }
}