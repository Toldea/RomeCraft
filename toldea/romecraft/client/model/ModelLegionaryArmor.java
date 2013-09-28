package toldea.romecraft.client.model;

import toldea.romecraft.managers.ItemManager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ModelLegionaryArmor extends ModelBiped {
	private boolean renderLoricaSegmentata = false;
	private boolean renderGalea = false;
	private boolean aimedPilum = false;

	private ModelRenderer lowertorso;
	private ModelRenderer uppertorso;

	private ModelRenderer rightshoulderupper;
	private ModelRenderer rightshouldermiddle;
	private ModelRenderer rightshoulderlower;

	private ModelRenderer leftshoulderupper;
	private ModelRenderer leftshouldermiddle;
	private ModelRenderer leftshoulderlower;

	private ModelRenderer galea1;
	private ModelRenderer galea2;
	private ModelRenderer galea3;

	private ModelRenderer crestholder;
	private ModelRenderer crest;

	public ModelLegionaryArmor() {
		textureWidth = 128;
		textureHeight = 64;

		lowertorso = new ModelRenderer(this, 28, 0);
		lowertorso.addBox(-4.5F, 4F, -2.5F, 9, 7, 5);
		lowertorso.setRotationPoint(0F, 0F, 0F);
		lowertorso.setTextureSize(128, 64);

		uppertorso = new ModelRenderer(this, 0, 0);
		uppertorso.addBox(-3.5F, 0F, -3.5F, 7, 5, 7);
		uppertorso.setRotationPoint(0F, 0F, 0F);
		uppertorso.setTextureSize(128, 64);

		rightshoulderupper = new ModelRenderer(this, 0, 12);
		rightshoulderupper.addBox(-7F, -1.5F, -4F, 3, 7, 8);
		rightshoulderupper.setRotationPoint(0F, 0F, 0F);
		rightshoulderupper.setTextureSize(128, 64);

		rightshouldermiddle = new ModelRenderer(this, 22, 12);
		rightshouldermiddle.addBox(-8F, -1F, -3.5F, 2, 6, 7);
		rightshouldermiddle.setRotationPoint(0F, 0F, 0F);
		rightshouldermiddle.setTextureSize(128, 64);

		rightshoulderlower = new ModelRenderer(this, 40, 12);
		rightshoulderlower.addBox(-9F, -0.5F, -3F, 2, 5, 6);
		rightshoulderlower.setRotationPoint(0F, 0F, 0F);
		rightshoulderlower.setTextureSize(128, 64);

		leftshoulderupper = new ModelRenderer(this, 0, 12);
		leftshoulderupper.addBox(4F, -1.5F, -4F, 3, 7, 8);
		leftshoulderupper.setRotationPoint(0F, 0F, 0F);
		leftshoulderupper.setTextureSize(128, 64);
		leftshoulderupper.mirror = true;

		leftshouldermiddle = new ModelRenderer(this, 22, 12);
		leftshouldermiddle.addBox(6F, -1F, -3.5F, 2, 6, 7);
		leftshouldermiddle.setRotationPoint(0F, 0F, 0F);
		leftshouldermiddle.setTextureSize(128, 64);
		leftshouldermiddle.mirror = true;

		leftshoulderlower = new ModelRenderer(this, 40, 12);
		leftshoulderlower.addBox(7F, -0.5F, -3F, 2, 5, 6);
		leftshoulderlower.setRotationPoint(0F, 0F, 0F);
		leftshoulderlower.setTextureSize(128, 64);
		leftshoulderlower.mirror = true;

		galea1 = new ModelRenderer(this, 0, 36);
		galea1.addBox(-4.5F, -8.5F, -5.5F, 9, 9, 10);
		galea1.setRotationPoint(0F, 0F, 0F);
		galea1.setTextureSize(128, 64);

		galea2 = new ModelRenderer(this, 0, 55);
		galea2.addBox(-3.5F, -9.5F, -4.5F, 7, 1, 8);
		galea2.setRotationPoint(0F, 0F, 0F);
		galea2.setTextureSize(128, 64);

		galea3 = new ModelRenderer(this, 30, 55);
		galea3.addBox(-2.5F, -10.5F, -3.5F, 5, 1, 6);
		galea3.setRotationPoint(0F, 0F, 0F);
		galea3.setTextureSize(128, 64);

		crestholder = new ModelRenderer(this, 52, 55);
		crestholder.addBox(-0.5F, -12.5F, -1F, 1, 2, 1);
		crestholder.setRotationPoint(0F, 0F, 0F);
		crestholder.setTextureSize(128, 64);

		crest = new ModelRenderer(this, 38, 35);
		crest.addBox(-0.5F, -16.5F, -7.5F, 1, 6, 14);
		crest.setRotationPoint(0F, 0F, 0F);
		crest.setTextureSize(128, 64);
	}

	/**
	 * Returns true or false depending on if parsed Entity's itemStack contains an object with the specified itemId in the specified item slot.
	 * 
	 * @return
	 */
	private boolean isItemIdEquippedInSlot(Entity entity, int slot, int itemId) {
		if (entity instanceof EntityLivingBase) {
			ItemStack itemStack = ((EntityLivingBase) entity).getCurrentItemOrArmor(slot);
			if (itemStack != null) {
				return (itemStack.itemID == itemId);
			}
		}
		return false;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		aimedPilum = isItemIdEquippedInSlot(entity, 0, ItemManager.itemPilum.itemID);
		renderLoricaSegmentata = isItemIdEquippedInSlot(entity, 3, ItemManager.itemLoricaSegmentata.itemID);
		renderGalea = isItemIdEquippedInSlot(entity, 4, ItemManager.itemGalea.itemID);

		this.isSneak = entity.isSneaking();
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		if (renderLoricaSegmentata) {
			uppertorso.render(f5);
			lowertorso.render(f5);

			rightshoulderupper.render(f5);
			rightshouldermiddle.render(f5);
			rightshoulderlower.render(f5);

			leftshoulderupper.render(f5);
			leftshouldermiddle.render(f5);
			leftshoulderlower.render(f5);
		}

		if (renderGalea) {
			galea1.render(f5);
			galea2.render(f5);
			galea3.render(f5);
			crestholder.render(f5);
			crest.render(f5);
		}
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
		if (!renderLoricaSegmentata && !renderGalea) {
			return;
		}

		float headAngleX = par5 / (180F / (float) Math.PI);
		float headAngleY = par4 / (180F / (float) Math.PI);

		float bodyAngleX = .0f;

		float rightArmAngleX = .0f;
		float rightArmAngleY = .0f;
		float rightArmAngleZ = .0f;

		float leftArmAngleX = .0f;
		float leftArmAngleY = .0f;
		float leftArmAngleZ = .0f;

		float rightLegAngleX = .0f;
		float rightLegAngleY = .0f;
		float rightLegAngleZ = .0f;

		float leftLegAngleX = .0f;
		float leftLegAngleY = .0f;
		float leftLegAngleZ = .0f;

		rightArmAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 2.0F * par2 * 0.5F;
		leftArmAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
		rightLegAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		leftLegAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;

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

		// if (this.onGround > -9990.0F) {} // Not sure when this is set.

		if (this.isSneak) {
			bodyAngleX = 0.5F;
			rightArmAngleX += 0.4F;
			leftArmAngleX += 0.4F;
			rightLegAngleZ = 4.0F;
			leftLegAngleZ = 4.0F;
			rightLegAngleY = 9.0F;
			leftLegAngleY = 9.0F;
			headAngleY = 1.0F;
		} else {
			bodyAngleX = 0.0F;
			rightLegAngleZ = 0.1F;
			leftLegAngleZ = 0.1F;
			rightLegAngleY = 12.0F;
			leftLegAngleY = 12.0F;
			headAngleY = 0.0F;
		}

		rightArmAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		leftArmAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		rightArmAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
		leftArmAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

		// if (this.aimedBow) {}
		if (aimedPilum) {
			/*
			 * f6 = 0.0F; f7 = 0.0F; rightArmAngleZ = -((float)Math.PI / 16); rightArmAngleY = -(0.1F - f6 * 0.6F) + this.bipedHead.rotateAngleY; rightArmAngleX
			 * = -((float)Math.PI) + this.bipedHead.rotateAngleX; rightArmAngleX -= f6 * 1.2F - f7 * 0.4F; rightArmAngleZ += MathHelper.cos(par3 * 0.09F) *
			 * 0.05F + 0.05F; rightArmAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
			 */
		}

		if (renderLoricaSegmentata) {
			lowertorso.rotateAngleX = uppertorso.rotateAngleX = bodyAngleX;

			rightshoulderupper.rotateAngleX = rightshouldermiddle.rotateAngleX = rightshoulderlower.rotateAngleX = rightArmAngleX;
			rightshoulderupper.rotateAngleY = rightshouldermiddle.rotateAngleY = rightshoulderlower.rotateAngleY = rightArmAngleY;
			rightshoulderupper.rotateAngleZ = rightshouldermiddle.rotateAngleZ = rightshoulderlower.rotateAngleZ = rightArmAngleZ;

			leftshoulderupper.rotateAngleX = leftshouldermiddle.rotateAngleX = leftshoulderlower.rotateAngleX = leftArmAngleX;
			leftshoulderupper.rotateAngleY = leftshouldermiddle.rotateAngleY = leftshoulderlower.rotateAngleY = leftArmAngleY;
			leftshoulderupper.rotateAngleZ = leftshouldermiddle.rotateAngleZ = leftshoulderlower.rotateAngleZ = leftArmAngleZ;
		}
		if (renderGalea) {
			galea1.rotateAngleX = galea2.rotateAngleX = galea3.rotateAngleX = crestholder.rotateAngleX = crest.rotateAngleX = headAngleX;
			galea1.rotateAngleY = galea2.rotateAngleY = galea3.rotateAngleY = crestholder.rotateAngleY = crest.rotateAngleY = headAngleY;
		}
	}
}
