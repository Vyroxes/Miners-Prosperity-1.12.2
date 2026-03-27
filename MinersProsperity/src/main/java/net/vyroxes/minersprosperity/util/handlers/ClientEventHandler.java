package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Tags;

@Mod.EventBusSubscriber
public class ClientEventHandler
{
    private static Float cachedToughness = null;
    private static int lastTick = -1;

    public static float getCachedArmorToughness(EntityPlayer player)
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
    public static void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ARMOR && ModConfig.GUI.armorOverlay)
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
    public static void onRenderGameOverlayEventPost(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD && ModConfig.GUI.toughnessOverlay)
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
            {ModConfig.GUI.color21to40.red, ModConfig.GUI.color21to40.green, ModConfig.GUI.color21to40.blue},
            {ModConfig.GUI.color41to60.red, ModConfig.GUI.color41to60.green, ModConfig.GUI.color41to60.blue},
            {ModConfig.GUI.color61to80.red, ModConfig.GUI.color61to80.green, ModConfig.GUI.color61to80.blue},
            {ModConfig.GUI.color81to100.red, ModConfig.GUI.color81to100.green, ModConfig.GUI.color81to100.blue},
            {ModConfig.GUI.color101to120.red, ModConfig.GUI.color101to120.green, ModConfig.GUI.color101to120.blue},
            {ModConfig.GUI.color121to140.red, ModConfig.GUI.color121to140.green, ModConfig.GUI.color121to140.blue}
    };

    private static void renderCustomArmorOverlay(ScaledResolution resolution, int armor)
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(ARMOR_TEXTURE);

        int baseX = resolution.getScaledWidth() / 2 + ModConfig.GUI.armorX;
        int y = resolution.getScaledHeight() + ModConfig.GUI.armorY;

        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (ModConfig.GUI.armorOverlayHealthShift && player.getMaxHealth() > 140)
        {
            y -= 28;
        } else if (ModConfig.GUI.armorOverlayHealthShift && player.getMaxHealth() > 100)
        {
            y -= 30;
        } else if (ModConfig.GUI.armorOverlayHealthShift && player.getMaxHealth() > 80)
        {
            y -= 28;
        } else if (ModConfig.GUI.armorOverlayHealthShift && player.getMaxHealth() > 60)
        {
            y -= 24;
        } else if (ModConfig.GUI.armorOverlayHealthShift && player.getMaxHealth() > 40)
        {
            y -= 18;
        } else if (ModConfig.GUI.armorOverlayHealthShift && player.getMaxHealth() > 20)
        {
            y -= 10;
        } else if (ModConfig.GUI.armorOverlayAbsorptionShift && player.isPotionActive(MobEffects.ABSORPTION))
        {
            y -= 10;
        }

        if (ModConfig.GUI.armorOverlayHealthShift && ModConfig.GUI.armorOverlayAbsorptionShift && player.isPotionActive(MobEffects.ABSORPTION) && player.getMaxHealth() > 140)
        {
            y -= -4;
        } else if (ModConfig.GUI.armorOverlayHealthShift && ModConfig.GUI.armorOverlayAbsorptionShift && player.isPotionActive(MobEffects.ABSORPTION) && player.getMaxHealth() > 120)
        {
            y -= -2;
        } else if (ModConfig.GUI.armorOverlayHealthShift && ModConfig.GUI.armorOverlayAbsorptionShift && player.isPotionActive(MobEffects.ABSORPTION) && player.getMaxHealth() > 100)
        {
            y -= 0;
        } else if (ModConfig.GUI.armorOverlayHealthShift && ModConfig.GUI.armorOverlayAbsorptionShift && player.isPotionActive(MobEffects.ABSORPTION) && player.getMaxHealth() > 80)
        {
            y -= 2;
        } else if (ModConfig.GUI.armorOverlayHealthShift && ModConfig.GUI.armorOverlayAbsorptionShift && player.isPotionActive(MobEffects.ABSORPTION) && player.getMaxHealth() > 60)
        {
            y -= 4;
        } else if (ModConfig.GUI.armorOverlayHealthShift && ModConfig.GUI.armorOverlayAbsorptionShift && player.isPotionActive(MobEffects.ABSORPTION) && player.getMaxHealth() > 40)
        {
            y -= 6;
        } else if (ModConfig.GUI.armorOverlayHealthShift && ModConfig.GUI.armorOverlayAbsorptionShift && player.isPotionActive(MobEffects.ABSORPTION) && player.getMaxHealth() > 20)
        {
            y -= 8;
        }

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
                if (ModConfig.GUI.armorOverlayTexture)
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
                if (ModConfig.GUI.armorOverlayTexture)
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
                if (ModConfig.GUI.armorOverlayTexture)
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

    private static void renderCustomToughnessOverlay(ScaledResolution resolution, float toughness)
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(TOUGHNESS_TEXTURE);

        int baseX = resolution.getScaledWidth() / 2 + ModConfig.GUI.toughnessX;
        int y = resolution.getScaledHeight() + ModConfig.GUI.toughnessY;

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
                if (ModConfig.GUI.toughnessOverlayTexture)
                {
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + (ICONS_PER_RANGE - 1 - i) * 8, y, 0, rangeIndex * 9, 9, 9);
                }
                else
                {
                    if (totalRanges == 1)
                    {
                        GlStateManager.color(1f, 1f, 1f, 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + (ICONS_PER_RANGE - 1 - i) * 8, y, 0, 0, 9, 9);
                    }
                    else
                    {
                        GlStateManager.color(previousColor[0], previousColor[1], previousColor[2], 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + (ICONS_PER_RANGE - 1 - i) * 8, y, 18, 0, 9, 9);
                    }
                }
            }

            int fullIcons = toughnessRanges / 2;
            boolean hasHalf = toughnessRanges % 2 == 1;

            for (int i = 0; i < fullIcons && i < ICONS_PER_RANGE; i++)
            {
                if (ModConfig.GUI.toughnessOverlayTexture)
                {
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + (ICONS_PER_RANGE - 1 - i) * 8, y, 18, rangeIndex * 9, 9, 9);
                }
                else
                {
                    GlStateManager.color(color[0], color[1], color[2], 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + (ICONS_PER_RANGE - 1 - i) * 8, y, 18, 0, 9, 9);
                }
            }

            if (hasHalf && fullIcons < ICONS_PER_RANGE)
            {
                if (ModConfig.GUI.toughnessOverlayTexture)
                {
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    mc.ingameGUI.drawTexturedModalRect(baseX + (ICONS_PER_RANGE - 1 - fullIcons) * 8, y, 9, rangeIndex * 9, 9, 9);
                }
                else
                {
                    if (totalRanges == 1)
                    {
                        GlStateManager.color(1f, 1f, 1f, 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + (ICONS_PER_RANGE - 1 - fullIcons) * 8, y, 9, 0, 9, 9);
                    }
                    else
                    {
                        GlStateManager.color(color[0], color[1], color[2], 1f);
                        mc.ingameGUI.drawTexturedModalRect(baseX + (ICONS_PER_RANGE - 1 - fullIcons) * 8 + 4, y, 9 + 4, 0, 5, 9);
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