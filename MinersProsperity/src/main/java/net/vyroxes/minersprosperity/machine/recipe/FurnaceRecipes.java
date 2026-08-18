package net.vyroxes.minersprosperity.machine.recipe;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.vyroxes.minersprosperity.init.ModBlocks;
import net.vyroxes.minersprosperity.init.ModItems;

public final class FurnaceRecipes {
    private FurnaceRecipes() {}

    public static void register() {
        smelt(ModItems.RESIN, ModItems.RUBBER, 0.7F);

        smelt(ModBlocks.HALITE_ORE, ModItems.SALT, 0.7F);
        smelt(ModBlocks.SULFUR_ORE, ModItems.SULFUR, 0.7F);
        smelt(ModBlocks.GRAPHITE_ORE, ModItems.GRAPHITE, 0.7F);
        smelt(ModBlocks.COPPER_ORE, ModItems.COPPER_INGOT, 0.7F);
        smelt(ModBlocks.TIN_ORE, ModItems.TIN_INGOT, 0.7F);
        smelt(ModBlocks.SILVER_ORE, ModItems.SILVER_INGOT, 0.7F);
        smelt(ModBlocks.LEAD_ORE, ModItems.LEAD_INGOT, 0.7F);
        smelt(ModBlocks.ZINC_ORE, ModItems.ZINC_INGOT, 0.7F);
        smelt(ModBlocks.ALUMINUM_ORE, ModItems.ALUMINUM_INGOT, 0.7F);

        GameRegistry.addSmelting(new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.REFINED_IRON_INGOT), 0.7F);

        smelt(ModBlocks.NICKEL_ORE, ModItems.NICKEL_INGOT, 0.7F);
        smelt(ModBlocks.CHROMIUM_ORE, ModItems.CHROMIUM_INGOT, 0.7F);
        smelt(ModBlocks.URANIUM_ORE, ModItems.URANIUM_INGOT, 0.7F);
        smelt(ModBlocks.TUNGSTEN_ORE, ModItems.TUNGSTEN_INGOT, 0.7F);
        smelt(ModBlocks.PLATINUM_ORE, ModItems.PLATINUM_INGOT, 0.7F);
        smelt(ModBlocks.IRIDIUM_ORE, ModItems.IRIDIUM_INGOT, 0.7F);
        smelt(ModBlocks.TITANIUM_ORE, ModItems.TITANIUM_INGOT, 0.7F);
        smelt(ModBlocks.RUBY_ORE, ModItems.RUBY, 0.7F);
        smelt(ModBlocks.SAPPHIRE_ORE, ModItems.SAPPHIRE, 0.7F);
        smelt(ModBlocks.AMETHYST_ORE, ModItems.AMETHYST, 0.7F);
        smelt(ModBlocks.TOPAZ_ORE, ModItems.TOPAZ, 0.7F);
        smelt(ModBlocks.PERIDOT_ORE, ModItems.PERIDOT, 0.7F);

        GameRegistry.addSmelting(new ItemStack(Blocks.OBSIDIAN), new ItemStack(ModItems.REFINED_OBSIDIAN_INGOT), 0.7F);

        smelt(ModBlocks.COBALT_ORE, ModItems.COBALT_INGOT, 0.7F);
        smelt(ModBlocks.ONYX_ORE, ModItems.ONYX, 0.7F);
        smelt(ModBlocks.BLACK_OPAL_ORE, ModItems.BLACK_OPAL, 0.7F);
        smelt(ModBlocks.AZURE_ORE, ModItems.AZURE_SHARD, 0.7F);
        smelt(ModBlocks.ROSALITE_ORE, ModItems.ROSALITE_SHARD, 0.7F);
        smelt(ModBlocks.VERDITE_ORE, ModItems.VERDITE_SHARD, 0.7F);
        smelt(ModBlocks.VOIDIUM_ORE, ModItems.VOIDIUM, 0.7F);
        smelt(ModBlocks.LUMINOSITE_ORE, ModItems.LUMINOSITE_SHARD, 0.7F);

        smelt(ModItems.FLINT_DUST, Items.FLINT, 0.1F);

        GameRegistry.addSmelting(new ItemStack(ModItems.CHARCOAL_DUST), new ItemStack(Items.COAL, 1, 1), 0.1F);

        smelt(ModItems.COAL_DUST, Items.COAL, 0.1F);
        smelt(ModItems.GRAPHITE_DUST, ModItems.GRAPHITE, 0.1F);
        smelt(ModItems.COKE_DUST, ModItems.COKE, 0.1F);

