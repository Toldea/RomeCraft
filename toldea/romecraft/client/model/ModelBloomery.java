package toldea.romecraft.client.model;

import javax.swing.text.html.parser.Entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBloomery extends ModelBase {
	private ModelRenderer BloomeryBottomBack;
	private ModelRenderer BloomeryBottomLeft;
	private ModelRenderer BloomeryBottomRight;
	private ModelRenderer BloomeryMiddleFront;
	private ModelRenderer BloomeryMiddleBack;
	private ModelRenderer BloomeryMiddleRight;
	private ModelRenderer BloomeryMiddleLeft;
	private ModelRenderer BloomeryTopFront;
	private ModelRenderer BloomeryTopBack;
	private ModelRenderer BloomeryTopLeft;
	private ModelRenderer BloomeryTopRight;

	public ModelBloomery() {
		textureWidth = 128;
		textureHeight = 64;

		BloomeryBottomBack = new ModelRenderer(this, 0, 0);
		BloomeryBottomBack.addBox(-8F, 0F, 3F, 16, 8, 5);
		BloomeryBottomBack.setRotationPoint(0F, 0F, 0F);
		BloomeryBottomBack.setTextureSize(textureWidth, textureHeight);

		BloomeryBottomLeft = new ModelRenderer(this, 71, 37);
		BloomeryBottomLeft.addBox(-8F, 0F, -8F, 5, 8, 11);
		BloomeryBottomLeft.setRotationPoint(0F, 0F, 0F);
		BloomeryBottomLeft.setTextureSize(textureWidth, textureHeight);

		BloomeryBottomRight = new ModelRenderer(this, 72, 0);
		BloomeryBottomRight.addBox(3F, 0F, -8F, 5, 8, 11);
		BloomeryBottomRight.setRotationPoint(0F, 0F, 0F);
		BloomeryBottomRight.setTextureSize(textureWidth, textureHeight);

		BloomeryMiddleFront = new ModelRenderer(this, 0, 13);
		BloomeryMiddleFront.addBox(-8F, -8F, -8F, 16, 8, 5);
		BloomeryMiddleFront.setRotationPoint(0F, 0F, 0F);
		BloomeryMiddleFront.setTextureSize(textureWidth, textureHeight);

		BloomeryMiddleBack = new ModelRenderer(this, 0, 26);
		BloomeryMiddleBack.addBox(-8F, -8F, 3F, 16, 8, 5);
		BloomeryMiddleBack.setRotationPoint(0F, 0F, 0F);
		BloomeryMiddleBack.setTextureSize(textureWidth, textureHeight);

		BloomeryMiddleRight = new ModelRenderer(this, 0, 39);
		BloomeryMiddleRight.addBox(3F, -8F, -3F, 5, 8, 6);
		BloomeryMiddleRight.setRotationPoint(0F, 0F, 0F);
		BloomeryMiddleRight.setTextureSize(textureWidth, textureHeight);

		BloomeryMiddleLeft = new ModelRenderer(this, 22, 39);
		BloomeryMiddleLeft.addBox(-8F, -8F, -3F, 5, 8, 6);
		BloomeryMiddleLeft.setRotationPoint(0F, 0F, 0F);
		BloomeryMiddleLeft.setTextureSize(textureWidth, textureHeight);

		BloomeryTopFront = new ModelRenderer(this, 42, 0);
		BloomeryTopFront.addBox(-6F, -6F, -6F, 12, 12, 3);
		BloomeryTopFront.setRotationPoint(0F, -14F, 0F);
		BloomeryTopFront.setTextureSize(textureWidth, textureHeight);

		BloomeryTopBack = new ModelRenderer(this, 42, 15);
		BloomeryTopBack.addBox(-6F, -6F, 3F, 12, 12, 3);
		BloomeryTopBack.setRotationPoint(0F, -14F, 0F);
		BloomeryTopBack.setTextureSize(textureWidth, textureHeight);

		BloomeryTopLeft = new ModelRenderer(this, 44, 30);
		BloomeryTopLeft.addBox(-6F, -6F, -3F, 3, 12, 6);
		BloomeryTopLeft.setRotationPoint(0F, -14F, 0F);
		BloomeryTopLeft.setTextureSize(textureWidth, textureHeight);

		BloomeryTopRight = new ModelRenderer(this, 62, 30);
		BloomeryTopRight.addBox(3F, -6F, -3F, 3, 12, 6);
		BloomeryTopRight.setRotationPoint(0F, -14F, 0F);
		BloomeryTopRight.setTextureSize(textureWidth, textureHeight);
	}

	public void renderBloomery(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		BloomeryBottomBack.render(f5);
		BloomeryBottomLeft.render(f5);
		BloomeryBottomRight.render(f5);
		BloomeryMiddleFront.render(f5);
		BloomeryMiddleBack.render(f5);
		BloomeryMiddleRight.render(f5);
		BloomeryMiddleLeft.render(f5);
		BloomeryTopFront.render(f5);
		BloomeryTopBack.render(f5);
		BloomeryTopLeft.render(f5);
		BloomeryTopRight.render(f5);
	}

}
