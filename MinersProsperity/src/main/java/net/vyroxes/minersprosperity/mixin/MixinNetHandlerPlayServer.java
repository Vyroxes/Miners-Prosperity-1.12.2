package net.vyroxes.minersprosperity.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.vyroxes.minersprosperity.init.ModEnchantments;
import net.vyroxes.minersprosperity.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer
{
    @Accessor("server")
    public abstract MinecraftServer getServer();

    /**
     * @author Vyroxes
     * @reason Allows the Far Reach enchantment to extend the interaction and attack range
     *         of the player based on enchantment level and visibility. Overrides the default
     *         distance checks for entity interaction.
     */
    @Overwrite
    public void processUseEntity(CPacketUseEntity packetIn)
    {
        NetHandlerPlayServer self = (NetHandlerPlayServer)(Object)this;
        EntityPlayerMP player = self.player;

        PacketThreadUtil.checkThreadAndEnqueue(packetIn, self, player.getServerWorld());
        WorldServer worldserver = this.getServer().getWorld(player.dimension);
        Entity entity = packetIn.getEntityFromWorld(worldserver);
        player.markPlayerActive();

        if (entity != null)
        {
            boolean flag = player.canEntityBeSeen(entity);
            ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FAR_REACH, leggings);
            double d0;
            if (level > 0)
            {
                d0 = flag ? Math.pow(ModConfig.ENCHANTMENTS.farReachDistance, 2) : Math.pow((double) ModConfig.ENCHANTMENTS.farReachDistance/2, 2);
            }
            else
            {
                d0 = flag ? 36 : 9;
            }

            if (player.getDistanceSq(entity) < d0)
            {
                switch (packetIn.getAction())
                {
                    case INTERACT:
                        player.interactOn(entity, packetIn.getHand());
                        break;
                    case INTERACT_AT:
                        entity.applyPlayerInteraction(player, packetIn.getHitVec(), packetIn.getHand());
                        break;
                    case ATTACK:
                        if (!entity.isDead && entity != player)
                        {
                            player.attackTargetEntityWithCurrentItem(entity);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid action");
                }
            }
        }
    }
}