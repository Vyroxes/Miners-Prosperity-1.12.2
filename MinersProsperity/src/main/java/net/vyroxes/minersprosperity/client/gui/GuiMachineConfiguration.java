package net.vyroxes.minersprosperity.client.gui;

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
import net.vyroxes.minersprosperity.gui.GuiHandler;
import net.vyroxes.minersprosperity.network.NetworkHandler;
import net.vyroxes.minersprosperity.tile.*;
import net.vyroxes.minersprosperity.machine.SidedIngredientHandler;
import net.vyroxes.minersprosperity.inventory.slot.SlotState;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

public class GuiMachineConfiguration extends GuiContainer
{
	private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
    private final TileEntityMachine tileEntity;
	private final int inputs;
	private final int outputs;
	private final Map<Integer, Integer> buttonToSlot = new HashMap<>();
	private int liquidExperienceTankId = -1;
	private int energyId = -1;
	private int backButtonId;
	private int disableAllButtonId;
	private int defaultAllButtonId;

	public GuiMachineConfiguration(InventoryPlayer player, TileEntityMachine tileEntity)
	{
		super(new ContainerInventory(player, tileEntity));
		this.tileEntity = tileEntity;
		if (this.tileEntity.getCustomItemStackHandler() != null) {
			this.inputs = this.tileEntity.getCustomItemStackHandler().getInputSlots();
			this.outputs = this.tileEntity.getCustomItemStackHandler().getOutputSlots();
		} else {
			this.inputs = 0;
			this.outputs = 0;
		}
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
        this.buttonToSlot.clear();

		int[] inputSlots = tileEntity instanceof TileEntityFluidTank ? new int[0] : tileEntity.getInputSlotIds();
		for (int i = 0; i < inputSlots.length; i++) // Input Slots
		{
			this.addButton(new GuiDefaultButton(buttonId, guiLeft + (tileEntity instanceof TileEntityLiquidFuelGenerator ? 54 : tileEntity instanceof TileEntitySolidFuelGenerator ? 79 : 32 + xOffset + i * 20), guiTop + (tileEntity instanceof TileEntityLiquidFuelGenerator ? 25 : tileEntity instanceof TileEntitySolidFuelGenerator ? 43 : 34), 18, 18, GUI_ELEMENTS, 106, 32, 0, 18, this.inputs > 1 ? I18n.format("gui.input_" + (i + 1) + "_slot.name") : I18n.format("gui.input.name")));
			buttonToSlot.put(buttonId, inputSlots[i]);
			buttonId++;
		}

		int energySlotId = tileEntity.getEnergySlotId(); // Energy Slot
		if (energySlotId >= 0 && !(tileEntity instanceof TileEntityEnergyStorage)) {
			this.addButton(new GuiDefaultButton(buttonId, guiLeft + 7, guiTop + 52, 18, 18, GUI_ELEMENTS, 124, 32, 0, 18, I18n.format("gui.energy_slot.name")));
			buttonToSlot.put(buttonId, energySlotId);
			buttonId++;
		}

        if (tileEntity instanceof TileEntityEnergyStorage)
        {
            for (int i = 0; i < 2; i++)
            {
                this.addButton(new GuiDefaultButton(buttonId, guiLeft + 50 + i * 58, guiTop + 35, 18, 18, GUI_ELEMENTS, 124, 32, 0, 18, I18n.format(i == 0 ? "gui.energy_storage.discharge_item" : "gui.energy_storage.charge_item")));
                buttonToSlot.put(buttonId++, tileEntity.getEnergySlotId(i));
            }
        }

		int[] outputSlots = tileEntity instanceof TileEntityFluidTank ? new int[0] : tileEntity.getOutputSlotIds();
		for (int i = 0; i < outputSlots.length; i++) // Output Slots
		{
			this.addButton(new GuiDefaultButton(buttonId, guiLeft + (tileEntity instanceof TileEntityLiquidFuelGenerator ? 54 : 110 - xOffset + i * 28), guiTop + (tileEntity instanceof TileEntityLiquidFuelGenerator ? 45 : 30), tileEntity instanceof TileEntityLiquidFuelGenerator ? 18 : 26, tileEntity instanceof TileEntityLiquidFuelGenerator ? 18 : 26, GUI_ELEMENTS, tileEntity instanceof TileEntityLiquidFuelGenerator ? 238 : 142, tileEntity instanceof TileEntityLiquidFuelGenerator ? 17 : 32, 0, tileEntity instanceof TileEntityLiquidFuelGenerator ? 18 : 26, this.outputs > 1 ? I18n.format("gui.output_" + (i + 1) + "_slot.name") : I18n.format("gui.output_slot.name")));
			buttonToSlot.put(buttonId, outputSlots[i]);
			buttonId++;
		}

        if (tileEntity instanceof TileEntityFluidTank)
        {
            for (int i = 0; i < 2; i++)
            {
                this.addButton(new GuiDefaultButton(buttonId, guiLeft + 50 + i * 58, guiTop + 25, 18, 18, GUI_ELEMENTS, 106, 32, 0, 18, I18n.format(i == 0 ? "gui.fluid_tank.empty_container" : "gui.fluid_tank.fill_container")));
                buttonToSlot.put(buttonId++, tileEntity.getInputSlotId(i));
                this.addButton(new GuiDefaultButton(buttonId, guiLeft + 50 + i * 58, guiTop + 45, 18, 18, GUI_ELEMENTS, 238, 17, 0, 18, I18n.format("gui.output_" + (i + 1) + "_slot.name")));
                buttonToSlot.put(buttonId++, tileEntity.getOutputSlotId(i));
            }
            this.addButton(new GuiDefaultButton(buttonId, guiLeft + 79, guiTop + 25, 18, 38, GUI_ELEMENTS, 238, 174, 0, 38, I18n.format("gui.fluid")));
            buttonToSlot.put(buttonId++, tileEntity.getFluidTankStateId(0));
        }

		int experienceTankStateId = tileEntity.getExperienceTankStateId();
		if (experienceTankStateId >= 0) // XP Tank
		{
			liquidExperienceTankId = buttonId;
			this.addButton(new GuiDefaultButton(buttonId, guiLeft + 138 - xOffset, guiTop + 30, 6, 26, GUI_ELEMENTS, 232, 17, 0, 26, I18n.format("gui.experience_tank.name")));
			buttonToSlot.put(buttonId, experienceTankStateId);
			buttonId++;
		}
		else liquidExperienceTankId = -1;

        if (tileEntity instanceof TileEntityLiquidFuelGenerator)
        {
            this.addButton(new GuiDefaultButton(buttonId, guiLeft + 103, guiTop + 25, 18, 38, GUI_ELEMENTS, 238, 174, 0, 38, I18n.format("gui.fluid")));
            buttonToSlot.put(buttonId++, tileEntity.getFluidTankStateId(0));
        }

		int energyHandlerStateId = tileEntity.getEnergyHandlerStateId();
		if (energyHandlerStateId >= 0) // Energy
		{
			energyId = buttonId;
            if (tileEntity instanceof TileEntityEnergyStorage)
            {
                this.addButton(new GuiDefaultButton(buttonId, guiLeft + 79, guiTop + 25, 18, 38, GUI_ELEMENTS, 52, 140, 18, 0, I18n.format("gui.energy.name")));
            }
            else this.addButton(new GuiDefaultButton(buttonId, guiLeft + 7, guiTop + 18, 18, 31, GUI_ELEMENTS, 48, 72, 18, 0, I18n.format("gui.energy.name")));
			buttonToSlot.put(buttonId, energyHandlerStateId);
			buttonId++;
		}
		else energyId = -1;

		backButtonId = buttonId;
		this.addButton(new GuiDefaultButton(buttonId++, guiLeft + 7, guiTop + 6, 18, 9, GUI_ELEMENTS, 88, 36, 0,9, I18n.format("gui.back.name"))); // Back Button

		disableAllButtonId = buttonId;
		this.addButton(new GuiDefaultButton(buttonId++, guiLeft + 153, guiTop + 18, 15, 15, GUI_ELEMENTS, 154, 0, 0,15, I18n.format("gui.set_all_disabled.name"))); // Disable All Button

		defaultAllButtonId = buttonId;
		this.addButton(new GuiDefaultButton(buttonId++, guiLeft + 153, guiTop + 34, 15, 15, GUI_ELEMENTS, 169, 0, 0,15, I18n.format("gui.set_all_default.name"))); // Default All Button

		String description = TextFormatting.AQUA + I18n.format("gui.shift_left_click") + TextFormatting.GRAY + I18n.format("gui.shift_left_click_desc")  + "\n" + TextFormatting.AQUA + I18n.format("gui.shift_right_click") + TextFormatting.GRAY + I18n.format("gui.shift_right_click_desc");
		GuiButton descButton = new GuiDefaultButton(buttonId, guiLeft + 163, guiTop + 6, 6, 8, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 0, 41, 0,8, description);
		descButton.enabled = false;
		this.addButton(descButton);
	}

