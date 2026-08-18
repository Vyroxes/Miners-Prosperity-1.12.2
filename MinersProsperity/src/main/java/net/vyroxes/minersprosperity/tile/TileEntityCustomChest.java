package net.vyroxes.minersprosperity.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.block.chest.ChestTier;
import org.jetbrains.annotations.NotNull;

public class TileEntityCustomChest extends TileEntity implements ITickable {
    private ChestTier tier;
    private ItemStackHandler handler;

    private String customName;

    public boolean isUpgrading = false;
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;

    public TileEntityCustomChest(ChestTier tier) {
        this.tier = tier;
        initHandler();
    }

    public TileEntityCustomChest() {
        this.tier = ChestTier.LEAD;
        initHandler();
    }

    private void initHandler() {
        this.handler = new ItemStackHandler(tier.inventorySize) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
    }

    public ChestTier getTier() {
        return tier;
    }

    public ItemStackHandler getHandler() {
        return handler;
    }

    public ResourceLocation getGuiTexture() {
        return new ResourceLocation(Tags.MODID, "textures/gui/chest_" + tier.inventorySize + ".png");
    }

    public ResourceLocation getGuiTexture2() {
        return new ResourceLocation(Tags.MODID, "textures/gui/chest_" + tier.inventorySize + "_2.png");
    }

    public boolean hasCustomName() {
        return customName != null && !customName.isEmpty();
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String name) {
        this.customName = name;
        markDirty();
    }

    public void openInventory(EntityPlayer player) {
        if (this.world == null || this.world.isRemote) return;

        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) this.numPlayersUsing = 0;
            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.world.getBlockState(this.pos).getBlock(), 1, this.numPlayersUsing);
            this.markDirty();
        }
    }

    public void closeInventory(EntityPlayer player) {
        if (this.world == null || this.world.isRemote) return;

        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            if (this.numPlayersUsing < 0) this.numPlayersUsing = 0;
            this.world.addBlockEvent(this.pos, this.world.getBlockState(this.pos).getBlock(), 1, this.numPlayersUsing);
            this.markDirty();
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            if (this.world != null) {
                this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
            }
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public void update() {
        if (this.world == null) return;

        this.ticksSinceSync++;

        if (this.world.isRemote) {
            this.prevLidAngle = this.lidAngle;

            float delta = 0.1F;

            if (this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
                this.lidAngle += delta;
            }
            if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F) {
                this.lidAngle -= delta;
            }

            if (this.lidAngle < 0.0F) this.lidAngle = 0.0F;
            if (this.lidAngle > 1.0F) this.lidAngle = 1.0F;

            return;
        }

        this.prevLidAngle = this.lidAngle;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            this.world.playSound(null, this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        float delta = 0.1F;
        if (this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            this.lidAngle += delta;
        } else if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F) {
            this.lidAngle -= delta;
        }

        if (this.lidAngle < 0.0F) this.lidAngle = 0.0F;
        if (this.lidAngle > 1.0F) this.lidAngle = 1.0F;

        if (this.numPlayersUsing == 0 && this.prevLidAngle >= 0.5F && this.lidAngle < 0.5F) {
            this.world.playSound(null, this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }

    public void dropInventory(World world, BlockPos pos) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                net.minecraft.inventory.InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setString("Tier", tier.name());
        compound.setTag("Items", handler.serializeNBT());
        if (hasCustomName()) compound.setString("CustomName", customName);
        return compound;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey("Tier")) {
            this.tier = ChestTier.valueOf(compound.getString("Tier"));
            initHandler();
        }
        if (compound.hasKey("Items")) {
            handler.deserializeNBT(compound.getCompoundTag("Items"));
        }
        if (compound.hasKey("CustomName")) {
            customName = compound.getString("CustomName");
        }
    }
}