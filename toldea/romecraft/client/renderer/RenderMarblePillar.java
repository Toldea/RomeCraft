package toldea.romecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.client.model.ModelMarblePillar;
import toldea.romecraft.client.model.ModelMarblePillarBase;
import toldea.romecraft.managers.BlockManager;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderMarblePillar extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
	public static int renderID;

	private final ModelMarblePillar modelPillar;
	private final ModelMarblePillarBase modelPillarBase;
	private final ResourceLocation texturePillar;
	private final ResourceLocation texturePillarBase;

	public RenderMarblePillar() {
		RenderMarblePillar.renderID = RenderingRegistry.getNextAvailableRenderId();
		modelPillar = new ModelMarblePillar();
		modelPillarBase = new ModelMarblePillarBase();
		texturePillar = new ResourceLocation("romecraft", "textures/blocks/marble_pillar.png");
		texturePillarBase = new ResourceLocation("romecraft", "textures/blocks/marble_pillar_base.png");
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glPushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texturePillar);
		modelPillar.renderPillar();
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

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + .5, z + .5);
		
		if (tileentity.worldObj.getBlockId(tileentity.xCoord, tileentity.yCoord - 1, tileentity.zCoord) != BlockManager.blockMarblePillar.blockID) {
			// Block below this pillar is not another pillar, draw the 'base'.
			GL11.glRotatef(180f, 0F, 0F, 1F);
			Minecraft.getMinecraft().getTextureManager().bindTexture(texturePillarBase);
			modelPillarBase.renderPillarBase();
		} else if (tileentity.worldObj.getBlockId(tileentity.xCoord, tileentity.yCoord + 1, tileentity.zCoord) != BlockManager.blockMarblePillar.blockID) {
			// Block above this pillar is not another pillar, draw the 'top' or 180deg rotated base.
			Minecraft.getMinecraft().getTextureManager().bindTexture(texturePillarBase);
			modelPillarBase.renderPillarBase();
		} else {
			// Block is surrounded by other pillar blocks on the bottom and top, draw the normal pillar part.
			GL11.glRotatef(180f, 0F, 0F, 1F);
			Minecraft.getMinecraft().getTextureManager().bindTexture(texturePillar);
			modelPillar.renderPillar();
		}
		
		GL11.glPopMatrix();

	}

}
