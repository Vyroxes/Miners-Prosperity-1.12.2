package net.vyroxes.minersprosperity.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.vyroxes.minersprosperity.integration.baubles.BaublesCompat;
import org.jetbrains.annotations.NotNull;

public class ItemTotemOfUndying extends Item
{
    public ItemTotemOfUndying() {
        setTranslationKey("totem");
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.COMBAT);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn)
    {
        ItemStack itemStack = playerIn.getHeldItem(handIn);

        if (Loader.isModLoaded("baubles"))
        {
            BaublesCompat.tryEquipBauble(playerIn, itemStack, worldIn.isRemote);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }
}