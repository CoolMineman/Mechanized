package net.snakefangox.mechanized.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.snakefangox.mechanized.MRegister;
import net.snakefangox.mechanized.blocks.entity.SteamChargerEntity;
import net.snakefangox.mechanized.parts.StandardInventory;
import net.snakefangox.mechanized.steam.SteamItem;

public class SteamCharger extends BlockWithEntity {

	private VoxelShape BOX = VoxelShapes.cuboid(0, 0, 0, 1, 0.75, 1);

	public SteamCharger(Settings settings) {
		super(settings);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : checkType(type, MRegister.STEAM_CHARGER_ENTITY, (w, p, s, be) -> be.tick());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (world.isClient)
			return ActionResult.SUCCESS;
		ItemStack held = player.getStackInHand(hand);
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof SteamChargerEntity) {
			if (held.getItem() instanceof SteamItem && !held.isEmpty() && ((SteamChargerEntity) be).getItems().get(0).isEmpty()) {
				ItemStack inStack = held.copy();
				inStack.setCount(1);
				((SteamChargerEntity) be).getItems().set(0, inStack);
				held.decrement(1);
				((SteamChargerEntity) be).sync();
			}else if(held.isEmpty() && !((SteamChargerEntity) be).getItems().get(0).isEmpty()) {
				player.setStackInHand(hand, ((SteamChargerEntity) be).getItems().get(0));
				((SteamChargerEntity) be).setStack(0, ItemStack.EMPTY);
				((SteamChargerEntity) be).sync();
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return BOX;
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
		return MRegister.STEAM_CHARGER_ENTITY.instantiate(pos, state);
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBreak(world, pos, state, player);
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof StandardInventory) {
			((StandardInventory) be).dropEverything(world, pos);
		}
	}

}
