package toldea.romecraft.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import toldea.romecraft.client.model.ModelScutum;

public class RenderScutum implements IItemRenderer {
	private static final ResourceLocation scutumTexture = new ResourceLocation("romecraft", "textures/models/armor/scutum.png");
	private ModelScutum modelScutum;

	public RenderScutum() {
		modelScutum = new ModelScutum();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case EQUIPPED:
			return true;
		case EQUIPPED_FIRST_PERSON:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			renderFirstPersonScutum((Entity) data[1]);
		}
	}

	float derp = .0f;
	
	public void renderFirstPersonScutum(Entity entity) {
		GL11.glPushMatrix();
		
		GL11.glRotatef(-45, 0f, 0f, 1f);
		GL11.glRotatef(-75, 0f, 1f, 0f);
		GL11.glRotatef(15, 1f, 0f, 0f);
		GL11.glRotatef(-15, 0f, 1f, 0f);
		GL11.glRotatef(10, 0f, 1f, 10f);
		
		GL11.glTranslatef(-2f, 0f, 0f);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(scutumTexture);
		this.modelScutum.render(entity);
		GL11.glPopMatrix();
	}
	
	public void renderThirdPersonScutum(Entity entity, ModelRenderer bipedLeftArm) {
		GL11.glPushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(scutumTexture);
		modelScutum.rotateAngleX = bipedLeftArm.rotateAngleX;
		modelScutum.rotateAngleY = bipedLeftArm.rotateAngleY;
		modelScutum.rotateAngleZ = bipedLeftArm.rotateAngleZ;
		modelScutum.render(entity);
		GL11.glPopMatrix();
	}
}
