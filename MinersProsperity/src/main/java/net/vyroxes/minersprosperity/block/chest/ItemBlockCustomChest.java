package net.vyroxes.minersprosperity.block.chest;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.vyroxes.minersprosperity.client.render.ItemRenderCustomChest;
import java.util.Objects;

public class ItemBlockCustomChest extends ItemBlock {
    private final ChestTier tier;

    public ItemBlockCustomChest(Block block, ChestTier tier) {
        super(block);
        this.tier = tier;
        this.setRegistryName(Objects.requireNonNull(block.getRegistryName()));

        this.setTileEntityItemStackRenderer(new ItemRenderCustomChest());
    }

    public ChestTier getTier() {
        return tier;
    }
}