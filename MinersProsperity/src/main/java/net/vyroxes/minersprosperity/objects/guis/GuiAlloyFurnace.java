package net.vyroxes.minersprosperity.objects.guis;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.Reference;
import net.vyroxes.minersprosperity.objects.containers.ContainerAlloyFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;

public class GuiAlloyFurnace extends GuiContainer
{
	private static final ResourceLocation CRUSHER_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/alloy_furnace.png");
	private final TileEntityAlloyFurnace tileEntity;
	private int redstoneControlButtonState;
	
	public GuiAlloyFurnace(InventoryPlayer player, TileEntityAlloyFurnace tileEntity)
	{
		super(new ContainerAlloyFurnace(player, tileEntity));
		this.tileEntity = tileEntity;
	}

	@Override
	public void initGui() 
	{
	    super.initGui();
	    int guiLeft = (this.width - this.xSize) / 2;
	    int guiTop = (this.height - this.ySize) / 2;
	    
	    this.buttonList.clear();

		this.redstoneControlButtonState = this.tileEntity.redstoneControlButtonState;

		if (this.redstoneControlButtonState == 0)
		{
			this.addButton(new GuiRedstoneControlButton(0, guiLeft + 7, guiTop + 6, 18, 18, new ResourceLocation(Reference.MODID, "textures/gui/alloy_furnace.png"), 176, 31, I18n.format("gui.redstone_ignored.name")));
		}
		else if (redstoneControlButtonState == 1)
		{
			this.addButton(new GuiRedstoneControlButton(0, guiLeft + 7, guiTop + 6, 18, 18, new ResourceLocation(Reference.MODID, "textures/gui/alloy_furnace.png"), 194, 31, I18n.format("gui.redstone_low_signal.name")));
		}
		else if (redstoneControlButtonState == 2)
		{
			this.addButton(new GuiRedstoneControlButton(0, guiLeft + 7, guiTop + 6, 18, 18, new ResourceLocation(Reference.MODID, "textures/gui/alloy_furnace.png"), 212, 31, I18n.format("gui.redstone_high_signal.name")));
		}
		this.addButton(new GuiSlotsConfigurationButton(1, guiLeft + 7, guiTop + 27, 18, 18, new ResourceLocation(Reference.MODID, "textures/gui/alloy_furnace.png"), 230, 31, I18n.format("gui.slots_configuration.name")));
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == 0)
		{
			if (this.redstoneControlButtonState < 2)
			{
				++this.tileEntity.redstoneControlButtonState;
			}
			else
			{
				this.tileEntity.redstoneControlButtonState = 0;
			}
			this.tileEntity.setRedstoneControlButtonState();
		}
		if (guiButton.id == 1)
		{
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOTS_CONFIGURATION.ordinal(), this.tileEntity.getPos());
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
				if (guiButton instanceof GuiRedstoneControlButton && ((GuiRedstoneControlButton) guiButton).isHovered(mouseX, mouseY))
				{
					if (guiButton.id == 0)
					{
						if (this.redstoneControlButtonState > 0)
						{
							--this.tileEntity.redstoneControlButtonState;
						}
						else
						{
							this.tileEntity.redstoneControlButtonState = 2;
						}
					}

					this.tileEntity.setRedstoneControlButtonState();
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
				if (guiButton instanceof GuiRedstoneControlButton && ((GuiRedstoneControlButton) guiButton).isHovered(mouseX, mouseY))
				{
					if (guiButton.id == 0)
					{
						this.tileEntity.redstoneControlButtonState = 0;
					}

					this.tileEntity.setRedstoneControlButtonState();
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
	        if (button instanceof GuiRedstoneControlButton) 
	        {
	            List<String> redstoneControlButtonTooltip = ((GuiRedstoneControlButton) button).getCurrentTooltip();
	            if (redstoneControlButtonTooltip != null)
	            {
	                GuiUtils.drawHoveringText(redstoneControlButtonTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
	            }
	        }
			if (button instanceof GuiSlotsConfigurationButton)
			{
				List<String> slotsConfigurationButtonTooltip = ((GuiSlotsConfigurationButton) button).getCurrentTooltip();
				if (slotsConfigurationButtonTooltip != null)
				{
					GuiUtils.drawHoveringText(slotsConfigurationButtonTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
				}
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
		this.mc.getTextureManager().bindTexture(CRUSHER_TEXTURE);
		int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		 
		if (TileEntityAlloyFurnace.isActive(tileEntity))
		{
			int k = this.getBurnLeftScaled();
			this.drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		
		int l = this.getCookProgressScaled();
		this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
	}

	private int getCookProgressScaled()
	{
		int i = this.tileEntity.cookTime;
		int j = this.tileEntity.totalCookTime;
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}
	
	private int getBurnLeftScaled()
	{
		int i = this.tileEntity.currentItemBurnTime;

        if (i == 0)
        {
            i = 200;
        }

        return this.tileEntity.burnTime * 13 / i;
	}
}