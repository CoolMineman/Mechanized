package net.snakefangox.mechanized.blocks;

import java.util.Optional;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.snakefangox.mechanized.MRegister;
import net.snakefangox.mechanized.blocks.entity.SteamCondenserEntity;

public class SteamCondenser extends BlockWithEntity implements FluidDrainable, AttributeProvider {

	public SteamCondenser(Settings settings) {
		super(settings);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : checkType(type, MRegister.STEAM_CONDENSER_ENTITY, (w, p, s, be) -> be.tick());
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction dir = Direction.fromHorizontal(ctx.getPlayerFacing().getHorizontal());
		return getDefaultState().with(HorizontalFacingBlock.FACING, dir);
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(HorizontalFacingBlock.FACING);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return MRegister.STEAM_CONDENSER_ENTITY.instantiate(pos, state);
	}

	@Override
	public ItemStack tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.getBlockEntity(pos) instanceof SteamCondenserEntity) {
			SteamCondenserEntity be = (SteamCondenserEntity) world.getBlockEntity(pos);
			if (be.tank.attemptAnyExtraction(FluidAmount.BUCKET, Simulation.SIMULATE).getAmount_F()
					.isLessThanOrEqual(FluidAmount.BUCKET)) {
				return new ItemStack(be.tank.attemptAnyExtraction(FluidAmount.BUCKET, Simulation.ACTION).getRawFluid().getBucketItem());
			} else {
				return ItemStack.EMPTY;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof SteamCondenserEntity) {
			SteamCondenserEntity tank = (SteamCondenserEntity) be;
			to.offer(tank.tank);
		}
	}

	@Override
	public Optional<SoundEvent> getBucketFillSound() {
		return Fluids.WATER.getBucketFillSound();
	}

}
