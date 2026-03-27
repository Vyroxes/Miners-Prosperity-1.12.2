package net.vyroxes.minersprosperity.util.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.objects.containers.ContainerAlloyFurnace;
import net.vyroxes.minersprosperity.objects.containers.ContainerElectricFurnace;
import net.vyroxes.minersprosperity.objects.containers.ContainerMetalPress;
import net.vyroxes.minersprosperity.objects.guis.GuiAlloyFurnace;
import net.vyroxes.minersprosperity.objects.guis.GuiElectricFurnace;
import net.vyroxes.minersprosperity.objects.guis.GuiMetalPress;
import net.vyroxes.minersprosperity.util.compat.jei.alloy_furnace.AlloyFurnaceRecipeCategory;
import net.vyroxes.minersprosperity.util.compat.jei.alloy_furnace.AlloyFurnaceRecipeMaker;
import net.vyroxes.minersprosperity.util.compat.jei.metal_press.MetalPressRecipeCategory;
import net.vyroxes.minersprosperity.util.compat.jei.metal_press.MetalPressRecipeMaker;

import java.util.IllegalFormatException;

@JEIPlugin
public class JEICompat implements IModPlugin

{
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        final IJeiHelpers helpers = registry.getJeiHelpers();
        final IGuiHelper gui = helpers.getGuiHelper();

        registry.addRecipeCategories(new AlloyFurnaceRecipeCategory(gui));
        registry.addRecipeCategories(new MetalPressRecipeCategory(gui));
    }

    @Override
    public void register(IModRegistry registry)
    {
        final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();

        registry.addRecipes(AnvilCustomRecipeMaker.getRepairRecipes(jeiHelpers.getVanillaRecipeFactory()), RecipeCategories.ANVIL);

        registry.addRecipes(AlloyFurnaceRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.ALLOY_FURNACE);
        registry.addRecipeClickArea(GuiAlloyFurnace.class, 79, 36, 21, 15, RecipeCategories.ALLOY_FURNACE);
        recipeTransfer.addRecipeTransferHandler(ContainerAlloyFurnace.class, RecipeCategories.ALLOY_FURNACE, 0, 2, 4, 36);
        registry.addRecipeCatalyst(new ItemStack(BlockInit.ALLOY_FURNACE), RecipeCategories.ALLOY_FURNACE);

        registry.addRecipes(MetalPressRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.METAL_PRESS);
        registry.addRecipeClickArea(GuiMetalPress.class, 79, 36, 21, 15, RecipeCategories.METAL_PRESS);
        recipeTransfer.addRecipeTransferHandler(ContainerMetalPress.class, RecipeCategories.METAL_PRESS, 0, 2, 4, 36);
        registry.addRecipeCatalyst(new ItemStack(BlockInit.METAL_PRESS), RecipeCategories.METAL_PRESS);

        registry.addRecipeClickArea(GuiElectricFurnace.class, 69, 36, 21, 15, RecipeCategories.SMELTING);
        recipeTransfer.addRecipeTransferHandler(ContainerElectricFurnace.class, RecipeCategories.SMELTING, 0, 1, 3, 36);
        registry.addRecipeCatalyst(new ItemStack(BlockInit.ELECTRIC_FURNACE), RecipeCategories.SMELTING);
    }

    @SuppressWarnings("deprecation")
    public static String translateToLocal(String key)
    {
        if(I18n.canTranslate(key)) return I18n.translateToLocal(key);
        else return I18n.translateToFallback(key);
    }

    public static String translateToLocalFormatted(String key, Object... format)
    {
        String s = translateToLocal(key);
        try
        {
            return String.format(s, format);
        }
        catch (IllegalFormatException e)
        {
            return "Format error " + s;
        }
    }
}