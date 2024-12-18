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
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.objects.containers.ContainerInventory;
import net.vyroxes.minersprosperity.objects.tileentities.TileEntityCrusher;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class GuiCrusherSlotConfiguration extends GuiContainer
{
	private static final ResourceLocation CRUSHER_SLOT_CONFIGURATION_TEXTURE = new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png");
	private final TileEntityCrusher tileEntity;
    private int textureX;

	public GuiCrusherSlotConfiguration(InventoryPlayer player, TileEntityCrusher tileEntity)
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

        int[] input1State = this.tileEntity.input1State;
        int[] input2State = this.tileEntity.input2State;
        int[] fuelState = this.tileEntity.fuelState;
        int[] outputState = this.tileEntity.outputState;
        String slot = this.tileEntity.slot;

        switch (slot) {
            case "Input 1" -> {
                if (input1State[0] == 0) {
                    textureX = 176;
                } else if (input1State[0] == 1) {
                    textureX = 192;
                } else if (input1State[0] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(0, guiLeft + 80, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Front", "Input 1", input1State[0]));

                if (input1State[1] == 0) {
                    textureX = 176;
                } else if (input1State[1] == 1) {
                    textureX = 192;
                } else if (input1State[1] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(1, guiLeft + 98, guiTop + 53, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Back", "Input 1", input1State[1]));

                if (input1State[2] == 0) {
                    textureX = 176;
                } else if (input1State[2] == 1) {
                    textureX = 192;
                } else if (input1State[2] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(2, guiLeft + 62, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Left", "Input 1", input1State[2]));

                if (input1State[3] == 0) {
                    textureX = 176;
                } else if (input1State[3] == 1) {
                    textureX = 192;
                } else if (input1State[3] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(3, guiLeft + 98, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Right", "Input 1", input1State[3]));

                if (input1State[4] == 0) {
                    textureX = 176;
                } else if (input1State[4] == 1) {
                    textureX = 192;
                } else if (input1State[4] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(4, guiLeft + 80, guiTop + 17, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Top", "Input 1", input1State[4]));

                if (input1State[5] == 0) {
                    textureX = 176;
                } else if (input1State[5] == 1) {
                    textureX = 192;
                } else if (input1State[5] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(5, guiLeft + 80, guiTop + 53, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Bottom", "Input 1", input1State[5]));
            }
            case "Input 2" -> {
                if (input2State[0] == 0) {
                    textureX = 176;
                } else if (input2State[0] == 1) {
                    textureX = 192;
                } else if (input2State[0] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(0, guiLeft + 80, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Front", "Input 2", input2State[0]));

                if (input2State[1] == 0) {
                    textureX = 176;
                } else if (input2State[1] == 1) {
                    textureX = 192;
                } else if (input2State[1] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(1, guiLeft + 98, guiTop + 53, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Back", "Input 2", input2State[1]));

                if (input2State[2] == 0) {
                    textureX = 176;
                } else if (input2State[2] == 1) {
                    textureX = 192;
                } else if (input2State[2] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(2, guiLeft + 62, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Left", "Input 2", input2State[2]));

                if (input2State[3] == 0) {
                    textureX = 176;
                } else if (input2State[3] == 1) {
                    textureX = 192;
                } else if (input2State[3] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(3, guiLeft + 98, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Right", "Input 2", input2State[3]));

                if (input2State[4] == 0) {
                    textureX = 176;
                } else if (input2State[4] == 1) {
                    textureX = 192;
                } else if (input2State[4] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(4, guiLeft + 80, guiTop + 17, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Top", "Input 2", input2State[4]));

                if (input2State[5] == 0) {
                    textureX = 176;
                } else if (input2State[5] == 1) {
                    textureX = 192;
                } else if (input2State[5] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(5, guiLeft + 80, guiTop + 53, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Bottom", "Input 2", input2State[5]));
            }
            case "Fuel" -> {
                if (fuelState[0] == 0) {
                    textureX = 176;
                } else if (fuelState[0] == 1) {
                    textureX = 192;
                } else if (fuelState[0] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(0, guiLeft + 80, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Front", "Fuel", fuelState[0]));

                if (fuelState[1] == 0) {
                    textureX = 176;
                } else if (fuelState[1] == 1) {
                    textureX = 192;
                } else if (fuelState[1] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(1, guiLeft + 98, guiTop + 53, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Back", "Fuel", fuelState[1]));

                if (fuelState[2] == 0) {
                    textureX = 176;
                } else if (fuelState[2] == 1) {
                    textureX = 192;
                } else if (fuelState[2] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(2, guiLeft + 62, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Left", "Fuel", fuelState[2]));

                if (fuelState[3] == 0) {
                    textureX = 176;
                } else if (fuelState[3] == 1) {
                    textureX = 192;
                } else if (fuelState[3] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(3, guiLeft + 98, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Right", "Fuel", fuelState[3]));

                if (fuelState[4] == 0) {
                    textureX = 176;
                } else if (fuelState[4] == 1) {
                    textureX = 192;
                } else if (fuelState[4] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(4, guiLeft + 80, guiTop + 17, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Top", "Fuel", fuelState[4]));

                if (fuelState[5] == 0) {
                    textureX = 176;
                } else if (fuelState[5] == 1) {
                    textureX = 192;
                } else if (fuelState[5] == 2) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(5, guiLeft + 80, guiTop + 53, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Bottom", "Fuel", fuelState[5]));
            }
            case "Output" -> {
                if (outputState[0] == 0) {
                    textureX = 176;
                } else if (outputState[0] == 1) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(0, guiLeft + 80, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Front", "Output", outputState[0]));

                if (outputState[1] == 0) {
                    textureX = 176;
                } else if (outputState[1] == 1) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(1, guiLeft + 98, guiTop + 53, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Back", "Output", outputState[1]));

                if (outputState[2] == 0) {
                    textureX = 176;
                } else if (outputState[2] == 1) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(2, guiLeft + 62, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Left", "Output", outputState[2]));

                if (outputState[3] == 0) {
                    textureX = 176;
                } else if (outputState[3] == 1) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(3, guiLeft + 98, guiTop + 35, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Right", "Output", outputState[3]));

                if (outputState[4] == 0) {
                    textureX = 176;
                } else if (outputState[4] == 1) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(4, guiLeft + 80, guiTop + 17, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Top", "Output", outputState[4]));

                if (outputState[5] == 0) {
                    textureX = 176;
                } else if (outputState[5] == 1) {
                    textureX = 208;
                }
                this.addButton(new GuiFaceButton(5, guiLeft + 80, guiTop + 53, 16, 16, new ResourceLocation("minersprosperity", "textures/gui/crusher_slot_configuration.png"), textureX, 0, "Bottom", "Output", outputState[5]));
            }
        }

		this.addButton(new GuiBackButton(6, guiLeft + 7, guiTop + 6, 18, 9, new ResourceLocation("minersprosperity", "textures/gui/crusher_slots_configuration.png"), 238, 0, "Back"));
	}

	@Override
	public void actionPerformed(@NotNull GuiButton guiButton)
	{
        switch (this.tileEntity.slot) {
            case "Input 1" -> {
                if (guiButton.id == 0) {
                    if (this.tileEntity.input1State[0] < 2) {
                        ++this.tileEntity.input1State[0];
                    } else {
                        this.tileEntity.input1State[0] = 0;
                    }
                } else if (guiButton.id == 1) {
                    if (this.tileEntity.input1State[1] < 2) {
                        ++this.tileEntity.input1State[1];
                    } else {
                        this.tileEntity.input1State[1] = 0;
                    }
                } else if (guiButton.id == 2) {
                    if (this.tileEntity.input1State[2] < 2) {
                        ++this.tileEntity.input1State[2];
                    } else {
                        this.tileEntity.input1State[2] = 0;
                    }
                } else if (guiButton.id == 3) {
                    if (this.tileEntity.input1State[3] < 2) {
                        ++this.tileEntity.input1State[3];
                    } else {
                        this.tileEntity.input1State[3] = 0;
                    }
                } else if (guiButton.id == 4) {
                    if (this.tileEntity.input1State[4] < 2) {
                        ++this.tileEntity.input1State[4];
                    } else {
                        this.tileEntity.input1State[4] = 0;
                    }
                } else if (guiButton.id == 5) {
                    if (this.tileEntity.input1State[5] < 2) {
                        ++this.tileEntity.input1State[5];
                    } else {
                        this.tileEntity.input1State[5] = 0;
                    }
                }
            }
            case "Input 2" -> {
                if (guiButton.id == 0) {
                    if (this.tileEntity.input2State[0] < 2) {
                        ++this.tileEntity.input2State[0];
                    } else {
                        this.tileEntity.input2State[0] = 0;
                    }
                } else if (guiButton.id == 1) {
                    if (this.tileEntity.input2State[1] < 2) {
                        ++this.tileEntity.input2State[1];
                    } else {
                        this.tileEntity.input2State[1] = 0;
                    }
                } else if (guiButton.id == 2) {
                    if (this.tileEntity.input2State[2] < 2) {
                        ++this.tileEntity.input2State[2];
                    } else {
                        this.tileEntity.input2State[2] = 0;
                    }
                } else if (guiButton.id == 3) {
                    if (this.tileEntity.input2State[3] < 2) {
                        ++this.tileEntity.input2State[3];
                    } else {
                        this.tileEntity.input2State[3] = 0;
                    }
                } else if (guiButton.id == 4) {
                    if (this.tileEntity.input2State[4] < 2) {
                        ++this.tileEntity.input2State[4];
                    } else {
                        this.tileEntity.input2State[4] = 0;
                    }
                } else if (guiButton.id == 5) {
                    if (this.tileEntity.input2State[5] < 2) {
                        ++this.tileEntity.input2State[5];
                    } else {
                        this.tileEntity.input2State[5] = 0;
                    }
                }
            }
            case "Fuel" -> {
                if (guiButton.id == 0) {
                    if (this.tileEntity.fuelState[0] < 2) {
                        ++this.tileEntity.fuelState[0];
                    } else {
                        this.tileEntity.fuelState[0] = 0;
                    }
                } else if (guiButton.id == 1) {
                    if (this.tileEntity.fuelState[1] < 2) {
                        ++this.tileEntity.fuelState[1];
                    } else {
                        this.tileEntity.fuelState[1] = 0;
                    }
                } else if (guiButton.id == 2) {
                    if (this.tileEntity.fuelState[2] < 2) {
                        ++this.tileEntity.fuelState[2];
                    } else {
                        this.tileEntity.fuelState[2] = 0;
                    }
                } else if (guiButton.id == 3) {
                    if (this.tileEntity.fuelState[3] < 2) {
                        ++this.tileEntity.fuelState[3];
                    } else {
                        this.tileEntity.fuelState[3] = 0;
                    }
                } else if (guiButton.id == 4) {
                    if (this.tileEntity.fuelState[4] < 2) {
                        ++this.tileEntity.fuelState[4];
                    } else {
                        this.tileEntity.fuelState[4] = 0;
                    }
                } else if (guiButton.id == 5) {
                    if (this.tileEntity.fuelState[5] < 2) {
                        ++this.tileEntity.fuelState[5];
                    } else {
                        this.tileEntity.fuelState[5] = 0;
                    }
                }
            }
            case "Output" -> {
                if (guiButton.id == 0) {
                    if (this.tileEntity.outputState[0] < 1) {
                        ++this.tileEntity.outputState[0];
                    } else {
                        this.tileEntity.outputState[0] = 0;
                    }
                } else if (guiButton.id == 1) {
                    if (this.tileEntity.outputState[1] < 1) {
                        ++this.tileEntity.outputState[1];
                    } else {
                        this.tileEntity.outputState[1] = 0;
                    }
                } else if (guiButton.id == 2) {
                    if (this.tileEntity.outputState[2] < 1) {
                        ++this.tileEntity.outputState[2];
                    } else {
                        this.tileEntity.outputState[2] = 0;
                    }
                } else if (guiButton.id == 3) {
                    if (this.tileEntity.outputState[3] < 1) {
                        ++this.tileEntity.outputState[3];
                    } else {
                        this.tileEntity.outputState[3] = 0;
                    }
                } else if (guiButton.id == 4) {
                    if (this.tileEntity.outputState[4] < 1) {
                        ++this.tileEntity.outputState[4];
                    } else {
                        this.tileEntity.outputState[4] = 0;
                    }
                } else if (guiButton.id == 5) {
                    if (this.tileEntity.outputState[5] < 1) {
                        ++this.tileEntity.outputState[5];
                    } else {
                        this.tileEntity.outputState[5] = 0;
                    }
                }
            }
        }
		this.tileEntity.setSlotsState();

		if (guiButton.id == 6)
		{
			//this.tileEntity.setCurrentGuiId(GuiHandler.GUI_CRUSHER_SLOTS_CONFIGURATION);
			this.mc.player.openGui(MinersProsperity.instance, GuiHandler.GUI_CRUSHER_SLOTS_CONFIGURATION, this.mc.world, this.tileEntity.getPos().getX(),  this.tileEntity.getPos().getY(),  this.tileEntity.getPos().getZ());
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
                    switch (this.tileEntity.slot) {
                        case "Input 1" -> {
                            if (guiButton.id == 0) {
                                if (this.tileEntity.input1State[0] > 0) {
                                    --this.tileEntity.input1State[0];
                                } else {
                                    this.tileEntity.input1State[0] = 2;
                                }
                            } else if (guiButton.id == 1) {
                                if (this.tileEntity.input1State[1] > 0) {
                                    --this.tileEntity.input1State[1];
                                } else {
                                    this.tileEntity.input1State[1] = 2;
                                }
                            } else if (guiButton.id == 2) {
                                if (this.tileEntity.input1State[2] > 0) {
                                    --this.tileEntity.input1State[2];
                                } else {
                                    this.tileEntity.input1State[2] = 2;
                                }
                            } else if (guiButton.id == 3) {
                                if (this.tileEntity.input1State[3] > 0) {
                                    --this.tileEntity.input1State[3];
                                } else {
                                    this.tileEntity.input1State[3] = 2;
                                }
                            } else if (guiButton.id == 4) {
                                if (this.tileEntity.input1State[4] > 0) {
                                    --this.tileEntity.input1State[4];
                                } else {
                                    this.tileEntity.input1State[4] = 2;
                                }
                            } else if (guiButton.id == 5) {
                                if (this.tileEntity.input1State[5] > 0) {
                                    --this.tileEntity.input1State[5];
                                } else {
                                    this.tileEntity.input1State[5] = 2;
                                }
                            }
                        }
                        case "Input 2" -> {
                            if (guiButton.id == 0) {
                                if (this.tileEntity.input2State[0] > 0) {
                                    --this.tileEntity.input2State[0];
                                } else {
                                    this.tileEntity.input2State[0] = 2;
                                }
                            } else if (guiButton.id == 1) {
                                if (this.tileEntity.input2State[1] > 0) {
                                    --this.tileEntity.input2State[1];
                                } else {
                                    this.tileEntity.input2State[1] = 2;
                                }
                            } else if (guiButton.id == 2) {
                                if (this.tileEntity.input2State[2] > 0) {
                                    --this.tileEntity.input2State[2];
                                } else {
                                    this.tileEntity.input2State[2] = 2;
                                }
                            } else if (guiButton.id == 3) {
                                if (this.tileEntity.input2State[3] > 0) {
                                    --this.tileEntity.input2State[3];
                                } else {
                                    this.tileEntity.input2State[3] = 2;
                                }
                            } else if (guiButton.id == 4) {
                                if (this.tileEntity.input2State[4] > 0) {
                                    --this.tileEntity.input2State[4];
                                } else {
                                    this.tileEntity.input2State[4] = 2;
                                }
                            } else if (guiButton.id == 5) {
                                if (this.tileEntity.input2State[5] > 0) {
                                    --this.tileEntity.input2State[5];
                                } else {
                                    this.tileEntity.input2State[5] = 2;
                                }
                            }
                        }
                        case "Fuel" -> {
                            if (guiButton.id == 0) {
                                if (this.tileEntity.fuelState[0] > 0) {
                                    --this.tileEntity.fuelState[0];
                                } else {
                                    this.tileEntity.fuelState[0] = 2;
                                }
                            } else if (guiButton.id == 1) {
                                if (this.tileEntity.fuelState[1] > 0) {
                                    --this.tileEntity.fuelState[1];
                                } else {
                                    this.tileEntity.fuelState[1] = 2;
                                }
                            } else if (guiButton.id == 2) {
                                if (this.tileEntity.fuelState[2] > 0) {
                                    --this.tileEntity.fuelState[2];
                                } else {
                                    this.tileEntity.fuelState[2] = 2;
                                }
                            } else if (guiButton.id == 3) {
                                if (this.tileEntity.fuelState[3] > 0) {
                                    --this.tileEntity.fuelState[3];
                                } else {
                                    this.tileEntity.fuelState[3] = 2;
                                }
                            } else if (guiButton.id == 4) {
                                if (this.tileEntity.fuelState[4] > 0) {
                                    --this.tileEntity.fuelState[4];
                                } else {
                                    this.tileEntity.fuelState[4] = 2;
                                }
                            } else if (guiButton.id == 5) {
                                if (this.tileEntity.fuelState[5] > 0) {
                                    --this.tileEntity.fuelState[5];
                                } else {
                                    this.tileEntity.fuelState[5] = 2;
                                }
                            }
                        }
                        case "Output" -> {
                            if (guiButton.id == 0) {
                                if (this.tileEntity.outputState[0] > 0) {
                                    --this.tileEntity.outputState[0];
                                } else {
                                    this.tileEntity.outputState[0] = 1;
                                }
                            } else if (guiButton.id == 1) {
                                if (this.tileEntity.outputState[1] > 0) {
                                    --this.tileEntity.outputState[1];
                                } else {
                                    this.tileEntity.outputState[1] = 1;
                                }
                            } else if (guiButton.id == 2) {
                                if (this.tileEntity.outputState[2] > 0) {
                                    --this.tileEntity.outputState[2];
                                } else {
                                    this.tileEntity.outputState[2] = 1;
                                }
                            } else if (guiButton.id == 3) {
                                if (this.tileEntity.outputState[3] > 0) {
                                    --this.tileEntity.outputState[3];
                                } else {
                                    this.tileEntity.outputState[3] = 1;
                                }
                            } else if (guiButton.id == 4) {
                                if (this.tileEntity.outputState[4] > 0) {
                                    --this.tileEntity.outputState[4];
                                } else {
                                    this.tileEntity.outputState[4] = 1;
                                }
                            } else if (guiButton.id == 5) {
                                if (this.tileEntity.outputState[5] > 0) {
                                    --this.tileEntity.outputState[5];
                                } else {
                                    this.tileEntity.outputState[5] = 1;
                                }
                            }
                        }
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
                    switch (this.tileEntity.slot) {
                        case "Input 1" -> {
                            if (guiButton.id == 0) {
                                this.tileEntity.input1State[0] = 0;
                            } else if (guiButton.id == 1) {
                                this.tileEntity.input1State[1] = 0;
                            } else if (guiButton.id == 2) {
                                this.tileEntity.input1State[2] = 0;
                            } else if (guiButton.id == 3) {
                                this.tileEntity.input1State[3] = 0;
                            } else if (guiButton.id == 4) {
                                this.tileEntity.input1State[4] = 0;
                            } else if (guiButton.id == 5) {
                                this.tileEntity.input1State[5] = 0;
                            }
                        }
                        case "Input 2" -> {
                            if (guiButton.id == 0) {
                                this.tileEntity.input2State[0] = 0;
                            } else if (guiButton.id == 1) {
                                this.tileEntity.input2State[1] = 0;
                            } else if (guiButton.id == 2) {
                                this.tileEntity.input2State[2] = 0;
                            } else if (guiButton.id == 3) {
                                this.tileEntity.input2State[3] = 0;
                            } else if (guiButton.id == 4) {
                                this.tileEntity.input2State[4] = 0;
                            } else if (guiButton.id == 5) {
                                this.tileEntity.input2State[5] = 0;
                            }
                        }
                        case "Fuel" -> {
                            if (guiButton.id == 0) {
                                this.tileEntity.fuelState[0] = 0;
                            } else if (guiButton.id == 1) {
                                this.tileEntity.fuelState[1] = 0;
                            } else if (guiButton.id == 2) {
                                this.tileEntity.fuelState[2] = 0;
                            } else if (guiButton.id == 3) {
                                this.tileEntity.fuelState[3] = 0;
                            } else if (guiButton.id == 4) {
                                this.tileEntity.fuelState[4] = 0;
                            } else if (guiButton.id == 5) {
                                this.tileEntity.fuelState[5] = 0;
                            }
                        }
                        case "Output" -> {
                            if (guiButton.id == 0) {
                                this.tileEntity.outputState[0] = 0;
                            } else if (guiButton.id == 1) {
                                this.tileEntity.outputState[1] = 0;
                            } else if (guiButton.id == 2) {
                                this.tileEntity.outputState[2] = 0;
                            } else if (guiButton.id == 3) {
                                this.tileEntity.outputState[3] = 0;
                            } else if (guiButton.id == 4) {
                                this.tileEntity.outputState[4] = 0;
                            } else if (guiButton.id == 5) {
                                this.tileEntity.outputState[5] = 0;
                            }
                        }
                    }

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
		this.mc.getTextureManager().bindTexture(CRUSHER_SLOT_CONFIGURATION_TEXTURE);
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
			//this.tileEntity.setCurrentGuiId(GuiHandler.GUI_CRUSHER_SLOTS_CONFIGURATION);
			this.mc.player.openGui(MinersProsperity.instance, GuiHandler.GUI_CRUSHER_SLOTS_CONFIGURATION, this.mc.world, this.tileEntity.getPos().getX(),  this.tileEntity.getPos().getY(),  this.tileEntity.getPos().getZ());
		}
	}
}