package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelRomanAnvil extends ModelBase {
	ModelRenderer anvilTop;
	ModelRenderer anvilBottom;

	public ModelRomanAnvil() {
		int textureWidth = 64;
		int textureHeight = 32;

		anvilTop = new ModelRenderer(this, 0, 0);
		anvilTop.addBox(-8F, -8F, -8F, 16, 9, 16);
		anvilTop.setRotationPoint(0F, 0F, 0F);
		anvilTop.setTextureSize(textureWidth, textureHeight);

		anvilBottom = new ModelRenderer(this, 13, 7);
		anvilBottom.addBox(-5F, 1F, -5F, 10, 7, 10);
		anvilBottom.setRotationPoint(0F, 0F, 0F);
		anvilBottom.setTextureSize(textureWidth, textureHeight);
	}

	public void renderAnvil(float f5) {
		anvilTop.render(f5);
		anvilBottom.render(f5);
	}
}
