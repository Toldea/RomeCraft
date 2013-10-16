package toldea.romecraft.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class RomeCraftRenderHelper {
	private RomeCraftRenderHelper() {}
	
	public static void renderCube(float x, float y, float z, float width, float height, float length, ResourceLocation texture) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

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

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		tessellator.draw();
	}
}
