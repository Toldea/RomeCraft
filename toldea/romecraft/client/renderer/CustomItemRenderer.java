package toldea.romecraft.client.renderer;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.FIRST_PERSON_MAP;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

public class CustomItemRenderer implements IItemRenderer {
	private static RenderItem renderItem = new RenderItem();

	@Override
	public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
		return type == ItemRenderType.EQUIPPED_FIRST_PERSON;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
		GL11.glPushMatrix();
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();

		IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemStack, type);
		Icon icon = itemStack.getIconIndex();

		if (icon == null) {
			GL11.glPopMatrix();
			return;
		}

		texturemanager.bindTexture(texturemanager.getResourceLocation(itemStack.getItemSpriteNumber()));
		Tessellator tessellator = Tessellator.instance;
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		float f4 = 0.0F;
		float f5 = 0.3F;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		
		GL11.glTranslatef(-f4, -f5, 0.0F);
		//GL11.glTranslatef(-f4, -f5 + .5f, 0.0F - 2f);
		
		float f6 = 1.5F;
		GL11.glScalef(f6, f6, f6);
		
		//GL11.glRotatef(-15.0F, 0.0F, 0.0F, 0.0F);
		
		//GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
		//GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
		//GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
		
		
		//GL11.glTranslatef(2.5F, 0.0F, 0.0F);
		
		renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		
		GL11.glPopMatrix();
	}

	public static void renderItemIn2D(Tessellator par0Tessellator, float par1, float par2, float par3, float par4, int par5, int par6, float par7) {
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, 0.0F, 1.0F);
		par0Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double) par1, (double) par4);
		par0Tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, (double) par3, (double) par4);
		par0Tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, (double) par3, (double) par2);
		par0Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, (double) par1, (double) par2);
		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, 0.0F, -1.0F);
		par0Tessellator.addVertexWithUV(0.0D, 1.0D, (double) (0.0F - par7), (double) par1, (double) par2);
		par0Tessellator.addVertexWithUV(1.0D, 1.0D, (double) (0.0F - par7), (double) par3, (double) par2);
		par0Tessellator.addVertexWithUV(1.0D, 0.0D, (double) (0.0F - par7), (double) par3, (double) par4);
		par0Tessellator.addVertexWithUV(0.0D, 0.0D, (double) (0.0F - par7), (double) par1, (double) par4);
		par0Tessellator.draw();
		float f5 = 0.5F * (par1 - par3) / (float) par5;
		float f6 = 0.5F * (par4 - par2) / (float) par6;
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		int k;
		float f7;
		float f8;

		for (k = 0; k < par5; ++k) {
			f7 = (float) k / (float) par5;
			f8 = par1 + (par3 - par1) * f7 - f5;
			par0Tessellator.addVertexWithUV((double) f7, 0.0D, (double) (0.0F - par7), (double) f8, (double) par4);
			par0Tessellator.addVertexWithUV((double) f7, 0.0D, 0.0D, (double) f8, (double) par4);
			par0Tessellator.addVertexWithUV((double) f7, 1.0D, 0.0D, (double) f8, (double) par2);
			par0Tessellator.addVertexWithUV((double) f7, 1.0D, (double) (0.0F - par7), (double) f8, (double) par2);
		}

		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(1.0F, 0.0F, 0.0F);
		float f9;

		for (k = 0; k < par5; ++k) {
			f7 = (float) k / (float) par5;
			f8 = par1 + (par3 - par1) * f7 - f5;
			f9 = f7 + 1.0F / (float) par5;
			par0Tessellator.addVertexWithUV((double) f9, 1.0D, (double) (0.0F - par7), (double) f8, (double) par2);
			par0Tessellator.addVertexWithUV((double) f9, 1.0D, 0.0D, (double) f8, (double) par2);
			par0Tessellator.addVertexWithUV((double) f9, 0.0D, 0.0D, (double) f8, (double) par4);
			par0Tessellator.addVertexWithUV((double) f9, 0.0D, (double) (0.0F - par7), (double) f8, (double) par4);
		}

		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, 1.0F, 0.0F);

		for (k = 0; k < par6; ++k) {
			f7 = (float) k / (float) par6;
			f8 = par4 + (par2 - par4) * f7 - f6;
			f9 = f7 + 1.0F / (float) par6;
			par0Tessellator.addVertexWithUV(0.0D, (double) f9, 0.0D, (double) par1, (double) f8);
			par0Tessellator.addVertexWithUV(1.0D, (double) f9, 0.0D, (double) par3, (double) f8);
			par0Tessellator.addVertexWithUV(1.0D, (double) f9, (double) (0.0F - par7), (double) par3, (double) f8);
			par0Tessellator.addVertexWithUV(0.0D, (double) f9, (double) (0.0F - par7), (double) par1, (double) f8);
		}

		par0Tessellator.draw();
		par0Tessellator.startDrawingQuads();
		par0Tessellator.setNormal(0.0F, -1.0F, 0.0F);

		for (k = 0; k < par6; ++k) {
			f7 = (float) k / (float) par6;
			f8 = par4 + (par2 - par4) * f7 - f6;
			par0Tessellator.addVertexWithUV(1.0D, (double) f7, 0.0D, (double) par3, (double) f8);
			par0Tessellator.addVertexWithUV(0.0D, (double) f7, 0.0D, (double) par1, (double) f8);
			par0Tessellator.addVertexWithUV(0.0D, (double) f7, (double) (0.0F - par7), (double) par1, (double) f8);
			par0Tessellator.addVertexWithUV(1.0D, (double) f7, (double) (0.0F - par7), (double) par3, (double) f8);
		}

		par0Tessellator.draw();
	}
	/*
	 * public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) { // Get icon index for the texture Icon icon =
	 * itemStack.getIconIndex(); // Use vanilla code to render the icon in a 16x16 square of inventory slot renderItem.renderIcon(0, 0, icon, 16, 16);
	 * 
	 * 
	 * 
	 * // Disable texturing, for now we only need colored shapes GL11.glDisable(GL11.GL_TEXTURE_2D); // The following 3 methods enable transparency of a certain
	 * flavor (see second tutorial link above) GL11.glEnable(GL11.GL_BLEND); GL11.glDepthMask(false); GL11.glBlendFunc(GL11.GL_SRC_ALPHA,
	 * GL11.GL_ONE_MINUS_SRC_ALPHA);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * // // Set drawing mode (see first tutorial link above). GL11.glBegin(GL11.GL_QUADS); // Set semi-transparent black color GL11.glColor4f(0F, 0F, 0F,
	 * 0.5F);
	 * 
	 * // Draw a 8x8 square GL11.glVertex3d(0, 0, 0); GL11.glVertex3d(0, 8, 0); GL11.glVertex3d(8, 8, 0); GL11.glVertex3d(8, 0, 0);
	 * 
	 * GL11.glEnd();
	 * 
	 * 
	 * 
	 * 
	 * 
	 * // Turn off unneeded transparency flags GL11.glDepthMask(true); GL11.glDisable(GL11.GL_BLEND);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
	 * 
	 * // Enable texturing, because Minecraft text font is actually a texture GL11.glEnable(GL11.GL_TEXTURE_2D); // Get our text value String text =
	 * "DERP";//Integer.toString(itemStack.getItemDamage()); // Draw our text at (1, 1) with white color fontRenderer.drawStringWithShadow(text, 1, 1,
	 * 0xFFFFFF); }
	 */
}