package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelScutum extends ModelBase {// RomeCraftArmorModel {
	// private boolean isSneak = false;

	private ModelRenderer scutum;
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

		scutum = new ModelRenderer(this, 0, 0);
		scutum.addBox(-4F, 10F, -8F, 10, 1, 16);
		scutum.setRotationPoint(5F, 2F, 0F);
		scutum.setTextureSize(textureWidth, textureHeight);

		scutumDetail = new ModelRenderer(this, 52, 0);
		scutumDetail.addBox(0F, 11F, -1F, 2, 1, 2);
		scutumDetail.setRotationPoint(5F, 2F, 0F);
		scutumDetail.setTextureSize(textureWidth, textureHeight);
	}

	public void render(Entity entity) {
		// this.isSneak = entity.isSneaking();
		setRotationAngles();
		scutum.render(0.0625F);
		scutumDetail.render(0.0625F);
	}

	public void setRotationAngles() {
		scutum.rotateAngleX = scutumDetail.rotateAngleX = rotateAngleX;
		scutum.rotateAngleY = scutumDetail.rotateAngleY = rotateAngleY;
		scutum.rotateAngleZ = scutumDetail.rotateAngleZ = rotateAngleZ;

		/*
		scutum.rotationPointX = scutumDetail.rotationPointX = rotationPointX;
		scutum.rotationPointY = scutumDetail.rotationPointY = rotationPointY;
		scutum.rotationPointZ = scutumDetail.rotationPointZ = rotationPointZ;
		*/
	}
}