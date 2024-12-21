package net.vyroxes.minersprosperity.util.compat.jei.alloy_furnace;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.Reference;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.util.compat.jei.RecipeCategories;

public class AlloyFurnaceRecipeCategory extends AbstractAlloyFurnaceRecipeCategory<AlloyFurnaceRecipe>
{
    private final IDrawable background;
    private final String name;
    private final IDrawable icon;

    public AlloyFurnaceRecipeCategory(IGuiHelper helper)
    {
        super(helper);
        background = helper.createDrawable(TEXTURES, 33, 21, 110, 54);
        name = I18n.format("tile.alloy_furnace.name");
        icon = helper.createDrawableIngredient(new ItemStack(BlockInit.ALLOY_FURNACE));
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {
        animatedArrow.draw(minecraft, 48, 15);
    }

    @Override
    public String getTitle()
    {
        return name;
    }

    @Override
    public String getModName()
    {
        return Reference.NAME;
    }

    @Override
    public String getUid()
    {
        return RecipeCategories.ALLOY_FURNACE;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlloyFurnaceRecipe recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(input1, true, 4, 13);
        stacks.init(input2, true, 22, 13);
        stacks.init(output, false, 84, 13);
        stacks.set(ingredients);
    }
}