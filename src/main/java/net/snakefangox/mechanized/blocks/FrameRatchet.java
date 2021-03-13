package net.snakefangox.mechanized.blocks;

import net.snakefangox.mechanized.MRegister;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FrameRatchet extends BlockWithEntity {

	public FrameRatchet(Settings settings) {
		super(settings);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : checkType(type, MRegister.FRAME_RATCHET_ENTITY, (w, p, s, be) -> be.tick());
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction dir = Direction.fromHorizontal(ctx.getPlayerFacing().getHorizontal());
		return getDefaultState().with(HorizontalFacingBlock.FACING, dir);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HorizontalFacingBlock.FACING);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return MRegister.FRAME_RATCHET_ENTITY.instantiate(pos, state);
	}
}
