package net.vyroxes.minersprosperity.integration.jei.metal_press;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;
import net.vyroxes.minersprosperity.Tags;

public abstract class AbstractMetalPressRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T>
{
    protected static final ResourceLocation METAL_PRESS = new ResourceLocation(Tags.MODID + ":textures/gui/metal_press.png");
    protected static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID + ":textures/gui/gui_elements.png");

    protected static final int input1 = 0;
    protected static final int die = 1;
    protected static final int output = 3;

    protected final IDrawableAnimated animatedArrow;

    public AbstractMetalPressRecipeCategory(IGuiHelper helper)
    {
        IDrawableStatic staticArrow = helper.createDrawable(GUI_ELEMENTS, 232, 86, 24, 17);
        animatedArrow = helper.createAnimatedDrawable(staticArrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }
}