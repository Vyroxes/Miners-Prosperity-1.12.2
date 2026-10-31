package net.vyroxes.minersprosperity.integration.jei.liquid_fuel_generator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.integration.jei.RecipeCategories;
import org.jetbrains.annotations.NotNull;

public class LiquidFuelGeneratorRecipeCategory implements IRecipeCategory<LiquidFuelGeneratorRecipe>
{
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;

    public LiquidFuelGeneratorRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createBlankDrawable(130, 52);
        this.slot = helper.getSlotDrawable();
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.LIQUID_FUEL_GENERATOR));
    }

    @Override
    public @NotNull String getUid() { return RecipeCategories.LIQUID_FUEL_GENERATOR; }
    @Override
    public @NotNull String getTitle() { return I18n.format("tile.liquid_fuel_generator.name"); }
    @Override
    public @NotNull String getModName() { return Tags.MODNAME; }
    @Override
    public @NotNull IDrawable getBackground() { return this.background; }
    @Override
    public IDrawable getIcon() { return this.icon; }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft)
    {
        this.slot.draw(minecraft, 1, 17);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, @NotNull LiquidFuelGeneratorRecipe recipe, @NotNull IIngredients ingredients)
    {
        layout.getFluidStacks().init(0, true, 2, 18, 16, 16, recipe.getFuelAmount(), false, null);
        layout.getFluidStacks().set(ingredients);
    }
}