package net.vyroxes.minersprosperity.integration.jei.metal_press;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.machine.recipe.MetalPressRecipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MetalPressRecipeMaker
{
    public static List<net.vyroxes.minersprosperity.integration.jei.metal_press.MetalPressRecipe> getRecipes(IJeiHelpers helpers)
    {
        IStackHelper stackHelper = helpers.getStackHelper();
        MetalPressRecipe instance = MetalPressRecipe.getInstance();
        List<net.vyroxes.minersprosperity.integration.jei.metal_press.MetalPressRecipe> jeiRecipes = new ArrayList<>();

        for (MetalPressRecipe.Recipe recipe : instance.getLookupTable().getRecipes())
        {
            List<ItemStack> input1Variants = getInputsFromStackOrOreDict(recipe.input1, recipe.oreDict1, recipe.quantity1);
            List<ItemStack> input2Variants = Arrays.asList(recipe.getDies(recipe.dieType));
            ItemStack output = recipe.result;
            int cookTime = recipe.totalCookTime;
            int energy = recipe.energyUsage;

            List<List<ItemStack>> inputs = new ArrayList<>();
            inputs.add(input1Variants);
            inputs.add(input2Variants);

            net.vyroxes.minersprosperity.integration.jei.metal_press.MetalPressRecipe jeiRecipe = new net.vyroxes.minersprosperity.integration.jei.metal_press.MetalPressRecipe(inputs, output, cookTime, energy);
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