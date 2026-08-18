package net.vyroxes.minersprosperity.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.init.ModItems;
import net.vyroxes.minersprosperity.util.EntityRelocatorUtils;
import org.jetbrains.annotations.NotNull;

public class EntityRelocatorProjectile extends EntityThrowable {
    private static final String TAG_STACK = "RelocatorStack";

    private ItemStack relocatorStack = ItemStack.EMPTY;

    public EntityRelocatorProjectile(World world)
    {
        super(world);
    }

    public EntityRelocatorProjectile(World world, EntityLivingBase thrower, ItemStack stack)
    {
        super(world, thrower);

        this.relocatorStack = stack.copy();
        this.relocatorStack.setCount(1);
    }

    @Override
    protected void onImpact(@NotNull RayTraceResult result)
    {
        if (this.world.isRemote) return;

        if (result.entityHit instanceof EntityLivingBase target) EntityRelocatorUtils.capture(this.relocatorStack, target);

        this.dropRelocator();
        this.setDead();
    }

    private void dropRelocator()
    {
        ItemStack stack = this.relocatorStack.isEmpty() ? new ItemStack(ModItems.ENTITY_RELOCATOR) : this.relocatorStack.copy();
        stack.setCount(1);

        EntityItem entityItem = new EntityItem(this.world, this.posX, this.posY, this.posZ, stack);
        entityItem.setPickupDelay(10);

        this.world.spawnEntity(entityItem);
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        if (!this.relocatorStack.isEmpty()) compound.setTag(TAG_STACK, this.relocatorStack.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey(TAG_STACK, 10)) this.relocatorStack = new ItemStack(compound.getCompoundTag(TAG_STACK));
    }
}