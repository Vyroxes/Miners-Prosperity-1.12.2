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
import net.minecraftforge.fml.client.config.GuiUtils;
import net.vyroxes.minersprosperity.Reference;
import net.vyroxes.minersprosperity.objects.containers.ContainerInventory;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityAlloyFurnace;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import net.vyroxes.minersprosperity.util.handlers.SidedItemHandler;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.*;


public class GuiAlloyFurnaceSlotConfiguration extends GuiContainer
{
	private static final ResourceLocation ALLOY_FURNACE_SLOT_CONFIGURATION_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/alloy_furnace_slot_configuration.png");
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
			SidedItemHandler sidedItemHandler = (SidedItemHandler) tileEntity.getSidedItemHandler(face);
			Map<Integer, String> faceNames = new HashMap<>();
			faceNames.put(0, I18n.format("gui.side_bottom.name"));
			faceNames.put(1, I18n.format("gui.side_top.name"));
			faceNames.put(2, I18n.format("gui.side_front.name"));
			faceNames.put(3, I18n.format("gui.side_back.name"));
			faceNames.put(4, I18n.format("gui.side_left.name"));
			faceNames.put(5, I18n.format("gui.side_right.name"));

			int slot = tileEntity.getSlotId();

			SidedItemHandler.SlotState slotState = sidedItemHandler.getSlotState(slot);

			int textureX = switch (slotState)
			{
				case DISABLED -> 176;
				case INPUT -> 192;
				case OUTPUT -> 208;
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

			this.addButton(new GuiFaceButton(
					face.ordinal(),
					guiLeft + xOffset,
					guiTop + yOffset,
					16, 16,
					new ResourceLocation(Reference.MODID, ALLOY_FURNACE_SLOT_CONFIGURATION_TEXTURE.getPath()),
					textureX,
					0,
					faceNames.get(face.ordinal()),
					"Slot " + slot,
					slotState.ordinal()
			));
		}

		this.addButton(new GuiBackButton(6, guiLeft + 7, guiTop + 6, 18, 9, new ResourceLocation(Reference.MODID, "textures/gui/alloy_furnace_slots_configuration.png"), 238, 0, I18n.format("gui.back.name")));
	}

	@Override
	public void actionPerformed(@NotNull GuiButton guiButton)
	{

		if (guiButton.id < 6)
		{
			int slot = tileEntity.getSlotId();
			int face = guiButton.id;

			SidedItemHandler sidedItemHandler = (SidedItemHandler) tileEntity.getSidedItemHandler(EnumFacing.byIndex(face));
			SidedItemHandler.SlotState currentState = sidedItemHandler.getSlotState(slot);

			if (sidedItemHandler.isSlotOutput(slot))
			{
				SidedItemHandler.SlotState nextState = switch (currentState)
				{
					case DISABLED -> SidedItemHandler.SlotState.OUTPUT;
                    case INPUT -> null;
                    case OUTPUT -> SidedItemHandler.SlotState.DISABLED;
				};

				sidedItemHandler.setSlotState(slot, nextState);
			}
			else
			{
				SidedItemHandler.SlotState nextState = switch (currentState)
				{
					case DISABLED -> SidedItemHandler.SlotState.INPUT;
					case INPUT -> SidedItemHandler.SlotState.OUTPUT;
					case OUTPUT -> SidedItemHandler.SlotState.DISABLED;
				};

				sidedItemHandler.setSlotState(slot, nextState);
			}
		}

		if (guiButton.id == 6)
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
				if (guiButton instanceof GuiFaceButton && ((GuiFaceButton) guiButton).isHovered(mouseX, mouseY))
				{
					int slot = tileEntity.getSlotId();
					int face = guiButton.id;

					SidedItemHandler sidedItemHandler = (SidedItemHandler) tileEntity.getSidedItemHandler(EnumFacing.byIndex(face));
					SidedItemHandler.SlotState currentState = sidedItemHandler.getSlotState(slot);

					if (sidedItemHandler.isSlotOutput(slot))
					{
						SidedItemHandler.SlotState nextState = switch (currentState)
						{
							case DISABLED -> SidedItemHandler.SlotState.OUTPUT;
							case INPUT -> null;
							case OUTPUT -> SidedItemHandler.SlotState.DISABLED;
						};

						sidedItemHandler.setSlotState(slot, nextState);
					}
					else
					{
						SidedItemHandler.SlotState nextState = switch (currentState)
						{
							case DISABLED -> SidedItemHandler.SlotState.OUTPUT;
							case INPUT -> SidedItemHandler.SlotState.DISABLED;
							case OUTPUT -> SidedItemHandler.SlotState.INPUT;
						};

						sidedItemHandler.setSlotState(slot, nextState);
					}

					Minecraft.getMinecraft().player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.25F, 1.0F);
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
				if (guiButton instanceof GuiFaceButton && ((GuiFaceButton) guiButton).isHovered(mouseX, mouseY))
				{
					int slot = tileEntity.getSlotId();
					int face = guiButton.id;

                    SidedItemHandler sidedItemHandler = (SidedItemHandler) tileEntity.getSidedItemHandler(EnumFacing.values()[face]);
                    sidedItemHandler.setSlotState(slot, SidedItemHandler.SlotState.DISABLED);
					Minecraft.getMinecraft().player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.25F, 1.0F);
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
	    this.renderHoveredToolTip(mouseX, mouseY);

		for (GuiButton button : this.buttonList)
		{
			if (button instanceof GuiFaceButton)
			{
				List<String> guiFaceButtonTooltip = ((GuiFaceButton) button).getCurrentTooltip();
				if (guiFaceButtonTooltip != null)
				{
					GuiUtils.drawHoveringText(guiFaceButtonTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
				}
			}

			if (button instanceof GuiBackButton)
			{
				List<String> guiBackButtonTooltip = ((GuiBackButton) button).getCurrentTooltip();
				if (guiBackButtonTooltip != null)
				{
					GuiUtils.drawHoveringText(guiBackButtonTooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
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
		this.mc.getTextureManager().bindTexture(ALLOY_FURNACE_SLOT_CONFIGURATION_TEXTURE);
		int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode)
	{
		if (keyCode == 1)
		{
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOTS_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
	}
}