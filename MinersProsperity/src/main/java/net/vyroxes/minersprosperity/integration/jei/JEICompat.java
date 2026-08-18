package net.vyroxes.minersprosperity.integration.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.inventory.container.ContainerAlloyFurnace;
import net.vyroxes.minersprosperity.inventory.container.ContainerElectricFurnace;
import net.vyroxes.minersprosperity.inventory.container.ContainerMetalPress;
import net.vyroxes.minersprosperity.inventory.container.ContainerPulverizer;
import net.vyroxes.minersprosperity.client.gui.GuiAlloyFurnace;
import net.vyroxes.minersprosperity.client.gui.GuiElectricFurnace;
import net.vyroxes.minersprosperity.client.gui.GuiMetalPress;
import net.vyroxes.minersprosperity.client.gui.GuiPulverizer;
import net.vyroxes.minersprosperity.integration.jei.alloy_furnace.AlloyFurnaceRecipeCategory;
import net.vyroxes.minersprosperity.integration.jei.alloy_furnace.AlloyFurnaceRecipeMaker;
import net.vyroxes.minersprosperity.integration.jei.metal_press.MetalPressRecipeCategory;
import net.vyroxes.minersprosperity.integration.jei.metal_press.MetalPressRecipeMaker;
import net.vyroxes.minersprosperity.integration.jei.pulverizer.PulverizerRecipeCategory;
import net.vyroxes.minersprosperity.integration.jei.pulverizer.PulverizerRecipeMaker;
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
        registry.addRecipeCategories(new PulverizerRecipeCategory(gui));
        registry.addRecipeCategories(new MetalPressRecipeCategory(gui));
    }

    @Override
    public void register(IModRegistry registry)
    {
        registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.RESIN_BLOCK));
        registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.RESIN_BLOCK_EMPTY));

        final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();

        registry.addRecipes(AnvilCustomRecipeMaker.getRepairRecipes(jeiHelpers.getVanillaRecipeFactory()), RecipeCategories.ANVIL);

        registry.addRecipes(AlloyFurnaceRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.ALLOY_FURNACE);
        registry.addRecipeClickArea(GuiAlloyFurnace.class, 79, 36, 21, 15, RecipeCategories.ALLOY_FURNACE);
        recipeTransfer.addRecipeTransferHandler(ContainerAlloyFurnace.class, RecipeCategories.ALLOY_FURNACE, 0, 2, 4, 36);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.ALLOY_FURNACE), RecipeCategories.ALLOY_FURNACE);

        registry.addRecipes(PulverizerRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.PULVERIZER);
        registry.addRecipeClickArea(GuiPulverizer.class, 69, 36, 21, 15, RecipeCategories.PULVERIZER);
        recipeTransfer.addRecipeTransferHandler(ContainerPulverizer.class, RecipeCategories.PULVERIZER, 0, 1, 3, 36);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.PULVERIZER), RecipeCategories.PULVERIZER);

        registry.addRecipes(MetalPressRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.METAL_PRESS);
        registry.addRecipeClickArea(GuiMetalPress.class, 79, 36, 21, 15, RecipeCategories.METAL_PRESS);
        recipeTransfer.addRecipeTransferHandler(ContainerMetalPress.class, RecipeCategories.METAL_PRESS, 0, 2, 4, 36);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.METAL_PRESS), RecipeCategories.METAL_PRESS);

        registry.addRecipeClickArea(GuiElectricFurnace.class, 69, 36, 21, 15, RecipeCategories.SMELTING);
        recipeTransfer.addRecipeTransferHandler(ContainerElectricFurnace.class, RecipeCategories.SMELTING, 0, 1, 3, 36);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.ELECTRIC_FURNACE), RecipeCategories.SMELTING);
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