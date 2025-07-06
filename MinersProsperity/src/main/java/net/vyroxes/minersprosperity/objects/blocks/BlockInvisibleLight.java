package net.vyroxes.minersprosperity.objects.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.init.EnchantmentInit;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockInvisibleLight extends BlockBase
{

    public BlockInvisibleLight(String name, Material material, MapColor mapColor) {
        super(name, material, mapColor);
        this.setLightLevel(1.0f);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0f);
        this.setTickRandomly(true);
        this.setCreativeTab(null);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public @Nullable AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public @Nullable RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return null;
    }

    @Override
    public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        boolean keep = worldIn.playerEntities.stream().anyMatch(player ->
        {
            BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
            if (Math.abs(playerPos.getX() - pos.getX()) > 1) return false;
            if (Math.abs(playerPos.getY() - pos.getY()) > 1) return false;
            if (Math.abs(playerPos.getZ() - pos.getZ()) > 1) return false;

            ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            return EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.GLOW, helmet) > 0;
        });

        if (!keep)
        {
            worldIn.setBlockToAir(pos);
        }
        else
        {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.scheduleUpdate(pos, this, 1);
    }
}