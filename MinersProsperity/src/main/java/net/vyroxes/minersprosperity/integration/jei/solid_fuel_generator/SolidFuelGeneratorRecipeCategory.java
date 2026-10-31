package net.vyroxes.minersprosperity.integration.jei.solid_fuel_generator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.integration.jei.RecipeCategories;
import org.jetbrains.annotations.NotNull;

public class SolidFuelGeneratorRecipeCategory implements IRecipeCategory<SolidFuelGeneratorRecipe>
{
    private final IDrawable background;
    private final IDrawable icon;

    public SolidFuelGeneratorRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createBlankDrawable(130, 44);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.SOLID_FUEL_GENERATOR));
    }

    @Override
    public @NotNull String getUid() { return RecipeCategories.SOLID_FUEL_GENERATOR; }

    @Override
    public @NotNull String getTitle() { return I18n.format("tile.solid_fuel_generator.name"); }

    @Override
    public @NotNull String getModName() { return Tags.MODNAME; }

    @Override
    public @NotNull IDrawable getBackground() { return this.background; }

    @Override
    public IDrawable getIcon() { return this.icon; }

    @Override
    public void setRecipe(IRecipeLayout layout, @NotNull SolidFuelGeneratorRecipe recipe, @NotNull IIngredients ingredients)
    {
        layout.getItemStacks().init(0, true, 1, 13);
        layout.getItemStacks().set(ingredients);
    }
}
