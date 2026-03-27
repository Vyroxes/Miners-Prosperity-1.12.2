package net.vyroxes.minersprosperity.objects.items;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.objects.enums.EBackpackTier;

public class ItemBackpack extends ItemBackpackBase
{
    private final EBackpackTier tier;

    public ItemBackpack(String name, EBackpackTier tier)
    {
        super(name);
        this.addPropertyOverride(new ResourceLocation("minersprosperity", "open_" + tier.translationKey), ((stack, worldIn, entityIn) -> {
            if (stack.hasTagCompound())
            {
                NBTTagCompound data = stack.getSubCompound("BackpackData");
                if (data != null && data.getBoolean("IsOpen"))
                {
                    return 1.0f;
                }
            }
            return 0.0f;
        }));
        this.tier = tier;
    }

    @Override
    public int getInventorySize()
    {
        return tier.inventorySize;
    }

    @Override
    public int getInventoryColumns()
    {
        return tier.inventoryColumns;
    }

    @Override
    public int getConfigurationSize() {
        if (tier.inventorySize == 27)
            return 18;
        else
            return tier.inventorySize - 12;
    }

    @Override
    public ResourceLocation getTexture()
    {
        return new ResourceLocation(Tags.MODID, "textures/gui/backpack_" + tier.inventorySize + ".png");
    }

    @Override
    public ResourceLocation getTexture2()
    {
        return new ResourceLocation(Tags.MODID, "textures/gui/backpack_" + tier.inventorySize + "_2.png");
    }

    @Override
    public ResourceLocation getConfigurationTexture() {
        return new ResourceLocation(Tags.MODID, "textures/gui/backpack_configuration_" + tier.inventorySize + ".png");
    }

    @Override
    public ResourceLocation getConfigurationTexture2() {
        return new ResourceLocation(Tags.MODID, "textures/gui/backpack_configuration_" + tier.inventorySize + "_2.png");
    }

    @Override
    public EBackpackTier getTier()
    {
        return tier;
    }
}