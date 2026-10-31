package net.vyroxes.minersprosperity.integration.jei.material_tree;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.vyroxes.minersprosperity.Tags;
import java.util.List;

public class MaterialTreeRecipe implements IRecipeWrapper
{
    private final List<List<ItemStack>> forms;
    private final int layout;
    private final ResourceLocation texture;

    public MaterialTreeRecipe(List<List<ItemStack>> forms, int layout)
    {
        this.forms = forms;
        this.layout = layout;
        String suffix = layout == 1 ? "" : "_" + layout;
        this.texture = new ResourceLocation(Tags.MODID, "textures/gui/material_tree" + suffix + ".png");
    }

    public int getLayout() { return this.layout; }

    public List<ItemStack> getForm(int index) { return this.forms.get(index); }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(this.texture);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 94, 94, 94, 94);
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputLists(ItemStack.class, this.forms);
        ingredients.setOutputLists(ItemStack.class, this.forms);
    }
}