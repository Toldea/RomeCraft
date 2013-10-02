package toldea.romecraft.client.model;

import toldea.romecraft.managers.ItemManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelScutum extends RomeCraftArmorModel {
	private boolean renderScutum = false;
	
	private ModelRenderer Scutum;
	private ModelRenderer ScutumDetail;

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
		renderScutum = isItemIdEquippedInSlot(entity, 0, ItemManager.itemScutum.itemID);
		System.out.println("Holding Scutum: " + isItemIdEquippedInSlot(entity, 0, ItemManager.itemScutum.itemID));
		this.isSneak = entity.isSneaking();
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		if (renderScutum) {
			Scutum.render(f5);
			ScutumDetail.render(f5);
		}
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
		float headAngleX = par5 / (180F / (float) Math.PI);
		float headAngleY = par4 / (180F / (float) Math.PI);

		float headRotationPointY = .0f;

		float bodyAngleX = .0f;
		float bodyAngleY = .0f;

		float rightArmAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 2.0F * par2 * 0.5F;
		float rightArmAngleY = 0.0F;
		float rightArmAngleZ = 0.0F;

		float rightArmRotationPointX = .0f;
		float rightArmRotationPointZ = .0f;

		float leftArmAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
		float leftArmAngleY = 0.0F;
		float leftArmAngleZ = 0.0F;

		float leftArmRotationPointX = .0f;
		float leftArmRotationPointZ = .0f;

		float rightLegAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		float rightLegAngleY = 0.0F;

		float rightLegRotationPointY = .0f;
		float rightLegRotationPointZ = .0f;

		float leftLegAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
		float leftLegAngleY = 0.0F;

		float leftLegRotationPointY = .0f;
		float leftLegRotationPointZ = .0f;

		if (this.isRiding) {
			rightArmAngleX += -((float) Math.PI / 5F);
			leftArmAngleX += -((float) Math.PI / 5F);
			rightLegAngleX = -((float) Math.PI * 2F / 5F);
			leftLegAngleX = -((float) Math.PI * 2F / 5F);
			rightLegAngleY = ((float) Math.PI / 10F);
			leftLegAngleY = -((float) Math.PI / 10F);
		}

		if (this.heldItemLeft != 0) {
			leftArmAngleX = leftArmAngleX * 0.5F - ((float) Math.PI / 10F) * (float) this.heldItemLeft;
		}

		if (this.heldItemRight != 0) {
			rightArmAngleX = rightArmAngleX * 0.5F - ((float) Math.PI / 10F) * (float) this.heldItemRight;
		}

		float f6;
		float f7;

		if (this.onGround > -9990.0F) {
			f6 = this.onGround;
			bodyAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float) Math.PI * 2.0F) * 0.2F;
			rightArmRotationPointZ = MathHelper.sin(bodyAngleY) * 5.0F;
			rightArmRotationPointX = -MathHelper.cos(bodyAngleY) * 5.0F;
			leftArmRotationPointZ = -MathHelper.sin(bodyAngleY) * 5.0F;
			leftArmRotationPointX = MathHelper.cos(bodyAngleY) * 5.0F;
			rightArmAngleY += bodyAngleY;
			leftArmAngleY += bodyAngleY;
			leftArmAngleX += bodyAngleY;
			f6 = 1.0F - this.onGround;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			f7 = MathHelper.sin(f6 * (float) Math.PI);
			float f8 = MathHelper.sin(this.onGround * (float) Math.PI) * -(headAngleX - 0.7F) * 0.75F;
			rightArmAngleX = (float) ((double) rightArmAngleX - ((double) f7 * 1.2D + (double) f8));
			rightArmAngleY += bodyAngleY * 2.0F;
			rightArmAngleZ = MathHelper.sin(this.onGround * (float) Math.PI) * -0.4F;
		}

		if (this.isSneak) {
			bodyAngleX = 0.5F;
			rightArmAngleX += 0.4F;
			leftArmAngleX += 0.4F;
			rightLegRotationPointZ = 4.0F;
			leftLegRotationPointZ = 4.0F;
			rightLegRotationPointY = 9.0F;
			leftLegRotationPointY = 9.0F;
			headRotationPointY = 1.0F;
		} else {
			bodyAngleX = 0.0F;
			rightLegRotationPointZ = 0.1F;
			leftLegRotationPointZ = 0.1F;
			rightLegRotationPointY = 12.0F;
			leftLegRotationPointY = 12.0F;
			headRotationPointY = 0.0F;
		}

		
		rightArmAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		leftArmAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		rightArmAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
		leftArmAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

		Scutum.rotateAngleX = ScutumDetail.rotateAngleX = leftArmAngleX;
		Scutum.rotateAngleY = ScutumDetail.rotateAngleY = leftArmAngleY;
		Scutum.rotateAngleZ = ScutumDetail.rotateAngleZ = leftArmAngleZ;

		Scutum.rotationPointX = ScutumDetail.rotationPointX = leftArmRotationPointX;
		Scutum.rotationPointZ = ScutumDetail.rotationPointZ = leftArmRotationPointZ;
	}
}
