package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.objects.containers.ContainerSolarPanel;
import net.vyroxes.minersprosperity.objects.enums.ESolarPanelTier;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntitySolarPanel;
import net.vyroxes.minersprosperity.util.UtilHelper;
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
				int efficiency = (int) ((this.tileEntity.getEnergyGeneration() / (float) this.tileEntity.getMaxExtract()) * 100);
				List<String> currentTooltip = new ArrayList<>();
				if (this.tileEntity instanceof TileEntitySolarPanel && ((TileEntitySolarPanel) this.tileEntity).getTier() == ESolarPanelTier.CREATIVE)
				{
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + TextFormatting.GRAY + ": " + UtilHelper.energy(0, 0, true, false, false));
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + TextFormatting.GRAY + ": " + UtilHelper.energy(0, 0, true, false, false));
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.efficiency") + TextFormatting.GRAY + ": " + efficiency + "%");
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.max_energy_output") + TextFormatting.GRAY + ": " + UtilHelper.energy(0, 0, true, false, false));
				}
				else
				{
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + TextFormatting.GRAY + ": " + UtilHelper.energy(this.tileEntity.getEnergyStored(), this.tileEntity.getMaxEnergyStored(), false,true, false));
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + TextFormatting.GRAY + ": " + UtilHelper.energy(this.tileEntity.getEnergyGeneration(), 0, false,false, true));
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.efficiency") + TextFormatting.GRAY + ": " + efficiency + "%");
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.max_energy_output") + TextFormatting.GRAY + ": " + UtilHelper.energy(this.tileEntity.getMaxExtract(), 0, false,false, true));
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