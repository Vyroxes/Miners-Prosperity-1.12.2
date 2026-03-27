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
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityElectricFurnace;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMachine;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityMetalPress;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;
import net.vyroxes.minersprosperity.util.handlers.SidedIngredientHandler;
import net.vyroxes.minersprosperity.util.handlers.SlotState;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;


public class GuiMachineSlotsConfiguration extends GuiContainer
{
	private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
    private final TileEntityMachine tileEntity;
	private final int inputs;
	private final int outputs;
	private final Map<Integer, Integer> buttonToSlot = new HashMap<>();
	private int liquidExperienceTankId;
	private int backButtonId;
	private int disableAllButtonId;
	private int defaultAllButtonId;

	public GuiMachineSlotsConfiguration(InventoryPlayer player, TileEntityMachine tileEntity)
	{
		super(new ContainerInventory(player, tileEntity));
		this.tileEntity = tileEntity;
		this.inputs = this.tileEntity.getCustomItemStackHandler().getInputSlots();
		this.outputs = this.tileEntity.getCustomItemStackHandler().getOutputSlots();
	}
	
	@Override
	public void initGui() 
	{
	    super.initGui();

		int guiLeft = (this.width - this.xSize) / 2;
		int guiTop = (this.height - this.ySize) / 2;

		int buttonId = 0;
		int xOffset = this.inputs > 1 ? 0 : 10;

		this.buttonList.clear();

		int[] inputSlots = tileEntity.getInputSlotIds();
		for (int i = 0; i < inputSlots.length; i++) // Input Slots
		{
			this.addButton(new GuiDefaultButton(buttonId, guiLeft + 32 + xOffset + i * 20, guiTop + 34, 18, 18, GUI_ELEMENTS, 106, 32, 18, this.inputs > 1 ? I18n.format("gui.input_" + (i + 1) + ".name") : I18n.format("gui.input.name")));
			buttonToSlot.put(buttonId, inputSlots[i]);
			buttonId++;
		}

		this.addButton(new GuiDefaultButton(buttonId, guiLeft + 7, guiTop + 52, 18, 18, GUI_ELEMENTS, 124, 32, 18, I18n.format("gui.energy.name"))); // Energy Slot
		buttonToSlot.put(buttonId, tileEntity.getEnergySlotId());
		buttonId++;

		int[] outputSlots = tileEntity.getOutputSlotIds();
		for (int i = 0; i < outputSlots.length; i++) // Output Slots
		{
			this.addButton(new GuiDefaultButton(buttonId, guiLeft + 110 - xOffset + i * 28, guiTop + 30, 26, 26, GUI_ELEMENTS, 142, 32, 26, this.outputs > 1 ? I18n.format("gui.output_" + (i + 1) + ".name") : I18n.format("gui.output.name")));
			buttonToSlot.put(buttonId, outputSlots[i]);
			buttonId++;
		}

		liquidExperienceTankId = buttonId;
		this.addButton(new GuiDefaultButton(buttonId, guiLeft + 138 - xOffset, guiTop + 30, 6, 26, GUI_ELEMENTS, 232, 17, 26, I18n.format("gui.experience.name"))); // Liquid Experience Tank
		buttonToSlot.put(buttonId, tileEntity.getLiquidExperienceTankId());
		buttonId++;

		backButtonId = buttonId;
		this.addButton(new GuiDefaultButton(buttonId++, guiLeft + 7, guiTop + 6, 18, 9, GUI_ELEMENTS, 88, 36, 9, I18n.format("gui.back.name"))); // Back Button

		disableAllButtonId = buttonId;
		this.addButton(new GuiDefaultButton(buttonId++, guiLeft + 7, guiTop + 18, 15, 15, GUI_ELEMENTS, 154, 0, 15, I18n.format("gui.set_all_disabled.name"))); // Disable All Button

		defaultAllButtonId = buttonId;
		this.addButton(new GuiDefaultButton(buttonId++, guiLeft + 7, guiTop + 34, 15, 15, GUI_ELEMENTS, 169, 0, 15, I18n.format("gui.set_all_default.name"))); // Default All Button

		String description = TextFormatting.AQUA + I18n.format("gui.shift_left_click") + TextFormatting.GRAY + I18n.format("gui.shift_left_click_desc")  + "\n" + TextFormatting.AQUA + I18n.format("gui.shift_right_click") + TextFormatting.GRAY + I18n.format("gui.shift_right_click_desc");
		GuiButton descButton = new GuiDefaultButton(buttonId, guiLeft + 163, guiTop + 6, 6, 8, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 0, 41, 8, description);
		descButton.enabled = false;
		this.addButton(descButton);
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if (buttonToSlot.containsKey(guiButton.id))
		{
			if (liquidExperienceTankId == guiButton.id)
			{
				tileEntity.setSlotEditedId(buttonToSlot.get(guiButton.id) + tileEntity.getCustomItemStackHandler().getUpgradeSlots());
			}
			else
			{
				tileEntity.setSlotEditedId(buttonToSlot.get(guiButton.id));
			}
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.MACHINE_SLOT_CONFIGURATION.ordinal(), tileEntity.getPos());
			return;
		}

		if (guiButton.id == backButtonId)
		{
			if (tileEntity instanceof TileEntityAlloyFurnace)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), this.tileEntity.getPos());
			}
			else if (tileEntity instanceof TileEntityMetalPress)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.METAL_PRESS.ordinal(), this.tileEntity.getPos());
			}
			else if (tileEntity instanceof TileEntityElectricFurnace)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ELECTRIC_FURNACE.ordinal(), this.tileEntity.getPos());
			}
			return;
		}

		if (guiButton.id == disableAllButtonId)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler handler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);

				for (int slot = 0; slot < handler.getSlots() + 1; slot++)
				{
					handler.setSlotMode(slot, SlotState.SlotMode.DISABLED);
				}
			}
		}

		if (guiButton.id == defaultAllButtonId)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler handler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);

				for (int slot = 0; slot < handler.getSlots() + 1; slot++)
				{
					handler.setSlotMode(slot, handler.isSlotOutput(slot) ? SlotState.SlotMode.OUTPUT : SlotState.SlotMode.INPUT);
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
					GuiUtils.drawHoveringText(guiButtonTooltip, mouseX, mouseY, this.width, this.height, this.width/2, this.fontRenderer);
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
        ResourceLocation MACHINE_SLOTS_CONFIGURATION = new ResourceLocation(Tags.MODID, "textures/gui/machine_slots_configuration_" + this.inputs + "_" + this.outputs + ".png");;

		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(MACHINE_SLOTS_CONFIGURATION);
		int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

		this.mc.getTextureManager().bindTexture(GUI_ELEMENTS);
		int l = this.getCookProgressScaled();
		int xOffset = inputs > 1 ? 0 : 10;
		this.drawTexturedModalRect(this.guiLeft + 78 - xOffset, this.guiTop + 35, 232, 0, l + 1, 16);
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

			if (tileEntity instanceof TileEntityAlloyFurnace)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), this.tileEntity.getPos());
			}
			else if (tileEntity instanceof TileEntityMetalPress)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.METAL_PRESS.ordinal(), this.tileEntity.getPos());
			}
			else if (tileEntity instanceof TileEntityElectricFurnace)
			{
				NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ELECTRIC_FURNACE.ordinal(), this.tileEntity.getPos());
			}
		}
	}
}