package net.vyroxes.minersprosperity.integration.baubles;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.registries.IForgeRegistry;
import net.vyroxes.minersprosperity.item.ItemTotemOfUndying;

public final class BaublesRegistryCompat {
    private static final String BAUBLES_MOD_ID = "baubles";

    private BaublesRegistryCompat() {}

    public static void registerItems(IForgeRegistry<Item> registry) {

        if (!Loader.isModLoaded(BAUBLES_MOD_ID)) return;

        ResourceLocation registryName = new ResourceLocation("minecraft", "totem_of_undying");

        ModContainer minecraftContainer = Loader.instance().getMinecraftModContainer();
        ModContainer previousContainer = Loader.instance().activeModContainer();

        Item customTotem = new ItemTotemOfUndying();

        try {
            Loader.instance().setActiveModContainer(minecraftContainer);
            customTotem.setRegistryName(registryName);
        } finally {
            Loader.instance().setActiveModContainer(previousContainer);
        }

        registry.register(customTotem);
    }
}