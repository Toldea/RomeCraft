package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelMarblePillar extends ModelBase {
	ModelRenderer pillar1;
	ModelRenderer pillar2;
	ModelRenderer pillar3;

	public ModelMarblePillar() {
		textureWidth = 64;
		textureHeight = 32;

		pillar1 = new ModelRenderer(this, 40, 0);
		pillar1.addBox(4F, -8F, -4F, 2, 16, 8);
		pillar1.setTextureSize(textureWidth, textureHeight);
		setRotation(pillar1, 0F, 1.570796F, 0F);

		pillar2 = new ModelRenderer(this, 0, 0);
		pillar2.addBox(-6F, -8F, -4F, 12, 16, 8);
		pillar2.setTextureSize(textureWidth, textureHeight);

		pillar3 = new ModelRenderer(this, 40, 0);
		pillar3.addBox(-6F, -8F, -4F, 2, 16, 8);
		pillar3.setTextureSize(textureWidth, textureHeight);
		setRotation(pillar3, 0F, 1.570796F, 0F);
	}

	public void renderPillar() {
		pillar1.render(.0625f);
		pillar2.render(.0625f);
		pillar3.render(.0625f);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
