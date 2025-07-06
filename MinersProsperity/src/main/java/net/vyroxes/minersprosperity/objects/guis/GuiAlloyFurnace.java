package net.vyroxes.minersprosperity.objects.guis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;

public class GuiAlloyFurnace extends GuiContainer
{
	private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
	private static final ResourceLocation ALLOY_FURNACE = new ResourceLocation(Tags.MODID, "textures/gui/alloy_furnace.png");
	private final TileEntityMachine tileEntity;
	private int redstoneControlButtonState;
	
	public GuiAlloyFurnace(InventoryPlayer player, TileEntityMachine tileEntity)
	{
		super(new ContainerAlloyFurnace(player, tileEntity));
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
			this.addButton(new GuiDefaultButton(0, guiLeft + 151, guiTop + 6, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 16, 0, 18, I18n.format("gui.redstone_ignored.name")));
		}
		else if (redstoneControlButtonState == 1)
		{
			this.addButton(new GuiDefaultButton(0, guiLeft + 151, guiTop + 6, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 34, 0, 18, I18n.format("gui.redstone_low_signal.name")));
		}
		else if (redstoneControlButtonState == 2)
		{
			this.addButton(new GuiDefaultButton(0, guiLeft + 151, guiTop + 6, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 52, 0, 18, I18n.format("gui.redstone_high_signal.name")));
		}
		this.addButton(new GuiDefaultButton(1, guiLeft + 151, guiTop + 27, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 70, 0, 18, I18n.format("gui.slots_configuration.name")));
		this.addButton(new GuiDefaultButton(2, guiLeft + 151, guiTop + 48, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 88, 0, 18, I18n.format("gui.upgrades.name")));
		GuiButton tankButton = this.addButton(new GuiDefaultButton(3, guiLeft + 138, guiTop + 30, 6, 26, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 238, 17, 0, ""));
        tankButton.enabled = this.tileEntity.getFluidStored() != 0 && this.tileEntity.getFluidTank() != null;
		this.addButton(tankButton);
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == 0)
		{
			if (this.redstoneControlButtonState < 2)
			{
				this.tileEntity.setRedstoneControlButtonState(this.tileEntity.getRedstoneControlButtonState() + 1);
			}
			else
			{
				this.tileEntity.setRedstoneControlButtonState(0);
			}
		}
		if (guiButton.id == 1)
		{
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOTS_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
		if (guiButton.id == 2)
		{
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_UPGRADES.ordinal(), this.tileEntity.getPos());
		}
		if (guiButton.id == 3)
		{
			if (this.tileEntity.getWorld().isRemote)
			{
				NetworkHandler.sendVariableUpdate(1, 0, this.tileEntity.getPos());
			}
		}
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
						if (this.redstoneControlButtonState > 0)
						{
							this.tileEntity.setRedstoneControlButtonState(this.tileEntity.getRedstoneControlButtonState() - 1);
						}
						else
						{
							this.tileEntity.setRedstoneControlButtonState(2);
						}
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
					if (guiButton.id == 0)
					{
						this.tileEntity.setRedstoneControlButtonState(0);
					}
					Minecraft.getMinecraft().player.playSound(net.minecraft.init.SoundEvents.UI_BUTTON_CLICK, 0.25F, 1.0F);
					this.initGui();
					return;
				}
			}
		}
		else
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
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
					if (guiButtonTooltip != null)
					{
						GuiUtils.drawHoveringText(guiButtonTooltip, mouseX, mouseY, this.width, this.height, this.width/2, this.fontRenderer);
					}
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
				float energyStored = (float) this.tileEntity.getEnergyStored() / 1000;
				String formattedEnergyStored = String.format("%.1f", energyStored);
				int energyUsage = (int) this.tileEntity.getEnergyUsage();
				float maxEnergyStored = (float) this.tileEntity.getMaxEnergyStored() / 1000;
				String formattedMaxEnergyStored = String.format("%.1f", maxEnergyStored);
				int maxReceive = (int) this.tileEntity.getMaxReceive();
				List<String> currentTooltip = new ArrayList<>();
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_stored") + TextFormatting.GRAY + ": " + formattedEnergyStored + "/" + formattedMaxEnergyStored + " kFE");
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.energy_usage") + TextFormatting.GRAY + ": " + energyUsage + " FE/t");
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.max_energy_input") + TextFormatting.GRAY + ": " + maxReceive + " FE/t");
				GuiUtils.drawHoveringText(currentTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
			}
		}

		int tankBarX = this.guiLeft + 138;
		int tankBarY = this.guiTop + 30;
		int tankBarWidth = 6;
		int tankBarHeight = 26;

		if (mouseX >= tankBarX && mouseX < tankBarX + tankBarWidth && mouseY >= tankBarY && mouseY < tankBarY + tankBarHeight)
		{
			if (this.tileEntity.getFluidTank() != null && this.tileEntity.getFluidTank().getFluid() != null && this.tileEntity.getFluidStored() > 0)
			{
				for (GuiButton guiButton : this.buttonList)
				{
					if (guiButton.id == 3 && this.tileEntity.getFluidStored() >= 10) guiButton.enabled = true;
					else if (guiButton.id == 3) guiButton.enabled = false;
				}

				int fluidStored = this.tileEntity.getFluidStored();
				String calculatedLevels = String.format("%.1f", calculateLevelsFromXP(fluidStored / 10));
				List<String> currentTooltip = new ArrayList<>();
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid") + TextFormatting.GRAY + ": " + this.tileEntity.getFluidTank().getFluid().getLocalizedName());
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_stored") + TextFormatting.GRAY + ": " + fluidStored + " mB");
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.xp_levels") + TextFormatting.GRAY + ": " + calculatedLevels);
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.left_click") + TextFormatting.GRAY + I18n.format("gui.left_click_desc"));
				GuiUtils.drawHoveringText(currentTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
			}
			else
			{
				for (GuiButton guiButton : this.buttonList)
				{
					if (guiButton.id == 3) guiButton.enabled = false;
				}

				List<String> currentTooltip = new ArrayList<>();
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid") + TextFormatting.GRAY + ": " + FluidInit.LIQUID_EXPERIENCE.getLocalizedName(new FluidStack(FluidInit.LIQUID_EXPERIENCE, 0)));
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.fluid_stored") + TextFormatting.GRAY + ": 0 mB");
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.xp_levels") + TextFormatting.GRAY + ": 0.0");
				currentTooltip.add(TextFormatting.AQUA + I18n.format("gui.left_click") + TextFormatting.GRAY + I18n.format("gui.left_click_desc"));
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
		this.mc.getTextureManager().bindTexture(ALLOY_FURNACE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		this.mc.getTextureManager().bindTexture(GUI_ELEMENTS);
		int l = this.getCookProgressScaled();
		this.drawTexturedModalRect(this.guiLeft + 78, this.guiTop + 35, 232, 0, l + 1, 16);

		int k = this.getEnergyStoredScaled();
		this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 7, 0, 0, 16, 41 - k);

		if (this.tileEntity.getFluidStored() == 0 || this.tileEntity.getFluidTank() == null)
		{
			this.drawTexturedModalRect(this.guiLeft + 139, this.guiTop + 31, 0, 0, 4, 24);
		}
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

	private int getCookProgressScaled()
	{
		int i = this.tileEntity.getCookTime();
		int j = this.tileEntity.getTotalCookTime();
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	private int getEnergyStoredScaled()
	{
		int i = (int) Math.round(this.tileEntity.getEnergyStored() / 100.0) * 100;
		int j = this.tileEntity.getMaxEnergyStored();
		return i != 0 && j != 0 ? i * 41 / j : 0;
    }
}