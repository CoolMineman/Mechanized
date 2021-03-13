package net.snakefangox.mechanized.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.util.math.BlockPos;
import net.snakefangox.mechanized.mixininterfaces.FurnaceInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class FurnaceMixin extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider, FurnaceInterface {

	@Shadow
	private int burnTime;
	
	@Shadow
	private int fuelTime;
	
	protected FurnaceMixin(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}
	
	public void addFuelTimeMech(int time) {
		burnTime += time;
		fuelTime = burnTime;
	}
	
}
