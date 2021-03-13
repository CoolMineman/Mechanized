package net.snakefangox.mechanized.blocks.entity;

import net.snakefangox.mechanized.MRegister;
import net.snakefangox.mechanized.steam.Steam;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FrameRatchetBE extends AbstractSteamEntity {

	public FrameRatchetBE(BlockPos pos, BlockState state) {
		super(MRegister.FRAME_RATCHET_ENTITY, pos, state);
	}

	@Override
	public int getMaxSteamAmount(Direction dir) {
		return Steam.UNIT;
	}
}
