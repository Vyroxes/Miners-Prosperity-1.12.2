package net.vyroxes.minersprosperity.util.compat.jei.alloy_furnace;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;

public class AlloyFurnaceRecipeMaker
{
    public static List<AlloyFurnaceRecipe> getRecipes(IJeiHelpers helpers)
    {
        IStackHelper stackHelper = helpers.getStackHelper();
        RecipesAlloyFurnace instance = RecipesAlloyFurnace.getInstance();
        List<AlloyFurnaceRecipe> jeiRecipes = new ArrayList<>();

        for (RecipesAlloyFurnace.Recipe recipe : instance.getLookupTable().getRecipes())
        {
            ItemStack input1 = recipe.input1;
            ItemStack input2 = recipe.input2;
            ItemStack output = recipe.result;
            int cookTime = recipe.totalCookTime;
            int energy = recipe.energyUsage;

            List<ItemStack> inputs = new ArrayList<>();
            inputs.add(input1);
            inputs.add(input2);

            AlloyFurnaceRecipe jeiRecipe = new AlloyFurnaceRecipe(inputs, output, cookTime, energy);

            jeiRecipes.add(jeiRecipe);
        }

        return jeiRecipes;
    }
}