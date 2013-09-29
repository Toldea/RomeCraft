package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelScutum extends ModelBase {
	ModelRenderer Scutum;
	ModelRenderer ScutumDetail;

	public ModelScutum() {
		textureWidth = 32;
		textureHeight = 32;

		Scutum = new ModelRenderer(this, 0, 0);
		Scutum.addBox(-5F, -8F, -0.5F, 10, 16, 1);
		Scutum.setRotationPoint(5F, 2F, 0F);
		Scutum.setTextureSize(textureWidth, textureHeight);


		ScutumDetail = new ModelRenderer(this, 22, 0);
		ScutumDetail.addBox(-1F, -1F, -1.5F, 2, 2, 1);
		ScutumDetail.setRotationPoint(5F, 2F, 0F);
		ScutumDetail.setTextureSize(textureWidth, textureHeight);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		//super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(entity.isSneaking());
		Scutum.render(f5);
		ScutumDetail.render(f5);
	}

	public void setRotationAngles(boolean isSneaking) {
		float leftArmAngleX = MathHelper.cos(0f * 0.6662F) * 2.0F * 0f * 0.5F;
		float leftArmAngleY = 0.0F;
		float leftArmAngleZ = 0.0F;

		float leftArmRotationPointX = .0f;
		float leftArmRotationPointZ = .0f;

		if (this.isRiding) {
			leftArmAngleX += -((float) Math.PI / 5F);
		}
		
		leftArmAngleX = leftArmAngleX * 0.5F - ((float) Math.PI / 10F);

		/*
		if (this.heldItemLeft != 0) {
			leftArmAngleX = leftArmAngleX * 0.5F - ((float) Math.PI / 10F) * (float) this.heldItemLeft;
		}
		 */
		
		float f6;
		float f7;
		//if (this.onGround > -9990.0F) {
		if (true) {
			f6 = this.onGround;
			float bodyAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float) Math.PI * 2.0F) * 0.2F;
			leftArmRotationPointZ = -MathHelper.sin(bodyAngleY) * 5.0F;
			leftArmRotationPointX = MathHelper.cos(bodyAngleY) * 5.0F;
			leftArmAngleY += bodyAngleY;
			leftArmAngleX += bodyAngleY;
		}

		if (isSneaking) {
			leftArmAngleX += 0.4F;
		}

		leftArmAngleZ -= MathHelper.cos(0f * 0.09F) * 0.05F + 0.05F;
		leftArmAngleX -= MathHelper.sin(0f * 0.067F) * 0.05F;
		
		//
		leftArmAngleZ += -1.570796F;
		//

		Scutum.rotateAngleX = ScutumDetail.rotateAngleX = leftArmAngleX;
		Scutum.rotateAngleY = ScutumDetail.rotateAngleY = leftArmAngleY;
		Scutum.rotateAngleZ = ScutumDetail.rotateAngleZ = leftArmAngleZ;

		Scutum.rotationPointX = ScutumDetail.rotationPointX = leftArmRotationPointX;
		Scutum.rotationPointZ = ScutumDetail.rotationPointZ = leftArmRotationPointZ;
	}
}
