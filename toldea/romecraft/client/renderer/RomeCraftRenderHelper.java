package toldea.romecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class RomeCraftRenderHelper {
	private RomeCraftRenderHelper() {
	}
	
	public static void addVerticesForCube(float x, float y, float z, float width, float height, float length) {
		Tessellator tessellator = Tessellator.instance;
		// Front
		tessellator.addVertexWithUV(x, y, z, 1, 1);
		tessellator.addVertexWithUV(x, y + height, z, 1, 0);
		tessellator.addVertexWithUV(x + width, y + height, z, 0, 0);
		tessellator.addVertexWithUV(x + width, y, z, 0, 1);

		// Back
		tessellator.addVertexWithUV(x, y, z + length, 0, 1);
		tessellator.addVertexWithUV(x + width, y, z + length, 1, 1);
		tessellator.addVertexWithUV(x + width, y + height, z + length, 1, 0);
		tessellator.addVertexWithUV(x, y + height, z + length, 0, 0);

		// Bottom
		tessellator.addVertexWithUV(x, y, z, 0, 0);
		tessellator.addVertexWithUV(x + width, y, z, 1, 0);
		tessellator.addVertexWithUV(x + width, y, z + length, 1, 1);
		tessellator.addVertexWithUV(x, y, z + length, 0, 1);

		// Top
		tessellator.addVertexWithUV(x, y + height, z, 0, 0);
		tessellator.addVertexWithUV(x, y + height, z + length, 0, 1);
		tessellator.addVertexWithUV(x + width, y + height, z + length, 1, 1);
		tessellator.addVertexWithUV(x + width, y + height, z, 1, 0);

		// Left
		tessellator.addVertexWithUV(x, y, z, 0, 1);
		tessellator.addVertexWithUV(x, y, z + length, 1, 1);
		tessellator.addVertexWithUV(x, y + height, z + length, 1, 0);
		tessellator.addVertexWithUV(x, y + height, z, 0, 0);

		// Right
		tessellator.addVertexWithUV(x + width, y, z, 1, 1);
		tessellator.addVertexWithUV(x + width, y + height, z, 1, 0);
		tessellator.addVertexWithUV(x + width, y + height, z + length, 0, 0);
		tessellator.addVertexWithUV(x + width, y, z + length, 0, 1);
	}
	
	public static void applyLightLevelsForRGB(float r, float g, float b, World world, int x, int y, int z, Block block) {
		Tessellator tessellator = Tessellator.instance;
		float f = block.getBlockBrightness(world, x, y, z);
		int l = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		tessellator.setColorOpaque_F(f * r, f * g, f * b);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)l1, (float)l2);
	}
}
