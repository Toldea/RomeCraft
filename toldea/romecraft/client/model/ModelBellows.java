package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import toldea.romecraft.tileentity.TileEntityBellows;

public class ModelBellows extends ModelBase {
	ModelRenderer BellowsPipe;
	ModelRenderer BellowsBottom1;
	ModelRenderer BellowsBottom2;
	ModelRenderer BellowsBottom3;
	ModelRenderer BellowsTop1;
	ModelRenderer BellowsTop2;
	ModelRenderer BellowsTop3;

	public ModelBellows() {
		textureWidth = 64;
		textureHeight = 32;

		BellowsPipe = new ModelRenderer(this, 12, 16);
		BellowsPipe.addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1);
		BellowsPipe.setRotationPoint(7F, 4F, 0F);
		BellowsPipe.setTextureSize(textureWidth, textureWidth);

		BellowsBottom1 = new ModelRenderer(this, 0, 16);
		BellowsBottom1.addBox(-2F, -0.5F, -2F, 2, 1, 4);
		BellowsBottom1.setRotationPoint(6F, 4.6F, 0F);
		BellowsBottom1.setTextureSize(textureWidth, textureWidth);

		BellowsBottom2 = new ModelRenderer(this, 0, 9);
		BellowsBottom2.addBox(-8F, -0.5F, -3F, 6, 1, 6);
		BellowsBottom2.setRotationPoint(6F, 4.6F, 0F);
		BellowsBottom2.setTextureSize(textureWidth, textureWidth);

		BellowsBottom3 = new ModelRenderer(this, 0, 0);
		BellowsBottom3.addBox(-12F, -0.5F, -4F, 4, 1, 8);
		BellowsBottom3.setRotationPoint(6F, 4.6F, 0F);
		BellowsBottom3.setTextureSize(textureWidth, textureWidth);

		BellowsTop1 = new ModelRenderer(this, 0, 16);
		BellowsTop1.addBox(-2F, -0.5F, -2F, 2, 1, 4);
		BellowsTop1.setRotationPoint(6F, 3.4F, 0F);
		BellowsTop1.setTextureSize(textureWidth, textureWidth);

		BellowsTop2 = new ModelRenderer(this, 0, 9);
		BellowsTop2.addBox(-8F, -0.5F, -3F, 6, 1, 6);
		BellowsTop2.setRotationPoint(6F, 3.4F, 0F);
		BellowsTop2.setTextureSize(textureWidth, textureWidth);

		BellowsTop3 = new ModelRenderer(this, 0, 0);
		BellowsTop3.addBox(-12F, -0.5F, -4F, 4, 1, 8);
		BellowsTop3.setRotationPoint(6F, 3.4F, 0F);
		BellowsTop3.setTextureSize(textureWidth, textureWidth);

	}

	public void render(TileEntityBellows bellows, float f, float f1, float f2, float f3, float f4, float f5) {
		setBellowsRotation(bellows);
		
		BellowsPipe.render(f5);
		BellowsBottom1.render(f5);
		BellowsBottom2.render(f5);
		BellowsBottom3.render(f5);
		BellowsTop1.render(f5);
		BellowsTop2.render(f5);
		BellowsTop3.render(f5);
	}
	
	private void setBellowsRotation(TileEntityBellows bellows) {
		float bellowsRotation = bellows.getBellowsRotation();
		BellowsTop1.rotateAngleZ = BellowsTop2.rotateAngleZ = BellowsTop3.rotateAngleZ = bellowsRotation;
	}
}
