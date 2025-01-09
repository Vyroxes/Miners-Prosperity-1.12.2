package net.vyroxes.minersprosperity.util.compat.jei.alloy_furnace;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;
import net.vyroxes.minersprosperity.util.compat.jei.JEICompat;
import java.util.List;

import java.awt.*;

public class AlloyFurnaceRecipe implements IRecipeWrapper
{
    private final List<List<ItemStack>> inputs;
    private final ItemStack output;
    private final int cookTime;
    private final int energy;

    public AlloyFurnaceRecipe(List<List<ItemStack>> inputs, ItemStack output, int cookTime, int energy)
    {
        this.inputs = inputs;
        this.output = output;
        this.cookTime = cookTime;
        this.energy = energy;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        FontRenderer renderer = minecraft.fontRenderer;
        RecipesAlloyFurnace recipes = RecipesAlloyFurnace.getInstance();
        float experience = recipes.getExperience(output);

        String experienceString = JEICompat.translateToLocalFormatted("gui.jei.category.smelting.experience", experience);
        int experienceStringWidth = renderer.getStringWidth(experienceString);
        renderer.drawString(experienceString, recipeWidth - experienceStringWidth, 0, Color.GRAY.getRGB());

        String cookTimeString = cookTime/20 + " seconds";
        int cookTimeStringWidth = renderer.getStringWidth(cookTimeString);
        renderer.drawString(cookTimeString, 1 + (recipeWidth - cookTimeStringWidth)/2, 37, Color.GRAY.getRGB());

        String energyString = energy + " FE/t";
        int energyStringWidth = renderer.getStringWidth(energyString);
        renderer.drawString(energyString, 1 + (recipeWidth - energyStringWidth)/2, 47, Color.GRAY.getRGB());
    }
}