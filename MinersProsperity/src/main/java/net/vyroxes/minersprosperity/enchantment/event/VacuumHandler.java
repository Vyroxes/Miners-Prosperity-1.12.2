package net.vyroxes.minersprosperity.enchantment.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.vyroxes.minersprosperity.init.ModEnchantments;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.item.backpack.ItemBackpackBase;
import java.util.*;

@Mod.EventBusSubscriber
public class VacuumHandler
{
    private static final List<RecentBreak> RECENT_BREAKS = new ArrayList<>();
    private static final int RECENT_BREAK_LIFETIME = 3;
    private static final int RECENT_BREAK_RANGE_SQ = 16;

    private static final class RecentBreak
    {
        private final BlockPos pos;
        private final EntityPlayer player;
        private final int dimension;
        private final long expiresAt;

        private RecentBreak(World world, BlockPos pos, EntityPlayer player)
        {
            this.pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
            this.player = player;
            this.dimension = world.provider.getDimension();
            this.expiresAt = world.getTotalWorldTime() + RECENT_BREAK_LIFETIME;
        }
    }

    private static boolean hasVacuum(EntityPlayer player)
    {
        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        if (boots.isEmpty()) return false;

        if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.VACUUM, boots) <= 0) return false;

        NBTTagCompound tag = boots.getTagCompound();

        return tag != null && tag.getBoolean("Vacuum");
    }

    private static ItemStack collectDirectly(EntityPlayer player, ItemStack input)
    {
        if (input.isEmpty()) return ItemStack.EMPTY;

        ItemStack remaining = input.copy();
        List<ItemStack> backpacks = ItemBackpackBase.getAllBackpacksFromPlayer(player);

        for (ItemStack backpack : backpacks)
        {
            if (remaining.isEmpty()) return ItemStack.EMPTY;

            NBTTagCompound compound = backpack.getTagCompound();

            if (compound == null) continue;

            NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");

            if (!backpackData.getBoolean("AutoCollect")) continue;

            if (AutoCollectHandler.addToBackpack(player, backpack, remaining)) return ItemStack.EMPTY;
        }

        if (!remaining.isEmpty()) player.inventory.addItemStackToInventory(remaining);

        return remaining.isEmpty() ? ItemStack.EMPTY : remaining;
    }

    private static void collectOrDrop(EntityPlayer player, World world, BlockPos pos, ItemStack stack)
    {
        ItemStack remaining = collectDirectly(player, stack);

        if (!remaining.isEmpty()) Block.spawnAsEntity(world, pos, remaining);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event)
    {
        World world = event.getWorld();

        if (world.isRemote) return;

        EntityPlayer player = event.getHarvester();

        if (player == null) player = findRecentBreaker(world, event.getPos());

        if (player == null || !hasVacuum(player)) return;

        if (event.getHarvester() == null) rememberBreak(world, event.getPos(), player);

        ListIterator<ItemStack> iterator = event.getDrops().listIterator();

        while (iterator.hasNext())
        {
            ItemStack drop = iterator.next();

            if (drop.isEmpty())
            {
                iterator.remove();
                continue;
            }

            ItemStack remaining = collectDirectly(player, drop);

            if (remaining.isEmpty()) iterator.remove();
            else iterator.set(remaining);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockBreak(BlockEvent.BreakEvent event)
    {
        EntityPlayer player = event.getPlayer();

        if (player == null || event.getWorld().isRemote || !hasVacuum(player)) return;

        ItemStack mainhand = player.getHeldItemMainhand();
        ItemStack offhand = player.getHeldItemOffhand();

        if ((!mainhand.isEmpty() && mainhand.getItem() == ModItems.MACHINE_RELOCATOR) || (!offhand.isEmpty() && offhand.getItem() == ModItems.MACHINE_RELOCATOR)) return;

        World world = event.getWorld();
        BlockPos pos = event.getPos();

        rememberBreak(world, pos, player);

        TileEntity tile = world.getTileEntity(pos);

        if (tile != null) collectTileInventory(player, world, pos, tile);

        ItemStack held = player.getHeldItemMainhand();

        if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.QUICK_MINING, held) > 0) return;

        int xp = event.getExpToDrop();

        if (xp > 0)
        {
            player.addExperience(xp);
            event.setExpToDrop(0);
        }
    }

    private static void collectTileInventory(EntityPlayer player, World world, BlockPos pos, TileEntity tile)
    {
        if (tile instanceof IInventory inventory)
        {
            for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
            {
                ItemStack extracted = inventory.removeStackFromSlot(slot);

                if (!extracted.isEmpty()) collectOrDrop(player, world, pos, extracted);
            }

            inventory.markDirty();
            return;
        }

        Set<IItemHandler> checkedHandlers = Collections.newSetFromMap(new IdentityHashMap<>());

        collectHandler(player, world, pos, tile, null, checkedHandlers);

        for (EnumFacing facing : EnumFacing.values()) collectHandler(player, world, pos, tile, facing, checkedHandlers);
    }

    private static void collectHandler(EntityPlayer player, World world, BlockPos pos, TileEntity tile, EnumFacing facing, Set<IItemHandler> checkedHandlers)
    {
        if (!tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) return;

        IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);

        if (handler == null || !checkedHandlers.add(handler)) return;

        for (int slot = 0; slot < handler.getSlots(); slot++)
        {
            ItemStack extracted = handler.extractItem(slot, Integer.MAX_VALUE, false);

            if (!extracted.isEmpty()) collectOrDrop(player, world, pos, extracted);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMobDrops(LivingDropsEvent event)
    {
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer player)) return;

        if (player.world.isRemote || !hasVacuum(player)) return;

        Iterator<EntityItem> iterator = event.getDrops().iterator();

        while (iterator.hasNext())
        {
            EntityItem entityItem = iterator.next();
            ItemStack drop = entityItem.getItem();

            if (drop.isEmpty())
            {
                entityItem.setDead();
                iterator.remove();
                continue;
            }

            ItemStack remaining = collectDirectly(player, drop);

            if (remaining.isEmpty())
            {
                entityItem.setDead();
                iterator.remove();
            }
            else entityItem.setItem(remaining);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemFished(ItemFishedEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();

        if (player.world.isRemote || !hasVacuum(player)) return;

        ListIterator<ItemStack> iterator = event.getDrops().listIterator();

        while (iterator.hasNext())
        {
            ItemStack drop = iterator.next();

            if (drop.isEmpty())
            {
                iterator.remove();
                continue;
            }

            ItemStack remaining = collectDirectly(player, drop);

            if (remaining.isEmpty()) iterator.remove();
            else iterator.set(remaining);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMobKilled(LivingExperienceDropEvent event)
    {
        EntityPlayer player = event.getAttackingPlayer();

        if (player == null || player.world.isRemote || !hasVacuum(player)) return;

        int xp = event.getDroppedExperience();
        ItemStack held = player.getHeldItemMainhand();
        int insightLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.INSIGHT, held);

        if (insightLevel > 0)
        {
            float multiplier = 1.0F + 0.2F + 0.1F * insightLevel;
            int bonusXP = Math.round(event.getOriginalExperience() * (multiplier - 1.0F));

            xp += bonusXP;
        }

        if (xp > 0)
        {
            player.addExperience(xp);
            event.setDroppedExperience(0);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END || event.player.world.isRemote) return;

        EntityPlayer player = event.player;

        for (ItemStack stack : player.inventory.mainInventory)
        {
            if (stack.isEmpty()) continue;

            if (!(stack.getItem() instanceof ItemArmor armor)) continue;

            if (armor.armorType != EntityEquipmentSlot.FEET) continue;

            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.VACUUM, stack) <= 0) continue;

            ensureVacuumTag(stack);
        }

        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        if (!boots.isEmpty() && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.VACUUM, boots) > 0) ensureVacuumTag(boots);
    }

    private static void ensureVacuumTag(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null)
        {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        if (!tag.hasKey("Vacuum")) tag.setBoolean("Vacuum", true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttackItemFrame(AttackEntityEvent event)
    {
        if (!(event.getTarget() instanceof EntityItemFrame frame)) return;

        EntityPlayer player = event.getEntityPlayer();

        if (!hasVacuum(player)) return;

        if (frame.isEntityInvulnerable(DamageSource.causePlayerDamage(player))) return;

        event.setCanceled(true);

        if (player.world.isRemote) return;

        ItemStack displayedItem = frame.getDisplayedItem();

        if (!displayedItem.isEmpty())
        {
            ItemStack collectedItem = displayedItem.copy();

            removeItemFrameData(frame, displayedItem);
            frame.setDisplayedItem(ItemStack.EMPTY);
            frame.setItemRotation(0);

            collectOrDrop(player, player.world, frame.getPosition(), collectedItem);
            return;
        }

        collectOrDrop(player, player.world, frame.getPosition(), new ItemStack(Items.ITEM_FRAME));
        frame.setDead();
    }

    private static void removeItemFrameData(EntityItemFrame frame, ItemStack stack)
    {
        if (stack.getItem() == Items.FILLED_MAP)
        {
            MapData mapData = ((ItemMap) stack.getItem()).getMapData(stack, frame.world);

            if (mapData != null)
            {
                mapData.mapDecorations.remove("frame-" + frame.getEntityId());
                mapData.markDirty();
            }
        }

        stack.setItemFrame(null);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttackArmorStand(AttackEntityEvent event)
    {
        if (!(event.getTarget() instanceof EntityArmorStand armorStand)) return;

        EntityPlayer player = event.getEntityPlayer();

        if (!hasVacuum(player)) return;

        if (armorStand.isEntityInvulnerable(DamageSource.causePlayerDamage(player))) return;

        event.setCanceled(true);

        if (player.world.isRemote) return;

        BlockPos pos = armorStand.getPosition();

        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
        {
            ItemStack stack = armorStand.getItemStackFromSlot(slot);

            if (stack.isEmpty()) continue;

            armorStand.setItemStackToSlot(slot, ItemStack.EMPTY);
            collectOrDrop(player, player.world, pos, stack.copy());
        }

        if (!player.capabilities.isCreativeMode) collectOrDrop(player, player.world, pos, new ItemStack(Items.ARMOR_STAND));

        armorStand.setDead();
    }

    private static void collectVerticalBlocksAbove(EntityPlayer player, World world, BlockPos brokenPos, Block block)
    {
        int topY = brokenPos.getY() + 1;

        while (topY < world.getHeight())
        {
            BlockPos checkPos = new BlockPos(brokenPos.getX(), topY, brokenPos.getZ());

            if (world.getBlockState(checkPos).getBlock() != block) break;

            topY++;
        }

        for (int y = topY - 1; y > brokenPos.getY(); y--)
        {
            BlockPos currentPos = new BlockPos(brokenPos.getX(), y, brokenPos.getZ());
            IBlockState state = world.getBlockState(currentPos);

            if (state.getBlock() != block) continue;

            NonNullList<ItemStack> drops = NonNullList.create();
            block.getDrops(drops, world, currentPos, state, 0);

            world.setBlockToAir(currentPos);

            for (ItemStack drop : drops) collectOrDrop(player, world, currentPos, drop);
        }
    }

    private static void rememberBreak(World world, BlockPos pos, EntityPlayer player)
    {
        long time = world.getTotalWorldTime();

        RECENT_BREAKS.removeIf(context -> context.expiresAt < time || context.player == null || context.player.isDead);

        RECENT_BREAKS.add(new RecentBreak(world, pos, player));
    }

    private static EntityPlayer findRecentBreaker(World world, BlockPos pos)
    {
        long time = world.getTotalWorldTime();
        int dimension = world.provider.getDimension();

        EntityPlayer closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;

        Iterator<RecentBreak> iterator = RECENT_BREAKS.iterator();

        while (iterator.hasNext())
        {
            RecentBreak context = iterator.next();

            if (context.expiresAt < time || context.player == null || context.player.isDead)
            {
                iterator.remove();
                continue;
            }

            if (context.dimension != dimension || context.player.world != world) continue;

            int x = context.pos.getX() - pos.getX();
            int y = context.pos.getY() - pos.getY();
            int z = context.pos.getZ() - pos.getZ();

            double distance = x * x + y * y + z * z;

            if (distance <= RECENT_BREAK_RANGE_SQ && distance < closestDistance)
            {
                closestDistance = distance;
                closestPlayer = context.player;
            }
        }

        return closestPlayer;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttackSpecialEntity(AttackEntityEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        Entity target = event.getTarget();

        if (player.world.isRemote || !hasVacuum(player)) return;

        if (!(target instanceof EntityHanging) && !(target instanceof EntityArmorStand)) return;

        rememberBreak(player.world, target.getPosition(), player);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemSpawn(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityItem entityItem)) return;

        if (event.getWorld().isRemote) return;

        if (entityItem.getItem().isEmpty()) return;

        if (entityItem.getThrower() != null) return;

        EntityPlayer player = findRecentBreaker(event.getWorld(), entityItem.getPosition());

        if (player == null || !hasVacuum(player)) return;

        ItemStack remaining = collectDirectly(player, entityItem.getItem());

        if (remaining.isEmpty()) event.setCanceled(true);
        else entityItem.setItem(remaining);
    }
}