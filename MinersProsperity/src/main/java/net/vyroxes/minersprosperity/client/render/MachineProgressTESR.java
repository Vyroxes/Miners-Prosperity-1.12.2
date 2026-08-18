package net.vyroxes.minersprosperity.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.vyroxes.minersprosperity.block.machine.BlockMachine;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class MachineProgressTESR<T extends TileEntityMachine> extends TileEntitySpecialRenderer<T> {
    private final ResourceLocation texture;
    private final int frameCount;
    private final int startPixelX;
    private final int startPixelY;
    private final int widthPixels;
    private final int heightPixels;

    public MachineProgressTESR(ResourceLocation texture, int frameCount, int startPixelX, int startPixelY, int widthPixels, int heightPixels)
    {
        this.texture = texture;
        this.frameCount = frameCount;
        this.startPixelX = startPixelX;
        this.startPixelY = startPixelY;
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
    }

    @Override
    public void render(@NotNull T tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (this.frameCount <= 0) return;

        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());

        if (!(state.getBlock() instanceof BlockMachine)) return;

        if (state.getValue(BlockMachine.STATE) != BlockMachine.MachineState.ACTIVE) return;

        if (tileEntity.getTotalCookTime() <= 0) return;

        float progress = tileEntity.getRenderProgress(partialTicks);

        int frame = MathHelper.clamp(Math.round(progress * (this.frameCount - 1)), 0, this.frameCount - 1);

        float vMin = (float) frame / (float) this.frameCount;
        float vMax = (float) (frame + 1) / (float) this.frameCount;

        EnumFacing facing = state.getValue(BlockMachine.FACING);

        this.bindTexture(this.texture);

        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5D, y, z + 0.5D);

        switch (facing)
        {
            case EAST:
                GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                break;
            case SOUTH:
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                break;
            case WEST:
                GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                break;
            case NORTH:
            default:
                break;
        }

        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        double front = -0.501D;
        double xMin = -0.5D + (this.startPixelX - 1) / 16.0D;
        double xMax = xMin + this.widthPixels / 16.0D;
        double yMin = (this.startPixelY - 1) / 16.0D;
        double yMax = yMin + this.heightPixels / 16.0D;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos(xMin, yMin, front).tex(1.0D, vMax).endVertex();
        buffer.pos(xMax, yMin, front).tex(0.0D, vMax).endVertex();
        buffer.pos(xMax, yMax, front).tex(0.0D, vMin).endVertex();
        buffer.pos(xMin, yMax, front).tex(1.0D, vMin).endVertex();

        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();

        GlStateManager.popMatrix();
    }
}