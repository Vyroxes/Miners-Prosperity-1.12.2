package net.vyroxes.minersprosperity.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.Tags;
import net.vyroxes.minersprosperity.entity.projectile.EntityRelocatorProjectile;
import net.vyroxes.minersprosperity.util.EntityRelocatorUtils;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public class ItemEntityRelocator extends ItemBase {
    private static final DecimalFormat DF = new DecimalFormat("0.##");

    public ItemEntityRelocator(String name)
    {
        super(name);
        this.setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation(Tags.MODID, "filled"), (stack, world, entity) -> EntityRelocatorUtils.hasEntity(stack) ? 1.0F : 0.0F);
    }

    @Override
    public @NotNull String getItemStackDisplayName(@NotNull ItemStack stack) {
        String name = super.getItemStackDisplayName(stack);
        if (!EntityRelocatorUtils.hasEntity(stack)) return name;

        String entityName = EntityRelocatorUtils.getStoredEntityName(stack);

        return entityName.isEmpty() ? name : name + " (" + entityName + ")";
    }

    @Override
    public boolean itemInteractionForEntity(@NotNull ItemStack stack, @NotNull EntityPlayer player, @NotNull EntityLivingBase target, @NotNull EnumHand hand)
    {
        if (EntityRelocatorUtils.hasEntity(stack) || !EntityRelocatorUtils.canCapture(target)) return false;

        if (!player.world.isRemote) EntityRelocatorUtils.capture(stack, target);

        player.swingArm(hand);

        return true;
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, EntityPlayer player, @NotNull EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (EntityRelocatorUtils.hasEntity(stack)) return new ActionResult<>(EnumActionResult.PASS, stack);

        player.swingArm(hand);

        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 0.5F, 0.8F + world.rand.nextFloat() * 0.2F);

        if (!world.isRemote)
        {
            ItemStack projectileStack = stack.copy();
            projectileStack.setCount(1);

            EntityRelocatorProjectile projectile = new EntityRelocatorProjectile(world, player, projectileStack);
            projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.spawnEntity(projectile);

            if (!player.capabilities.isCreativeMode) stack.shrink(1);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public @NotNull EnumActionResult onItemUse(EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (!EntityRelocatorUtils.hasEntity(stack)) return EnumActionResult.PASS;
        if (world.isRemote) return EnumActionResult.SUCCESS;

        BlockPos spawnPos = pos.offset(facing);

        double x = spawnPos.getX() + 0.5D;
        double y = spawnPos.getY();
        double z = spawnPos.getZ() + 0.5D;

        return EntityRelocatorUtils.release(stack, world, x, y, z) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
    }

    @Override
    public boolean hasEffect(@NotNull ItemStack stack)
    {
        return EntityRelocatorUtils.hasEntity(stack) || super.hasEffect(stack);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);

        if (!EntityRelocatorUtils.hasEntity(stack))
        {
            tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.entity_relocator.empty"));
            return;
        }

        String name = EntityRelocatorUtils.getStoredEntityName(stack);
        String id = EntityRelocatorUtils.getStoredEntityId(stack);
        float health = EntityRelocatorUtils.getStoredHealth(stack);
        float maxHealth = EntityRelocatorUtils.getStoredMaxHealth(stack);

        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.entity_relocator.entity") + ": " + TextFormatting.AQUA + name);
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.entity_relocator.health") + ": " + TextFormatting.RED + DF.format(health) + TextFormatting.GRAY + "/" + TextFormatting.RED + DF.format(maxHealth));
        if (flag.isAdvanced()) tooltip.add(TextFormatting.DARK_GRAY + id);
    }
}