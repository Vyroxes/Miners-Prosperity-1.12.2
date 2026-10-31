package net.vyroxes.minersprosperity.integration.jei.solid_fuel_generator;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.config.ModConfig;

public class SolidFuelGeneratorRecipe implements IRecipeWrapper
{
    private final ItemStack fuel;
    private final int burnTime;

    public SolidFuelGeneratorRecipe(ItemStack fuel, int burnTime)
    {
        this.fuel = fuel.copy();
        this.fuel.setCount(1);
        this.burnTime = burnTime;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInput(ItemStack.class, this.fuel);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        int generation = ModConfig.MACHINES.solidFuelGeneratorBaseGeneration;
        minecraft.fontRenderer.drawString(I18n.format("gui.solid_fuel_generator.burn_time", this.burnTime / 20.0), 26, 4, 0x808080);
        minecraft.fontRenderer.drawString(generation + " FE/t", 26, 17, 0x808080);
        minecraft.fontRenderer.drawString((long) generation * this.burnTime + " FE", 26, 30, 0x808080);
    }
}
