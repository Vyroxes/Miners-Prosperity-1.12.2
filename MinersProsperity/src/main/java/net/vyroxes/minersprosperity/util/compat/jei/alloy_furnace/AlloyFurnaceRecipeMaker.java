package net.vyroxes.minersprosperity.util.compat.jei.alloy_furnace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
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
            List<ItemStack> input1Variants = getInputsFromStackOrOreDict(recipe.input1, recipe.oreDict1, recipe.quantity1);
            List<ItemStack> input2Variants = getInputsFromStackOrOreDict(recipe.input2, recipe.oreDict2, recipe.quantity2);
            ItemStack output = recipe.result;
            int cookTime = recipe.totalCookTime;
            int energy = recipe.energyUsage;

            List<List<ItemStack>> inputs = new ArrayList<>();
            inputs.add(input1Variants);
            inputs.add(input2Variants);

            AlloyFurnaceRecipe jeiRecipe = new AlloyFurnaceRecipe(inputs, output, cookTime, energy);
            jeiRecipes.add(jeiRecipe);
        }

        return jeiRecipes;
    }

    private static List<ItemStack> getInputsFromStackOrOreDict(ItemStack stack, String oreDictName, int amount)
    {
        if (stack != null && !stack.isEmpty())
        {
            ItemStack modifiedStack = stack.copy();
            modifiedStack.setCount(amount);
            return Collections.singletonList(modifiedStack);
        }

        if (oreDictName != null && !oreDictName.isEmpty())
        {
            int oreID = OreDictionary.getOreID(oreDictName);
            if (oreID != -1)
            {
                List<ItemStack> oreStacks = new ArrayList<>();
                for (ItemStack oreStack : OreDictionary.getOres(oreDictName))
                {
                    ItemStack modifiedStack = oreStack.copy();
                    modifiedStack.setCount(amount);
                    oreStacks.add(modifiedStack);
                }
                return oreStacks;
            }
        }

        return Collections.emptyList();
    }
}