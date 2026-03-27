package net.vyroxes.minersprosperity.objects.guis;

import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Loader;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.objects.containers.ContainerBackpackConfiguration;
import net.vyroxes.minersprosperity.objects.items.ItemBackpackBase;
import net.vyroxes.minersprosperity.util.handlers.GuiHandler;
import net.vyroxes.minersprosperity.util.handlers.NetworkHandler;

import java.io.IOException;
import java.util.List;

public class GuiBackpackConfiguration extends GuiContainer
{
    private static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Tags.MODID, "textures/gui/gui_elements.png");
    private final ItemStack backpackItemStack;
    private final ResourceLocation texture;
    private final ResourceLocation texture2;
    private final ContainerBackpackConfiguration container;
    private final int backpackRows;
    private final int backpackColumns;
    private final boolean fromBaubles;

    public GuiBackpackConfiguration(ContainerBackpackConfiguration container, ItemStack backpackItemStack, boolean fromBaubles, ResourceLocation texture, ResourceLocation texture2)
    {
        super(container);
        this.container = container;
        this.backpackItemStack = backpackItemStack;
        this.texture = texture;
        this.texture2 = texture2;
        this.backpackRows = container.getRows();
        this.backpackColumns = container.getColumns();

        int slotSize = 18;
        this.xSize = 8 + backpackColumns * slotSize + 8;
        this.ySize = 23 + 17 + backpackRows * slotSize + 97;
        this.fromBaubles = fromBaubles;
    }

    @Override
    public void initGui() {
        super.initGui();

        NetworkHandler.sendBackpackStateUpdate(true, "IsOpen", fromBaubles);

        int guiLeft = (this.width - this.xSize) / 2;
        int guiRight = guiLeft + this.xSize;
        int guiTop = (this.height - this.ySize) / 2;

        this.buttonList.clear();

        boolean autoCollectButtonState = this.container.getState("AutoCollect");
        boolean autoCollectModeButtonState = this.container.getState("AutoCollectMode");
        boolean whitelistOrBlacklistButtonState = this.container.getState("WhitelistOrBlacklist");
        boolean metadataButtonState = this.container.getState("Metadata");
        boolean nbtButtonState = this.container.getState("NBT");
        boolean damageButtonState = this.container.getState("Damage");
        boolean oreDictButtonState = this.container.getState("OreDict");
        boolean modIdButtonState = this.container.getState("ModID");

        int x = 57;
        int y = backpackRows > 3 ? 30 : 28;

        if (autoCollectButtonState)
        {
            this.addButton(new GuiDefaultButton(0, guiRight - x, guiTop + y, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 0, 104, 18, I18n.format("gui.backpack.auto-collect.on")));
        }
        else
        {
            this.addButton(new GuiDefaultButton(0, guiRight - x, guiTop + y, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 18, 104, 18, I18n.format("gui.backpack.auto-collect.off")));
        }

        if (autoCollectModeButtonState)
        {
            this.addButton(new GuiDefaultButton(1, guiRight - x + 24, guiTop + y, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 36, 104, 18, I18n.format("gui.backpack.auto-collect_mode.on")));
        }
        else
        {
            this.addButton(new GuiDefaultButton(1, guiRight - x + 24, guiTop + y, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 54, 104, 18, I18n.format("gui.backpack.auto-collect_mode.off")));
        }

        if (whitelistOrBlacklistButtonState)
        {
            this.addButton(new GuiDefaultButton(2, guiRight - x, guiTop + y + 19, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 72, 104, 18, I18n.format("gui.backpack.whitelist_or_blacklist.on")));
        }
        else
        {
            this.addButton(new GuiDefaultButton(2, guiRight - x, guiTop + y + 19, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 90, 104, 18, I18n.format("gui.backpack.whitelist_or_blacklist.off")));
        }

        if (metadataButtonState)
        {
            this.addButton(new GuiDefaultButton(3, guiRight - x + 24, guiTop + y + 19, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 108, 104, 18, I18n.format("gui.backpack.metadata.on")));
        }
        else
        {
            this.addButton(new GuiDefaultButton(3, guiRight - x + 24, guiTop + y + 19, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 126, 104, 18, I18n.format("gui.backpack.metadata.off")));
        }

        if (nbtButtonState)
        {
            this.addButton(new GuiDefaultButton(4, guiRight - x, guiTop + y + 38, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 144, 104, 18, I18n.format("gui.backpack.nbt.on")));
        }
        else
        {
            this.addButton(new GuiDefaultButton(4, guiRight - x, guiTop + y + 38, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 162, 104, 18, I18n.format("gui.backpack.nbt.off")));
        }

        if (damageButtonState)
        {
            this.addButton(new GuiDefaultButton(5, guiRight - x + 24, guiTop + y + 38, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 180, 104, 18, I18n.format("gui.backpack.damage.on")));
        }
        else
        {
            this.addButton(new GuiDefaultButton(5, guiRight - x + 24, guiTop + y + 38, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 198, 104, 18, I18n.format("gui.backpack.damage.off")));
        }

        if (oreDictButtonState)
        {
            this.addButton(new GuiDefaultButton(6, guiRight - x, guiTop + y + 57, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 216, 104, 18, I18n.format("gui.backpack.ore_dict.on")));
        }
        else
        {
            this.addButton(new GuiDefaultButton(6, guiRight - x, guiTop + y + 57, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 234, 104, 18, I18n.format("gui.backpack.ore_dict.off")));
        }

        if (modIdButtonState)
        {
            this.addButton(new GuiDefaultButton(7, guiRight - x + 24, guiTop + y + 57, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 106, 68, 18, I18n.format("gui.backpack.modid.on")));
        }
        else
        {
            this.addButton(new GuiDefaultButton(7, guiRight - x + 24, guiTop + y + 57, 18, 18, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 124, 68, 18, I18n.format("gui.backpack.modid.off")));
        }

        this.addButton(new GuiDefaultButton(8, guiLeft + 3, guiTop + 1, 26, 21, new ResourceLocation(Tags.MODID, GUI_ELEMENTS.getPath()), 0, 140, 21, I18n.format("gui.backpack.name")));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

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
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.id == 0)
        {
            this.container.setState("AutoCollect");
        }
        else if (guiButton.id == 1)
        {
            this.container.setState("AutoCollectMode");
        }
        else if (guiButton.id == 2)
        {
            this.container.setState("WhitelistOrBlacklist");
        }
        else if (guiButton.id == 3)
        {
            this.container.setState("Metadata");
        }
        else if (guiButton.id == 4)
        {
            this.container.setState("NBT");
        }
        else if (guiButton.id == 5)
        {
            this.container.setState("Damage");
        }
        else if (guiButton.id == 6)
        {
            this.container.setState("OreDict");
        }
        else if (guiButton.id == 7)
        {
            this.container.setState("ModID");
        }
        else if (guiButton.id == 8)
        {
            if (fromBaubles)
            {
                BlockPos playerPos = Minecraft.getMinecraft().player.getPosition();
                NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.BACKPACK.ordinal(), new BlockPos(playerPos.getX(), 1, playerPos.getZ()));
            }
            else
            {
                NetworkHandler.sendOpenGuiUpdate(GuiHandler.GuiTypes.BACKPACK.ordinal(), Minecraft.getMinecraft().player.getPosition());
            }
        }
        this.initGui();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String title = this.mc.player.getHeldItemMainhand().getDisplayName();
        if (fromBaubles && Loader.isModLoaded("baubles"))
        {
            if (BaublesApi.getBaublesHandler(mc.player) != null)
            {
                ItemStack bauble = BaublesApi.getBaublesHandler(mc.player).getStackInSlot(5);
                if (!bauble.isEmpty() && bauble.getItem() instanceof ItemBackpackBase)
                {
                    title = bauble.getDisplayName();
                }
            }
        }

        this.fontRenderer.drawString(title, 8, 23 + 5, 4210752);
        int inventoryLabelY = 23 + 19 + this.backpackRows * 18;
        int slotSize = 18;
        int textOffsetX = Math.max(0, (backpackColumns - 9) / 2) * slotSize;
        this.fontRenderer.drawString(I18n.format("key.categories.inventory"), 8 + textOffsetX, inventoryLabelY, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        if (this.backpackColumns > 13)
        {
            int width = (this.backpackColumns - 13) * 18 + 7;
            this.drawTexturedModalRect(this.guiLeft, this.guiTop - 1, 0, 0, 248, this.ySize);
            this.mc.getTextureManager().bindTexture(texture2);
            this.drawTexturedModalRect(this.guiLeft + 241, this.guiTop + 22, 0, 0, width, 221);
        }
        else
        {
            this.drawTexturedModalRect(this.guiLeft, this.guiTop - 1, 0, 0, this.xSize, this.ySize);
        }
    }

    @Override
    protected boolean checkHotbarKeys(int keyCode)
    {
        return true;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode() || keyCode == 1)
        {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.2f);
        }
    }
}