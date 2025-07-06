package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Tags;

public class ClientEventHandler
{
    private Float cachedToughness = null;
    private int lastTick = -1;

    public float getCachedArmorToughness(EntityPlayer player)
    {
        int currentTick = Minecraft.getMinecraft().player.ticksExisted;
        if (cachedToughness == null || lastTick != currentTick)
        {
            cachedToughness = getTotalArmorToughness(player);
            lastTick = currentTick;
        }
        return cachedToughness;
    }

    public static float getTotalArmorToughness(EntityPlayer player)
    {
        float toughness = 0f;

        for (ItemStack stack : player.getArmorInventoryList())
        {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemArmor armor)
            {
                toughness += armor.getArmorMaterial().getToughness();
            }
        }

        return toughness;
    }

    @SubscribeEvent
    public void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ARMOR && ConfigHandler.armorOverlay)
        {
            event.setCanceled(true);

            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            if (player == null)
                return;

            int armor = player.getTotalArmorValue();

            if (armor >= 1) renderCustomArmorOverlay(event.getResolution(), armor);
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlayEventPost(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD && ConfigHandler.toughnessOverlay)
        {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            if (player == null)
                return;

            float toughness = getCachedArmorToughness(player);

            if (toughness > 0)
                renderCustomToughnessOverlay(event.getResolution(), toughness);
        }
    }

    private static final ResourceLocation ARMOR_TEXTURE = new ResourceLocation(Tags.MODID, "textures/gui/armor_overlay.png");
    private static final ResourceLocation TOUGHNESS_TEXTURE = new ResourceLocation(Tags.MODID, "textures/gui/armor_toughness_overlay.png");

    private static final int ARMOR_RANGE = 20;
    private static final int ICONS_PER_RANGE = 10;

    private static final float[][] COLORS = new float[][] {
            {1.0f, 1.0f, 1.0f},
            {ConfigHandler.colorRed21to40, ConfigHandler.colorGreen21to40, ConfigHandler.colorBlue21to40},
            {ConfigHandler.colorRed41to60, ConfigHandler.colorGreen41to60, ConfigHandler.colorBlue41to60},
            {ConfigHandler.colorRed61to80, ConfigHandler.colorGreen61to80, ConfigHandler.colorBlue61to80},
            {ConfigHandler.colorRed81to100, ConfigHandler.colorGreen81to100, ConfigHandler.colorBlue81to100},
            {ConfigHandler.colorRed101to120, ConfigHandler.colorGreen101to120, ConfigHandler.colorBlue101to120},
            {ConfigHandler.colorRed121to140, ConfigHandler.colorGreen121to140, ConfigHandler.colorBlue121to140}
    };

    private void renderCustomArmorOverlay(ScaledResolution resolution, int armor)
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(ARMOR_TEXTURE);

        int baseX = resolution.getScaledWidth() / 2 + ConfigHandler.armorX;
        int y = resolution.getScaledHeight() + ConfigHandler.armorY;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();

        int armorRanges = (armor + ARMOR_RANGE - 1) / ARMOR_RANGE;

        for (int rangeIndex = 0; rangeIndex < armorRanges; rangeIndex++)
        {
            int endArmor = (rangeIndex + 1) * ARMOR_RANGE;
            int armorInRange = Math.min(armor, endArmor) - (rangeIndex * ARMOR_RANGE);

            float[] color = rangeIndex < COLORS.length ? COLORS[rangeIndex] : new float[]{1f, 1f, 1f};
            float[] previousColor = (rangeIndex > 0 && rangeIndex - 1 < COLORS.length) ? COLORS[rangeIndex - 1] : new float[]{1f, 1f, 1f};

            for (int i = 0; i < ICONS_PER_RANGE; i++)
            {
                if (ConfigHandler.armorOverlayTexture)
                {
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + i * 8, y, 0, rangeIndex * 9, 9, 9);
                }
                else
                {
                    if (armorRanges == 1)
                    {
                        GlStateManager.color(1f, 1f, 1f, 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + i * 8, y, 0, 0, 9, 9);
                    }
                    else
                    {
                        GlStateManager.color(previousColor[0], previousColor[1], previousColor[2], 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + i * 8, y, 18, 0, 9, 9);
                    }
                }
            }

            int fullIcons = armorInRange / 2;
            boolean hasHalf = armorInRange % 2 == 1;

            for (int i = 0; i < fullIcons && i < ICONS_PER_RANGE; i++)
            {
                if (ConfigHandler.armorOverlayTexture)
                {
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + i * 8, y, 18, rangeIndex * 9, 9, 9);
                }
                else
                {
                    GlStateManager.color(color[0], color[1], color[2], 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + i * 8, y, 18, 0, 9, 9);
                }
            }

            if (hasHalf && fullIcons < ICONS_PER_RANGE)
            {
                if (ConfigHandler.armorOverlayTexture)
                {
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + fullIcons * 8, y, 9, rangeIndex * 9, 9, 9);
                }
                else
                {
                    if (armorRanges == 1)
                    {
                        GlStateManager.color(1f, 1f, 1f, 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + fullIcons * 8, y, 9, 0, 9, 9);
                    }
                    else
                    {
                        mc.ingameGUI.drawTexturedModalRect(baseX + fullIcons * 8, y, 9, 0, 5, 9);
                        GlStateManager.color(previousColor[0], previousColor[1], previousColor[2], 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + fullIcons * 8 + 5, y, 18 + 5, 0, 4, 9);
                    }
                }
            }
        }

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
    }

    private void renderCustomToughnessOverlay(ScaledResolution resolution, float toughness)
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(TOUGHNESS_TEXTURE);

        int baseX = resolution.getScaledWidth() / 2 + ConfigHandler.toughnessX;
        int y = resolution.getScaledHeight() + ConfigHandler.toughnessY;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();

        int totalRanges = (int) ((toughness + ARMOR_RANGE - 1) / ARMOR_RANGE);
        int toughnessValue = (int) toughness;

        for (int rangeIndex = 0; rangeIndex < totalRanges; rangeIndex++)
        {
            int endToughness = (rangeIndex + 1) * ARMOR_RANGE;
            int toughnessRanges = Math.min(toughnessValue, endToughness) - (rangeIndex * ARMOR_RANGE);

            float[] color = rangeIndex < COLORS.length ? COLORS[rangeIndex] : new float[]{1f, 1f, 1f};
            float[] previousColor = (rangeIndex > 0 && rangeIndex - 1 < COLORS.length) ? COLORS[rangeIndex - 1] : new float[]{1f, 1f, 1f};

            for (int i = 0; i < ICONS_PER_RANGE; i++)
            {
                if (ConfigHandler.toughnessOverlayTexture)
                {
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + i * 8, y, 0, rangeIndex * 9, 9, 9);
                }
                else
                {
                    if (totalRanges == 1)
                    {
                        GlStateManager.color(1f, 1f, 1f, 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + i * 8, y, 0, 0, 9, 9);
                    }
                    else
                    {
                        GlStateManager.color(previousColor[0], previousColor[1], previousColor[2], 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + i * 8, y, 18, 0, 9, 9);
                    }
                }
            }

            int fullIcons = toughnessRanges / 2;
            boolean hasHalf = toughnessRanges % 2 == 1;

            for (int i = 0; i < fullIcons && i < ICONS_PER_RANGE; i++)
            {
                if (ConfigHandler.toughnessOverlayTexture)
                {
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + i * 8, y, 18, rangeIndex * 9, 9, 9);
                }
                else
                {
                    GlStateManager.color(color[0], color[1], color[2], 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + i * 8, y, 18, 0, 9, 9);
                }
            }

            if (hasHalf && fullIcons < ICONS_PER_RANGE)
            {
                if (ConfigHandler.toughnessOverlayTexture)
                {
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + fullIcons * 8, y, 9, rangeIndex * 9, 9, 9);
                }
                else
                {
                    if (totalRanges == 1)
                    {
                        GlStateManager.color(1f, 1f, 1f, 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + fullIcons * 8, y, 9, 0, 9, 9);
                    }
                    else
                    {
                        mc.ingameGUI.drawTexturedModalRect(baseX + fullIcons * 8, y, 9, 0, 5, 9);
                        GlStateManager.color(previousColor[0], previousColor[1], previousColor[2], 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + fullIcons * 8 + 5, y, 18 + 5, 0, 4, 9);
                    }
                }
            }
        }

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
    }
}