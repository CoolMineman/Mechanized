package net.snakefangox.mechanized.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.snakefangox.mechanized.MRegister;
import net.snakefangox.mechanized.blocks.entity.SteamPistonEntity;

public class SteamPiston extends BlockWithEntity {

	public static final BooleanProperty EXTENDED = BooleanProperty.of("extended");

	public SteamPiston(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(EXTENDED, false));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : checkType(type, MRegister.STEAM_PISTON_ENTITY, (w, p, s, be) -> be.tick());
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos,
			boolean moved) {
		super.neighborUpdate(state, world, pos, block, neighborPos, moved);
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof SteamPistonEntity) {
			((SteamPistonEntity) be).updateSignal(world.getReceivedStrongRedstonePower(pos));
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(Properties.FACING, ctx.getSide());
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING, EXTENDED);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return MRegister.STEAM_PISTON_ENTITY.instantiate(pos, state);
	}
}
