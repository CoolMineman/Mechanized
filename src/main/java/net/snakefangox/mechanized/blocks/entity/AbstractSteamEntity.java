package net.snakefangox.mechanized.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.snakefangox.mechanized.steam.Steam;
import net.snakefangox.mechanized.steam.SteamUtil;

public abstract class AbstractSteamEntity extends BlockEntity implements Steam {

	int steamAmount = 0;

	public AbstractSteamEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void tick() {
		if (world.isClient)
			return;
		if (world.getTime() % 5 == 0) {
			SteamUtil.equalizeSteam(world, this, pos, null);
		}
	}

	@Override
	public int getSteamAmount(Direction dir) {
		return steamAmount;
	}

	@Override
	public void setSteamAmount(Direction dir, int amount) {
		steamAmount = amount;
	}

	@Override
	public void readNbt(CompoundTag tag) {
		super.readNbt(tag);
		steamAmount = tag.getInt("steamAmount");
	}

	@Override
	public CompoundTag writeNbt(CompoundTag tag) {
		tag.putInt("steamAmount", steamAmount);
		return super.writeNbt(tag);
	}

}
