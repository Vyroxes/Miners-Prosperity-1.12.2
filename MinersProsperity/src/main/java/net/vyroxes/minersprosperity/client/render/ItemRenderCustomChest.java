package net.vyroxes.minersprosperity.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.block.chest.BlockCustomChest;
import net.vyroxes.minersprosperity.block.chest.ChestTier;
import org.jetbrains.annotations.NotNull;

public class ItemRenderCustomChest extends TileEntityItemStackRenderer {
    private static final ModelChest MODEL = new ModelChest();

    @Override
    public void renderByItem(@NotNull ItemStack itemStackIn, float partialTicks) {
        if (itemStackIn.isEmpty()) return;

        ChestTier tier = null;
        if (itemStackIn.getItem() instanceof ItemBlock ib) {
            if (ib.getBlock() instanceof BlockCustomChest chest) {
                tier = chest.getTier();
            }
        }
        if (tier == null) return;

        ResourceLocation tex = new ResourceLocation(Tags.MODID, "textures/entity/chest/" + tier.translationKey + ".png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 1.0F, 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        MODEL.chestLid.rotateAngleX = 0.0F;
        MODEL.renderAll();
        GlStateManager.popMatrix();
    }
}