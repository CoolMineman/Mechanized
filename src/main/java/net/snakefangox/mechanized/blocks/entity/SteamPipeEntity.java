package net.snakefangox.mechanized.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.snakefangox.mechanized.MRegister;
import net.snakefangox.mechanized.steam.Steam;
import net.snakefangox.mechanized.steam.SteamPipeNetworkStorage;
import net.snakefangox.mechanized.steam.SteamPipeNetworkStorage.NetworkMember;
import net.snakefangox.mechanized.steam.SteamPipeNetworkStorage.PipeNetwork;

public class SteamPipeEntity extends BlockEntity implements Steam, NetworkMember {
	
	int pipeNetworkID = 0;

	public SteamPipeEntity(BlockPos pos, BlockState state) {
		super(MRegister.STEAM_PIPE_ENTITY, pos, state);
	}
	
	@Override
	public void setNetwork(int id) {
		pipeNetworkID = id;
	}
	
	@Override
	public int getNetwork() {
		return pipeNetworkID;
	}

	@Override
	public int getSteamAmount(Direction dir) {
		if(world instanceof ServerWorld) {
			PipeNetwork pipeNetwork = SteamPipeNetworkStorage.getInstance((ServerWorld)world).getPipeNetwork(pipeNetworkID);
			if(pipeNetwork != null)
				return pipeNetwork.getSteamAmount(dir);
		}
		return 0;
	}

	@Override
	public int getMaxSteamAmount(Direction dir) {
		return PipeNetwork.STEAM_CAPACITY;
	}

	@Override
	public void setSteamAmount(Direction dir, int amount) {
		if(world instanceof ServerWorld) {
			PipeNetwork pipeNetwork = SteamPipeNetworkStorage.getInstance((ServerWorld)world).getPipeNetwork(pipeNetworkID);
			if(pipeNetwork != null)
				pipeNetwork.setSteamAmount(dir, amount);
		}
	}

	@Override
	public void readNbt(CompoundTag tag) {
		super.readNbt(tag);
		pipeNetworkID = tag.getInt("pipeNetworkID");
	}

	@Override
	public CompoundTag writeNbt(CompoundTag tag) {
		tag.putInt("pipeNetworkID", pipeNetworkID);
		return super.writeNbt(tag);
	}
}
