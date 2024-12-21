package net.vyroxes.minersprosperity.util.compat.jei.alloy_furnace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Table;
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
        Table<ItemStack, ItemStack, RecipesAlloyFurnace.RecipeData> recipes = instance.getRecipesList();
        List<AlloyFurnaceRecipe> jeiRecipes = new ArrayList<>();

        for (Map.Entry<ItemStack, Map<ItemStack, RecipesAlloyFurnace.RecipeData>> entry : recipes.columnMap().entrySet())
        {
            ItemStack input1 = entry.getKey();
            for (Map.Entry<ItemStack, RecipesAlloyFurnace.RecipeData> ent : entry.getValue().entrySet())
            {
                ItemStack input2 = ent.getKey();
                RecipesAlloyFurnace.RecipeData recipeData = ent.getValue();

                ItemStack output = recipeData.getResult();
                int cookTime = recipeData.getCookTime();
                int energy = recipeData.getEnergyUsage();

                List<ItemStack> inputs = new ArrayList<>();
                inputs.add(input1);
                inputs.add(input2);

                AlloyFurnaceRecipe recipe = new AlloyFurnaceRecipe(inputs, output, cookTime, energy);
                jeiRecipes.add(recipe);
            }
        }

        return jeiRecipes;
    }
}