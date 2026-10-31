package net.vyroxes.minersprosperity.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;
import net.vyroxes.minersprosperity.tile.TileEntityFluidTank;
import org.lwjgl.opengl.GL11;

public class FluidTankTESR extends TileEntitySpecialRenderer<TileEntityFluidTank>
{
    @Override
    public void render(TileEntityFluidTank tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        FluidStack fluid = tile.getFluidTank(0).getFluid();
        if (fluid == null || fluid.amount <= 0) return;
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill(fluid).toString());
        int color = fluid.getFluid().getColor(fluid);
        double min = 3.01 / 16.0;
        double max = 12.99 / 16.0;
        double height = (max - min) * Math.min(1.0, (double) fluid.amount / tile.getFluidTank(0).getCapacity());
        double bottom = fluid.getFluid().isGaseous(fluid) ? max - height : min;
        double top = bottom + height;
        float u0 = sprite.getMinU(), u1 = sprite.getMaxU();
        float v0 = sprite.getMinV(), v1 = sprite.getMaxV();
        float sideV = sprite.getInterpolatedV(16 * height);
        int light = tile.getWorld().getCombinedLight(tile.getPos(), fluid.getFluid().getLuminosity(fluid));
        float previousX = OpenGlHelper.lastBrightnessX, previousY = OpenGlHelper.lastBrightnessY;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, light & 65535, light >>> 16);
        GlStateManager.color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >>> 24) / 255.0F * alpha);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(min, bottom, min).tex(u0, sideV).endVertex();
        buffer.pos(max, bottom, min).tex(u1, sideV).endVertex();
        buffer.pos(max, top, min).tex(u1, v0).endVertex();
        buffer.pos(min, top, min).tex(u0, v0).endVertex();
        buffer.pos(max, bottom, max).tex(u0, sideV).endVertex();
        buffer.pos(min, bottom, max).tex(u1, sideV).endVertex();
        buffer.pos(min, top, max).tex(u1, v0).endVertex();
        buffer.pos(max, top, max).tex(u0, v0).endVertex();
        buffer.pos(min, bottom, max).tex(u0, sideV).endVertex();
        buffer.pos(min, bottom, min).tex(u1, sideV).endVertex();
        buffer.pos(min, top, min).tex(u1, v0).endVertex();
        buffer.pos(min, top, max).tex(u0, v0).endVertex();
        buffer.pos(max, bottom, min).tex(u0, sideV).endVertex();
        buffer.pos(max, bottom, max).tex(u1, sideV).endVertex();
        buffer.pos(max, top, max).tex(u1, v0).endVertex();
        buffer.pos(max, top, min).tex(u0, v0).endVertex();
        buffer.pos(min, top, min).tex(u0, v0).endVertex();
        buffer.pos(max, top, min).tex(u1, v0).endVertex();
        buffer.pos(max, top, max).tex(u1, v1).endVertex();
        buffer.pos(min, top, max).tex(u0, v1).endVertex();
        buffer.pos(min, bottom, max).tex(u0, v0).endVertex();
        buffer.pos(max, bottom, max).tex(u1, v0).endVertex();
        buffer.pos(max, bottom, min).tex(u1, v1).endVertex();
        buffer.pos(min, bottom, min).tex(u0, v1).endVertex();
        Tessellator.getInstance().draw();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, previousX, previousY);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}