package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.Minecraft;
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
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.*;


public class GuiAlloyFurnaceSlotConfiguration extends GuiContainer
{
	private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
	private static final ResourceLocation ALLOY_FURNACE_SLOT_CONFIGURATION = new ResourceLocation(Tags.MODID, "textures/gui/alloy_furnace_slot_configuration.png");
	private final TileEntityAlloyFurnace tileEntity;

	public GuiAlloyFurnaceSlotConfiguration(InventoryPlayer player, TileEntityAlloyFurnace tileEntity)
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

        for (EnumFacing face : EnumFacing.values())
		{
			SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntity.getSidedItemHandler(face);
			Map<Integer, String> faceNames = new HashMap<>();
			faceNames.put(0, I18n.format("gui.side_bottom.name"));
			faceNames.put(1, I18n.format("gui.side_top.name"));
			faceNames.put(2, I18n.format("gui.side_front.name"));
			faceNames.put(3, I18n.format("gui.side_back.name"));
			faceNames.put(4, I18n.format("gui.side_left.name"));
			faceNames.put(5, I18n.format("gui.side_right.name"));

			int slot = tileEntity.getSlotId();

			SidedItemStackHandler.SlotState.SlotMode slotState = sidedItemStackHandler.getSlotMode(slot);

			int textureX = switch (slotState)
			{
				case DISABLED -> 184;
				case INPUT -> 200;
				case OUTPUT -> 216;
			};

			int xOffset = switch (face)
			{
				case UP, NORTH, DOWN -> 80;
				case WEST -> 62;
				case SOUTH, EAST -> 98;
			};

			int yOffset = switch (face)
			{
				case UP -> 17;
				case WEST, NORTH, EAST -> 35;
				case DOWN, SOUTH -> 53;
			};

			String disabled = I18n.format("gui.slot_disabled.name");
			String input = I18n.format("gui.slot_input.name");
			String output = I18n.format("gui.slot_output.name");
			String tooltip;

			if (slotState.ordinal() == 0)
			{
				tooltip = faceNames.get(face.ordinal()) + ": " + disabled;
			}
			else if (slotState.ordinal() == 1)
			{
				tooltip = faceNames.get(face.ordinal()) + ": " + input;
			}
			else
			{
				tooltip = faceNames.get(face.ordinal()) + ": " + output;
			}

			this.addButton(new GuiDefaultButton(
					face.ordinal(),
					guiLeft + xOffset,
					guiTop + yOffset,
					16,
					16,
					new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()),
					textureX,
					0,
					16,
					tooltip
			));
		}

		this.addButton(new GuiDefaultButton(6, guiLeft + 7, guiTop + 6, 18, 9, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 88, 36, 9, I18n.format("gui.back.name")));
		this.addButton(new GuiDefaultButton(7, guiLeft + 7, guiTop + 18, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 106, 0, 16, I18n.format("gui.set_disabled.name")));

		if (tileEntity.isSlotOutput(tileEntity.getSlotId()))
		{
			this.addButton(new GuiDefaultButton(9, guiLeft + 7, guiTop + 36, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 138, 0, 16, I18n.format("gui.set_output.name")));
		}
		else
		{
			this.addButton(new GuiDefaultButton(8, guiLeft + 7, guiTop + 36, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 122, 0, 16, I18n.format("gui.set_input.name")));
			this.addButton(new GuiDefaultButton(9, guiLeft + 7, guiTop + 54, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 138, 0, 16, I18n.format("gui.set_output.name")));
		}

		String description = TextFormatting.AQUA + "Shift + left click" + TextFormatting.GRAY + " on the machine disables input and output for all slots on every side\n" + TextFormatting.AQUA + "Shift + right click" + TextFormatting.GRAY + " on the machine from a specific side disables input and output for all slots on that specific side.";
		GuiButton descButton = new GuiDefaultButton(10, guiLeft + 163, guiTop + 6, 6, 8, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 0, 41, 8, description);
		descButton.enabled = false;
		this.addButton(descButton);
	}

	@Override
	public void actionPerformed(@NotNull GuiButton guiButton)
	{
		int slot = tileEntity.getSlotId();

		if (guiButton.id < 6)
		{
			int face = guiButton.id;

			SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntity.getSidedItemHandler(EnumFacing.byIndex(face));
			SidedItemStackHandler.SlotState.SlotMode currentState = sidedItemStackHandler.getSlotMode(slot);

			if (sidedItemStackHandler.isSlotOutput(slot))
			{
				SidedItemStackHandler.SlotState.SlotMode nextState = switch (currentState)
				{
					case DISABLED -> SidedItemStackHandler.SlotState.SlotMode.OUTPUT;
                    case INPUT -> null;
                    case OUTPUT -> SidedItemStackHandler.SlotState.SlotMode.DISABLED;
				};

				sidedItemStackHandler.setSlotMode(slot, nextState);
			}
			else
			{
				SidedItemStackHandler.SlotState.SlotMode nextState = switch (currentState)
				{
					case DISABLED -> SidedItemStackHandler.SlotState.SlotMode.INPUT;
					case INPUT -> SidedItemStackHandler.SlotState.SlotMode.OUTPUT;
					case OUTPUT -> SidedItemStackHandler.SlotState.SlotMode.DISABLED;
				};

				sidedItemStackHandler.setSlotMode(slot, nextState);
			}
		}

		if (guiButton.id == 6)
		{
            NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOTS_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}

		if (guiButton.id == 7)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntity.getSidedItemHandler(face);
				sidedItemStackHandler.setSlotMode(slot, SidedItemStackHandler.SlotState.SlotMode.DISABLED);
			}
		}

		if (guiButton.id == 8)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntity.getSidedItemHandler(face);
				sidedItemStackHandler.setSlotMode(slot, SidedItemStackHandler.SlotState.SlotMode.INPUT);
			}
		}

		if (guiButton.id == 9)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntity.getSidedItemHandler(face);
				sidedItemStackHandler.setSlotMode(slot, SidedItemStackHandler.SlotState.SlotMode.OUTPUT);
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
					if (guiButton.id < 6)
					{
						int slot = tileEntity.getSlotId();
						int face = guiButton.id;

						SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntity.getSidedItemHandler(EnumFacing.byIndex(face));
						SidedItemStackHandler.SlotState.SlotMode currentState = sidedItemStackHandler.getSlotMode(slot);

						if (sidedItemStackHandler.isSlotOutput(slot))
						{
							SidedItemStackHandler.SlotState.SlotMode nextState = switch (currentState)
							{
								case DISABLED -> SidedItemStackHandler.SlotState.SlotMode.OUTPUT;
								case INPUT -> null;
								case OUTPUT -> SidedItemStackHandler.SlotState.SlotMode.DISABLED;
							};

							sidedItemStackHandler.setSlotMode(slot, nextState);
						}
						else
						{
							SidedItemStackHandler.SlotState.SlotMode nextState = switch (currentState)
							{
								case DISABLED -> SidedItemStackHandler.SlotState.SlotMode.OUTPUT;
								case INPUT -> SidedItemStackHandler.SlotState.SlotMode.DISABLED;
								case OUTPUT -> SidedItemStackHandler.SlotState.SlotMode.INPUT;
							};

							sidedItemStackHandler.setSlotMode(slot, nextState);
						}

						Minecraft.getMinecraft().player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.25F, 1.0F);
						this.initGui();
						return;
					}
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
					if (guiButton.id < 6)
					{
						int slot = tileEntity.getSlotId();
						int face = guiButton.id;

						SidedItemStackHandler sidedItemStackHandler = (SidedItemStackHandler) tileEntity.getSidedItemHandler(EnumFacing.values()[face]);
						sidedItemStackHandler.setSlotMode(slot, SidedItemStackHandler.SlotState.SlotMode.DISABLED);
						Minecraft.getMinecraft().player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.25F, 1.0F);
						this.initGui();
						return;
					}
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
		this.mc.getTextureManager().bindTexture(ALLOY_FURNACE_SLOT_CONFIGURATION);
		int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode)
	{
		if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOTS_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
	}
}