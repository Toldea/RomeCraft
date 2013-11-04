package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelScutum extends ModelBase {// RomeCraftArmorModel {
	// private boolean isSneak = false;

	private ModelRenderer armRotationPoint;
	private ModelRenderer handRotationPoint;
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
		
		armRotationPoint = new ModelRenderer(this, 0, 0);
		armRotationPoint.setRotationPoint(5F, 2F, 0F);
		
		handRotationPoint = new ModelRenderer(this, 0, 0);
		//handRotationPoint.setRotationPoint(1F, 10F, 0F); // Original rotation point
		handRotationPoint.setRotationPoint(1F, 10.7F, 0F); // Slightly offset rotation point to prevent the hand from clipping at angles.
		
		scutum = new ModelRenderer(this, 0, 0);
		scutum.addBox(-5F, -.5F, -8F, 10, 1, 16);
		scutum.setTextureSize(textureWidth, textureHeight);

		scutumDetail = new ModelRenderer(this, 0, 0);
		scutumDetail.addBox(-1f, .5f, -1f, 2, 1, 2);
		scutumDetail.setTextureSize(textureWidth, textureHeight);
		
		handRotationPoint.addChild(scutum);
		handRotationPoint.addChild(scutumDetail);
		
		armRotationPoint.addChild(handRotationPoint);
		
		handRotationPoint.rotateAngleX = -(float)Math.PI / 6f;
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
}