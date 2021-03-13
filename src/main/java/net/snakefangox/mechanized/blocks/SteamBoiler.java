package net.snakefangox.mechanized.blocks;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.snakefangox.mechanized.blocks.entity.AbstractSteamBoilerEntity;
import net.snakefangox.mechanized.gui.SteamBoilerContainer;

import java.util.Random;

public class SteamBoiler extends BlockWithEntity implements AttributeProvider {

	public static final BooleanProperty LIT = Properties.LIT;
	public final FabricBlockEntityTypeBuilder.Factory<? extends AbstractSteamBoilerEntity> beSupplier;

	public SteamBoiler(Settings settings, FabricBlockEntityTypeBuilder.Factory<? extends AbstractSteamBoilerEntity> supp) {
		super(settings);
		this.beSupplier = supp;
		setDefaultState(getDefaultState().with(LIT, false));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (world.isClient)
			return ActionResult.SUCCESS;

		BlockEntity be = world.getBlockEntity(pos);
		ItemStack heldItem = player.getStackInHand(hand);
		if (be instanceof AbstractSteamBoilerEntity) {
			AbstractSteamBoilerEntity sb = (AbstractSteamBoilerEntity) be;
			if (heldItem.getItem() == Items.WATER_BUCKET) {
				FluidVolume fluid = new FluidVolume(FluidKeys.WATER, FluidAmount.BUCKET) {
				};
				if (sb.waterTank.attemptInsertion(fluid, Simulation.SIMULATE).isEmpty()) {
					sb.waterTank.attemptInsertion(fluid, Simulation.ACTION);
					if (!player.isCreative())
						player.setStackInHand(hand, new ItemStack(Items.BUCKET));
				}
			} else {
				player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
			}
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		//noop
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBreak(world, pos, state, player);
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof AbstractSteamBoilerEntity) {
			((AbstractSteamBoilerEntity) be).dropEverything(world, pos);
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction dir = Direction.fromHorizontal(ctx.getPlayerFacing().getOpposite().getHorizontal());
		return getDefaultState().with(HorizontalFacingBlock.FACING, dir);
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(HorizontalFacingBlock.FACING, LIT);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return beSupplier.create(pos, state);
	}

	@Override
	public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof AbstractSteamBoilerEntity) {
			AbstractSteamBoilerEntity tank = (AbstractSteamBoilerEntity) be;
			to.offer(tank.waterTank);
		}
	}

	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof AbstractSteamBoilerEntity) {
			return new ExtendedScreenHandlerFactory() {
				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
					return new SteamBoilerContainer(syncId, inv, ScreenHandlerContext.create(world, pos));
				}

				@Override
				public Text getDisplayName() {
					return LiteralText.EMPTY;
				}

				@Override
				public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
					packetByteBuf.writeBlockPos(pos);
				}
			};
		} else {
			return null;
		}
	}
}
