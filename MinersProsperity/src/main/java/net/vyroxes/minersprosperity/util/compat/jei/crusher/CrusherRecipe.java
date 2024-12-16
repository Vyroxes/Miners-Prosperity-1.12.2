package net.vyroxes.minersprosperity.util.compat.jei.crusher;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesCrusher;
import net.vyroxes.minersprosperity.util.compat.jei.JEICompat;
import java.util.List;

import java.awt.*;

public class CrusherRecipe implements IRecipeWrapper
{
    private final List<ItemStack> inputs;
    private final ItemStack output;
    private final int cookTime;

    public CrusherRecipe(List<ItemStack> inputs, ItemStack output, int cookTime)
    {
        this.inputs = inputs;
        this.output = output;
        this.cookTime = cookTime;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        RecipesCrusher recipes = RecipesCrusher.getInstance();
        float experience = recipes.getCrusherExperience(output);

        if (experience > 0)
        {
            String experienceString = JEICompat.translateToLocalFormatted("gui.jei.category.smelting.experience", experience);
            FontRenderer renderer = minecraft.fontRenderer;
            int experienceStringWidth = renderer.getStringWidth(experienceString);
            renderer.drawString(experienceString, recipeWidth - experienceStringWidth, 0, Color.GRAY.getRGB());
        }

        String cookTimeString = cookTime/20 + " seconds";
        FontRenderer renderer = minecraft.fontRenderer;
        int cookTimeStringWidth = renderer.getStringWidth(cookTimeString);
        renderer.drawString(cookTimeString, 1 + (recipeWidth - cookTimeStringWidth)/2, recipeHeight - 7, Color.GRAY.getRGB());
    }
}