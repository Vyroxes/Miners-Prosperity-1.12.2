package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vyroxes.minersprosperity.init.EnchantmentInit;
import net.vyroxes.minersprosperity.objects.items.ItemBackpackBase;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber
public class VacuumHandler
{
    @SubscribeEvent
    public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event)
    {
        if (!(event.getHarvester() instanceof EntityPlayer))
            return;

        EntityPlayer player = event.getHarvester();
        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        if (boots.isEmpty())
            return;

        NBTTagCompound tag = boots.getTagCompound();
        if (tag == null || !tag.getBoolean("Vacuum"))
            return;

        if (EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.VACUUM, boots) > 0)
        {
            List<ItemStack> drops = event.getDrops();
            World world = event.getWorld();
            BlockPos pos = event.getPos();

            List<ItemStack> backpacks = ItemBackpackBase.getAllBackpacksFromPlayer(player);
            boolean hasBackpack = !backpacks.isEmpty();

            for (ItemStack drop : drops)
            {
                if (drop.isEmpty())
                    continue;

                ItemStack copy = drop.copy();
                boolean stored = false;

                if (hasBackpack)
                {
                    for (ItemStack backpack : backpacks)
                    {
                        NBTTagCompound compound = backpack.getTagCompound();

                        if (compound == null)
                            break;

                        NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
                        boolean autoCollect = backpackData.getBoolean("AutoCollect");

                        if (autoCollect && AutoCollectHandler.addToBackpack(player, backpack, copy))
                        {
                            stored = true;
                            break;
                        }
                    }
                }

                if (!stored)
                {
                    boolean added = player.inventory.addItemStackToInventory(copy);
                    if (!added && !world.isRemote)
                    {
                        Block.spawnAsEntity(world, pos, copy);
                    }
                }
            }

            drops.clear();
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player == null)
            return;

        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (boots.isEmpty())
            return;

        NBTTagCompound tag = boots.getTagCompound();
        if (tag == null || !tag.getBoolean("Vacuum"))
            return;

        World world = event.getWorld();
        BlockPos pos = event.getPos();

        TileEntity tile = world.getTileEntity(pos);
        if (tile == null)
            return;

        List<ItemStack> toCollect = new java.util.ArrayList<>();
        boolean usedIInventory = false;

        if (tile instanceof IInventory inv) {
            usedIInventory = true;

            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    toCollect.add(stack.copy());
                    inv.setInventorySlotContents(i, ItemStack.EMPTY);
                }
            }
        }

        Set<TileEntity> alreadyChecked = new HashSet<>();

        if (!usedIInventory)
        {
            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
            {
                IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (handler != null && handler.getSlots() > 0) {
                    alreadyChecked.add(tile);
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            toCollect.add(stack.copy());
                            if (handler instanceof IItemHandlerModifiable modHandler) {
                                modHandler.setStackInSlot(i, ItemStack.EMPTY);
                            }
                        }
                    }
                }
            }

            for (EnumFacing facing : EnumFacing.values())
            {
                if (!tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing))
                    continue;

                IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                if (handler == null || handler.getSlots() == 0)
                    continue;

                if (!alreadyChecked.add(tile))
                    break;

                for (int i = 0; i < handler.getSlots(); i++)
                {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty())
                    {
                        toCollect.add(stack.copy());
                        if (handler instanceof IItemHandlerModifiable modHandler)
                        {
                            modHandler.setStackInSlot(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }

        if (toCollect.isEmpty())
            return;

        List<ItemStack> backpacks = ItemBackpackBase.getAllBackpacksFromPlayer(player);
        boolean hasBackpack = !backpacks.isEmpty();

        for (ItemStack stack : toCollect)
        {
            boolean stored = false;

            if (hasBackpack)
            {
                for (ItemStack backpack : backpacks)
                {
                    NBTTagCompound compound = backpack.getTagCompound();
                    if (compound == null)
                        continue;

                    NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
                    boolean autoCollect = backpackData.getBoolean("AutoCollect");

                    if (autoCollect && AutoCollectHandler.addToBackpack(player, backpack, stack.copy()))
                    {
                        stored = true;
                        break;
                    }
                }
            }

            if (!stored)
            {
                boolean added = player.inventory.addItemStackToInventory(stack.copy());
                if (!added)
                {
                    Block.spawnAsEntity(world, pos, stack.copy());
                }
            }
        }

        if (EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.VACUUM, boots) > 0)
        {
            ItemStack held = player.getHeldItemMainhand();
            if (EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.QUICK_MINING, held) > 0)
                return;

            int xp = event.getExpToDrop();
            if (xp > 0 && !player.world.isRemote)
            {
                player.addExperience(xp);
                event.setExpToDrop(0);
            }
        }
    }

    @SubscribeEvent
    public static void onItemSpawn(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityItem entityItem))
            return;

        if (event.getWorld().isRemote)
            return;

        ItemStack drop = entityItem.getItem();
        if (drop.isEmpty())
            return;

        if (entityItem.getThrower() != null)
            return;

        EntityPlayer nearestPlayer = event.getWorld().getClosestPlayer(entityItem.posX, entityItem.posY, entityItem.posZ, ModConfig.ENCHANTMENTS.farReachDistance, false);
        if (nearestPlayer == null)
            return;

        ItemStack boots = nearestPlayer.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (boots.isEmpty() || EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.VACUUM, boots) <= 0)
            return;

        NBTTagCompound tag = boots.getTagCompound();
        if (tag == null || !tag.getBoolean("Vacuum"))
            return;

        ItemStack copy = drop.copy();
        boolean stored = false;

        List<ItemStack> backpacks = ItemBackpackBase.getAllBackpacksFromPlayer(nearestPlayer);
        boolean hasBackpack = !backpacks.isEmpty();

        if (hasBackpack)
        {
            for (ItemStack backpack : backpacks)
            {
                NBTTagCompound compound = backpack.getTagCompound();
                if (compound == null)
                    continue;

                NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
                boolean autoCollect = backpackData.getBoolean("AutoCollect");

                if (autoCollect && AutoCollectHandler.addToBackpack(nearestPlayer, backpack, copy))
                {
                    stored = true;
                    break;
                }
            }
        }

        if (!stored)
        {
            boolean added = nearestPlayer.inventory.addItemStackToInventory(copy);
            if (!added)
            {
                return;
            }
        }

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onMobDrops(LivingDropsEvent event)
    {
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer player))
            return;

        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (boots.isEmpty() || EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.VACUUM, boots) <= 0)
            return;

        NBTTagCompound tag = boots.getTagCompound();
        if (tag == null || !tag.getBoolean("Vacuum"))
            return;

        List<ItemStack> backpacks = ItemBackpackBase.getAllBackpacksFromPlayer(player);
        boolean hasBackpack = !backpacks.isEmpty();

        for (EntityItem entityItem : event.getDrops())
        {
            ItemStack drop = entityItem.getItem();
            if (drop.isEmpty())
                continue;

            ItemStack copy = drop.copy();
            boolean stored = false;

            if (hasBackpack)
            {
                for (ItemStack backpack : backpacks)
                {
                    NBTTagCompound compound = backpack.getTagCompound();
                    if (compound == null)
                        break;

                    NBTTagCompound backpackData = compound.getCompoundTag("BackpackData");
                    boolean autoCollect = backpackData.getBoolean("AutoCollect");

                    if (autoCollect && AutoCollectHandler.addToBackpack(player, backpack, copy))
                    {
                        stored = true;
                        break;
                    }
                }
            }

            if (!stored)
            {
                boolean added = player.inventory.addItemStackToInventory(copy);
                if (!added && !player.world.isRemote)
                {
                    Block.spawnAsEntity(player.world, entityItem.getPosition(), copy);
                }
            }

            entityItem.setDead();
        }

        event.getDrops().clear();
    }

    @SubscribeEvent
    public static void onMobKilled(LivingExperienceDropEvent event)
    {
        EntityPlayer player = event.getAttackingPlayer();
        if (player == null)
            return;

        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (boots.isEmpty())
            return;

        NBTTagCompound tag = boots.getTagCompound();
        if (tag == null || !tag.getBoolean("Vacuum"))
            return;

        if (EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.VACUUM, boots) > 0)
        {
            int xp = event.getDroppedExperience();
            ItemStack held = player.getHeldItemMainhand();
            int insightLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.INSIGHT, held);
            if (insightLevel > 0)
            {
                float multiplier = 1.0F + (0.2F + 0.1F * insightLevel);
                int bonusXP = Math.round(event.getOriginalExperience() * (multiplier - 1));
                xp += bonusXP;
            }

            if (xp > 0)
            {
                player.addExperience(xp);
                event.setDroppedExperience(0);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
            return;

        EntityPlayer player = event.player;

        for (ItemStack stack : player.inventory.mainInventory)
        {
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.FEET && EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.VACUUM, stack) > 0)
            {
                NBTTagCompound tag = stack.getTagCompound();
                if (tag == null)
                {
                    tag = new NBTTagCompound();
                    stack.setTagCompound(tag);
                }

                if (!tag.hasKey("Vacuum"))
                {
                    tag.setBoolean("Vacuum", true);
                }
            }
        }

        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (boots.isEmpty() || EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.VACUUM, boots) <= 0)
            return;

        NBTTagCompound compound = boots.getTagCompound();
        if (compound == null)
        {
            compound = new NBTTagCompound();
            boots.setTagCompound(compound);
        }

        if (!compound.hasKey("Vacuum"))
        {
            compound.setBoolean("Vacuum", true);
            boots.setTagCompound(compound);
        }
    }
}