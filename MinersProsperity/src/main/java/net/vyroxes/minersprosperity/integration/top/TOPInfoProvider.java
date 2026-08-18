package net.vyroxes.minersprosperity.integration.top;

import mcjty.theoneprobe.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import net.vyroxes.minersprosperity.config.ModConfig;

public class TOPInfoProvider implements IProbeInfoProvider
{

    @Override
    public String getID() {
        return "minersprosperity:alloy_furnace";
    }

    public float calculateLevelsFromXP(int xp)
    {
        int level = 0;

        while (xp > 0)
        {
            int xpToNextLevel;

            if (level < 16)
            {
                xpToNextLevel = 2 * level + 7;
            }
            else if (level < 31)
            {
                xpToNextLevel = 5 * level - 38;
            }
            else
            {
                xpToNextLevel = 9 * level - 158;
            }

            if (xp >= xpToNextLevel)
            {
                xp -= xpToNextLevel;
                level++;
            }
            else
            {
                return level + (float) xp / xpToNextLevel;
            }
        }

        return level;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, EntityPlayer entityPlayer, World world, IBlockState iBlockState, IProbeHitData iProbeHitData)
    {
        if (ModConfig.INTEGRATION.theOneProbeCompat)
        {
            Block block = iBlockState.getBlock();
            boolean vanillaBlock = block.getClass().getName().startsWith("net.minecraft.block.");

            if (vanillaBlock && !ModConfig.INTEGRATION.theOneProbeVanillaTileEntities)
                return;

            if (!vanillaBlock && !(world.getTileEntity(iProbeHitData.getPos()) instanceof TileEntityMachine))
                return;

            TileEntity te = world.getTileEntity(iProbeHitData.getPos());
            if (te == null)
                return;

            IFluidHandler fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

            if (fluidHandler instanceof IFluidTank tank)
            {
                FluidStack fluidStack = tank.getFluid();

                if (fluidStack != null && fluidStack.getFluid().equals(TileEntityMachine.getConfiguredXpFluid().getFluid()))
                {
                    String fluidName = fluidStack.getLocalizedName();
                    int amount = fluidStack.amount;
                    String calculatedLevels = String.format("%.1f", calculateLevelsFromXP(amount / 10));

                    iProbeInfo.text(fluidName);
                    iProbeInfo.progress(amount, 10,
                            iProbeInfo.defaultProgressStyle()
                                    .suffix("mB")
                                    .filledColor(0xFF80FF20)
                                    .alternateFilledColor(0xFF408010)
                                    .borderColor(0xFF2A5B00)
                                    .numberFormat(NumberFormat.COMPACT));
                    iProbeInfo.text(I18n.format("gui.xp_levels") + ": " + calculatedLevels);
                }
            }

            if (probeMode != ProbeMode.EXTENDED)
            {
                IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (handler == null)
                {
                    return;
                }

                if (entityPlayer.isSneaking())
                {
                    iProbeInfo.text("Inventory");
                    IProbeInfo vertical = iProbeInfo.vertical(iProbeInfo.defaultLayoutStyle());

                    for (int i = 0; i < handler.getSlots(); i++)
                    {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (!stack.isEmpty())
                        {
                            IProbeInfo row = vertical.horizontal(iProbeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
                            row.item(stack);
                            row.text(" " + stack.getDisplayName());
                        }
                    }
                }
                else
                {
                    int itemsPerRow = ModConfig.INTEGRATION.theOneProbeItemsPerRow;
                    int maxRows = ModConfig.INTEGRATION.theOneProbeMaxRows;

                    IProbeInfo vertical = iProbeInfo.vertical(iProbeInfo.defaultLayoutStyle());

                    IProbeInfo currentRow = null;
                    int countInRow = 0;
                    int rowCount = 0;

                    for (int i = 0; i < handler.getSlots(); i++)
                    {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (!stack.isEmpty())
                        {
                            if (countInRow == 0)
                            {
                                if (rowCount >= maxRows)
                                {
                                    break;
                                }

                                currentRow = vertical.horizontal(iProbeInfo.defaultLayoutStyle());
                                rowCount++;
                            }

                            currentRow.item(stack);
                            countInRow++;

                            if (countInRow >= itemsPerRow)
                            {
                                countInRow = 0;
                            }
                        }
                    }
                }
            }
        }
    }
}