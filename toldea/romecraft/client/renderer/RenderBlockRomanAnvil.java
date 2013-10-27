package toldea.romecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.client.model.ModelRomanAnvil;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderBlockRomanAnvil implements ISimpleBlockRenderingHandler {
	public static int renderID;
	private final ModelRomanAnvil modelRomanAnvil;
	private static final ResourceLocation romanAnvilTexture = new ResourceLocation("romecraft", "textures/entity/romananvil.png");

	public RenderBlockRomanAnvil() {
		modelRomanAnvil = new ModelRomanAnvil();
		renderID = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glRotatef(180f, 0F, 0F, 1F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(romanAnvilTexture);
		modelRomanAnvil.renderAnvil(.0625f);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderID;
	}

}
