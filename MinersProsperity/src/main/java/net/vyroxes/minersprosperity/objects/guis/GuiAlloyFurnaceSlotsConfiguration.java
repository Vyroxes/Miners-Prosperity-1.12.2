package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.Reference;
import net.vyroxes.minersprosperity.objects.containers.ContainerInventory;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class GuiAlloyFurnaceSlotsConfiguration extends GuiContainer
{
	private static final ResourceLocation ALLOY_FURNACE_SLOTS_CONFIGURATION_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/alloy_furnace_slots_configuration.png");
	private final TileEntityAlloyFurnace tileEntity;

	public GuiAlloyFurnaceSlotsConfiguration(InventoryPlayer player, TileEntityAlloyFurnace tileEntity)
	{
		super(new ContainerInventory(player, tileEntity));
		this.tileEntity = tileEntity;
	}
	
	@Override
	public void initGui() 
	{
	    super.initGui();

		int guiLeft = (this.width - this.xSize) / 2;
		int guiTop = (this.height - this.ySize) / 2;

		this.buttonList.clear();

		this.addButton(new GuiSlotButton(0, guiLeft + 37, guiTop + 34, 18, 18, new ResourceLocation(Reference.MODID, ALLOY_FURNACE_SLOTS_CONFIGURATION_TEXTURE.getPath()), 176, 0, I18n.format("gui.input1.name")));
		this.addButton(new GuiSlotButton(1, guiLeft + 55, guiTop + 34, 18, 18, new ResourceLocation(Reference.MODID, ALLOY_FURNACE_SLOTS_CONFIGURATION_TEXTURE.getPath()), 176, 0, I18n.format("gui.input2.name")));
		this.addButton(new GuiSlotButton(2, guiLeft + 7, guiTop + 52, 18, 18, new ResourceLocation(Reference.MODID, ALLOY_FURNACE_SLOTS_CONFIGURATION_TEXTURE.getPath()), 194, 0, I18n.format("gui.energy.name")));
		this.addButton(new GuiSlotButton(3, guiLeft + 113, guiTop + 30, 26, 26, new ResourceLocation(Reference.MODID, ALLOY_FURNACE_SLOTS_CONFIGURATION_TEXTURE.getPath()), 212, 0, I18n.format("gui.output.name")));
		this.addButton(new GuiBackButton(4, guiLeft + 7, guiTop + 6, 18, 9, new ResourceLocation(Reference.MODID, ALLOY_FURNACE_SLOTS_CONFIGURATION_TEXTURE.getPath()), 238, 0, I18n.format("gui.back.name")));
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == 0)
		{
			this.tileEntity.setSlot("Input 1");
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOT_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
		else if (guiButton.id == 1)
		{
			this.tileEntity.setSlot("Input 2");
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOT_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
		else if (guiButton.id == 2)
		{
			this.tileEntity.setSlot("Energy");
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOT_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
		else if (guiButton.id == 3)
		{
			this.tileEntity.setSlot("Output");
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOT_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
		else if (guiButton.id == 4)
		{
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), this.tileEntity.getPos());
		}
		this.initGui();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
	    this.drawDefaultBackground();
	    super.drawScreen(mouseX, mouseY, partialTicks);
	    this.renderHoveredToolTip(mouseX, mouseY);

		for (GuiButton button : this.buttonList)
		{
			if (button instanceof GuiSlotButton)
			{
				List<String> guiSlotButtonTooltip = ((GuiSlotButton) button).getCurrentTooltip();
				if (guiSlotButtonTooltip != null)
				{
					GuiUtils.drawHoveringText(guiSlotButtonTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
				}
			}
			if (button instanceof GuiBackButton)
			{
				List<String> guiButtonTooltip = ((GuiBackButton) button).getCurrentTooltip();
				if (guiButtonTooltip != null)
				{
					GuiUtils.drawHoveringText(guiButtonTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
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
		this.mc.getTextureManager().bindTexture(ALLOY_FURNACE_SLOTS_CONFIGURATION_TEXTURE);
		int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode)
	{
		if (keyCode == 1) {
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), this.tileEntity.getPos());
		}
	}
}