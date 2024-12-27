package net.vyroxes.minersprosperity.util.compat.jei.alloy_furnace;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;
import net.vyroxes.minersprosperity.Tags;

public abstract class AbstractAlloyFurnaceRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T>
{
    protected static final ResourceLocation ALLOY_FURNACE = new ResourceLocation(Tags.MODID + ":textures/gui/alloy_furnace.png");
    protected static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID + ":textures/gui/gui_elements.png");

    protected static final int input1 = 0;
    protected static final int input2 = 1;
    protected static final int output = 3;

    protected final IDrawableAnimated animatedArrow;

    public AbstractAlloyFurnaceRecipeCategory(IGuiHelper helper)
    {
        IDrawableStatic staticArrow = helper.createDrawable(GUI_ELEMENTS, 232, 0, 24, 17);
        animatedArrow = helper.createAnimatedDrawable(staticArrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }
}