package net.snakefangox.mechanized.tools;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RenderHelper {

	//Taken (with permission) from https://github.com/ejektaflex/Runik and converted to java

	public static void drawQuad(int x, int y, int width, int height, int red, int green, int blue) {
		int alpha = 255;
		RenderSystem.setShader(GameRenderer::method_34540);
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		buffer.vertex(x, y, 0.0).color(red, green, blue, alpha).next();
		buffer.vertex((double) x, y + height, 0.0).color(red, green, blue, alpha).next();
		buffer.vertex((double) x + width, y + height, 0.0).color(red, green, blue, alpha).next();
		buffer.vertex((double) x + width, y, 0.0).color(red, green, blue, alpha).next();
		buffer.end();
		BufferRenderer.draw(buffer);
	}

	public static void drawQuad(int x, int y, int width, int height, int color) {
		drawQuad(x, y, width, height, (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);
	}
}
