package toldea.romecraft.client.renderer;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import toldea.romecraft.client.model.TestModelBloomery;
import toldea.romecraft.client.model.ModelPilum;
import toldea.romecraft.entity.EntityPilum;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityPilum extends Render {
	private static final ResourceLocation pilumTexture = new ResourceLocation("romecraft", "textures/entity/pilum.png");
	private ModelPilum modelPilum;

	public RenderEntityPilum() {
		modelPilum = new ModelPilum();
	}
	
	public void renderPilum(EntityPilum entityPilum, double par2, double par4, double par6, float par8, float par9) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
		
		// Rotate according to the current yaw and pitch of the pilum.
		GL11.glRotatef(entityPilum.prevRotationYaw + (entityPilum.rotationYaw - entityPilum.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(entityPilum.prevRotationPitch + (entityPilum.rotationPitch - entityPilum.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
		
		// Factor in the custom model rotation.
		GL11.glRotatef(90f, 0F, 0.0F, 1.0F);
		
		// Define the scale factor. 
		float f10 = 0.5f;
		
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		
		float f11 = (float) entityPilum.arrowShake - par9;
		if (f11 > 0.0F) {
			float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
			GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
		}

		GL11.glScalef(f10, f10, f10);
		GL11.glNormal3f(f10, 0.0F, 0.0F);
		
		bindTexture(pilumTexture);
		this.modelPilum.render(entityPilum, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return pilumTexture;
	}

	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderPilum((EntityPilum) par1Entity, par2, par4, par6, par8, par9);
	}
}