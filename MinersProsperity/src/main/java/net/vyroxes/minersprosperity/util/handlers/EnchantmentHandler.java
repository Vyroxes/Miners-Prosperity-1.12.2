package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.vyroxes.minersprosperity.init.EnchantmentInit;

@Mod.EventBusSubscriber
public class EnchantmentHandler
{
    @SubscribeEvent
    public static void onEnchantmentRegister(RegistryEvent.Register<Enchantment> event)
    {
        event.getRegistry().registerAll(EnchantmentInit.ENCHANTMENTS.toArray(new Enchantment[0]));
    }
}