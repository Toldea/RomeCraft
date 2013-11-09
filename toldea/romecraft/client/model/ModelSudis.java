package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelSudis extends ModelBase {
	ModelRenderer sudisBase;
	ModelRenderer sudisTopBottom;
	ModelRenderer sudisTopTop;
	ModelRenderer sudisMiddleTop;
	ModelRenderer sudisMiddleBottom;

	public ModelSudis() {
		textureWidth = 64;
		textureHeight = 32;

		sudisBase = new ModelRenderer(this, 0, 0);
		sudisBase.addBox(-0.5F, -15F, -0.5F, 1, 30, 1);
		sudisBase.setTextureSize(textureWidth, textureHeight);

		sudisTopBottom = new ModelRenderer(this, 4, 0);
		sudisTopBottom.addBox(-1.5F, 1.5F, -1.5F, 3, 6, 3);
		sudisTopBottom.setTextureSize(textureWidth, textureHeight);

		sudisTopTop = new ModelRenderer(this, 4, 0);
		sudisTopTop.addBox(-1.5F, -7.5F, -1.5F, 3, 6, 3);
		sudisTopTop.setTextureSize(textureWidth, textureHeight);

		sudisMiddleTop = new ModelRenderer(this, 4, 9);
		sudisMiddleTop.addBox(-1F, -11F, -1F, 2, 4, 2);
		sudisMiddleTop.setTextureSize(textureWidth, textureHeight);

		sudisMiddleBottom = new ModelRenderer(this, 4, 9);
		sudisMiddleBottom.addBox(-1F, 7F, -1F, 2, 4, 2);
		sudisMiddleBottom.setTextureSize(textureWidth, textureHeight);
	}

	public void render(float f5) {
		sudisBase.render(f5);
		sudisTopBottom.render(f5);
		sudisTopTop.render(f5);
		sudisMiddleTop.render(f5);
		sudisMiddleBottom.render(f5);
	}
}
