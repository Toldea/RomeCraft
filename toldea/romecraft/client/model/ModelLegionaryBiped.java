package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import toldea.romecraft.client.renderer.RenderScutum;
import toldea.romecraft.managers.ItemManager;

public class ModelLegionaryBiped extends ModelBiped {
	public boolean aimedPilum;
	private boolean renderScutum = false;

	private RenderScutum scutumRenderer = null;

	public ModelLegionaryBiped() {
		super();
	}

	public ModelLegionaryBiped(float par1) {
		super(par1);
	}

	public ModelLegionaryBiped(float par1, float par2, int par3, int par4) {
		super(par1, par2, par3, par4);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms and legs, where par1 represents the
	 * time(so that arms and legs swing back and forth) and par2 represents how "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);

		if (this.aimedPilum) {
			float f6 = 0.0F;
			float f7 = 0.0F;
			// this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleZ = -((float) Math.PI / 16);
			this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F) + this.bipedHead.rotateAngleY;
			// this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedRightArm.rotateAngleX = -((float) Math.PI) + this.bipedHead.rotateAngleX;
			this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
		}
	}
	
	public void renderEquippedScutum(Entity entity) {
		renderScutum = isItemIdEquippedInSlot(entity, 0, ItemManager.itemScutum.itemID);
		if (renderScutum) {
			if (scutumRenderer == null) {
				scutumRenderer = new RenderScutum();
			}
			scutumRenderer.renderThirdPersonScutum(entity, bipedLeftArm);
		}
	}

	/**
	 * Returns true or false depending on if parsed Entity's itemStack contains an object with the specified itemId in the specified item slot.
	 * 
	 * @return
	 */
	protected boolean isItemIdEquippedInSlot(Entity entity, int slot, int itemId) {
		if (entity instanceof EntityLivingBase) {
			ItemStack itemStack = ((EntityLivingBase) entity).getCurrentItemOrArmor(slot);
			if (itemStack != null) {
				return (itemStack.itemID == itemId);
			}
		}
		return false;
	}
}
