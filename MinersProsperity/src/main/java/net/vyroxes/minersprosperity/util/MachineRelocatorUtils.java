package net.vyroxes.minersprosperity.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.vyroxes.minersprosperity.config.ModConfig;
import java.util.Locale;

public final class MachineRelocatorUtils {
    private static final String TAG_RELOCATOR = "MachineRelocator";
    private static final String TAG_SNAPSHOT = "Snapshot";
    private static final String TAG_MACHINE_NAME = "MachineName";

    private MachineRelocatorUtils() {}

    public static boolean hasMachine(ItemStack stack)
    {
        return !stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_RELOCATOR, 10) && stack.getTagCompound().getCompoundTag(TAG_RELOCATOR).hasKey(TAG_SNAPSHOT, 10);
    }

    public static boolean canCapture(World world, BlockPos pos)
    {
        if (!world.isBlockLoaded(pos)) return false;

        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);

        if (tile == null || tile.isInvalid()) return false;
        if (!state.getBlock().hasTileEntity(state)) return false;
        if (state.getBlockHardness(world, pos) < 0.0F) return false;
        if (state.getPushReaction() == EnumPushReaction.BLOCK) return false;
        if (isUnsafeVanillaTile(tile)) return false;
        if (isDoubleChest(world, pos, state)) return false;
        return !isBlacklisted(state, tile);
    }

    public static boolean capture(ItemStack stack, World world, BlockPos pos, EntityPlayer player, EnumHand hand)
    {
        if (world.isRemote || hasMachine(stack) || !canCapture(world, pos)) return false;
        if (!world.isBlockModifiable(player, pos) || !player.canPlayerEdit(pos, EnumFacing.UP, stack)) return false;

        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);

        if (tile == null) return false;

        BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(world, pos, state, player);
        if (MinecraftForge.EVENT_BUS.post(breakEvent)) return false;

        BlockSnapshot snapshot;

        try
        {
            snapshot = BlockSnapshot.getBlockSnapshot(world, pos);
        }
        catch (Exception e)
        {
            return false;
        }

        NBTTagCompound snapshotTag = new NBTTagCompound();
        snapshot.writeToNBT(snapshotTag);

        NBTTagCompound relocatorTag = new NBTTagCompound();
        relocatorTag.setTag(TAG_SNAPSHOT, snapshotTag);
        relocatorTag.setString(TAG_MACHINE_NAME, getMachineName(state));

        world.removeTileEntity(pos);

        if (!world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3))
        {
            world.setTileEntity(pos, tile);
            return false;
        }

        NBTTagCompound root = stack.getTagCompound();
        if (root == null) root = new NBTTagCompound();

        root.setTag(TAG_RELOCATOR, relocatorTag);
        stack.setTagCompound(root);

        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 0.6F, 1.15F);

        return true;
    }

    @SuppressWarnings("deprecation")
    public static boolean release(ItemStack stack, World world, BlockPos pos, EntityPlayer player, EnumFacing facing, EnumHand hand)
    {
        if (world.isRemote || !hasMachine(stack) || !world.isBlockLoaded(pos)) return false;
        if (!world.isAirBlock(pos)) return false;
        if (!world.isBlockModifiable(player, pos) || !player.canPlayerEdit(pos, facing, stack)) return false;

        NBTTagCompound relocatorTag = getStoredTag(stack);
        if (relocatorTag == null) return false;

        NBTTagCompound snapshotTag = relocatorTag.getCompoundTag(TAG_SNAPSHOT);
        BlockSnapshot storedSnapshot;

        try
        {
            storedSnapshot = BlockSnapshot.readFromNBT(snapshotTag);
        }
        catch (Exception e)
        {
            return false;
        }

        if (!ModConfig.ITEMS.machineRelocatorCrossDimension && storedSnapshot.getDimId() != world.provider.getDimension()) return false;

        Block block = ForgeRegistries.BLOCKS.getValue(storedSnapshot.getRegistryName());
        if (block == null || block == Blocks.AIR) return false;

        IBlockState state;

        try
        {
            state = block.getStateFromMeta(storedSnapshot.getMeta());
        }
        catch (Exception e)
        {
            return false;
        }

        if (isBlockBlacklisted(state)) return false;

        state = applyPlacementFacing(state, player);

        BlockSnapshot replacedSnapshot = BlockSnapshot.getBlockSnapshot(world, pos);
        IBlockState placedAgainst = world.getBlockState(pos.offset(facing.getOpposite()));

        if (!world.setBlockState(pos, state, 3)) return false;

        BlockEvent.PlaceEvent placeEvent = new BlockEvent.PlaceEvent(replacedSnapshot, placedAgainst, player, hand);

        if (MinecraftForge.EVENT_BUS.post(placeEvent))
        {
            replacedSnapshot.restore(true, true);
            return false;
        }

        NBTTagCompound tileData = storedSnapshot.getNbt();

        if (tileData != null)
        {
            TileEntity placedTile = world.getTileEntity(pos);

            if (placedTile == null)
            {
                replacedSnapshot.restore(true, true);
                return false;
            }

            if (isBlacklisted(state, placedTile))
            {
                replacedSnapshot.restore(true, true);
                return false;
            }

            NBTTagCompound relocatedData = tileData.copy();
            relocatedData.setInteger("x", pos.getX());
            relocatedData.setInteger("y", pos.getY());
            relocatedData.setInteger("z", pos.getZ());

            try
            {
                placedTile.readFromNBT(relocatedData);
                placedTile.markDirty();
            }
            catch (Exception e)
            {
                replacedSnapshot.restore(true, true);
                return false;
            }
        }


        world.notifyBlockUpdate(pos, state, state, 3);
        clearMachine(stack);

        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 0.6F, 0.9F);

        return true;
    }

    public static boolean isBlacklisted(IBlockState state, TileEntity tile)
    {
        return isBlockBlacklisted(state) || isClassBlacklisted(tile.getClass());
    }

    private static boolean isBlockBlacklisted(IBlockState state)
    {
        Block block = state.getBlock();
        ResourceLocation id = block.getRegistryName();
        if (id == null) return true;

        String blockId = id.toString().toLowerCase(Locale.ROOT);
        String modId = id.getNamespace().toLowerCase(Locale.ROOT);
        int metadata = block.getMetaFromState(state);
        String[] blacklist = ModConfig.ITEMS.machineRelocatorBlacklist;

        if (blacklist == null) return false;

        for (String rawEntry : blacklist)
        {
            if (rawEntry == null || rawEntry.trim().isEmpty()) continue;

            String entry = rawEntry.trim().toLowerCase(Locale.ROOT);
            if (entry.startsWith("class:")) continue;

            if (entry.equals(modId + ":*")) return true;
            if (entry.equals(blockId) || entry.equals(blockId + "/*")) return true;

            String metadataPrefix = blockId + "/";
            if (!entry.startsWith(metadataPrefix)) continue;

            String metadataString = entry.substring(metadataPrefix.length());
            if (metadataString.equals("*")) return true;

            try
            {
                if (Integer.parseInt(metadataString) == metadata) return true;
            }
            catch (NumberFormatException ignored) {}
        }

        return false;
    }

    private static boolean isClassBlacklisted(Class<?> clazz)
    {
        String className = clazz.getName().toLowerCase(Locale.ROOT);
        String[] blacklist = ModConfig.ITEMS.machineRelocatorBlacklist;

        if (blacklist == null) return false;

        for (String rawEntry : blacklist)
        {
            if (rawEntry == null || rawEntry.trim().isEmpty()) continue;

            String entry = rawEntry.trim().toLowerCase(Locale.ROOT);
            if (!entry.startsWith("class:")) continue;

            String pattern = entry.substring("class:".length());

            if (pattern.endsWith("*"))
            {
                String prefix = pattern.substring(0, pattern.length() - 1);
                if (className.startsWith(prefix)) return true;
            }
            else if (className.equals(pattern)) return true;
        }

        return false;
    }

    private static boolean isUnsafeVanillaTile(TileEntity tile)
    {
        return tile instanceof TileEntityPiston || tile instanceof TileEntityEndPortal || tile instanceof TileEntityEndGateway || tile instanceof TileEntityStructure || tile instanceof TileEntityCommandBlock;
    }

    private static boolean isDoubleChest(World world, BlockPos pos, IBlockState state)
    {
        if (!(state.getBlock() instanceof BlockChest)) return false;

        for (EnumFacing facing : EnumFacing.HORIZONTALS) if (world.getBlockState(pos.offset(facing)).getBlock() == state.getBlock()) return true;

        return false;
    }

    public static String getStoredMachineName(ItemStack stack)
    {
        NBTTagCompound tag = getStoredTag(stack);
        return tag == null ? "" : tag.getString(TAG_MACHINE_NAME);
    }

    public static String getStoredBlockId(ItemStack stack)
    {
        NBTTagCompound tag = getStoredTag(stack);
        if (tag == null) return "";

        NBTTagCompound snapshot = tag.getCompoundTag(TAG_SNAPSHOT);
        String domain = snapshot.getString("blockMod");
        String path = snapshot.getString("blockName");

        return domain.isEmpty() || path.isEmpty() ? "" : domain + ":" + path;
    }

    public static void clearMachine(ItemStack stack)
    {
        if (!stack.hasTagCompound()) return;

        NBTTagCompound root = stack.getTagCompound();
        root.removeTag(TAG_RELOCATOR);

        if (root.getKeySet().isEmpty()) stack.setTagCompound(null);
    }

    private static NBTTagCompound getStoredTag(ItemStack stack)
    {
        if (!hasMachine(stack)) return null;

        return stack.getTagCompound().getCompoundTag(TAG_RELOCATOR);
    }

    private static String getMachineName(IBlockState state)
    {
        ItemStack blockStack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
        if (!blockStack.isEmpty() && blockStack.getItem() != Items.AIR) return blockStack.getDisplayName();

        return state.getBlock().getLocalizedName();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static IBlockState applyPlacementFacing(IBlockState state, EntityPlayer player)
    {
        EnumFacing facing = player.getHorizontalFacing().getOpposite();

        for (IProperty<?> property : state.getPropertyKeys())
        {
            if (!"facing".equals(property.getName())) continue;
            if (!property.getAllowedValues().contains(facing)) continue;

            return state.withProperty((IProperty) property, facing);
        }

        return state;
    }
}