// Date: 9/19/2013 7:47:06 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPilum extends ModelBase {
	// fields
	ModelRenderer SpearBase;
	ModelRenderer SpearPoint1;
	ModelRenderer SpearPoint2;
	ModelRenderer Handle;
	ModelRenderer HandleGuard;

	public ModelPilum() {
		textureWidth = 32;
		textureHeight = 64;

		SpearBase = new ModelRenderer(this, 8, 0);
		SpearBase.addBox(0F, 0F, 0F, 1, 20, 1);
		SpearBase.setRotationPoint(0.5F, 0F, 0.5F);
		SpearBase.setTextureSize(64, 32);
		SpearBase.mirror = true;
		setRotation(SpearBase, 0F, 0F, 0F);
		SpearPoint1 = new ModelRenderer(this, 12, 0);
		SpearPoint1.addBox(0F, 0F, 0F, 3, 5, 2);
		SpearPoint1.setRotationPoint(-0.5F, 1.5F, 0F);
		SpearPoint1.setTextureSize(64, 32);
		SpearPoint1.mirror = true;
		setRotation(SpearPoint1, 0F, 0F, 0F);
		SpearPoint2 = new ModelRenderer(this, 12, 7);
		SpearPoint2.addBox(0F, 0F, 0F, 5, 3, 1);
		SpearPoint2.setRotationPoint(-1.5F, 2.5F, 0.5F);
		SpearPoint2.setTextureSize(64, 32);
		SpearPoint2.mirror = true;
		setRotation(SpearPoint2, 0F, 0F, 0F);
		Handle = new ModelRenderer(this, 0, 0);
		Handle.addBox(0F, 0F, 0F, 2, 44, 2);
		Handle.setRotationPoint(0F, 20F, 0F);
		Handle.setTextureSize(64, 32);
		Handle.mirror = true;
		setRotation(Handle, 0F, 0F, 0F);
		HandleGuard = new ModelRenderer(this, 12, 11);
		HandleGuard.addBox(0F, 0F, 0F, 3, 6, 3);
		HandleGuard.setRotationPoint(-0.5F, 21F, -0.5F);
		HandleGuard.setTextureSize(32, 64);
		HandleGuard.mirror = true;
		setRotation(HandleGuard, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		SpearBase.render(f5);
		SpearPoint1.render(f5);
		SpearPoint2.render(f5);
		Handle.render(f5);
		HandleGuard.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
	}

}