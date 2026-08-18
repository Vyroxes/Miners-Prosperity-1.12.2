package net.vyroxes.minersprosperity.client.gui;

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
import net.vyroxes.minersprosperity.inventory.container.ContainerInventory;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import net.vyroxes.minersprosperity.gui.GuiHandler;
import net.vyroxes.minersprosperity.network.NetworkHandler;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.*;


public class GuiMachineSlotConfiguration extends GuiContainer
{
	private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
	private static final ResourceLocation MACHINE_SLOT_CONFIGURATION = new ResourceLocation(Tags.MODID, "textures/gui/machine_slot_configuration.png");
	private final TileEntityMachine tileEntity;

	public GuiMachineSlotConfiguration(InventoryPlayer player, TileEntityMachine tileEntity)
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

		int slot = tileEntity.getSlotEditedId();

		if (this.tileEntity.getSidedIngredientHandlers() != null)
		{
			for (EnumFacing face : EnumFacing.values()) {
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				Map<Integer, String> faceNames = new HashMap<>();
				faceNames.put(0, I18n.format("gui.side_bottom.name"));
				faceNames.put(1, I18n.format("gui.side_top.name"));
				faceNames.put(2, I18n.format("gui.side_front.name"));
				faceNames.put(3, I18n.format("gui.side_back.name"));
				faceNames.put(4, I18n.format("gui.side_left.name"));
				faceNames.put(5, I18n.format("gui.side_right.name"));

				SlotState.SlotMode slotState = sidedIngredientHandler.getSlotMode(slot);

				int textureX = switch (slotState) {
					case DISABLED, AUTO_OUTPUT -> 184;
					case INPUT -> 200;
					case AUTO_INPUT -> 168;
					case OUTPUT -> 216;
					case INPUT_OUTPUT -> 168;
				};

				int textureY = switch (slotState) {
					case DISABLED, INPUT, OUTPUT -> 0;
					case AUTO_INPUT -> 30;
					case AUTO_OUTPUT -> 32;
					case INPUT_OUTPUT -> 62;
				};

				int xOffset = switch (face) {
					case UP, NORTH, DOWN -> 80;
					case WEST -> 62;
					case SOUTH, EAST -> 98;
				};

				int yOffset = switch (face) {
					case UP -> 17;
					case WEST, NORTH, EAST -> 35;
					case DOWN, SOUTH -> 53;
				};

				String disabled = I18n.format("gui.slot_disabled.name");
				String input = I18n.format("gui.slot_input.name");
				String autoInput = I18n.format("gui.slot_auto_input.name");
				String output = I18n.format("gui.slot_output.name");
				String autoOutput = I18n.format("gui.slot_auto_output.name");
				String inputOutput = I18n.format("gui.slot_input_output.name");

				String slotModeName = switch (slotState)
				{
					case DISABLED -> disabled;
					case INPUT -> input;
					case AUTO_INPUT -> autoInput;
					case OUTPUT -> output;
					case AUTO_OUTPUT -> autoOutput;
					case INPUT_OUTPUT -> inputOutput;
				};

				String tooltip = faceNames.get(face.ordinal()) + ": " + TextFormatting.GRAY + slotModeName;

				if (sidedIngredientHandler.isSlotEnergy(slot) && sidedIngredientHandler.getIngredientType(slot) == SlotState.IngredientType.ITEM)
				{
					SlotState.SlotCondition condition = sidedIngredientHandler.getSlotCondition(slot);

					if (condition == SlotState.SlotCondition.WHEN_EMPTY) tooltip += "\n" + TextFormatting.DARK_GRAY + I18n.format("gui.slot_condition.name") + ": " + TextFormatting.AQUA + I18n.format("gui.slot_condition_when_empty.name");
					else if (condition == SlotState.SlotCondition.WHEN_FULL) tooltip += "\n" + TextFormatting.DARK_GRAY + I18n.format("gui.slot_condition.name") + ": " + TextFormatting.AQUA + I18n.format("gui.slot_condition_when_full.name");
				}

				this.addButton(new GuiDefaultButton(face.ordinal(), guiLeft + xOffset, guiTop + yOffset, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), textureX, textureY, 0,16, tooltip));
			}
		}

		this.addButton(new GuiDefaultButton(6, guiLeft + 7, guiTop + 6, 18, 9, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 88, 36, 0,9, I18n.format("gui.back.name")));
		this.addButton(new GuiDefaultButton(7, guiLeft + 7, guiTop + 18, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 106, 0, 0, 16, I18n.format("gui.set_disabled.name")));

		SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(EnumFacing.UP);
		boolean isEnergyHandler = sidedIngredientHandler.getIngredientType(slot) == SlotState.IngredientType.ENERGY;

		if (isEnergyHandler) {
			boolean canInput = sidedIngredientHandler.supportsEnergyInput();
			boolean canOutput = sidedIngredientHandler.supportsEnergyOutput();

			if (canInput) this.addButton(new GuiDefaultButton(8, guiLeft + 7, guiTop + 36, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 122, 0, 0, 16, I18n.format("gui.set_input.name")));
			if (canOutput) this.addButton(new GuiDefaultButton(10, guiLeft + 153, guiTop + 18, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 138, 0, 0, 16, I18n.format("gui.set_output.name")));
			if (canInput && canOutput) this.addButton(new GuiDefaultButton(15, guiLeft + 153, guiTop + 36, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 216, 64, 0, 16, I18n.format("gui.set_input_output.name")));
		}
		else if (this.tileEntity.isSlotOutput(slot)) {
			this.addButton(new GuiDefaultButton(10, guiLeft + 7, guiTop + 36, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 138, 0, 0, 16, I18n.format("gui.set_output.name")));
			this.addButton(new GuiDefaultButton(11, guiLeft + 7, guiTop + 54, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 216, 32, 0, 16, I18n.format("gui.set_auto_output.name")));
		}
		else {
			this.addButton(new GuiDefaultButton(8, guiLeft + 7, guiTop + 36, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 122, 0, 0, 16, I18n.format("gui.set_input.name")));
			this.addButton(new GuiDefaultButton(9, guiLeft + 7, guiTop + 54, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 200, 32, 0, 16, I18n.format("gui.set_auto_input.name")));
			this.addButton(new GuiDefaultButton(10, guiLeft + 153, guiTop + 18, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 138, 0, 0, 16, I18n.format("gui.set_output.name")));
			this.addButton(new GuiDefaultButton(11, guiLeft + 153, guiTop + 36, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 216, 32, 0, 16, I18n.format("gui.set_auto_output.name")));
			this.addButton(new GuiDefaultButton(12, guiLeft + 153, guiTop + 54, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 216, 64, 0, 16, I18n.format("gui.set_input_output.name")));
		}

		if (this.tileEntity.isSlotOutput(slot))
		{
			if (sidedIngredientHandler.getSlotOutputMode(slot).equals(SlotState.SlotOutputMode.DEFAULT)) this.addButton(new GuiDefaultButton(13, guiLeft + 153, guiTop + 18, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 0, 57, 0,16, I18n.format("gui.output_default.name")));
			else if (sidedIngredientHandler.getSlotOutputMode(slot).equals(SlotState.SlotOutputMode.VOID_EXCESS)) this.addButton(new GuiDefaultButton(13, guiLeft + 153, guiTop + 18, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 16, 72, 0,16, I18n.format("gui.output_void_excess.name")));
			else this.addButton(new GuiDefaultButton(13, guiLeft + 153, guiTop + 18, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 32, 72, 0,16, I18n.format("gui.output_void_all.name")));
		}

		String description = TextFormatting.AQUA + I18n.format("gui.shift_left_click") + TextFormatting.GRAY + I18n.format("gui.shift_left_click_desc")  + "\n" + TextFormatting.AQUA + I18n.format("gui.shift_right_click") + TextFormatting.GRAY + I18n.format("gui.shift_right_click_desc");
		GuiButton descButton = new GuiDefaultButton(14, guiLeft + 163, guiTop + 6, 6, 8, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 0, 41, 0,8, description);
		descButton.enabled = false;
		this.addButton(descButton);
	}

	@Override
	public void actionPerformed(@NotNull GuiButton guiButton)
	{
		int slot = tileEntity.getSlotEditedId();

		if (guiButton.id < 6)
		{
			int face = guiButton.id;

			SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(EnumFacing.byIndex(face));

			SlotState.SlotMode currentState = sidedIngredientHandler.getSlotMode(slot);

			if (sidedIngredientHandler.getIngredientType(slot) == SlotState.IngredientType.ENERGY) sidedIngredientHandler.setSlotMode(slot, sidedIngredientHandler.getNextEnergyMode(currentState, false));
			else if (sidedIngredientHandler.isSlotOutput(slot))
			{
				SlotState.SlotMode nextState = switch (currentState)
				{
					case DISABLED -> SlotState.SlotMode.OUTPUT;
					case OUTPUT -> SlotState.SlotMode.AUTO_OUTPUT;
					case AUTO_OUTPUT -> SlotState.SlotMode.DISABLED;
					case INPUT, AUTO_INPUT, INPUT_OUTPUT -> SlotState.SlotMode.DISABLED;
				};

				sidedIngredientHandler.setSlotMode(slot, nextState);
			}
			else
			{
				SlotState.SlotMode nextState = switch (currentState)
				{
					case DISABLED -> SlotState.SlotMode.INPUT;
					case INPUT -> SlotState.SlotMode.AUTO_INPUT;
					case AUTO_INPUT -> SlotState.SlotMode.OUTPUT;
					case OUTPUT -> SlotState.SlotMode.AUTO_OUTPUT;
					case AUTO_OUTPUT -> SlotState.SlotMode.INPUT_OUTPUT;
					case INPUT_OUTPUT -> SlotState.SlotMode.DISABLED;
				};

				sidedIngredientHandler.setSlotMode(slot, nextState);
			}
		}
		else if (guiButton.id == 6) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.MACHINE_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		else if (guiButton.id == 7)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.DISABLED);
			}
		}
		else if (guiButton.id == 8)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.INPUT);
			}
		}
		else if (guiButton.id == 9)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.AUTO_INPUT);
			}
		}
		else if (guiButton.id == 10)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.OUTPUT);
			}
		}
		else if (guiButton.id == 11)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.AUTO_OUTPUT);
			}
		}
		else if (guiButton.id == 12)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.INPUT_OUTPUT);
			}
		}
		else if (guiButton.id == 13)
		{
			SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(EnumFacing.UP);
			SlotState.SlotOutputMode currentState = sidedIngredientHandler.getSlotOutputMode(slot);

			SlotState.SlotOutputMode nextState = switch (currentState)
			{
				case DEFAULT -> SlotState.SlotOutputMode.VOID_EXCESS;
				case VOID_EXCESS -> SlotState.SlotOutputMode.VOID_ALL;
				case VOID_ALL -> SlotState.SlotOutputMode.DEFAULT;
			};

			sidedIngredientHandler.setSlotOutputMode(slot, nextState);
		}
		else if (guiButton.id == 15)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.INPUT_OUTPUT);
			}
		}

		this.initGui();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (isShiftKeyDown() && (mouseButton == 0 || mouseButton == 1))
		{
			for (GuiButton guiButton : this.buttonList)
			{
				if (!(guiButton instanceof GuiDefaultButton) || !((GuiDefaultButton) guiButton).isHovered(mouseX, mouseY) || guiButton.id >= 6) continue;

				int slot = tileEntity.getSlotEditedId();
				int face = guiButton.id;

				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(EnumFacing.byIndex(face));

				if (!sidedIngredientHandler.isSlotEnergy(slot) || sidedIngredientHandler.getIngredientType(slot) != SlotState.IngredientType.ITEM) break;

				SlotState.SlotCondition currentState = sidedIngredientHandler.getSlotCondition(slot);

				SlotState.SlotCondition nextState;

				if (mouseButton == 0)
				{
					nextState = switch (currentState)
					{
						case DEFAULT -> SlotState.SlotCondition.WHEN_EMPTY;
						case WHEN_EMPTY -> SlotState.SlotCondition.WHEN_FULL;
						case WHEN_FULL -> SlotState.SlotCondition.DEFAULT;
					};
				}
				else
				{
					nextState = switch (currentState)
					{
						case DEFAULT -> SlotState.SlotCondition.WHEN_FULL;
						case WHEN_EMPTY -> SlotState.SlotCondition.DEFAULT;
						case WHEN_FULL -> SlotState.SlotCondition.WHEN_EMPTY;
					};
				}

				sidedIngredientHandler.setSlotCondition(slot, nextState);

				Minecraft.getMinecraft().player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.25F, 1.0F);

				this.initGui();
				return;
			}
		}

		if (mouseButton == 1)
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
			for (GuiButton guiButton : this.buttonList)
			{
				if (guiButton instanceof GuiDefaultButton && ((GuiDefaultButton) guiButton).isHovered(mouseX, mouseY))
				{
					if (guiButton.id < 6)
					{
						int slot = tileEntity.getSlotEditedId();
						int face = guiButton.id;

						SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(EnumFacing.byIndex(face));

						SlotState.SlotMode currentState = sidedIngredientHandler.getSlotMode(slot);

						if (sidedIngredientHandler.getIngredientType(slot) == SlotState.IngredientType.ENERGY) sidedIngredientHandler.setSlotMode(slot, sidedIngredientHandler.getNextEnergyMode(currentState, true));
						else if (sidedIngredientHandler.isSlotOutput(slot))
						{
							SlotState.SlotMode nextState = switch (currentState)
							{
								case DISABLED -> SlotState.SlotMode.AUTO_OUTPUT;
								case OUTPUT -> SlotState.SlotMode.DISABLED;
								case AUTO_OUTPUT -> SlotState.SlotMode.OUTPUT;
								case INPUT, AUTO_INPUT, INPUT_OUTPUT -> SlotState.SlotMode.DISABLED;
							};

							sidedIngredientHandler.setSlotMode(slot, nextState);
						}
						else
						{
							SlotState.SlotMode nextState = switch (currentState)
							{
								case DISABLED -> SlotState.SlotMode.INPUT_OUTPUT;
								case INPUT -> SlotState.SlotMode.DISABLED;
								case AUTO_INPUT -> SlotState.SlotMode.INPUT;
								case OUTPUT -> SlotState.SlotMode.AUTO_INPUT;
								case AUTO_OUTPUT -> SlotState.SlotMode.OUTPUT;
								case INPUT_OUTPUT -> SlotState.SlotMode.AUTO_OUTPUT;
							};

							sidedIngredientHandler.setSlotMode(slot, nextState);
						}

						Minecraft.getMinecraft().player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.25F, 1.0F);

						this.initGui();
						return;
					}
					else if (guiButton.id == 13)
					{
						int slot = tileEntity.getSlotEditedId();

						SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(EnumFacing.UP);
						SlotState.SlotOutputMode currentState = sidedIngredientHandler.getSlotOutputMode(slot);

						SlotState.SlotOutputMode nextState = switch (currentState)
						{
							case DEFAULT -> SlotState.SlotOutputMode.VOID_ALL;
							case VOID_EXCESS -> SlotState.SlotOutputMode.DEFAULT;
							case VOID_ALL -> SlotState.SlotOutputMode.VOID_EXCESS;
						};

						sidedIngredientHandler.setSlotOutputMode(slot, nextState);

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
						int slot = tileEntity.getSlotEditedId();
						int face = guiButton.id;

						SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(EnumFacing.values()[face]);
						sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.DISABLED);

						Minecraft.getMinecraft().player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.25F, 1.0F);
						this.initGui();
						return;
					}
					else if (guiButton.id == 13)
					{
						int slot = tileEntity.getSlotEditedId();

						SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(EnumFacing.UP);
						sidedIngredientHandler.setSlotOutputMode(slot, SlotState.SlotOutputMode.DEFAULT);

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
		this.drawSlotConditionOverlays(mouseX, mouseY);
	    this.renderHoveredToolTip(mouseX, mouseY);

		for (GuiButton button : this.buttonList)
		{
			if (button instanceof GuiDefaultButton)
			{
				List<String> guiButtonTooltip = ((GuiDefaultButton) button).getCurrentTooltip();

				if (guiButtonTooltip != null)
				{
					List<String> tooltipLines = new ArrayList<>();

					for (String line : guiButtonTooltip) Collections.addAll(tooltipLines, line.split("\\r?\\n"));

					GuiUtils.drawHoveringText(tooltipLines, mouseX, mouseY, this.width, this.height, this.width / 2, this.fontRenderer);
				}
			}
		}
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
		this.mc.getTextureManager().bindTexture(MACHINE_SLOT_CONFIGURATION);
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
            NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.MACHINE_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
	}

	private void drawSlotConditionOverlays(int mouseX, int mouseY)
	{
		int slot = tileEntity.getSlotEditedId();

		for (GuiButton guiButton : this.buttonList)
		{
			if (!(guiButton instanceof GuiDefaultButton) || guiButton.id >= 6) continue;

			EnumFacing face = EnumFacing.byIndex(guiButton.id);

			SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);

			if (!sidedIngredientHandler.isSlotEnergy(slot) || sidedIngredientHandler.getIngredientType(slot) != SlotState.IngredientType.ITEM) continue;

			SlotState.SlotCondition condition = sidedIngredientHandler.getSlotCondition(slot);

			if (condition == null || condition == SlotState.SlotCondition.DEFAULT) continue;

			boolean hovered = ((GuiDefaultButton) guiButton).isHovered(mouseX, mouseY);

			int textureX;
			int textureY;

			if (condition == SlotState.SlotCondition.WHEN_FULL)
			{
				textureX = 184;
				textureY = hovered ? 80 : 64;
			}
			else
			{
				textureX = 200;
				textureY = hovered ? 80 : 64;
			}

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			this.mc.getTextureManager().bindTexture(GUI_ELEMENTS);
			this.drawTexturedModalRect(guiButton.x, guiButton.y, textureX, textureY, 16, 16);
			GlStateManager.enableLighting();
		}
	}
}