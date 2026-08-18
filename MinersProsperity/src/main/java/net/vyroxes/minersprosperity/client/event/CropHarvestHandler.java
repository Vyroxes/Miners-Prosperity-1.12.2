package net.vyroxes.minersprosperity.client.event;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.vyroxes.minersprosperity.config.ModConfig;
import net.vyroxes.minersprosperity.init.ModEnchantments;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Mod.EventBusSubscriber
public class CropHarvestHandler
{
    private static final CropBlacklist blacklist = new CropBlacklist(ModConfig.ENCHANTMENTS.farmersBlessingCropBlacklist);
    private static final Random rand = new Random();

    @SubscribeEvent
    public static void onRightClickCrop(PlayerInteractEvent.RightClickBlock event)
    {
        if (!ModConfig.OTHER.rightClickHarvest)
            return;

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        EntityPlayer player = event.getEntityPlayer();

        if (!world.isRemote)
        {
            if (!(block instanceof BlockCrops crops))
                return;

            if (!crops.isMaxAge(state))
                return;

            ItemStack held = player.getHeldItemMainhand();
            int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FARMERS_BLESSING, held);
            NonNullList<ItemStack> drops = NonNullList.create();
            crops.getDrops(drops, world, pos, state, 0);

            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            boolean seedRemoved = false;

            for (ItemStack stack : drops)
            {
                if (stack.isEmpty())
                    continue;

                boolean isSeed = Arrays.stream(OreDictionary.getOreIDs(stack)).mapToObj(OreDictionary::getOreName).anyMatch(name -> name.toLowerCase().contains("seed"));

                if (isSeed && !seedRemoved)
                {
                    stack.shrink(1);
                    seedRemoved = true;

                    if (stack.isEmpty())
                        continue;
                }
                else if (!isSeed && !blacklist.isBlacklisted(block, state, stack))
                {
                    int bonus = switch (level) {
                        case 1 -> 1;
                        case 2 -> 1 + rand.nextInt(2);
                        case 3 -> 2 + rand.nextInt(2);
                        default -> 0;
                    };
                    stack.grow(bonus);

                    if (ModConfig.OTHER.harvestXP)
                    {
                        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                        int xp = 1 + rand.nextInt(2);
                        if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.INSIGHT, held) > 0)
                        {
                            float multiplier = 1.0F + (0.2F + 0.1F * level);
                            int bonusXP = Math.max(1, Math.round(xp * (multiplier - 1)));
                            xp += bonusXP;
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.VACUUM, boots) > 0)
                        {
                            player.addExperience(xp);
                        }
                        else
                        {
                            world.spawnEntity(new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), xp));
                        }
                    }
                }

                if (!stack.isEmpty())
                {
                    Block.spawnAsEntity(world, pos, stack);
                }
            }

            world.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.setBlockState(pos, crops.withAge(0), 2);
        }
        else if (block instanceof BlockCrops crops && crops.isMaxAge(state))
        {
            player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    @SubscribeEvent
    public static void onBreakCrop(BlockEvent.BreakEvent event)
    {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        EntityPlayer player = event.getPlayer();

        if (!world.isRemote)
        {
            if (!(block instanceof BlockCrops crops))
                return;

            if (!crops.isMaxAge(state))
                return;

            ItemStack held = player.getHeldItemMainhand();
            int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FARMERS_BLESSING, held);

            if (ModConfig.OTHER.harvestXP)
            {
                ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                int xp = 1 + rand.nextInt(2);
                if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.INSIGHT, held) > 0)
                {
                    float multiplier = 1.0F + (0.2F + 0.1F * level);
                    int bonusXP = Math.max(1, Math.round(xp * (multiplier - 1)));
                    xp += bonusXP;
                }

                if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.VACUUM, boots) > 0)
                {
                    player.addExperience(xp);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                }
                else
                {
                    world.spawnEntity(new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), xp));
                }
            }

            if (level <= 0)
                return;

            event.setCanceled(true);
            world.setBlockToAir(pos);
            world.playSound(null, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);

            NonNullList<ItemStack> drops = NonNullList.create();
            crops.getDrops(drops, world, pos, state, 0);

            boolean seedRemoved = false;

            for (ItemStack stack : drops)
            {
                if (stack.isEmpty())
                    continue;

                boolean isSeed = Arrays.stream(OreDictionary.getOreIDs(stack)).mapToObj(OreDictionary::getOreName).anyMatch(name -> name.toLowerCase().contains("seed"));

                if (isSeed && !seedRemoved)
                {
                    stack.shrink(1);
                    seedRemoved = true;

                    if (stack.isEmpty())
                        continue;
                }
                else if (!isSeed && !blacklist.isBlacklisted(block, state, stack))
                {
                    int bonus = switch (level) {
                        case 1 -> 1;
                        case 2 -> 1 + rand.nextInt(2);
                        case 3 -> 2 + rand.nextInt(2);
                        default -> 0;
                    };
                    stack.grow(bonus);
                }

                if (!stack.isEmpty())
                {
                    Block.spawnAsEntity(world, pos, stack);
                }
            }
        }
    }

    public static class CropBlacklist
    {
        private final Set<ResourceLocation> blockSet = new HashSet<>();
        private final Set<String> blockWithMeta = new HashSet<>();
        private final Set<String> blockWildcard = new HashSet<>();
        private final Set<String> modWildcard = new HashSet<>();
        private final Set<String> oreDictSet = new HashSet<>();
        private final Set<String> oreDictWildcard = new HashSet<>();

        public CropBlacklist(String[] entries)
        {
            for (String entry : entries)
            {
                if (entry == null || entry.trim().isEmpty()) continue;

                entry = entry.trim();

                if (entry.contains(":") && entry.endsWith("*"))
                {
                    String modid = entry.substring(0, entry.indexOf(':')).toLowerCase();
                    modWildcard.add(modid);
                }

                else if (entry.contains(":") && entry.endsWith("/*")) blockWildcard.add(entry.toLowerCase());

                else if (entry.contains(":") && entry.contains("/")) blockWithMeta.add(entry.toLowerCase());

                else if (entry.contains(":")) blockSet.add(new ResourceLocation(entry.toLowerCase()));

                else if (entry.endsWith("*")) oreDictWildcard.add(entry.substring(0, entry.length() - 1).toLowerCase());

                else oreDictSet.add(entry.toLowerCase());
            }
        }

        public boolean isBlacklisted(Block block, IBlockState state, ItemStack stack)
        {
            ResourceLocation id = block.getRegistryName();

            if (id == null) return false;

            String modid = id.getNamespace();
            String name = id.getPath();
            String base = modid + ":" + name;
            int meta = block.getMetaFromState(state);

            if (modWildcard.contains(modid.toLowerCase())) return true;

            if (blockWildcard.contains((base + "/*").toLowerCase())) return true;

            if (blockSet.contains(id) && meta == 0) return true;

            String full = base + "/" + meta;

            if (blockWithMeta.contains(full.toLowerCase())) return true;

            if (!stack.isEmpty())
            {
                int[] oreIds = OreDictionary.getOreIDs(stack);

                for (int oreId : oreIds)
                {
                    String oreName = OreDictionary.getOreName(oreId).toLowerCase();

                    if (oreDictSet.contains(oreName)) return true;

                    for (String prefix : oreDictWildcard) if (oreName.startsWith(prefix)) return true;
                }
            }

            return false;
        }
    }
}