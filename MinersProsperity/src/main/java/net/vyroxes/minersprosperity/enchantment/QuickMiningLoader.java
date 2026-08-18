package net.vyroxes.minersprosperity.enchantment;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.Tags;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuickMiningLoader
{
    private static final Set<BlockMeta> allowedBlocks = new HashSet<>();
    private static final Set<String> allowedOreDicts = new HashSet<>();
    private static final Pattern METADATA_PATTERN = Pattern.compile("^[^:]+:[^/]+/(\\d+|\\*)$");
    private static final Pattern ORE_PATTERN = Pattern.compile("(^|[^a-z])ore(\\d*|[^a-z]|$)");

    private static boolean isOreBlock(Block block)
    {
        String registryName = Objects.requireNonNull(block.getRegistryName()).getPath().toLowerCase();
        Matcher matcher = ORE_PATTERN.matcher(registryName);
        return matcher.find();
    }

    public static void loadFromJSON()
    {
        File configDir = new File(Loader.instance().getConfigDir(), Tags.MODID);
        File jsonFile = new File(configDir, "quick_mining.json");

        if (!jsonFile.exists()) createDefaultFile(jsonFile);

        try (Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8))
        {
            JsonArray array = new JsonParser().parse(reader).getAsJsonArray();

            for (JsonElement element : array)
            {
                if (!element.isJsonPrimitive()) continue;
                String entry = element.getAsString().trim();

                if (entry.startsWith("ore")) allowedOreDicts.add(entry);
                else if (METADATA_PATTERN.matcher(entry).matches())
                {
                    String[] parts = entry.split("/");
                    String blockName = parts[0];

                    int meta;

                    if (parts[1].equals("*")) meta = OreDictionary.WILDCARD_VALUE;
                    else
                    {
                        meta = Integer.parseInt(parts[1]);

                        if (meta < 0 || meta > 15)
                        {
                            MinersProsperity.logger.warn("Invalid metadata in quick mining entry: {}", entry);
                            continue;
                        }
                    }

                    Block block = Block.getBlockFromName(blockName);
                    if (block != null) allowedBlocks.add(new BlockMeta(block, meta));
                    else MinersProsperity.logger.warn("Unknown block: {}", blockName);
                }
                else
                {
                    Block block = Block.getBlockFromName(entry);
                    if (block != null) allowedBlocks.add(new BlockMeta(block, 0));
                    else MinersProsperity.logger.warn("Unknown block: {}", entry);
                }
            }

            MinersProsperity.logger.info("Loaded {} quick mining blocks and {} oreDict entries", allowedBlocks.size(), allowedOreDicts.size());
        }
        catch (Exception e)
        {
            MinersProsperity.logger.error("Error while loading quick mining configuration", e);
        }
    }

    public static boolean canQuickMine(ItemStack stack)
    {
        if (stack == null || stack.isEmpty()) return false;

        Block block = Block.getBlockFromItem(stack.getItem());

        int meta = stack.getMetadata();

        if (allowedBlocks.contains(new BlockMeta(block, meta)) || allowedBlocks.contains(new BlockMeta(block, OreDictionary.WILDCARD_VALUE))) return true;

        int[] ids = OreDictionary.getOreIDs(stack);
        for (int id : ids)
        {
            String name = OreDictionary.getOreName(id);
            if (allowedOreDicts.contains(name)) return true;
        }

        return false;
    }

    private static void createDefaultFile(File file)
    {
        try
        {
            File configDir = new File(Loader.instance().getConfigDir(), Tags.MODID);
            if (!configDir.exists() && !configDir.mkdirs())
            {
                MinersProsperity.logger.error("Could not create config directory: {}", configDir.getAbsolutePath());
                return;
            }

            JsonArray defaultList = new JsonArray();

            String exampleComment =
                    """
                            // Example:
                            // [
                            //   "modid:block/metadata",
                            //   "modid:block/*",
                            //   "modid:block",
                            //   "oreDict"
                            // ]
                            //
                            // block/3 = only metadata 3
                            // block/* = all metadata
                            // block = only metadata 0
                            // oreBlock = any block registered under this Ore Dictionary entry
                            
                            """;

            for (ResourceLocation blockId : Block.REGISTRY.getKeys())
            {
                Block block = Block.REGISTRY.getObject(blockId);

                if (block == Block.getBlockFromName("air")) continue;

                boolean added = false;

                if (isOreBlock(block))
                {
                    defaultList.add(blockId.toString());
                    added = true;
                }

                boolean hasMultipleMetas = false;

                for (int meta = 1; meta <= 15; meta++)
                {
                    ItemStack stack = new ItemStack(block, 1, meta);

                    if (!stack.isEmpty() && OreDictionary.getOreIDs(stack).length > 0)
                    {
                        hasMultipleMetas = true;
                        break;
                    }
                }

                for (int meta = 0; meta <= 15; meta++)
                {
                    ItemStack stack = new ItemStack(block, 1, meta);

                    if (stack.isEmpty()) continue;

                    int[] oreIDs = OreDictionary.getOreIDs(stack);

                    boolean localAdded = false;

                    for (int id : oreIDs)
                    {
                        String oreName = OreDictionary.getOreName(id);
                        if (oreName != null && oreName.startsWith("ore"))
                        {
                            if (hasMultipleMetas)
                            {
                                String blockMeta = blockId.toString() + "/" + meta;
                                if (!defaultList.contains(new JsonPrimitive(blockMeta)))
                                {
                                    defaultList.add(blockMeta);
                                    localAdded = true;
                                }
                            }
                            else
                            {
                                if (meta == 0 && !defaultList.contains(new JsonPrimitive(blockId.toString())))
                                {
                                    defaultList.add(blockId.toString());
                                    localAdded = true;
                                }
                            }

                            if (!defaultList.contains(new JsonPrimitive(oreName))) defaultList.add(oreName);
                        }
                    }

                    if (!added && localAdded) added = true;
                }
            }

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))
            {
                writer.write(exampleComment);
                new GsonBuilder().setPrettyPrinting().create().toJson(defaultList, writer);
            }

            MinersProsperity.logger.info("Created default quick_miming.json file in the config directory");
        }
        catch (IOException e)
        {
            MinersProsperity.logger.error("Failed to create default quick_miming.json file in the config directory", e);
        }
    }

    @Desugar
    private record BlockMeta(Block block, int meta)
    {
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (obj instanceof BlockMeta other) return block == other.block && meta == other.meta;
            return false;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(block, meta);
        }
    }
}