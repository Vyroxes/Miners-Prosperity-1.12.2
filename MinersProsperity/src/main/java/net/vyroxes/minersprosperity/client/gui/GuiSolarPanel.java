package net.vyroxes.minersprosperity.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.inventory.container.ContainerSolarPanel;
import net.vyroxes.minersprosperity.machine.SolarPanelTier;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import net.vyroxes.minersprosperity.tile.TileEntitySolarPanel;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import net.vyroxes.minersprosperity.network.NetworkHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuiSolarPanel extends GuiContainer
{
	private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
	private static final ResourceLocation SOLAR_PANEL = new ResourceLocation(Tags.MODID, "textures/gui/solar_panel.png");
	private final TileEntityMachine tileEntity;
	private int redstoneControlButtonState;

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
		int guiLeft = (this.width - this.xSize) / 2;
		int guiTop = (this.height - this.ySize) / 2;

		this.buttonList.clear();

		this.redstoneControlButtonState = this.tileEntity.getRedstoneControlButtonState();

		if (this.redstoneControlButtonState == 0)
		{
			this.addButton(new GuiDefaultButton(0, guiLeft + 151, guiTop + 6, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 16, 0, 0,18, I18n.format("gui.redstone_ignored.name")));
		}
		else if (redstoneControlButtonState == 1)
		{
			this.addButton(new GuiDefaultButton(0, guiLeft + 151, guiTop + 6, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 34, 0, 0,18, I18n.format("gui.redstone_low_signal.name")));
		}
		else if (redstoneControlButtonState == 2)
		{
			this.addButton(new GuiDefaultButton(0, guiLeft + 151, guiTop + 6, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 52, 0, 0,18, I18n.format("gui.redstone_high_signal.name")));
		}
		this.addButton(new GuiDefaultButton(1, guiLeft + 151, guiTop + 27, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 70, 0, 0,18, I18n.format("gui.configuration.name")));
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == 0)
		{
			if (this.redstoneControlButtonState < 2) this.tileEntity.setRedstoneControlButtonState(this.tileEntity.getRedstoneControlButtonState() + 1);
			else this.tileEntity.setRedstoneControlButtonState(0);
		}
		else if (guiButton.id == 1) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.MACHINE_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		else if (guiButton.id == 2) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.MACHINE_UPGRADES.ordinal(), this.tileEntity.getPos());
		this.initGui();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseButton == 1)
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
			for (GuiButton guiButton : this.buttonList)
			{
				if (guiButton instanceof GuiDefaultButton && ((GuiDefaultButton) guiButton).isHovered(mouseX, mouseY))
				{
					if (guiButton.id == 0)
					{
						if (this.redstoneControlButtonState > 0) this.tileEntity.setRedstoneControlButtonState(this.tileEntity.getRedstoneControlButtonState() - 1);
						else this.tileEntity.setRedstoneControlButtonState(2);
					}
					Minecraft.getMinecraft().player.playSound(net.minecraft.init.SoundEvents.UI_BUTTON_CLICK, 0.25F, 1.0F);
					this.initGui();
					return;
				}
			}
		}
		else if (mouseButton == 2)
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
			for (GuiButton guiButton : this.buttonList)
			{
				if (guiButton instanceof GuiDefaultButton && ((GuiDefaultButton) guiButton).isHovered(mouseX, mouseY))
				{
					if (guiButton.id == 0) this.tileEntity.setRedstoneControlButtonState(0);
					Minecraft.getMinecraft().player.playSound(net.minecraft.init.SoundEvents.UI_BUTTON_CLICK, 0.25F, 1.0F);
					this.initGui();
					return;
				}
			}
		}
		else super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
	    this.drawDefaultBackground();
	    super.drawScreen(mouseX, mouseY, partialTicks);

		for (GuiButton button : this.buttonList)
		{
			if (button instanceof GuiDefaultButton)
			{
				if (button.id != 3)
				{
					List<String> guiButtonTooltip = ((GuiDefaultButton) button).getCurrentTooltip();
					if (guiButtonTooltip != null) GuiUtils.drawHoveringText(guiButtonTooltip, mouseX, mouseY, this.width, this.height, this.width/2, this.fontRenderer);
				}
			}
		}

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
				if (this.tileEntity instanceof TileEntitySolarPanel && ((TileEntitySolarPanel) this.tileEntity).getTier() == SolarPanelTier.CREATIVE)
				{
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + TextFormatting.GRAY + ": " + EnergyFormatter.capacity(0, 0, true));
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + TextFormatting.GRAY + ": " + EnergyFormatter.usage(0, true));
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.efficiency") + TextFormatting.GRAY + ": " + efficiency + "%");
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.max_energy_output") + TextFormatting.GRAY + ": " + EnergyFormatter.usage(0, true));
				}
				else
				{
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + TextFormatting.GRAY + ": " + EnergyFormatter.capacity(this.tileEntity.getEnergyStored(), this.tileEntity.getMaxEnergyStored(), false));
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_generation") + TextFormatting.GRAY + ": " + EnergyFormatter.usage(this.tileEntity.getEnergyGeneration(), false));
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.efficiency") + TextFormatting.GRAY + ": " + efficiency + "%");
					currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.max_energy_output") + TextFormatting.GRAY + ": " + EnergyFormatter.usage(this.tileEntity.getMaxExtract(), false));
				}

				GuiUtils.drawHoveringText(currentTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
			}
		}

	    this.renderHoveredToolTip(mouseX, mouseY);
	    GuiMachineTitle.drawTooltip(this.fontRenderer, Objects.requireNonNull(this.tileEntity.getDisplayName()).getUnformattedText(), this.xSize, this.guiLeft, this.guiTop, this.buttonList, mouseX, mouseY, this.width, this.height);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String title = Objects.requireNonNull(this.tileEntity.getDisplayName()).getUnformattedText();
		GuiMachineTitle.draw(this.fontRenderer, title, this.xSize, this.guiLeft, this.guiTop, this.buttonList);
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