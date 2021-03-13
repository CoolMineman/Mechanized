package net.snakefangox.mechanized.blocks.entity;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.snakefangox.mechanized.MRegister;
import net.snakefangox.mechanized.steam.Steam;

public class BasicBoilerEntity extends AbstractSteamBoilerEntity {

	public static final FluidAmount FLUID_PER_OP = FluidAmount.of(1, Steam.UNIT);
	
	public BasicBoilerEntity(BlockPos pos, BlockState state) {
		super(MRegister.BASIC_BOILER_ENTITY, pos, state);
	}

	@Override
	protected FluidAmount fluidPerOp() {
		return FLUID_PER_OP;
	}

	@Override
	protected int steamPerOp() {
		return 1;
	}

	@Override
	protected void extractTick() {
		//noop
	}
}
