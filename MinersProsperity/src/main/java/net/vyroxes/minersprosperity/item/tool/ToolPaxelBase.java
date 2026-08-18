package net.vyroxes.minersprosperity.item.tool;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vyroxes.minersprosperity.MinersProsperity;
import net.vyroxes.minersprosperity.init.ModTools;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class ToolPaxelBase extends ItemTool
{
	private final ToolTier tier;

	public ToolPaxelBase(String name, ToolTier tier, float damage, float speed)
	{
		super(damage, speed, tier.material, Sets.newHashSet());

		this.tier = tier;

		this.setHarvestLevel("pickaxe", tier.material.getHarvestLevel());
		this.setHarvestLevel("axe", tier.material.getHarvestLevel());
		this.setHarvestLevel("shovel", tier.material.getHarvestLevel());
		setMaxDamage(tier.material.getMaxUses() * ToolTier.PAXEL_DURABILITY_MULTIPLIER);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(MinersProsperity.tools_tab);
		
		ModTools.TOOLS.add(this);
	}

	@Override
	public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack, @NotNull Enchantment enchantment)
	{
		if (super.canApplyAtEnchantingTable(stack, enchantment)) return true;

		return enchantment.type == EnumEnchantmentType.WEAPON;
	}


	@Override
	public @NotNull Set<String> getToolClasses(@NotNull ItemStack stack)
	{
		return Sets.newHashSet("sword", "pickaxe", "axe", "shovel");
	}

	@Override
	public float getDestroySpeed(@NotNull ItemStack stack, @NotNull IBlockState state)
	{
		return this.efficiency;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state)
	{
		return this.toolMaterial.getHarvestLevel() >= state.getBlock().getHarvestLevel(state);
	}

	@Override
	public @NotNull EnumActionResult onItemUse(EntityPlayer player, World world, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		BlockPos targetPos = pos.offset(facing);

		if (player.isSneaking())
		{
			if (facing != EnumFacing.DOWN && world.isAirBlock(targetPos))
			{
				if (block == Blocks.GRASS)
				{
					IBlockState pathState = Blocks.GRASS_PATH.getDefaultState();

					world.playSound(player, pos, pathState.getBlock().getSoundType(pathState, world, pos, player).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);

					if (!world.isRemote)
					{
						world.setBlockState(pos, pathState, 11);
						stack.damageItem(1, player);
					}
					return EnumActionResult.SUCCESS;
				}
			}
		}
		else
		{
			if (player.canPlayerEdit(targetPos, facing, stack))
			{
				if (facing != EnumFacing.DOWN && world.isAirBlock(targetPos))
				{
					IBlockState farmland = Blocks.FARMLAND.getDefaultState();

					if (block == Blocks.GRASS || block == Blocks.GRASS_PATH || block == Blocks.DIRT && block.getMetaFromState(state) == 0)
					{
						world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

						if (!world.isRemote)
						{
							world.setBlockState(pos, farmland, 11);
							stack.damageItem(1, player);
						}
						return EnumActionResult.SUCCESS;
					}
					else if (block == Blocks.DIRT && block.getMetaFromState(state) == 1)
					{
						world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

						if (!world.isRemote)
						{
							world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 11);
							stack.damageItem(1, player);
						}
						return EnumActionResult.SUCCESS;
					}
				}
			}
		}

		return EnumActionResult.PASS;
	}

	@Override
	public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair)
	{
		return tier.isRepairMaterial(repair) || super.getIsRepairable(toRepair, repair);
	}
}