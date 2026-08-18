package net.vyroxes.minersprosperity.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.vyroxes.minersprosperity.item.ItemShieldCustom;

public class ItemRenderCustomShield extends TileEntityItemStackRenderer {
    private final ModelShield MODEL = new ModelShield();

    @Override
    public void renderByItem(ItemStack stack)
    {
        if (!(stack.getItem() instanceof ItemShieldCustom shield)) return;

        Minecraft.getMinecraft().getTextureManager().bindTexture(shield.getTier().texture);

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0F, -1.0F, -1.0F);

        MODEL.render();

        GlStateManager.popMatrix();
    }
}