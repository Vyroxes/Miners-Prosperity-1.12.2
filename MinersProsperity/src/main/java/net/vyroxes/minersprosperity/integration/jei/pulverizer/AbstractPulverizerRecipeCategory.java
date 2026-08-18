package net.vyroxes.minersprosperity.integration.jei.pulverizer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;
import net.vyroxes.minersprosperity.Tags;

public abstract class AbstractPulverizerRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T>
{
    protected static final ResourceLocation PULVERIZER = new ResourceLocation(Tags.MODID + ":textures/gui/pulverizer.png");
    protected static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID + ":textures/gui/gui_elements.png");

    protected static final int input = 0;
    protected static final int output = 2;

    protected final IDrawableAnimated animatedArrow;

    public AbstractPulverizerRecipeCategory(IGuiHelper helper)
    {
        IDrawableStatic staticArrow = helper.createDrawable(GUI_ELEMENTS, 232, 140, 24, 17);
        animatedArrow = helper.createAnimatedDrawable(staticArrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }
}