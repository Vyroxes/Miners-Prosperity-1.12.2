package net.vyroxes.minersprosperity.block.machine;

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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.machine.SolarPanelTier;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import net.vyroxes.minersprosperity.tile.TileEntitySolarPanel;
import net.vyroxes.minersprosperity.annotation.NonnullByDefault;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Random;

public class BlockMachineSolarPanel extends BlockMachine
{
    private final SolarPanelTier tier;

    public BlockMachineSolarPanel(String name, SolarPanelTier tier)
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

        if (tier == SolarPanelTier.CREATIVE)
        {
            tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + TextFormatting.GRAY + ": " + I18n.format("gui.energy_infinite"));
        }
        else
        {
            if (energyGeneration >= 1000)
            {
                tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + TextFormatting.GRAY + ": " + formattedEnergyGeneration + " kFE/t");
            }
            else
            {
                tooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + TextFormatting.GRAY + ": " + formattedEnergyGeneration + " FE/t");
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @NonnullByDefault
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (stateIn.getValue(STATE) == MachineState.ACTIVE)
        {
            double x = pos.getX() + rand.nextDouble();
            double y = pos.getY() + 1.0D + rand.nextDouble() * 0.05D;
            double z = pos.getZ() + rand.nextDouble();

            double offsetX = rand.nextGaussian() * 0.02D;
            double offsetY = rand.nextGaussian() * 0.02D;
            double offsetZ = rand.nextGaussian() * 0.02D;

            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, offsetX, offsetY, offsetZ);

            float red = tier.getParticleRed();
            float green = tier.getParticleGreen();
            float blue = tier.getParticleBlue();

            if (red == 0.0F) red = 0.001F;

            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, red, green, blue);
        }
    }

    public SolarPanelTier getTier()
    {
        return this.tier;
    }
}