package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.init.FluidInit;
import net.vyroxes.minersprosperity.objects.containers.ContainerAlloyFurnace;
import net.vyroxes.minersprosperity.objects.containers.ContainerSolarPanel;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuiSolarPanel extends GuiContainer
{
	private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
	private static final ResourceLocation SOLAR_PANEL = new ResourceLocation(Tags.MODID, "textures/gui/solar_panel.png");
	private final TileEntityMachine tileEntity;

	public GuiSolarPanel(InventoryPlayer player, TileEntityMachine tileEntity)
	{
		super(new ContainerSolarPanel(player, tileEntity));
		this.tileEntity = tileEntity;
	}

	public TileEntityMachine getTileEntity()
	{
		return this.tileEntity;
	}

	@Override
	public void initGui() 
	{
	    super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
	    this.drawDefaultBackground();
	    super.drawScreen(mouseX, mouseY, partialTicks);

		if (this.tileEntity.getEnergyStorage() != null)
		{
			int energyBarX = this.guiLeft + 8;
			int energyBarY = this.guiTop + 7;
			int energyBarWidth = 16;
			int energyBarHeight = 41;

			if (mouseX >= energyBarX && mouseX < energyBarX + energyBarWidth && mouseY >= energyBarY && mouseY < energyBarY + energyBarHeight)
			{
				float energyStored = (float) this.tileEntity.getEnergyStored() / 1000;
				String formattedEnergyStored = String.format("%.1f", energyStored);
				int energyGeneration = this.tileEntity.getEnergyGeneration();
				float maxEnergyStored = (float) this.tileEntity.getMaxEnergyStored() / 1000;
				String formattedMaxEnergyStored = String.format("%.1f", maxEnergyStored);
				int maxExtract = this.tileEntity.getMaxExtract();
				List<String> currentTooltip = new ArrayList<>();
				if (this.tileEntity.getEnergyStored() == Integer.MAX_VALUE)
				{
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + TextFormatting.GRAY + ": " + I18n.format("gui.energy_infinite"));
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + TextFormatting.GRAY + ": " + I18n.format("gui.energy_infinite"));
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.max_energy_output") + TextFormatting.GRAY + ": " + I18n.format("gui.energy_infinite"));
				}
				else
				{
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + TextFormatting.GRAY + ": " + formattedEnergyStored + "/" + formattedMaxEnergyStored + " kFE");
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + TextFormatting.GRAY + ": " + energyGeneration + " FE/t");
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.max_energy_output") + TextFormatting.GRAY + ": " + maxExtract + " FE/t");
				}

				GuiUtils.drawHoveringText(currentTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
			}
		}

	    this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String title = Objects.requireNonNull(this.tileEntity.getDisplayName()).getUnformattedText();
		this.fontRenderer.drawString(title, this.xSize / 2 - this.fontRenderer.getStringWidth(title) / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("key.categories.inventory"), 8, 73, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(SOLAR_PANEL);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		this.mc.getTextureManager().bindTexture(GUI_ELEMENTS);
		int k = this.getEnergyStoredScaled();
		if (this.tileEntity.getEnergyStored() == Integer.MAX_VALUE) k = 41;
		this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 7, 0, 0, 16, 41 - k);
	}

	private int getEnergyStoredScaled()
	{
		int i = (int) Math.round(this.tileEntity.getEnergyStored() / 100.0) * 100;
		int j = this.tileEntity.getMaxEnergyStored();
		return i != 0 && j != 0 ? i * 41 / j : 0;
    }
}