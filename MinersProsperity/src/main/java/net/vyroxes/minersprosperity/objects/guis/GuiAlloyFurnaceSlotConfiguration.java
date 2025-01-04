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
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import net.vyroxes.minersprosperity.util.handlers.SidedIngredientHandler;
import net.vyroxes.minersprosperity.util.handlers.SlotState;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.*;


public class GuiAlloyFurnaceSlotConfiguration extends GuiContainer
{
	private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
	private static final ResourceLocation ALLOY_FURNACE_SLOT_CONFIGURATION = new ResourceLocation(Tags.MODID, "textures/gui/alloy_furnace_slot_configuration.png");
	private final TileEntityMachine tileEntity;

	public GuiAlloyFurnaceSlotConfiguration(InventoryPlayer player, TileEntityMachine tileEntity)
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

				int slot = tileEntity.getSlotEditedId();

				SlotState.SlotMode slotState = sidedIngredientHandler.getSlotMode(slot);

				int textureX = switch (slotState) {
					case DISABLED, AUTO_OUTPUT -> 184;
					case INPUT -> 200;
					case AUTO_INPUT -> 168;
					case OUTPUT -> 216;
				};

				int textureY = switch (slotState) {
					case DISABLED, INPUT, OUTPUT -> 0;
					case AUTO_INPUT -> 30;
					case AUTO_OUTPUT -> 32;
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
				String auto_input = I18n.format("gui.slot_auto_input.name");
				String output = I18n.format("gui.slot_output.name");
				String auto_output = I18n.format("gui.slot_auto_output.name");
				String tooltip;

				if (slotState.ordinal() == 0) {
					tooltip = faceNames.get(face.ordinal()) + ": " + disabled;
				} else if (slotState.ordinal() == 1) {
					tooltip = faceNames.get(face.ordinal()) + ": " + input;
				} else if (slotState.ordinal() == 2) {
					tooltip = faceNames.get(face.ordinal()) + ": " + auto_input;
				} else if (slotState.ordinal() == 3) {
					tooltip = faceNames.get(face.ordinal()) + ": " + output;
				} else {
					tooltip = faceNames.get(face.ordinal()) + ": " + auto_output;
				}

				this.addButton(new GuiDefaultButton(
						face.ordinal(),
						guiLeft + xOffset,
						guiTop + yOffset,
						16,
						16,
						new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()),
						textureX,
						textureY,
						16,
						tooltip
				));
			}
		}

		this.addButton(new GuiDefaultButton(6, guiLeft + 7, guiTop + 6, 18, 9, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 88, 36, 9, I18n.format("gui.back.name")));
		this.addButton(new GuiDefaultButton(7, guiLeft + 7, guiTop + 18, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 106, 0, 16, I18n.format("gui.set_disabled.name")));

		if (this.tileEntity.isSlotOutput(this.tileEntity.getSlotEditedId()))
		{
			this.addButton(new GuiDefaultButton(10, guiLeft + 7, guiTop + 36, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 138, 0, 16, I18n.format("gui.set_output.name")));
			this.addButton(new GuiDefaultButton(11, guiLeft + 7, guiTop + 54, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 216, 32, 16, I18n.format("gui.set_auto_output.name")));
		}
		else
		{
			this.addButton(new GuiDefaultButton(8, guiLeft + 7, guiTop + 36, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 122, 0, 16, I18n.format("gui.set_input.name")));
			this.addButton(new GuiDefaultButton(9, guiLeft + 7, guiTop + 54, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 200, 32, 16, I18n.format("gui.set_auto_input.name")));
			this.addButton(new GuiDefaultButton(10, guiLeft + 153, guiTop + 18, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 138, 0, 16, I18n.format("gui.set_output.name")));
			this.addButton(new GuiDefaultButton(11, guiLeft + 153, guiTop + 36, 16, 16, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 216, 32, 16, I18n.format("gui.set_auto_output.name")));
		}

		String description = TextFormatting.AQUA + "Shift + left click" + TextFormatting.GRAY + " on the machine disables input and output for all slots on every side\n" + TextFormatting.AQUA + "Shift + right click" + TextFormatting.GRAY + " on the machine from a specific side disables input and output for all slots on that specific side.";
		GuiButton descButton = new GuiDefaultButton(12, guiLeft + 163, guiTop + 6, 6, 8, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 0, 41, 8, description);
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

			if (sidedIngredientHandler.isSlotOutput(slot))
			{
				SlotState.SlotMode nextState = switch (currentState)
				{
					case DISABLED -> SlotState.SlotMode.OUTPUT;
                    case INPUT, AUTO_INPUT -> null;
                    case OUTPUT -> SlotState.SlotMode.AUTO_OUTPUT;
					case AUTO_OUTPUT -> SlotState.SlotMode.DISABLED;
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
					case AUTO_OUTPUT -> SlotState.SlotMode.DISABLED;
				};

				sidedIngredientHandler.setSlotMode(slot, nextState);
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
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.DISABLED);
			}
		}

		if (guiButton.id == 8)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.INPUT);
			}
		}

		if (guiButton.id == 9)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.AUTO_INPUT);
			}
		}

		if (guiButton.id == 10)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.OUTPUT);
			}
		}

		if (guiButton.id == 11)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.AUTO_OUTPUT);
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
						int slot = tileEntity.getSlotEditedId();
						int face = guiButton.id;

						SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(EnumFacing.byIndex(face));
						SlotState.SlotMode currentState = sidedIngredientHandler.getSlotMode(slot);

						if (sidedIngredientHandler.isSlotOutput(slot))
						{
							SlotState.SlotMode nextState = switch (currentState)
							{
								case DISABLED -> SlotState.SlotMode.AUTO_OUTPUT;
								case INPUT, AUTO_INPUT -> null;
								case OUTPUT -> SlotState.SlotMode.DISABLED;
								case AUTO_OUTPUT -> SlotState.SlotMode.OUTPUT;
							};

							sidedIngredientHandler.setSlotMode(slot, nextState);
						}
						else
						{
							SlotState.SlotMode nextState = switch (currentState)
							{
								case DISABLED -> SlotState.SlotMode.AUTO_OUTPUT;
								case INPUT -> SlotState.SlotMode.DISABLED;
								case AUTO_INPUT -> SlotState.SlotMode.INPUT;
								case OUTPUT -> SlotState.SlotMode.AUTO_INPUT;
								case AUTO_OUTPUT -> SlotState.SlotMode.OUTPUT;
							};

							sidedIngredientHandler.setSlotMode(slot, nextState);
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
						int slot = tileEntity.getSlotEditedId();
						int face = guiButton.id;

						SidedIngredientHandler sidedIngredientHandler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(EnumFacing.values()[face]);
						sidedIngredientHandler.setSlotMode(slot, SlotState.SlotMode.DISABLED);
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