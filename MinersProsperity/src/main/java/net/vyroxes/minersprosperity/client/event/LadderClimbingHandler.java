package net.vyroxes.minersprosperity.client.event;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.config.ModConfig;

@Mod.EventBusSubscriber
public class LadderClimbingHandler
{
    private static final int SPEED_MODIFIER = 5;

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (!ModConfig.OTHER.fasterLadderClimbing)
            return;

        if (!(event.getEntityLiving() instanceof EntityPlayer player))
            return;

        if (!player.isOnLadder() || player.isSneaking() || player.onGround)
            return;

        float pitch = player.rotationPitch;
        float factor = Math.min(Math.abs(pitch) / 90f * (ModConfig.OTHER.fasterLadderClimbingSpeedModifier / 10f), 1.0f);
        float dy = factor * 0.5f;

        if (pitch < -45 && player.moveForward != 0)
        {
            player.move(MoverType.SELF, player.motionX, dy, player.motionZ);
        } else if (pitch > 45 && player.moveForward == 0)
        {
            player.move(MoverType.SELF, player.motionX, -dy, player.motionZ);
        }
    }
}