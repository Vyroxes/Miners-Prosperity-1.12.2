package net.vyroxes.minersprosperity.objects.blocks.machines;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.objects.enums.EBatteryTier;
import net.vyroxes.minersprosperity.objects.enums.ESolarPanelTier;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntitySolarPanel;
import net.vyroxes.minersprosperity.util.annotations.NonnullByDefault;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class MachineSolarPanel extends Machine
{
    private final ESolarPanelTier tier;

    public MachineSolarPanel(String name, ESolarPanelTier tier)
    {
        super(name, Material.IRON, MapColor.STONE, SoundType.STONE, MinersProsperity.blocks_tab, "pickaxe", 0, 5.0F, 10.0F, GuiHandler.GuiTypes.SOLAR_PANEL);

        this.tier = tier;
    }

    @Override
    public TileEntityMachine getTileEntity(World world, BlockPos pos)
    {
        return (TileEntitySolarPanel) world.getTileEntity(pos);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World worldIn, int meta)
    {
        return new TileEntitySolarPanel(tier);
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        float energyGeneration = tier.generationClear;
        String formattedEnergyGeneration;
        if (energyGeneration >= 1000)
        {
            formattedEnergyGeneration = String.format("%.1f", energyGeneration / 1000);
        }
        else
        {
            formattedEnergyGeneration = String.format("%.1f", energyGeneration);
        }

        if (tier == ESolarPanelTier.CREATIVE)
        {
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + ": " + TextFormatting.GRAY + I18n.format("gui.energy_infinite"));
        }
        else
        {
            if (energyGeneration >= 1000)
            {
                tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + ": " + TextFormatting.GRAY + formattedEnergyGeneration + " kFE");
            }
            else
            {
                tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + ": " + TextFormatting.GRAY + formattedEnergyGeneration + " FE");
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @NonnullByDefault
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(POWERED)) {
            double x = pos.getX() + rand.nextDouble();
            double y = pos.getY() + 1.0D + rand.nextDouble() * 0.05D;
            double z = pos.getZ() + rand.nextDouble();

            double offsetX = rand.nextGaussian() * 0.02D;
            double offsetY = rand.nextGaussian() * 0.02D;
            double offsetZ = rand.nextGaussian() * 0.02D;

            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, offsetX, offsetY, offsetZ);
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    public ESolarPanelTier getTier()
    {
        return this.tier;
    }
}