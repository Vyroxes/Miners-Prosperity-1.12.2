package net.vyroxes.minersprosperity.util.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.vyroxes.minersprosperity.objects.containers.*;
import net.vyroxes.minersprosperity.objects.guis.*;
import net.minecraft.util.math.BlockPos;
import net.vyroxes.minersprosperity.objects.items.ItemBackpackBase;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.util.compat.baubles.BaublesCompat;

import java.util.Objects;

public class GuiHandler implements IGuiHandler
{
    public enum GuiTypes
    {
        MACHINE_SLOTS_CONFIGURATION,
        MACHINE_SLOT_CONFIGURATION,
        MACHINE_UPGRADES,
        ALLOY_FURNACE,
        METAL_PRESS,
        ELECTRIC_FURNACE,
        SOLAR_PANEL,
        BACKPACK,
        BACKPACK_CONFIGURATION;

        public static GuiTypes fromId(int id)
        {
            if (id < 0 || id >= values().length)
            {
                throw new IllegalArgumentException("Invalid GUI ID: " + id);
            }
            return values()[id];
        }
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        return switch (GuiTypes.fromId(id))
        {
            case MACHINE_SLOTS_CONFIGURATION, MACHINE_SLOT_CONFIGURATION -> new ContainerInventory(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case MACHINE_UPGRADES -> new ContainerUpgrades(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case ALLOY_FURNACE -> new ContainerAlloyFurnace(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case METAL_PRESS -> new ContainerMetalPress(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case ELECTRIC_FURNACE -> new ContainerElectricFurnace(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case SOLAR_PANEL -> new ContainerSolarPanel(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case BACKPACK -> new ContainerBackpack(player.inventory, y == 1 ? getBackpackFromBaubles(player) : getBackpackFromHand(player), y == 1);
            case BACKPACK_CONFIGURATION -> new ContainerBackpackConfiguration(player.inventory, y == 1 ? getBackpackFromBaubles(player) : getBackpackFromHand(player), y == 1);
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        return switch (GuiTypes.fromId(id)) {
            case MACHINE_SLOTS_CONFIGURATION ->
                    new GuiMachineSlotsConfiguration(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case MACHINE_SLOT_CONFIGURATION ->
                    new GuiMachineSlotConfiguration(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case MACHINE_UPGRADES ->
                    new GuiMachineUpgrades(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case ALLOY_FURNACE ->
                    new GuiAlloyFurnace(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case METAL_PRESS ->
                    new GuiMetalPress(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case ELECTRIC_FURNACE ->
                    new GuiElectricFurnace(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case SOLAR_PANEL ->
                    new GuiSolarPanel(player.inventory, (TileEntityMachine) Objects.requireNonNull(world.getTileEntity(pos)));
            case BACKPACK -> {
                ItemStack stack = y == 1 ? getBackpackFromBaubles(player) : getBackpackFromHand(player);
                ItemBackpackBase item = (ItemBackpackBase) stack.getItem();
                ResourceLocation texture = item.getTexture();
                ResourceLocation texture2 = item.getTexture2();
                yield new GuiBackpack(new ContainerBackpack(player.inventory, stack, y == 1), stack, y == 1, texture, texture2);
            }
            case BACKPACK_CONFIGURATION -> {
                ItemStack stack = y == 1 ? getBackpackFromBaubles(player) : getBackpackFromHand(player);
                ItemBackpackBase item = (ItemBackpackBase) stack.getItem();
                ResourceLocation texture = item.getConfigurationTexture();
                ResourceLocation texture2 = item.getConfigurationTexture2();
                yield new GuiBackpackConfiguration(new ContainerBackpackConfiguration(player.inventory, stack, y == 1), stack, y == 1, texture, texture2);
            }
        };
    }

    private static ItemStack getBackpackFromHand(EntityPlayer player)
    {
        ItemStack main = player.getHeldItemMainhand();
        if (!main.isEmpty() && main.getItem() instanceof ItemBackpackBase)
        {
            return main;
        }

        return ItemStack.EMPTY;
    }

    private static ItemStack getBackpackFromBaubles(EntityPlayer player)
    {
        if (Loader.isModLoaded("baubles"))
        {
            return BaublesCompat.getBackpackStackFromBaubles(player);
        }

        return ItemStack.EMPTY;
    }
}