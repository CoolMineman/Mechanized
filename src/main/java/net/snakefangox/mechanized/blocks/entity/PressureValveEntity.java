package net.snakefangox.mechanized.blocks.entity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.snakefangox.mechanized.MRegister;
import net.snakefangox.mechanized.blocks.PressureValve;
import net.snakefangox.mechanized.networking.PacketIdentifiers;
import net.snakefangox.mechanized.steam.Steam;
import net.snakefangox.mechanized.steam.SteamUtil;

import java.util.stream.Stream;

public class PressureValveEntity extends BlockEntity implements Steam, PropertyDelegateHolder {

	private static final int STEAM_TANK_CAPACITY = (int) (Steam.UNIT * 0.1);
	private static final int VENT_PER_QUARTER_SEC = 16;
	int ventPressure = 100;
	int steamAmount = 0;
	int ventSoundTime = 0;
	boolean isOpen = false;

	public PressureValveEntity(BlockPos pos, BlockState state) {
		super(MRegister.PRESSURE_VALVE_ENTITY, pos, state);
	}

	public void setPressure(int press) {
		ventPressure = press;
	}

	public void tick() {
		if (world.isClient)
			return;
		if (world.getTime() % 5 == 0) {
			Direction dir = getCachedState().get(Properties.FACING);
			SteamUtil.directionalEqualizeSteam(world, this, pos, dir, dir);
			if (getPressurePSB(dir) > ventPressure && !isOpen) {
				changeValveState(true);
			}
			if (getPressurePSB(dir) <= ventPressure && isOpen) {
				changeValveState(false);
			}
			if (isOpen) {
				vent(dir);
			}
		}
	}

	private void vent(Direction dir) {
		Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world, pos);
		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeBlockPos(pos);
		passedData.writeByte(getCachedState().get(Properties.FACING).getId());
		watchingPlayers.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player,
				PacketIdentifiers.VENT_PARTICLES, passedData));
		removeSteam(dir, VENT_PER_QUARTER_SEC);
		--ventSoundTime;
		if (ventSoundTime <= 0) {
			((ServerWorld) world).playSound(null, pos, MRegister.STEAM_ESCAPES, SoundCategory.BLOCKS, 0.02F, 0);
			ventSoundTime = 5;
		}
	}

	private void changeValveState(boolean open) {
		world.setBlockState(pos, getCachedState().with(PressureValve.OPEN, open));
		isOpen = open;
	}

	@Override
	public int getSteamAmount(Direction dir) {
		if (dir == getCachedState().get(Properties.FACING))
			return steamAmount;
		return 0;
	}

	@Override
	public int getMaxSteamAmount(Direction dir) {
		if (dir == getCachedState().get(Properties.FACING))
			return STEAM_TANK_CAPACITY;
		return 0;
	}

	@Override
	public void setSteamAmount(Direction dir, int amount) {
		if (dir == getCachedState().get(Properties.FACING))
			steamAmount = amount;
	}

	@Override
	public boolean canPipeConnect(Direction dir) {
		return getCachedState().get(Properties.FACING) == dir;
	}

	@Override
	public int getPressurePSBForReadout(Direction dir) {
		return getPressurePSB(getCachedState().get(HorizontalFacingBlock.FACING));
	}

	@Override
	public void readNbt(CompoundTag tag) {
		super.readNbt(tag);
		ventPressure = tag.getInt("ventPressure");
		steamAmount = tag.getInt("steamAmount");
		isOpen = tag.getBoolean("isOpen");
	}

	@Override
	public CompoundTag writeNbt(CompoundTag tag) {
		tag.putInt("ventPressure", ventPressure);
		tag.putInt("steamAmount", steamAmount);
		tag.putBoolean("isOpen", isOpen);
		return super.writeNbt(tag);
	}

	@SuppressWarnings("all")
	PropertyDelegate propdel = new PropertyDelegate() {

		@Override
		public int size() {
			return 1;
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 0:
				ventPressure = value;
				break;
			}
		}

		@Override
		public int get(int index) {
			switch (index) {
			case 0:
				return ventPressure;
			}
			return 0;
		}
	};

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return propdel;
	}
}
