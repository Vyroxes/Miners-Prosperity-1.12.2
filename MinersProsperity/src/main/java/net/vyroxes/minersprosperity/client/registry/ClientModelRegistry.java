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
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.vyroxes.minersprosperity.block.machine.ItemBlockEnergyStorage;
import net.vyroxes.minersprosperity.client.render.EnergyStorageTESR;

@Mod.EventBusSubscriber(modid = Tags.MODID, value = Side.CLIENT)
public final class ClientModelRegistry {
    private ClientModelRegistry() {}

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Item item : ModItems.ITEMS) if (Item.REGISTRY.getNameForObject(item) != null) registerDefaultModel(item);

        for (Item item : ModTools.TOOLS) registerToolModel(item);

        for (Item item : ModArmor.ARMORS) registerDefaultModel(item);
    }

    @SubscribeEvent
    public static void registerEnergyStorageCoreTexture(TextureStitchEvent.Pre event)
    {
        for (String shape : new String[]{"hexagon", "pentagon"})
        {
            event.getMap().registerSprite(new ResourceLocation(Tags.MODID, "blocks/energy_storage_core_" + shape + "_base"));
            event.getMap().registerSprite(new ResourceLocation(Tags.MODID, "blocks/energy_storage_core_" + shape + "_stripe"));
        }
    }

    @SubscribeEvent
    public static void bakeEnergyStorageCore(ModelBakeEvent event)
    {
        List<BakedQuad> core = new ArrayList<>();
        for (int layer = 0; layer < 2; layer++)
        {
            String suffix = layer == 0 ? "_base" : "_stripe";
            TextureAtlasSprite hexagon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(Tags.MODID + ":blocks/energy_storage_core_hexagon" + suffix);
            TextureAtlasSprite pentagon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(Tags.MODID + ":blocks/energy_storage_core_pentagon" + suffix);
            double[][] vertices = EnergyStorageTESR.createCoreVertices(layer == 0 ? 0.14D : 0.1401D);
            for (int face = 0; face < vertices.length; face += 4)
            {
                TextureAtlasSprite texture = vertices[face][5] == 6 ? hexagon : pentagon;
                double[] a = vertices[face];
                double[] b = vertices[face + 1];
                double[] c = vertices[face + 2];
                float nx = (float) ((b[1] - a[1]) * (c[2] - a[2]) - (b[2] - a[2]) * (c[1] - a[1]));
                float ny = (float) ((b[2] - a[2]) * (c[0] - a[0]) - (b[0] - a[0]) * (c[2] - a[2]));
                float nz = (float) ((b[0] - a[0]) * (c[1] - a[1]) - (b[1] - a[1]) * (c[0] - a[0]));
                float length = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
                nx /= length;
                ny /= length;
                nz /= length;
                UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
                builder.setTexture(texture);
                builder.setQuadTint(layer == 0 ? -1 : 0);
                builder.setQuadOrientation(EnumFacing.getFacingFromVector(nx, ny, nz));
                builder.setApplyDiffuseLighting(false);
                for (int vertex = 0; vertex < 4; vertex++)
                {
                    double[] point = vertices[face + vertex];
                    for (int element = 0; element < DefaultVertexFormats.ITEM.getElementCount(); element++)
                    {
                        switch (DefaultVertexFormats.ITEM.getElement(element).getUsage())
                        {
                            case POSITION -> builder.put(element, (float) point[0] + 0.5F, (float) point[1] + 0.5F, (float) point[2] + 0.5F, 1);
                            case COLOR -> builder.put(element, 1, 1, 1, 1);
                            case UV -> builder.put(element, texture.getInterpolatedU(point[3]), texture.getInterpolatedV(point[4]), 0, 1);
                            case NORMAL -> builder.put(element, nx, ny, nz, 0);
                            default -> builder.put(element);
                        }
                    }
                }
                core.add(builder.build());
            }
        }
        for (Item item : ModItems.ITEMS)
        {
            if (!(item instanceof ItemBlockEnergyStorage) || Item.REGISTRY.getNameForObject(item) == null) continue;
            ModelResourceLocation location = new ModelResourceLocation(item.getRegistryName(), "inventory");
            IBakedModel model = event.getModelRegistry().getObject(location);
            if (model == null) continue;
            event.getModelRegistry().putObject(location, new BakedModelWrapper<IBakedModel>(model)
            {
                @Override
                public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
                {
                    if (side != null) return super.getQuads(state, side, rand);
                    List<BakedQuad> quads = new ArrayList<>(super.getQuads(state, null, rand));
                    quads.addAll(core);
                    return quads;
                }
            });
        }
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