package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelRomanAnvil extends ModelBase {
	ModelRenderer anvilTop;
	ModelRenderer anvilRim;
	ModelRenderer anvilShaft;
	ModelRenderer anvilBase;

	public ModelRomanAnvil() {
		textureWidth = 72;
		textureHeight = 38;

		anvilTop = new ModelRenderer(this, 0, 0);
		anvilTop.addBox(-8F, -8F, -8F, 16, 16, 7);
		anvilTop.setTextureSize(textureWidth, textureHeight);
		setRotation(anvilTop, -1.570796F, 0F, 0F);
		
		anvilRim = new ModelRenderer(this, 28, 23);
		anvilRim.addBox(-6F, -6F, -1F, 12, 12, 3);
		anvilRim.setTextureSize(textureWidth, textureHeight);
		setRotation(anvilRim, -1.570796F, 0F, 0F);

		anvilShaft = new ModelRenderer(this, 0, 23);
		anvilShaft.addBox(-5F, -5F, 2F, 10, 10, 4);
		anvilShaft.setTextureSize(textureWidth, textureHeight);
		setRotation(anvilShaft, -1.570796F, 0F, 0F);

		anvilBase = new ModelRenderer(this, 46, 0);
		anvilBase.addBox(-5.5F, -5.5F, 6F, 11, 11, 2);
		anvilBase.setTextureSize(textureWidth, textureHeight);
		setRotation(anvilBase, -1.570796F, 0F, 0F);
	}

	public void renderAnvil(float f5) {
		anvilTop.render(f5);
		anvilRim.render(f5);
		anvilShaft.render(f5);
		anvilBase.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