        smelt(ModItems.COPPER_DUST, ModItems.COPPER_INGOT, 0.7F);
        smelt(ModItems.TIN_DUST, ModItems.TIN_INGOT, 0.7F);
        smelt(ModItems.BRONZE_DUST, ModItems.BRONZE_INGOT, 0.7F);
        smelt(ModItems.SILVER_DUST, ModItems.SILVER_INGOT, 0.7F);
        smelt(ModItems.LEAD_DUST, ModItems.LEAD_INGOT, 0.7F);
        smelt(ModItems.ZINC_DUST, ModItems.ZINC_INGOT, 0.7F);
        smelt(ModItems.BRASS_DUST, ModItems.BRASS_INGOT, 0.7F);
        smelt(ModItems.ALUMINUM_DUST, ModItems.ALUMINUM_INGOT, 0.7F);
        smelt(ModItems.DURALUMINUM_DUST, ModItems.DURALUMINUM_INGOT, 0.7F);
        smelt(ModItems.IRON_DUST, Items.IRON_INGOT, 0.7F);
        smelt(ModItems.REFINED_IRON_DUST, ModItems.REFINED_IRON_INGOT, 0.7F);
        smelt(ModItems.STEEL_DUST, ModItems.STEEL_INGOT, 0.7F);
        smelt(ModItems.NICKEL_DUST, ModItems.NICKEL_INGOT, 0.7F);
        smelt(ModItems.CHROMIUM_DUST, ModItems.CHROMIUM_INGOT, 0.7F);

        smelt(Items.GLOWSTONE_DUST, ModItems.GLOWSTONE_SHARD, 0.7F);
        smelt(ModItems.QUARTZ_DUST, Items.QUARTZ, 0.2F);

        smelt(ModItems.INCONEL_DUST, ModItems.INCONEL_INGOT, 0.7F);
        smelt(ModItems.URANIUM_DUST, ModItems.URANIUM_INGOT, 0.7F);
        smelt(ModItems.TUNGSTEN_DUST, ModItems.TUNGSTEN_INGOT, 0.7F);
        smelt(ModItems.GOLD_DUST, Items.GOLD_INGOT, 1.0F);
        smelt(ModItems.PLATINUM_DUST, ModItems.PLATINUM_INGOT, 0.7F);
        smelt(ModItems.WHITE_GOLD_DUST, ModItems.WHITE_GOLD_INGOT, 0.7F);
        smelt(ModItems.ELECTRUM_DUST, ModItems.ELECTRUM_INGOT, 0.7F);
        smelt(ModItems.IRIDIUM_DUST, ModItems.IRIDIUM_INGOT, 0.7F);
        smelt(ModItems.TITANIUM_DUST, ModItems.TITANIUM_INGOT, 0.7F);

        GameRegistry.addSmelting(new ItemStack(ModItems.LAPIS_DUST), new ItemStack(Items.DYE, 1, 4), 0.7F);

        smelt(Items.REDSTONE, ModItems.REDSTONE_SHARD, 0.7F);

        smelt(ModItems.RUBY_DUST, ModItems.RUBY, 0.7F);
        smelt(ModItems.SAPPHIRE_DUST, ModItems.SAPPHIRE, 0.7F);
        smelt(ModItems.AMETHYST_DUST, ModItems.AMETHYST, 0.7F);
        smelt(ModItems.TOPAZ_DUST, ModItems.TOPAZ, 0.7F);
        smelt(ModItems.PERIDOT_DUST, ModItems.PERIDOT, 0.7F);
        smelt(ModItems.DIAMOND_DUST, Items.DIAMOND, 1.0F);
        smelt(ModItems.REFINED_OBSIDIAN_DUST, ModItems.REFINED_OBSIDIAN_INGOT, 0.7F);
        smelt(ModItems.EMERALD_DUST, Items.EMERALD, 0.7F);
        smelt(ModItems.COBALT_DUST, ModItems.COBALT_INGOT, 0.7F);
        smelt(ModItems.COBALTSTEEL_DUST, ModItems.COBALTSTEEL_INGOT, 0.7F);
        smelt(ModItems.ONYX_DUST, ModItems.ONYX, 0.7F);
        smelt(ModItems.DARK_STEEL_DUST, ModItems.DARK_STEEL_INGOT, 0.7F);
        smelt(ModItems.BLACK_OPAL_DUST, ModItems.BLACK_OPAL, 0.7F);
        smelt(ModItems.AZURE_DUST, ModItems.AZURE_SHARD, 0.7F);
        smelt(ModItems.ROSALITE_DUST, ModItems.ROSALITE_SHARD, 0.7F);
        smelt(ModItems.VERDITE_DUST, ModItems.VERDITE_SHARD, 0.7F);
        smelt(ModItems.VOIDIUM_DUST, ModItems.VOIDIUM, 0.7F);
        smelt(ModItems.LUMINOSITE_DUST, ModItems.LUMINOSITE_SHARD, 0.7F);
    }

    private static void smelt(Item input, Item output, float experience) { GameRegistry.addSmelting(new ItemStack(input), new ItemStack(output), experience); }

    private static void smelt(Block input, Item output, float experience) { GameRegistry.addSmelting(new ItemStack(input), new ItemStack(output), experience); }
}