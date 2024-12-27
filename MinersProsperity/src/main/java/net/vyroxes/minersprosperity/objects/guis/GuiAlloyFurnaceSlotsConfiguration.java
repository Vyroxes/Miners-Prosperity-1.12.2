package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.objects.containers.ContainerInventory;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import net.vyroxes.minersprosperity.util.handlers.SidedItemStackHandler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class GuiAlloyFurnaceSlotsConfiguration extends GuiContainer
{
	private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
	private static final ResourceLocation ALLOY_FURNACE_SLOTS_CONFIGURATION = new ResourceLocation(Tags.MODID, "textures/gui/alloy_furnace_slots_configuration.png");
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

		this.addButton(new GuiDefaultButton(0, guiLeft + 36, guiTop + 34, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 106, 32, 18, I18n.format("gui.input1.name")));
		this.addButton(new GuiDefaultButton(1, guiLeft + 56, guiTop + 34, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 106, 32, 18, I18n.format("gui.input2.name")));
		this.addButton(new GuiDefaultButton(2, guiLeft + 7, guiTop + 52, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 124, 32, 18, I18n.format("gui.energy.name")));
		this.addButton(new GuiDefaultButton(3, guiLeft + 114, guiTop + 30, 26, 26, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 142, 32, 26, I18n.format("gui.output.name")));
		this.addButton(new GuiDefaultButton(4, guiLeft + 7, guiTop + 6, 18, 9, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 88, 36, 9, I18n.format("gui.back.name")));
		this.addButton(new GuiDefaultButton(5, guiLeft + 7, guiTop + 18, 15, 15, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 154, 0, 15, I18n.format("gui.set_all_disabled.name")));
		this.addButton(new GuiDefaultButton(6, guiLeft + 7, guiTop + 34, 15, 15, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 169, 0, 15, I18n.format("gui.set_all_default.name")));

		String description = TextFormatting.AQUA + "Shift + left click" + TextFormatting.GRAY + " on the machine disables input and output for all slots on every side\n" + TextFormatting.AQUA + "Shift + right click" + TextFormatting.GRAY + " on the machine from a specific side disables input and output for all slots on that specific side.";
		GuiButton descButton = new GuiDefaultButton(7, guiLeft + 163, guiTop + 6, 6, 8, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 0, 41, 8, description);
		descButton.enabled = false;
		this.addButton(descButton);
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if (guiButton.id == 0)
		{
			this.tileEntity.setSlotId(0);
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOT_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
		else if (guiButton.id == 1)
		{
			this.tileEntity.setSlotId(1);
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOT_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
		else if (guiButton.id == 2)
		{
			this.tileEntity.setSlotId(2);
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOT_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
		else if (guiButton.id == 3)
		{
			this.tileEntity.setSlotId(3);
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOT_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
		else if (guiButton.id == 4)
		{
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), this.tileEntity.getPos());
		}
		else if (guiButton.id == 5)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntity.getSidedItemHandler(face);
				for (int slot = 0; slot < sidedItemStackHandler.getSlots(); slot++)
				{
					sidedItemStackHandler.setSlotMode(slot, SidedItemStackHandler.SlotState.SlotMode.DISABLED);
				}
			}
		}
		else if (guiButton.id == 6)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntity.getSidedItemHandler(face);
				for (int slot = 0; slot < sidedItemStackHandler.getSlots(); slot++)
				{
					if (sidedItemStackHandler.isSlotOutput(slot))
					{
						sidedItemStackHandler.setSlotMode(slot, SidedItemStackHandler.SlotState.SlotMode.OUTPUT);
					}
					else
					{
						sidedItemStackHandler.setSlotMode(slot, SidedItemStackHandler.SlotState.SlotMode.INPUT);
					}
				}
			}
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
			if (button instanceof GuiDefaultButton)
			{
				List<String> guiButtonTooltip = ((GuiDefaultButton) button).getCurrentTooltip();
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
		this.mc.getTextureManager().bindTexture(ALLOY_FURNACE_SLOTS_CONFIGURATION);
		int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);


		this.mc.getTextureManager().bindTexture(GUI_ELEMENTS);
		int l = this.getCookProgressScaled();
		this.drawTexturedModalRect(this.guiLeft + 82, this.guiTop + 35, 232, 0, l + 1, 16);
	}

	private int getCookProgressScaled()
	{
		int i = this.tileEntity.getCookTime();
		int j = this.tileEntity.getTotalCookTime();
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	@Override
	public void keyTyped(char typedChar, int keyCode)
	{
		if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), this.tileEntity.getPos());
		}
	}
}