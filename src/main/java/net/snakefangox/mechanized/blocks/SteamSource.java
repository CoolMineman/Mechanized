package net.snakefangox.mechanized.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.snakefangox.mechanized.MRegister;

public class SteamSource extends Block implements BlockEntityProvider {

	public SteamSource(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return MRegister.STEAM_SOURCE_ENTITY.instantiate(pos, state);
	}

}
