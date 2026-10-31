package net.vyroxes.minersprosperity.integration.jei.material_tree;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.integration.jei.RecipeCategories;
import org.jetbrains.annotations.NotNull;

public class MaterialTreeRecipeCategory implements IRecipeCategory<MaterialTreeRecipe>
{
    private final IDrawable background;
    private final IDrawable icon;

    public MaterialTreeRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createBlankDrawable(94, 94);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModItems.COPPER_INGOT));
    }

    @Override
    public @NotNull String getUid() { return RecipeCategories.MATERIAL_TREE; }

    @Override
    public @NotNull String getTitle() { return I18n.format("gui.jei.category.material_tree"); }

    @Override
    public @NotNull String getModName() { return Tags.MODNAME; }

    @Override
    public @NotNull IDrawable getBackground() { return this.background; }

    @Override
    public IDrawable getIcon() { return this.icon; }

    @Override
    public void setRecipe(IRecipeLayout layout, @NotNull MaterialTreeRecipe recipe, @NotNull IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = layout.getItemStacks();
        int variant = recipe.getLayout();
        stacks.init(0, true, 38, 38);
        stacks.set(0, recipe.getForm(0));

        if (variant <= 3)
        {
            stacks.init(4, true, 10, 18);
            stacks.set(4, recipe.getForm(4));
            stacks.init(5, true, 10, 38);
            stacks.set(5, recipe.getForm(5));
            stacks.init(6, true, 10, 58);
            stacks.set(6, recipe.getForm(6));
        }

        if (variant == 1 || variant == 3)
        {
            stacks.init(1, true, 38, 66);
            stacks.set(1, recipe.getForm(1));
        }

        if (variant == 1 || variant == 5)
        {
            stacks.init(2, true, 66, 38);
            stacks.set(2, recipe.getForm(2));
        }

        if (variant == 2 || variant == 4) stacks.init(3, true, 38, 66);
        else if (variant == 3) stacks.init(3, true, 66, 38);
        else stacks.init(3, true, 66, 10);

        stacks.set(3, recipe.getForm(3));
    }
}