package net.vyroxes.minersprosperity.objects.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemFireExtinguisher extends ItemBase
{
    public ItemFireExtinguisher(String name)
    {
        super(name);
        setMaxStackSize(1);
        setMaxDamage(128);
        setNoRepair();
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack)
    {
        return 1;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public @NotNull EnumActionResult onItemUse(EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        int radius = 4;

        if (!worldIn.isRemote)
        {
            BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();

            for (int dx = -radius; dx <= radius; dx++)
            {
                for (int dy = -radius; dy <= radius; dy++)
                {
                    for (int dz = -radius; dz <= radius; dz++)
                    {
                        checkPos.setPos(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                        if (worldIn.getBlockState(checkPos).getBlock() == Blocks.FIRE)
                        {
                            worldIn.setBlockToAir(checkPos);
                        }
                    }
                }
            }

            AxisAlignedBB area = new AxisAlignedBB(
                    pos.add(-radius, -radius, -radius),
                    pos.add(radius + 1, radius + 1, radius + 1)
            );
            List<EntityLivingBase> entities = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, area);
            for (EntityLivingBase entity : entities)
            {
                if (entity.isBurning())
                {
                    entity.extinguish();
                }
            }

            stack.damageItem(1, player);
        }
        else
        {
            for (int i = 0; i < 80; i++)
            {
                double px = pos.getX() + (worldIn.rand.nextDouble() * 8.0D - 4.0D);
                double py = pos.getY() + (worldIn.rand.nextDouble() * 8.0D - 4.0D);
                double pz = pos.getZ() + (worldIn.rand.nextDouble() * 8.0D - 4.0D);
                double vx = worldIn.rand.nextGaussian() * 0.02D;
                double vy = worldIn.rand.nextGaussian() * 0.02D;
                double vz = worldIn.rand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, px, py, pz, vx, vy, vz);
            }

            player.swingArm(hand);
        }

        return EnumActionResult.SUCCESS;
    }
}