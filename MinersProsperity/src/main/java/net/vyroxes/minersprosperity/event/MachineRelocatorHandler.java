package net.vyroxes.minersprosperity.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.util.MachineRelocatorUtils;

@Mod.EventBusSubscriber(modid = Tags.MODID)
public class MachineRelocatorHandler {
    private MachineRelocatorHandler() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItem(event.getHand());

        if (stack.isEmpty() || stack.getItem() != ModItems.MACHINE_RELOCATOR) return;

        event.setCanceled(true);
        event.setCancellationResult(EnumActionResult.SUCCESS);

        if (player.world.isRemote) return;

        if (MachineRelocatorUtils.hasMachine(stack))
        {
            EnumFacing facing = event.getFace();
            if (facing == null) return;

            BlockPos targetPos = event.getPos().offset(facing);
            MachineRelocatorUtils.release(stack, player.world, targetPos, player, facing, event.getHand());
            return;
        }

        MachineRelocatorUtils.capture(stack, player.world, event.getPos(), player, event.getHand());
    }
}