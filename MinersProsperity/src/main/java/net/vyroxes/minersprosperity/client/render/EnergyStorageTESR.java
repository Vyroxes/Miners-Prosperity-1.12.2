package net.vyroxes.minersprosperity.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.machine.EnergyStorageTier;
import net.vyroxes.minersprosperity.tile.TileEntityEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class EnergyStorageTESR extends TileEntitySpecialRenderer<TileEntityEnergyStorage>
{
    private static final ResourceLocation HEXAGON_BASE = new ResourceLocation(Tags.MODID, "textures/blocks/energy_storage_core_hexagon_base.png");
    private static final ResourceLocation HEXAGON_STRIPE = new ResourceLocation(Tags.MODID, "textures/blocks/energy_storage_core_hexagon_stripe.png");
    private static final ResourceLocation PENTAGON_BASE = new ResourceLocation(Tags.MODID, "textures/blocks/energy_storage_core_pentagon_base.png");
    private static final ResourceLocation PENTAGON_STRIPE = new ResourceLocation(Tags.MODID, "textures/blocks/energy_storage_core_pentagon_stripe.png");

    private static final double[][] CORE_POINTS = {
            {0.000000000000D, -0.201774106168D, -0.979432085486D},
            {-0.201774106168D, -0.730025574164D, -0.652954723658D},
            {0.201774106168D, -0.730025574164D, -0.652954723658D},
            {-0.326477361829D, -0.403548212335D, -0.854728829825D},
            {0.326477361829D, -0.403548212335D, -0.854728829825D},
            {0.000000000000D, -0.201774106168D, 0.979432085486D},
            {-0.201774106168D, -0.730025574164D, 0.652954723658D},
            {0.201774106168D, -0.730025574164D, 0.652954723658D},
            {-0.326477361829D, -0.403548212335D, 0.854728829825D},
            {0.326477361829D, -0.403548212335D, 0.854728829825D},
            {0.000000000000D, 0.201774106168D, -0.979432085486D},
            {-0.201774106168D, 0.730025574164D, -0.652954723658D},
            {0.201774106168D, 0.730025574164D, -0.652954723658D},
            {-0.326477361829D, 0.403548212335D, -0.854728829825D},
            {0.326477361829D, 0.403548212335D, -0.854728829825D},
            {0.000000000000D, 0.201774106168D, 0.979432085486D},
            {-0.201774106168D, 0.730025574164D, 0.652954723658D},
            {0.201774106168D, 0.730025574164D, 0.652954723658D},
            {-0.326477361829D, 0.403548212335D, 0.854728829825D},
            {0.326477361829D, 0.403548212335D, 0.854728829825D},
            {-0.403548212335D, -0.854728829825D, -0.326477361829D},
            {-0.403548212335D, -0.854728829825D, 0.326477361829D},
            {-0.201774106168D, -0.979432085486D, 0.000000000000D},
            {-0.730025574164D, -0.652954723658D, -0.201774106168D},
            {-0.730025574164D, -0.652954723658D, 0.201774106168D},
            {-0.403548212335D, 0.854728829825D, -0.326477361829D},
            {-0.403548212335D, 0.854728829825D, 0.326477361829D},
            {-0.201774106168D, 0.979432085486D, 0.000000000000D},
            {-0.730025574164D, 0.652954723658D, -0.201774106168D},
            {-0.730025574164D, 0.652954723658D, 0.201774106168D},
            {0.403548212335D, -0.854728829825D, -0.326477361829D},
            {0.403548212335D, -0.854728829825D, 0.326477361829D},
            {0.201774106168D, -0.979432085486D, 0.000000000000D},
            {0.730025574164D, -0.652954723658D, -0.201774106168D},
            {0.730025574164D, -0.652954723658D, 0.201774106168D},
            {0.403548212335D, 0.854728829825D, -0.326477361829D},
            {0.403548212335D, 0.854728829825D, 0.326477361829D},
            {0.201774106168D, 0.979432085486D, 0.000000000000D},
            {0.730025574164D, 0.652954723658D, -0.201774106168D},
            {0.730025574164D, 0.652954723658D, 0.201774106168D},
            {-0.652954723658D, -0.201774106168D, -0.730025574164D},
            {-0.652954723658D, 0.201774106168D, -0.730025574164D},
            {-0.854728829825D, -0.326477361829D, -0.403548212335D},
            {-0.854728829825D, 0.326477361829D, -0.403548212335D},
            {-0.979432085486D, 0.000000000000D, -0.201774106168D},
            {-0.652954723658D, -0.201774106168D, 0.730025574164D},
            {-0.652954723658D, 0.201774106168D, 0.730025574164D},
            {-0.854728829825D, -0.326477361829D, 0.403548212335D},
            {-0.854728829825D, 0.326477361829D, 0.403548212335D},
            {-0.979432085486D, 0.000000000000D, 0.201774106168D},
            {0.652954723658D, -0.201774106168D, -0.730025574164D},
            {0.652954723658D, 0.201774106168D, -0.730025574164D},
            {0.854728829825D, -0.326477361829D, -0.403548212335D},
            {0.854728829825D, 0.326477361829D, -0.403548212335D},
            {0.979432085486D, 0.000000000000D, -0.201774106168D},
            {0.652954723658D, -0.201774106168D, 0.730025574164D},
            {0.652954723658D, 0.201774106168D, 0.730025574164D},
            {0.854728829825D, -0.326477361829D, 0.403548212335D},
            {0.854728829825D, 0.326477361829D, 0.403548212335D},
            {0.979432085486D, 0.000000000000D, 0.201774106168D}
    };
    private static final int[][] CORE_FACES = {
            {3, 40, 41, 13, 10, 0},
            {0, 10, 14, 51, 50, 4},
            {2, 30, 32, 22, 20, 1},
            {1, 20, 23, 42, 40, 3},
            {4, 50, 52, 33, 30, 2},
            {5, 15, 18, 46, 45, 8},
            {9, 55, 56, 19, 15, 5},
            {6, 21, 22, 32, 31, 7},
            {8, 45, 47, 24, 21, 6},
            {7, 31, 34, 57, 55, 9},
            {11, 25, 27, 37, 35, 12},
            {13, 41, 43, 28, 25, 11},
            {12, 35, 38, 53, 51, 14},
            {17, 36, 37, 27, 26, 16},
            {16, 26, 29, 48, 46, 18},
            {19, 56, 58, 39, 36, 17},
            {24, 47, 49, 44, 42, 23},
            {28, 43, 44, 49, 48, 29},
            {33, 52, 54, 59, 57, 34},
            {39, 58, 59, 54, 53, 38},
            {1, 3, 0, 4, 2},
            {7, 9, 5, 8, 6},
            {12, 14, 10, 13, 11},
            {16, 18, 15, 19, 17},
            {24, 23, 20, 22, 21},
            {26, 27, 25, 28, 29},
            {31, 32, 30, 33, 34},
            {39, 38, 35, 37, 36},
            {43, 41, 40, 42, 44},
            {49, 47, 45, 46, 48},
            {54, 52, 50, 51, 53},
            {58, 56, 55, 57, 59}
    };

    private static final double[][] CORE_VERTICES = createCoreVertices(0.14D);
    private static final double[][] GLOW_VERTICES = createCoreVertices(0.1401D);

    @Override
    public void render(@NotNull TileEntityEnergyStorage tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (destroyStage >= 0) return;
        double time = tile.getWorld().getTotalWorldTime() % 7200L + partialTicks;
        double phase = (tile.getPos().toLong() & 1023L) * 0.035;
        double bob = Math.sin(time * Math.PI / 60.0 + phase) * 0.025;
        float charge = tile.getTier() == EnergyStorageTier.CREATIVE ? 1.0F
                : Math.max(0.0F, Math.min(1.0F, (float) tile.getEnergyStored() / tile.getMaxEnergyStored()));
        double coreScale = 0.5D + 0.5D * charge;
        boolean fullyCharged = tile.getEnergyStored() >= tile.getMaxEnergyStored();
        double beamScale = fullyCharged ? 1.5D : 1.0D;
        int color = tile.getTier().coreColor;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        float previousX = OpenGlHelper.lastBrightnessX;
        float previousY = OpenGlHelper.lastBrightnessY;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5 + bob, z + 0.5);
        GlStateManager.rotate((float) (time * 0.9 + phase * 30), 0, 1, 0);
        GlStateManager.rotate(22.5F, 1, 0, 0);
        GlStateManager.rotate(22.5F, 0, 0, 1);
        GlStateManager.scale(coreScale, coreScale, coreScale);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
        GlStateManager.color(1, 1, 1, alpha);
        this.drawCore(CORE_VERTICES, false);
        GlStateManager.color(red, green, blue, alpha);
        this.drawCore(GLOW_VERTICES, true);
        if (charge > 0)
        {
            float glow = (0.30F + 0.08F * (float) Math.sin(time * Math.PI / 30.0 + phase)) * charge;
            GlStateManager.depthMask(false);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01F);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(red, green, blue, glow * alpha);
            this.drawCore(GLOW_VERTICES, true);
            GlStateManager.depthMask(true);
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
        float pulse = (0.85F + 0.15F * (float) Math.sin(time * Math.PI / 30.0D + phase)) * charge * alpha;
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01F);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.001F);
        if (charge > 0)
        {
            this.drawAura(bob, (0.27D + 0.008D * Math.sin(time * Math.PI / 40.0D + phase)) * coreScale, red, green, blue, 0.32F * pulse);
            this.drawParticles(time, phase, bob, red, green, blue, pulse, fullyCharged ? 32 : 16);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01F);
            GlStateManager.color(red, green, blue, 0.12F * pulse);
            this.drawBeams(bob, 0.036D * beamScale);
            GlStateManager.color(red, green, blue, 0.75F * pulse);
            this.drawBeams(bob, 0.008D * beamScale);
        }
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        double centerRadius = (0.08D + 0.003D * Math.sin(time * Math.PI / 30.0D + phase)) * coreScale;
        GlStateManager.enableCull();
        this.drawCenterSphere(bob, centerRadius, red, green, blue, (0.35F + 0.50F * charge) * alpha);
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.001F);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        this.drawAura(bob, centerRadius * 2.0D, red, green, blue, 0.65F * pulse);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, previousX, previousY);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void drawCenterSphere(double bob, double radius, float red, float green, float blue, float alpha)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        for (int latitude = 0; latitude < 12; latitude++)
        {
            for (int longitude = 0; longitude < 20; longitude++)
            {
                for (int corner = 0; corner < 4; corner++)
                {
                    double vertical = (latitude + (corner == 0 || corner == 3 ? 0 : 1)) * Math.PI / 12.0D - Math.PI / 2.0D;
                    double horizontal = (longitude + (corner < 2 ? 0 : 1)) * Math.PI * 2.0D / 20;
                    double nx = Math.cos(vertical) * Math.cos(horizontal);
                    double ny = Math.sin(vertical);
                    double nz = Math.cos(vertical) * Math.sin(horizontal);
                    buffer.pos(nx * radius, bob + ny * radius, nz * radius).color(red, green, blue, alpha).endVertex();
                }
            }
        }
        Tessellator.getInstance().draw();
    }

    private void drawAura(double bob, double radius, float red, float green, float blue, float alpha)
    {
        Vec3d right = new Vec3d(ActiveRenderInfo.getRotationX(), 0, ActiveRenderInfo.getRotationZ());
        Vec3d up = new Vec3d(ActiveRenderInfo.getRotationYZ(), ActiveRenderInfo.getRotationXZ(), ActiveRenderInfo.getRotationXY());
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(0, bob, 0).color(red, green, blue, alpha).endVertex();
        for (int segment = 0; segment <= 48; segment++)
        {
            double angle = segment * Math.PI * 2.0D / 48;
            double horizontal = Math.cos(angle) * radius;
            double vertical = Math.sin(angle) * radius;
            buffer.pos(right.x * horizontal + up.x * vertical, bob + up.y * vertical, right.z * horizontal + up.z * vertical).color(red, green, blue, 0).endVertex();
        }
        Tessellator.getInstance().draw();
    }

    private void drawParticles(double time, double phase, double bob, float red, float green, float blue, float alpha, int particleCount)
    {
        Vec3d right = new Vec3d(ActiveRenderInfo.getRotationX(), 0, ActiveRenderInfo.getRotationZ());
        Vec3d up = new Vec3d(ActiveRenderInfo.getRotationYZ(), ActiveRenderInfo.getRotationXZ(), ActiveRenderInfo.getRotationXY());
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        for (int particle = 0; particle < particleCount; particle++)
        {
            double angle = time * (0.018D + particle * 0.002D) + phase + particle * 2.39996D;
            double radius = 0.225D + 0.015D * Math.sin(time * 0.035D + particle);
            double x = Math.cos(angle) * radius;
            double y = bob + Math.sin(angle * 1.3D + particle) * 0.12D;
            double z = Math.sin(angle) * radius;
            float flicker = 0.6F + 0.4F * (float) Math.sin(time * 0.12D + particle * 1.7D);
            double size = 0.006D + 0.004D * flicker;
            for (int layer = 0; layer < 2; layer++)
            {
                double width = layer == 0 ? size * 2.8D : size;
                float opacity = (layer == 0 ? 0.10F : 0.85F) * flicker * alpha;
                for (int corner = 0; corner < 4; corner++)
                {
                    double horizontal = corner == 0 || corner == 3 ? -width : width;
                    double vertical = corner < 2 ? -width : width;
                    buffer.pos(x + right.x * horizontal + up.x * vertical, y + up.y * vertical, z + right.z * horizontal + up.z * vertical).color(red, green, blue, opacity).endVertex();
                }
            }
        }
        Tessellator.getInstance().draw();
    }

    private void drawBeams(double bob, double width)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        Vec3d end = new Vec3d(0, bob, 0);
        for (int x = -1; x <= 1; x += 2)
        {
            for (int y = -1; y <= 1; y += 2)
            {
                for (int z = -1; z <= 1; z += 2)
                {
                    Vec3d start = new Vec3d(x * 0.309D, y * 0.309D, z * 0.309D);
                    Vec3d direction = end.subtract(start).normalize();
                    Vec3d side = direction.crossProduct(new Vec3d(0, 1, 0)).normalize().scale(width);
                    Vec3d otherSide = direction.crossProduct(side).normalize().scale(width);
                    for (Vec3d offset : new Vec3d[]{side, otherSide})
                    {
                        buffer.pos(start.x + offset.x, start.y + offset.y, start.z + offset.z).endVertex();
                        buffer.pos(start.x - offset.x, start.y - offset.y, start.z - offset.z).endVertex();
                        buffer.pos(end.x - offset.x, end.y - offset.y, end.z - offset.z).endVertex();
                        buffer.pos(end.x + offset.x, end.y + offset.y, end.z + offset.z).endVertex();
                    }
                }
            }
        }
        Tessellator.getInstance().draw();
    }

    private void drawCore(double[][] vertices, boolean stripe)
    {
        for (int sides : new int[]{6, 5})
        {
            this.bindTexture(sides == 6 ? (stripe ? HEXAGON_STRIPE : HEXAGON_BASE) : (stripe ? PENTAGON_STRIPE : PENTAGON_BASE));
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            for (double[] vertex : vertices)
            {
                if (vertex[5] == sides) buffer.pos(vertex[0], vertex[1], vertex[2]).tex(vertex[3] / 16.0D, vertex[4] / 16.0D).endVertex();
            }
            Tessellator.getInstance().draw();
        }
    }

    public static double[][] createCoreVertices(double size)
    {
        double[][] hexagonUVs = {{254, 128}, {190, 238}, {64, 238}, {1, 128}, {64, 17}, {190, 17}};
        double[][] pentagonUVs = {{128, 9}, {254, 101}, {206, 245}, {49, 245}, {1, 101}};
        double radius = size * Math.sqrt(2.0D);
        double[][] vertices = new double[720][];
        int index = 0;
        for (int[] face : CORE_FACES)
        {
            double[][] uvs = face.length == 6 ? hexagonUVs : pentagonUVs;
            double centerX = 0;
            double centerY = 0;
            double centerZ = 0;
            for (int point : face)
            {
                centerX += CORE_POINTS[point][0] * radius / face.length;
                centerY += CORE_POINTS[point][1] * radius / face.length;
                centerZ += CORE_POINTS[point][2] * radius / face.length;
            }
            for (int corner = 0; corner < face.length; corner++)
            {
                int next = (corner + 1) % face.length;
                double[] a = CORE_POINTS[face[corner]];
                double[] b = CORE_POINTS[face[next]];
                vertices[index++] = new double[]{centerX, centerY, centerZ, 8.0D, face.length == 6 ? 8.0D : 8.75D, face.length};
                vertices[index++] = new double[]{a[0] * radius, a[1] * radius, a[2] * radius, uvs[corner][0] / 16.0D, uvs[corner][1] / 16.0D, face.length};
                vertices[index++] = new double[]{b[0] * radius, b[1] * radius, b[2] * radius, uvs[next][0] / 16.0D, uvs[next][1] / 16.0D, face.length};
                vertices[index] = vertices[index - 1];
                index++;
            }
        }
        return vertices;
    }
}