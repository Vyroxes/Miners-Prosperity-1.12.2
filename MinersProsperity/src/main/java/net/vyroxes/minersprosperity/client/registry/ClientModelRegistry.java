package net.vyroxes.minersprosperity.client.registry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.client.render.ItemRenderCustomShield;
import net.vyroxes.minersprosperity.client.render.RenderEntityRelocator;
import net.vyroxes.minersprosperity.entity.projectile.EntityRelocatorProjectile;
import net.vyroxes.minersprosperity.init.ModArmor;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.init.ModTools;
import net.vyroxes.minersprosperity.item.ItemShieldCustom;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Tags.MODID, value = Side.CLIENT)
public final class ClientModelRegistry {
    private ClientModelRegistry() {}

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Item item : ModItems.ITEMS) if (Item.REGISTRY.getNameForObject(item) != null) registerDefaultModel(item);

        for (Item item : ModTools.TOOLS) registerToolModel(item);

        for (Item item : ModArmor.ARMORS) registerDefaultModel(item);
    }

    private static void registerToolModel(Item item) {
        if (item instanceof ItemShieldCustom) {
            registerShieldModel(item);
            return;
        }

        registerDefaultModel(item);
    }

    private static void registerDefaultModel(Item item) {
        ResourceLocation registryName = Objects.requireNonNull(item.getRegistryName(), "Item without registry name: " + item);

        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(registryName, "inventory"));
    }

    private static void registerShieldModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Tags.MODID, "custom_shield"), "inventory"));

        item.setTileEntityItemStackRenderer(new ItemRenderCustomShield());
    }

    public static void registerEntityRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityRelocatorProjectile.class, RenderEntityRelocator::new);
    }
}