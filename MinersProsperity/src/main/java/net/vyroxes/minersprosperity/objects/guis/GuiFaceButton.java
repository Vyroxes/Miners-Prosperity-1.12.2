package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GuiFaceButton extends GuiButton
{
	private final ResourceLocation texture;
	private final int textureX;
	private final int textureY;
    private final String tooltip;
    private final String slot;
    private final int slotState;
    private List<String> currentTooltip = null;

	public GuiFaceButton(int buttonId, int x, int y, int width, int height, ResourceLocation texture, int textureX, int textureY, String tooltip, String slot, int slotState)
    {
        super(buttonId, x, y, width, height, "");
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
        this.tooltip = tooltip;
        this.slot = slot;
        this.slotState = slotState;
    }

	public List<String> getCurrentTooltip()
	{
	    return currentTooltip;
	}

    public boolean isHovered(int mouseX, int mouseY)
    {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) 
    {
    	if (this.visible) 
        {
            boolean isHovered = mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;

            int yOffset = isHovered ? 16 : 0;

            mc.getTextureManager().bindTexture(this.texture);
            this.drawTexturedModalRect(this.x, this.y, this.textureX, this.textureY + yOffset, this.width, this.height);

            if (isHovered)
            {
                currentTooltip = new ArrayList<>();
                if (this.slot.equals("Output"))
                {
                    if(this.slotState == 0)
                    {
                        currentTooltip.add(tooltip + ": Ignored");
                    }
                    else if (this.slotState == 1)
                    {
                        currentTooltip.add(tooltip + ": Output");
                    }
                }
                else
                {
                    if (this.slotState == 0)
                    {
                        currentTooltip.add(tooltip + ": Ignored");
                    }
                    else if (this.slotState == 1)
                    {
                        currentTooltip.add(tooltip + ": Input");
                    }
                    else if (this.slotState == 2)
                    {
                        currentTooltip.add(tooltip + ": Output");
                    }
                }

            }
            else
            {
                currentTooltip = null;
            }
        }
    }
}