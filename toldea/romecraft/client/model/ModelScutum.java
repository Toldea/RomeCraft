package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelScutum extends ModelBase {// RomeCraftArmorModel {
	// private boolean isSneak = false;

	private ModelRenderer armRotationPoint;
	private ModelRenderer handRotationPoint;
	private ModelRenderer scutumFront;
	private ModelRenderer scutumMid1;
	private ModelRenderer scutumMid2;
	private ModelRenderer scutumBack1;
	private ModelRenderer scutumBack2;
	private ModelRenderer scutumDetail;

	public float rotateAngleX = .0f;
	public float rotateAngleY = .0f;
	public float rotateAngleZ = .0f;

	public float rotationPointX = .0f;
	public float rotationPointY = .0f;
	public float rotationPointZ = .0f;

	public ModelScutum() {
		textureWidth = 64;
		textureHeight = 32;

		armRotationPoint = new ModelRenderer(this, 0, 0);
		armRotationPoint.setRotationPoint(5F, 2F, 0F);

		handRotationPoint = new ModelRenderer(this, 0, 0);
		// handRotationPoint.setRotationPoint(1F, 10F, 0F); // Original rotation point
		handRotationPoint.setRotationPoint(1F, 10.7F, 0F); // Slightly offset rotation point to prevent the hand from clipping at angles.

		scutumFront = new ModelRenderer(this, 0, 0);
		scutumFront.addBox(-8F, -0.5F, -3F, 16, 1, 6);
		scutumFront.setTextureSize(textureWidth, textureHeight);
		setRotation(scutumFront, 0F, 1.570796F, 0F);

		scutumMid1 = new ModelRenderer(this, 0, 7);
		scutumMid1.addBox(-8F, -1.5F, -4F, 16, 1, 1);
		scutumMid1.setTextureSize(textureWidth, textureHeight);
		setRotation(scutumMid1, 0F, 1.570796F, 0F);

		scutumMid2 = new ModelRenderer(this, 0, 9);
		scutumMid2.addBox(-8F, -1.5F, 3F, 16, 1, 1);
		scutumMid2.setTextureSize(textureWidth, textureHeight);
		setRotation(scutumMid2, 0F, 1.570796F, 0F);

		scutumBack1 = new ModelRenderer(this, 0, 11);
		scutumBack1.addBox(-8F, -2.5F, -5F, 16, 1, 1);
		scutumBack1.setTextureSize(textureWidth, textureHeight);
		setRotation(scutumBack1, 0F, 1.570796F, 0F);

		scutumBack2 = new ModelRenderer(this, 0, 13);
		scutumBack2.addBox(-8F, -2.5F, 4F, 16, 1, 1);
		scutumBack2.setTextureSize(textureWidth, textureHeight);
		setRotation(scutumBack2, 0F, 1.570796F, 0F);

		scutumDetail = new ModelRenderer(this, 34, 7);
		scutumDetail.addBox(-1F, 0.5F, -1F, 2, 1, 2);
		scutumDetail.setTextureSize(textureWidth, textureHeight);
		setRotation(scutumDetail, 0F, 1.570796F, 0F);

		handRotationPoint.addChild(scutumFront);
		handRotationPoint.addChild(scutumMid1);
		handRotationPoint.addChild(scutumMid2);
		handRotationPoint.addChild(scutumBack1);
		handRotationPoint.addChild(scutumBack2);
		handRotationPoint.addChild(scutumDetail);

		armRotationPoint.addChild(handRotationPoint);

		handRotationPoint.rotateAngleX = -(float) Math.PI / 6f;
	}

	public void render(Entity entity) {
		// this.isSneak = entity.isSneaking();
		setRotationAngles();
		armRotationPoint.render(0.0625F);
	}

	public void setRotationAngles() {
		armRotationPoint.rotateAngleX = rotateAngleX;
		armRotationPoint.rotateAngleY = rotateAngleY;
		armRotationPoint.rotateAngleZ = rotateAngleZ;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}