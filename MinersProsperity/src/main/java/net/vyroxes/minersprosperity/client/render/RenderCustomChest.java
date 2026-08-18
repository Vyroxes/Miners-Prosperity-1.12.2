package net.vyroxes.minersprosperity.client.render;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.tile.TileEntityCustomChest;

public class RenderCustomChest extends TileEntitySpecialRenderer<TileEntityCustomChest> {
    private static final ModelChest MODEL = new ModelChest();

    @Override
    public void render(TileEntityCustomChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ResourceLocation texture = new ResourceLocation(Tags.MODID, "textures/entity/chest/" + te.getTier().translationKey + ".png");
        this.bindTexture(texture);

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableDepth();

        GlStateManager.translate(x, y + 1.0F, z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);

        float rotation = 0.0F;

        if (te.hasWorld()) {
            IBlockState state = te.getWorld().getBlockState(te.getPos());
            if (state.getProperties().containsKey(BlockHorizontal.FACING)) {
                EnumFacing facing = state.getValue(BlockHorizontal.FACING);
                if (facing == EnumFacing.NORTH) rotation = 180.0F;
                else if (facing == EnumFacing.SOUTH) rotation = 0.0F;
                else if (facing == EnumFacing.WEST) rotation = 90.0F;
                else if (facing == EnumFacing.EAST) rotation = -90.0F;
            }
        }

        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        float lid = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        lid = 1.0F - lid;
        lid = 1.0F - lid * lid * lid;
        MODEL.chestLid.rotateAngleX = -(lid * ((float) Math.PI / 2F));

        MODEL.renderAll();

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }
}