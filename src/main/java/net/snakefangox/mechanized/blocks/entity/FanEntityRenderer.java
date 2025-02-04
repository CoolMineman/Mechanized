package net.snakefangox.mechanized.blocks.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.snakefangox.mechanized.MRegister;

public class FanEntityRenderer implements BlockEntityRenderer<FanEntity> {

	private final ItemStack fan;

	public FanEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		fan = new ItemStack(MRegister.FAN_BLADE);
	}

	@Override
	public void render(FanEntity blockEntity, float tickDelta, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light, int overlay) {
		Direction facing = blockEntity.getCachedState().get(Properties.FACING);
		matrices.push();
		matrices.translate(0.5, 0.5, 0.5);
		matrices.translate(facing.getOffsetX() * -0.2, facing.getOffsetY() * -0.2, facing.getOffsetZ() * -0.2);
		matrices.scale(0.8f, 0.8f, 0.8f);
		if (facing == Direction.UP || facing == Direction.DOWN) {
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
		} else if (facing == Direction.EAST || facing == Direction.WEST) {
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
		}
		matrices.multiply(
				Vec3f.POSITIVE_Z.getDegreesQuaternion((blockEntity.getWorld().getTime() + tickDelta) * 16));
		MinecraftClient.getInstance().getItemRenderer().renderItem(fan, ModelTransformation.Mode.FIXED, light, overlay,
				matrices, vertexConsumers, 0);
		matrices.pop();
	}

}
