package toldea.romecraft.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import toldea.romecraft.client.model.ModelPilum;

public class RenderItemPilum implements IItemRenderer {
	private static final ResourceLocation pilumTexture = new ResourceLocation("romecraft", "textures/entity/pilum.png");
	private ModelPilum modelPilum;

	public RenderItemPilum() {
		modelPilum = new ModelPilum();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case EQUIPPED:
		case EQUIPPED_FIRST_PERSON:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		switch (type) {
		case EQUIPPED: {
			//if (helper == ItemRendererHelper.EQUIPPED_BLOCK) return true;
			//if (helper == ItemRendererHelper.ENTITY_BOBBING) return true;
			//if (helper == ItemRendererHelper.ENTITY_ROTATION) return true;
			//if (helper == ItemRendererHelper.BLOCK_3D) return true;
			//if (helper == ItemRendererHelper.INVENTORY_BLOCK) return true;
			return false;
		}
		case EQUIPPED_FIRST_PERSON: {
			return false;
			//return (helper == ItemRendererHelper.EQUIPPED_BLOCK);
		}
		default: {
			return false;
		}
		}
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (data[1] instanceof EntityPlayer) {
			oldRenderPilum((Entity) data[1], 0d, 0d, 0d, 0f, 0f);
		} else {
			renderPilum((Entity) data[1], 0d, 0d, 0d, 0f, 0f);
		}
	}

	public void renderPilum(Entity entity, double par2, double par4, double par6, float par8, float par9) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) par2, (float) par4, (float) par6);

		// Factor in the custom model rotation.
		GL11.glRotatef(40f, 0F, 0.0F, 1.0F);
		GL11.glRotatef(-3f, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(-5f, 0.0F, 1.0F, 0.0F);
		
		// Translate a bit to move the pilum in the correct position.
		// up/down - forward/backward - left/right
		GL11.glTranslatef(.6f, -2.3f, -.15f);

		// Define the scale factor.
		float f10 = 1.0f;

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		
		GL11.glScalef(f10, f10, f10);
		GL11.glNormal3f(f10, 0.0F, 0.0F);

		Minecraft.getMinecraft().renderEngine.bindTexture(pilumTexture);
		this.modelPilum.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
	
	public void oldRenderPilum(Entity entity, double par2, double par4, double par6, float par8, float par9) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) par2, (float) par4, (float) par6);

		// Factor in the custom model rotation.
		GL11.glRotatef(135f, 0F, 0.0F, 1.0F);
		GL11.glRotatef(-7f, 1.0F, 0.0F, 0.0F);
		
		// Translate a bit to move the pilum in the correct position.
		// up/down - forward/backward - left/right
		GL11.glTranslatef(0f, -1.8f, -0.2f);

		// Define the scale factor.
		float f10 = 1.0f;

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		
		GL11.glScalef(f10, f10, f10);
		GL11.glNormal3f(f10, 0.0F, 0.0F);

		Minecraft.getMinecraft().renderEngine.bindTexture(pilumTexture);
		this.modelPilum.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
}
