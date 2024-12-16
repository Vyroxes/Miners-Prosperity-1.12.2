package net.vyroxes.minersprosperity.util.compat.jei.crusher;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.Reference;
import net.vyroxes.minersprosperity.init.BlockInit;
import net.vyroxes.minersprosperity.util.compat.jei.RecipeCategories;

public class CrusherRecipeCategory extends AbstractCrusherRecipeCategory<CrusherRecipe>
{
    private final IDrawable background;
    private final String name;
    private final IDrawable icon;

    public CrusherRecipeCategory(IGuiHelper helper)
    {
        super(helper);
        background = helper.createDrawable(TEXTURES, 40, 16, 99, 63);
        //background = helper.createDrawable(TEXTURES, 42, 16, 95, 63);
        name = "Crusher";
        icon = helper.createDrawableIngredient(new ItemStack(BlockInit.CRUSHER));
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
        animatedFlame.draw(minecraft, 16, 20);
        animatedArrow.draw(minecraft, 39, 18);
        //animatedFlame.draw(minecraft, 14, 20);
        //animatedArrow.draw(minecraft, 37, 18);

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
        return RecipeCategories.CRUSHER;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrusherRecipe recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(input1, true, 2, 0);
        stacks.init(input2, true, 28, 0);
        stacks.init(output, false, 75, 18);
        //stacks.init(input1, true, 0, 0);
        //stacks.init(input2, true, 26, 0);
        //stacks.init(output, false, 73, 18);
        stacks.set(ingredients);
    }
}