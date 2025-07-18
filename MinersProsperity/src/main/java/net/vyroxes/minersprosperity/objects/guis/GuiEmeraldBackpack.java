package net.vyroxes.minersprosperity.objects.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.objects.containers.ContainerEmeraldBackpack;

public class GuiEmeraldBackpack extends GuiContainer
{
    private static final ResourceLocation EMERALD_BACKPACK_TEXTURE = new ResourceLocation(Tags.MODID, "textures/gui/emerald_backpack.png");
    private final int backpackRows;

    public GuiEmeraldBackpack(ContainerEmeraldBackpack container)
    {
        super(container);
        this.backpackRows = container.getRows();
        this.xSize = 176;
        this.ySize = 221;
        
        GuiEmeraldBackpack.playBackpackOpenSound();
    }

    public static void playBackpackOpenSound()
    {
    	BlockPos playerPos = Minecraft.getMinecraft().player.getPosition();
    	
    	PositionedSoundRecord sound = new PositionedSoundRecord(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.0F, playerPos);
    
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
    }
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String title = this.mc.player.getHeldItemMainhand().getDisplayName();
        this.fontRenderer.drawString(title, 8, 5, 4210752);
        int inventoryLabelY = 19 + this.backpackRows * 18;
        this.fontRenderer.drawString(I18n.format("key.categories.inventory"), 8, inventoryLabelY, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {   	 
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(EMERALD_BACKPACK_TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop - 1, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected boolean checkHotbarKeys(int keyCode)
    {
        return true;
    }
}