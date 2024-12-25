package net.vyroxes.minersprosperity.util.compat.jei.alloy_furnace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Table;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.objects.blocks.machines.recipes.RecipesAlloyFurnace;

public class AlloyFurnaceRecipeMaker
{
    public static List<AlloyFurnaceRecipe> getRecipes(IJeiHelpers helpers)
    {
        IStackHelper stackHelper = helpers.getStackHelper();
        RecipesAlloyFurnace instance = RecipesAlloyFurnace.getInstance();
        List<AlloyFurnaceRecipe> jeiRecipes = new ArrayList<>();

        for (RecipesAlloyFurnace.Row row : instance.getLookupTable().rows)
        {
            ItemStack input1 = row.getRecipe().input1;
            ItemStack input2 = row.getRecipe().input2;
            ItemStack output = row.getRecipe().result;
            int cookTime = row.getRecipe().totalCookTime;
            int energy = row.getRecipe().energyUsage;

            List<ItemStack> inputs = new ArrayList<>();
            inputs.add(input1);
            inputs.add(input2);

            AlloyFurnaceRecipe recipe = new AlloyFurnaceRecipe(inputs, output, cookTime, energy);

            System.out.println("Receptura: " + recipe);
            jeiRecipes.add(recipe);
        }

        return jeiRecipes;
    }
}