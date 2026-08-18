package net.vyroxes.minersprosperity.client.particle;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.vyroxes.minersprosperity.Tags;

@Mod.EventBusSubscriber(modid = Tags.MODID, value = Side.CLIENT)
public final class FluidParticleTextures
{
    public static final ResourceLocation BUBBLE = new ResourceLocation(Tags.MODID, "particles/fluid_bubble");
    public static final ResourceLocation SPLASH = new ResourceLocation(Tags.MODID, "particles/fluid_splash");
    public static final ResourceLocation WAKE_0 = new ResourceLocation(Tags.MODID, "particles/fluid_wake_0");
    public static final ResourceLocation WAKE_1 = new ResourceLocation(Tags.MODID, "particles/fluid_wake_1");
    public static final ResourceLocation WAKE_2 = new ResourceLocation(Tags.MODID, "particles/fluid_wake_2");
    public static final ResourceLocation WAKE_3 = new ResourceLocation(Tags.MODID, "particles/fluid_wake_3");

    private static TextureAtlasSprite bubble;
    private static TextureAtlasSprite splash;

    private static final TextureAtlasSprite[] wake = new TextureAtlasSprite[4];

    private FluidParticleTextures() {}

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event)
    {
        bubble = event.getMap().registerSprite(BUBBLE);
        splash = event.getMap().registerSprite(SPLASH);

        wake[0] = event.getMap().registerSprite(WAKE_0);
        wake[1] = event.getMap().registerSprite(WAKE_1);
        wake[2] = event.getMap().registerSprite(WAKE_2);
        wake[3] = event.getMap().registerSprite(WAKE_3);
    }

    public static TextureAtlasSprite getBubble() { return bubble; }

    public static TextureAtlasSprite getSplash() { return splash; }

    public static TextureAtlasSprite[] getWake() { return wake; }
}