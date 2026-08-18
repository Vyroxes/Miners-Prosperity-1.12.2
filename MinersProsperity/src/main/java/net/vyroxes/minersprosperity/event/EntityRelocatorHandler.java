package net.vyroxes.minersprosperity.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.util.EntityRelocatorUtils;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class EntityRelocatorHandler {
    private EntityRelocatorHandler() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItem(event.getHand());

        if (stack.isEmpty() || stack.getItem() != ModItems.ENTITY_RELOCATOR) return;
        if (EntityRelocatorUtils.hasEntity(stack)) return;
        if (!(event.getTarget() instanceof EntityLivingBase target)) return;
        if (!EntityRelocatorUtils.canCapture(target)) return;

        event.setCanceled(true);
        event.setCancellationResult(EnumActionResult.SUCCESS);

        if (!player.world.isRemote) EntityRelocatorUtils.capture(stack, target);

        player.swingArm(event.getHand());
    }
}