	@Override
	public void actionPerformed(GuiButton guiButton)
	{
		if (buttonToSlot.containsKey(guiButton.id))
		{
			tileEntity.setSlotEditedId(buttonToSlot.get(guiButton.id));
			NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.MACHINE_SLOT_CONFIGURATION.ordinal(), tileEntity.getPos());
			return;
		}

		if (guiButton.id == backButtonId)
		{
			if (tileEntity instanceof TileEntityAlloyFurnace) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), this.tileEntity.getPos());
			else if (tileEntity instanceof TileEntityPulverizer) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.PULVERIZER.ordinal(), this.tileEntity.getPos());
			else if (tileEntity instanceof TileEntityMetalPress) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.METAL_PRESS.ordinal(), this.tileEntity.getPos());
			else if (tileEntity instanceof TileEntityElectricFurnace) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ELECTRIC_FURNACE.ordinal(), this.tileEntity.getPos());
            else if (tileEntity instanceof TileEntitySolidFuelGenerator) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.SOLID_FUEL_GENERATOR.ordinal(), this.tileEntity.getPos());
            else if (tileEntity instanceof TileEntityLiquidFuelGenerator) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.LIQUID_FUEL_GENERATOR.ordinal(), this.tileEntity.getPos());
            else if (tileEntity instanceof TileEntityFluidTank) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.FLUID_TANK.ordinal(), this.tileEntity.getPos());
            else if (tileEntity instanceof TileEntityEnergyStorage) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ENERGY_STORAGE.ordinal(), this.tileEntity.getPos());
			else if (tileEntity instanceof TileEntitySolarPanel) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.SOLAR_PANEL.ordinal(), this.tileEntity.getPos());
			return;
		}

		if (guiButton.id == disableAllButtonId)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler handler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				for (int slot = 0; slot < Objects.requireNonNull(handler).getStateCount(); slot++) handler.setSlotMode(slot, SlotState.SlotMode.DISABLED);
			}
		}

		if (guiButton.id == defaultAllButtonId)
		{
			for (EnumFacing face : EnumFacing.values())
			{
				SidedIngredientHandler handler = (SidedIngredientHandler) tileEntity.getSidedIngredientHandler(face);
				for (int slot = 0; slot < Objects.requireNonNull(handler).getStateCount(); slot++) handler.setSlotMode(slot, handler.getDefaultMode(slot));
			}
		}

		this.initGui();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException { super.mouseClicked(mouseX, mouseY, mouseButton); }

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
				if (guiButtonTooltip != null) GuiUtils.drawHoveringText(guiButtonTooltip, mouseX, mouseY, this.width, this.height, this.width/2, this.fontRenderer);
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
        ResourceLocation MACHINE_CONFIGURATION = new ResourceLocation(Tags.MODID, "textures/gui/machine_configuration_" + this.inputs + "_" + this.outputs + ".png");
		if (tileEntity instanceof TileEntityLiquidFuelGenerator) MACHINE_CONFIGURATION = new ResourceLocation(Tags.MODID, "textures/gui/machine_configuration_2_0.png");
        if (tileEntity instanceof TileEntityFluidTank) MACHINE_CONFIGURATION = new ResourceLocation(Tags.MODID, "textures/gui/machine_configuration_3_2.png");
        if (tileEntity instanceof TileEntityEnergyStorage) MACHINE_CONFIGURATION = new ResourceLocation(Tags.MODID, "textures/gui/machine_configuration_energy_storage.png");
		if (tileEntity instanceof TileEntityMetalPress) MACHINE_CONFIGURATION = new ResourceLocation(Tags.MODID, "textures/gui/machine_configuration_" + this.inputs + "_" + this.outputs + "_2" + ".png");

		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(MACHINE_CONFIGURATION);
		int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        if (tileEntity instanceof TileEntityFluidTank)
        {
            drawRect(i + 7, j + 18, i + 25, j + 70, 0xFFC6C6C6);
            GlStateManager.color(1, 1, 1, 1);
        }

		this.mc.getTextureManager().bindTexture(GUI_ELEMENTS);
		int l = this.getCookProgressScaled();
		int xOffset = inputs > 1 ? 0 : 10;
		int textureY = 0;
		if (tileEntity instanceof TileEntityAlloyFurnace) textureY = 69;
		else if (tileEntity instanceof TileEntityMetalPress) textureY = 86;
		if (!(tileEntity instanceof TileEntitySolidFuelGenerator) && !(tileEntity instanceof TileEntityLiquidFuelGenerator) && !(tileEntity instanceof TileEntityFluidTank) && !(tileEntity instanceof TileEntityEnergyStorage)) this.drawTexturedModalRect(this.guiLeft + 78 - xOffset, this.guiTop + 35, 232, textureY, l + 1, 16);
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

			if (tileEntity instanceof TileEntityAlloyFurnace) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE.ordinal(), this.tileEntity.getPos());
			if (tileEntity instanceof TileEntityPulverizer) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.PULVERIZER.ordinal(), this.tileEntity.getPos());
			else if (tileEntity instanceof TileEntityMetalPress) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.METAL_PRESS.ordinal(), this.tileEntity.getPos());
			else if (tileEntity instanceof TileEntityElectricFurnace) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ELECTRIC_FURNACE.ordinal(), this.tileEntity.getPos());
            else if (tileEntity instanceof TileEntitySolidFuelGenerator) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.SOLID_FUEL_GENERATOR.ordinal(), this.tileEntity.getPos());
            else if (tileEntity instanceof TileEntityLiquidFuelGenerator) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.LIQUID_FUEL_GENERATOR.ordinal(), this.tileEntity.getPos());
            else if (tileEntity instanceof TileEntityFluidTank) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.FLUID_TANK.ordinal(), this.tileEntity.getPos());
            else if (tileEntity instanceof TileEntityEnergyStorage) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ENERGY_STORAGE.ordinal(), this.tileEntity.getPos());
			else if (tileEntity instanceof TileEntitySolarPanel) NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.SOLAR_PANEL.ordinal(), this.tileEntity.getPos());
		}
	}
}