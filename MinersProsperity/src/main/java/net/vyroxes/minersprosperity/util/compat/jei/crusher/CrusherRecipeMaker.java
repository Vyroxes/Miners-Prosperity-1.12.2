package net.vyroxes.minersprosperity.util.compat.jei.crusher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Table;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesCrusher;

public class CrusherRecipeMaker
{
    public static List<CrusherRecipe> getRecipes(IJeiHelpers helpers)
    {
        IStackHelper stackHelper = helpers.getStackHelper();
        RecipesCrusher instance = RecipesCrusher.getInstance();
        Table<ItemStack, ItemStack, RecipesCrusher.RecipeData> recipes = instance.getRecipesList();
        List<CrusherRecipe> jeiRecipes = new ArrayList<>();

        for (Map.Entry<ItemStack, Map<ItemStack, RecipesCrusher.RecipeData>> entry : recipes.columnMap().entrySet())
        {
            ItemStack input1 = entry.getKey();
            for (Map.Entry<ItemStack, RecipesCrusher.RecipeData> ent : entry.getValue().entrySet())
            {
                ItemStack input2 = ent.getKey();
                RecipesCrusher.RecipeData recipeData = ent.getValue();

                ItemStack output = recipeData.getResult();
                int cookTime = recipeData.getCookTime();

                List<ItemStack> inputs = new ArrayList<>();
                inputs.add(input1);
                inputs.add(input2);

                CrusherRecipe recipe = new CrusherRecipe(inputs, output, cookTime);
                jeiRecipes.add(recipe);
            }
        }

        return jeiRecipes;
    }
}