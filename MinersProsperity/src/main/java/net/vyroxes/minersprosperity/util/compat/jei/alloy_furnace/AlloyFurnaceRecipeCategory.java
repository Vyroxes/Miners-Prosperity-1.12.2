package net.vyroxes.minersprosperity.util.compat.jei.alloy_furnace;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.util.compat.jei.RecipeCategories;
import org.jetbrains.annotations.NotNull;

public class AlloyFurnaceRecipeCategory extends AbstractAlloyFurnaceRecipeCategory<AlloyFurnaceRecipe>
{
    private final IDrawable background;
    private final String name;
    private final IDrawable icon;

    public AlloyFurnaceRecipeCategory(IGuiHelper helper)
    {
        super(helper);
        background = helper.createDrawable(ALLOY_FURNACE, 33, 21, 102, 54);
        name = I18n.format("tile.alloy_furnace.name");
        icon = helper.createDrawableIngredient(new ItemStack(BlockInit.ALLOY_FURNACE));
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public @NotNull IDrawable getBackground()
    {
        return background;
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft)
    {
        animatedArrow.draw(minecraft, 45, 14);
    }

    @Override
    public @NotNull String getTitle()
    {
        return name;
    }

    @Override
    public @NotNull String getModName()
    {
        return Tags.MODNAME;
    }

    @Override
    public @NotNull String getUid()
    {
        return RecipeCategories.ALLOY_FURNACE;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull AlloyFurnaceRecipe recipeWrapper, @NotNull IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(input1, true, -1, 13);
        stacks.init(input2, true, 19, 13);
        stacks.init(output, false, 81, 13);
        stacks.set(ingredients);
    }
}