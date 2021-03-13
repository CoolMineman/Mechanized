package net.snakefangox.mechanized.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.snakefangox.mechanized.MRegister;
import net.snakefangox.mechanized.steam.Steam;

public class SteamSourceEntity extends AbstractSteamEntity {

	private static final int STEAM_CAPACITY = Steam.UNIT * 16;

	public SteamSourceEntity(BlockPos pos, BlockState state) {
		super(MRegister.STEAM_SOURCE_ENTITY, pos, state);
	}

	@Override
	public int getSteamAmount(Direction dir) {
		return STEAM_CAPACITY;
	}

	@Override
	public int getMaxSteamAmount(Direction dir) {
		return STEAM_CAPACITY;
	}

	@Override
	public void setSteamAmount(Direction dir, int amount) {
		//noop
	}
}
