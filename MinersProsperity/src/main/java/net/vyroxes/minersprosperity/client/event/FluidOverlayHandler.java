package net.vyroxes.minersprosperity.client.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.fluid.FluidBase;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = Tags.MODID, value = Side.CLIENT)
public class FluidOverlayHandler {
    @SubscribeEvent
    public static void onRenderBlockOverlay(RenderBlockOverlayEvent event)
    {
        if (event.getOverlayType() != RenderBlockOverlayEvent.OverlayType.WATER) return;

        EntityPlayer player = event.getPlayer();

        Vec3d eyePosition = ActiveRenderInfo.projectViewFromEntity(player, event.getRenderPartialTicks());

        BlockPos pos = new BlockPos(eyePosition);
        IBlockState state = player.world.getBlockState(pos);

        if (!(state.getBlock() instanceof IFluidBlock)) return;

        Fluid fluid = ((IFluidBlock) state.getBlock()).getFluid();

        if (!(fluid instanceof FluidBase)) return;

        ResourceLocation overlay = fluid.getOverlay();

        if (overlay == null) return;

        event.setCanceled(true);

        renderFluidOverlay(player, (FluidBase) fluid, overlay);
    }

    private static void renderFluidOverlay(EntityPlayer player, FluidBase fluid, ResourceLocation overlay)
    {
        Minecraft minecraft = Minecraft.getMinecraft();

        ResourceLocation texture = new ResourceLocation(overlay.getNamespace(), "textures/" + overlay.getPath() + ".png");

        minecraft.getTextureManager().bindTexture(texture);

        int color = fluid.getColor();

        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        float alpha = fluid.getAlpha();

        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();

        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.color(red, green, blue, alpha);

        float uOffset = -player.rotationYaw / 64.0F;
        float vOffset = player.rotationPitch / 64.0F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos(-1.0D, -1.0D, -0.5D).tex(4.0F + uOffset, 4.0F + vOffset).endVertex();
        buffer.pos(1.0D, -1.0D, -0.5D).tex(0.0F + uOffset, 4.0F + vOffset).endVertex();
        buffer.pos(1.0D, 1.0D, -0.5D).tex(0.0F + uOffset, 0.0F + vOffset).endVertex();
        buffer.pos(-1.0D, 1.0D, -0.5D).tex(4.0F + uOffset, 0.0F + vOffset).endVertex();

        tessellator.draw();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }
}