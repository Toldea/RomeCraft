package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelMarblePillarBase extends ModelBase {
	ModelRenderer pillarBaseBottom;
	ModelRenderer pillarBase1;
	ModelRenderer pillarBase2;
	ModelRenderer pillarBase3;
	ModelRenderer pillarBase4;
	ModelRenderer pillarBase5;
	ModelRenderer pillarBase6;

	public ModelMarblePillarBase() {
		textureWidth = 128;
		textureHeight = 64;

		pillarBaseBottom = new ModelRenderer(this, 0, 41);
		pillarBaseBottom.addBox(-8F, 5F, -8F, 16, 3, 16);
		pillarBaseBottom.setTextureSize(textureWidth, textureHeight);

		pillarBase1 = new ModelRenderer(this, 0, 0);
		pillarBase1.addBox(-6F, -6F, -4F, 12, 4, 8);
		pillarBase1.setTextureSize(textureWidth, textureHeight);
		setRotation(pillarBase1, 0F, 1.570796F, 0F);

		pillarBase2 = new ModelRenderer(this, 0, 0);
		pillarBase2.addBox(-6F, -6F, -4F, 12, 4, 8);
		pillarBase2.setTextureSize(textureWidth, textureHeight);

		pillarBase3 = new ModelRenderer(this, 38, 14);
		pillarBase3.addBox(-7F, -8F, -5F, 14, 2, 10);
		pillarBase3.setTextureSize(textureWidth, textureHeight);
		setRotation(pillarBase3, 0F, 1.570796F, 0F);

		pillarBase4 = new ModelRenderer(this, 0, 12);
		pillarBase4.addBox(-7F, -8F, -5F, 14, 2, 10);
		pillarBase4.setTextureSize(textureWidth, textureHeight);

		pillarBase5 = new ModelRenderer(this, 0, 24);
		pillarBase5.addBox(-7F, -2F, -5F, 14, 7, 10);
		pillarBase5.setTextureSize(textureWidth, textureHeight);
		setRotation(pillarBase5, 0F, 1.570796F, 0F);

		pillarBase6 = new ModelRenderer(this, 0, 24);
		pillarBase6.addBox(-7F, -2F, -5F, 14, 7, 10);
		pillarBase6.setTextureSize(textureWidth, textureHeight);
	}

	public void renderPillarBase() {
		pillarBaseBottom.render(.0625f);
		pillarBase1.render(.0625f);
		pillarBase2.render(.0625f);
		pillarBase3.render(.0625f);
		pillarBase4.render(.0625f);
		pillarBase5.render(.0625f);
		pillarBase6.render(.0625f);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
