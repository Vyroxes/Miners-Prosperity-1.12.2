package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.Minecraft;
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
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class GuiAlloyFurnaceSlotConfiguration extends GuiContainer
{
	private static final ResourceLocation ALLOY_FURNACE_SLOT_CONFIGURATION_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/alloy_furnace_slot_configuration.png");
	private final TileEntityAlloyFurnace tileEntity;
    private int textureX;

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

        Map<String, int[]> slotStates = new HashMap<>();
        slotStates.put("Input 1", this.tileEntity.getInput1State());
        slotStates.put("Input 2", this.tileEntity.getInput2State());
        slotStates.put("Fuel", this.tileEntity.getEnergyState());
        slotStates.put("Output", this.tileEntity.getOutputState());

        Map<Integer, String> faceNames = new HashMap<>();
        faceNames.put(0, I18n.format("gui.side_front.name"));
        faceNames.put(1, I18n.format("gui.side_back.name"));
        faceNames.put(2, I18n.format("gui.side_left.name"));
        faceNames.put(3, I18n.format("gui.side_right.name"));
        faceNames.put(4, I18n.format("gui.side_top.name"));
        faceNames.put(5, I18n.format("gui.side_bottom.name"));

        Map<String, String> slotNames = new HashMap<>();
        slotNames.put("Input 1", "Input 1 Slot");
        slotNames.put("Input 2", "Input 2 Slot");
        slotNames.put("Fuel", "Fuel Slot");
        slotNames.put("Output", "Output Slot");

        String currentSlot = this.tileEntity.getSlot();
        int[] currentState = slotStates.getOrDefault(currentSlot, new int[6]);

        for (int i = 0; i < currentState.length; i++)
        {
            int textureX = switch (currentState[i])
            {
                case 1 -> 192;
                case 2 -> 208;
                default -> 176;
            };

            int xOffset = switch (i)
            {
                case 0, 4, 5 -> 80;
                case 1, 3 -> 98;
                case 2 -> 62;
                default -> 80;
            };

            int yOffset = switch (i)
            {
                case 4 -> 17;
                case 5 -> 53;
                case 0, 3 -> 35;
                case 1 -> 53;
                case 2 -> 35;
                default -> 35;
            };

            this.addButton(new GuiFaceButton(
                    i,
                    guiLeft + xOffset,
                    guiTop + yOffset,
                    16, 16,
                    new ResourceLocation(Reference.MODID, ALLOY_FURNACE_SLOT_CONFIGURATION_TEXTURE.getPath()),
                    textureX,
                    0,
                    faceNames.get(i),
                    slotNames.get(currentSlot),
                    currentState[i]
            ));
        }

		this.addButton(new GuiBackButton(6, guiLeft + 7, guiTop + 6, 18, 9, new ResourceLocation(Reference.MODID, "textures/gui/alloy_furnace_slots_configuration.png"), 238, 0, I18n.format("gui.back.name")));
	}

	@Override
	public void actionPerformed(@NotNull GuiButton guiButton)
	{
        int[] input1State = this.tileEntity.getInput1State();
        int[] input2State = this.tileEntity.getInput2State();
        int[] energyState = this.tileEntity.getEnergyState();
        int[] outputState = this.tileEntity.getOutputState();

        switch (this.tileEntity.getSlot())
        {
            case "Input 1" ->
            {
                if (guiButton.id >= 0 && guiButton.id < input1State.length)
                {
                    int newValue = (input1State[guiButton.id] + 1) % 3;
                    this.tileEntity.setInput1State(guiButton.id, newValue);
                }
            }
            case "Input 2" ->
            {
                if (guiButton.id >= 0 && guiButton.id < input2State.length)
                {
                    int newValue = (input2State[guiButton.id] + 1) % 3;
                    this.tileEntity.setInput2State(guiButton.id, newValue);
                }
            }
            case "Fuel" ->
            {
                if (guiButton.id >= 0 && guiButton.id < energyState.length)
                {
                    int newValue = (energyState[guiButton.id] + 1) % 3;
                    this.tileEntity.setEnergyState(guiButton.id, newValue);
                }
            }
            case "Output" ->
            {
                if (guiButton.id >= 0 && guiButton.id < outputState.length)
                {
                    int newValue = (outputState[guiButton.id] + 1) % 2;
                    this.tileEntity.setOutputState(guiButton.id, newValue);
                }
            }
        }
		this.tileEntity.setSlotsState();

		if (guiButton.id == 6)
		{
            NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.ALLOY_FURNACE_SLOTS_CONFIGURATION.ordinal(), this.tileEntity.getPos());
		}
		this.initGui();
	}

    private void adjustState(Supplier<int[]> getter, BiConsumer<Integer, Integer> setter, int index, int maxValue)
    {
        int[] state = getter.get();
        if (state[index] > 0)
        {
            setter.accept(index, state[index] - 1);
        }
        else
        {
            setter.accept(index, maxValue);
        }
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
                    switch (this.tileEntity.getSlot())
                    {
                        case "Input 1" -> adjustState(this.tileEntity::getInput1State, this.tileEntity::setInput1State, guiButton.id, 2);
                        case "Input 2" -> adjustState(this.tileEntity::getInput2State, this.tileEntity::setInput2State, guiButton.id, 2);
                        case "Fuel" -> adjustState(this.tileEntity::getEnergyState, this.tileEntity::setEnergyState, guiButton.id, 2);
                        case "Output" -> adjustState(this.tileEntity::getOutputState, this.tileEntity::setOutputState, guiButton.id, 1);
                    }

					this.tileEntity.setSlotsState();
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
				if (guiButton instanceof GuiFaceButton && ((GuiFaceButton) guiButton).isHovered(mouseX, mouseY))
				{
                    resetState(this.tileEntity.getSlot(), guiButton.id);
                    this.tileEntity.setSlotsState();
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

    private void resetState(String slot, int id)
    {
        switch (slot)
        {
            case "Input 1" -> this.tileEntity.setInput1State(id, 0);
            case "Input 2" -> this.tileEntity.setInput2State(id, 0);
            case "Fuel" -> this.tileEntity.setEnergyState(id, 0);
            case "Output" -> this.tileEntity.setOutputState(id, 0);
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