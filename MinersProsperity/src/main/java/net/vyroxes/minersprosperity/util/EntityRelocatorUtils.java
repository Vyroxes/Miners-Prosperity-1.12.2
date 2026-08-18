package net.vyroxes.minersprosperity.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.config.ModConfig;

import java.util.Locale;

public class EntityRelocatorUtils {
    private static final String TAG_RELOCATOR = "EntityRelocator";
    private static final String TAG_ENTITY_DATA = "EntityData";
    private static final String TAG_ENTITY_ID = "EntityId";
    private static final String TAG_ENTITY_NAME = "EntityName";
    private static final String TAG_HEALTH = "Health";
    private static final String TAG_MAX_HEALTH = "MaxHealth";

    private EntityRelocatorUtils() {}

    public static boolean hasEntity(ItemStack stack)
    {
        return !stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_RELOCATOR, 10) && stack.getTagCompound().getCompoundTag(TAG_RELOCATOR).hasKey(TAG_ENTITY_DATA, 10);
    }

    public static boolean canCapture(EntityLivingBase entity)
    {
        if (entity == null || entity instanceof EntityPlayer) return false;
        if (entity.isDead || !entity.isEntityAlive()) return false;
        if (!entity.isNonBoss()) return false;
        if (isBlacklisted(entity)) return false;

        return EntityList.getKey(entity) != null;
    }

    public static boolean capture(ItemStack stack, EntityLivingBase entity)
    {
        if (stack.isEmpty() || hasEntity(stack) || !canCapture(entity) || entity.world.isRemote) return false;

        ResourceLocation entityId = EntityList.getKey(entity);
        if (entityId == null) return false;

        NBTTagCompound entityData = new NBTTagCompound();
        entity.writeToNBT(entityData);
        entityData.setString("id", entityId.toString());

        entityData.removeTag("Passengers");
        entityData.removeTag("Leashed");
        entityData.removeTag("Leash");

        NBTTagCompound storedEntity = new NBTTagCompound();
        storedEntity.setString(TAG_ENTITY_ID, entityId.toString());
        storedEntity.setString(TAG_ENTITY_NAME, entity.getDisplayName().getUnformattedText());
        storedEntity.setFloat(TAG_HEALTH, entity.getHealth());
        storedEntity.setFloat(TAG_MAX_HEALTH, entity.getMaxHealth());
        storedEntity.setTag(TAG_ENTITY_DATA, entityData);

        NBTTagCompound root = stack.getTagCompound();
        if (root == null) root = new NBTTagCompound();

        root.setTag(TAG_RELOCATOR, storedEntity);
        stack.setTagCompound(root);

        if (entity.isRiding()) entity.dismountRidingEntity();
        if (entity.isBeingRidden()) entity.removePassengers();

        entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 0.5F, 1.25F);
        entity.setDead();

        return true;
    }

    public static boolean release(ItemStack stack, World world, double x, double y, double z)
    {
        if (world.isRemote || !hasEntity(stack)) return false;

        NBTTagCompound storedEntity = getStoredEntityTag(stack);
        if (storedEntity == null || !storedEntity.hasKey(TAG_ENTITY_DATA, 10)) return false;

        NBTTagCompound entityData = storedEntity.getCompoundTag(TAG_ENTITY_DATA).copy();
        Entity entity = EntityList.createEntityFromNBT(entityData, world);

        if (!(entity instanceof EntityLivingBase)) return false;

        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, 0.0F);
        entity.motionX = 0.0D;
        entity.motionY = 0.0D;
        entity.motionZ = 0.0D;
        entity.fallDistance = 0.0F;

        if (!world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty()) return false;
        if (!world.spawnEntity(entity)) return false;

        clearEntity(stack);

        world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 0.5F, 0.9F);

        return true;
    }

    public static void clearEntity(ItemStack stack)
    {
        if (!stack.hasTagCompound()) return;

        NBTTagCompound root = stack.getTagCompound();
        if (root == null) return;
        root.removeTag(TAG_RELOCATOR);

        if (root.getKeySet().isEmpty()) stack.setTagCompound(null);
    }

    public static String getStoredEntityId(ItemStack stack)
    {
        NBTTagCompound tag = getStoredEntityTag(stack);
        return tag == null ? "" : tag.getString(TAG_ENTITY_ID);
    }

    public static String getStoredEntityName(ItemStack stack)
    {
        NBTTagCompound tag = getStoredEntityTag(stack);
        return tag == null ? "" : tag.getString(TAG_ENTITY_NAME);
    }

    public static float getStoredHealth(ItemStack stack)
    {
        NBTTagCompound tag = getStoredEntityTag(stack);
        return tag == null ? 0.0F : tag.getFloat(TAG_HEALTH);
    }

    public static float getStoredMaxHealth(ItemStack stack)
    {
        NBTTagCompound tag = getStoredEntityTag(stack);
        return tag == null ? 0.0F : tag.getFloat(TAG_MAX_HEALTH);
    }

    private static NBTTagCompound getStoredEntityTag(ItemStack stack)
    {
        if (!hasEntity(stack)) return null;

        NBTTagCompound tagCompound = stack.getTagCompound();
        return tagCompound != null ? tagCompound.getCompoundTag(TAG_RELOCATOR) : null;
    }

    public static boolean isBlacklisted(Entity entity)
    {
        ResourceLocation id = EntityList.getKey(entity);
        if (id == null) return true;

        String entityId = id.toString().toLowerCase(Locale.ROOT);
        String modId = id.getNamespace().toLowerCase(Locale.ROOT);
        String[] blacklist = ModConfig.ITEMS.entityRelocatorBlacklist;

        if (blacklist == null) return false;

        for (String rawEntry : blacklist)
        {
            if (rawEntry == null || rawEntry.trim().isEmpty()) continue;

            String entry = rawEntry.trim().toLowerCase(Locale.ROOT);

            if (entry.endsWith(":*") && modId.equals(entry.substring(0, entry.length() - 2))) return true;
            if (entry.equals(entityId)) return true;
        }

        return false;
    }
}