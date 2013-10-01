package toldea.romecraft.entity.ai;

import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.managers.TickManager;
import toldea.romecraft.romanvillage.RomanVillage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityAIPlebMate extends EntityAIBase {
	private EntityPleb plebObj;
	private EntityPleb mate;
	private World worldObj;
	private int matingTimeout;
	RomanVillage romanVillageObj;

	public EntityAIPlebMate(EntityPleb par1EntityPleb) {
		this.plebObj = par1EntityPleb;
		this.worldObj = par1EntityPleb.worldObj;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.plebObj.getGrowingAge() != 0) {
			return false;
		} else if (this.plebObj.getRNG().nextInt(500) != 0) {
			return false;
		} else {
			this.romanVillageObj = TickManager.romanVillageCollection
					.findNearestVillage(
							MathHelper.floor_double(this.plebObj.posX),
							MathHelper.floor_double(this.plebObj.posY),
							MathHelper.floor_double(this.plebObj.posZ), 0);
			if (this.romanVillageObj == null) {
				return false;
			} else if (!this.checkSufficientDoorsPresentForNewVillager()) {
				return false;
			} else {
				Entity entity = this.worldObj.findNearestEntityWithinAABB(
						EntityPleb.class,
						this.plebObj.boundingBox.expand(8.0D, 3.0D, 8.0D),
						this.plebObj);

				if (entity == null) {
					return false;
				} else {
					this.mate = (EntityPleb) entity;
					return this.mate.getGrowingAge() == 0;
				}
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.matingTimeout = 300;
		this.plebObj.setMating(true);
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.romanVillageObj = null;
		this.mate = null;
		this.plebObj.setMating(false);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.matingTimeout >= 0
				&& this.checkSufficientDoorsPresentForNewVillager()
				&& this.plebObj.getGrowingAge() == 0;
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		--this.matingTimeout;
		this.plebObj.getLookHelper().setLookPositionWithEntity(this.mate,
				10.0F, 30.0F);

		if (this.plebObj.getDistanceSqToEntity(this.mate) > 2.25D) {
			this.plebObj.getNavigator().tryMoveToEntityLiving(this.mate, 0.25D);
		} else if (this.matingTimeout == 0 && this.mate.isMating()) {
			System.out.println("ITS HAPPENING! :D");
			this.giveBirth();
		}

		if (this.plebObj.getRNG().nextInt(35) == 0) {
			this.worldObj.setEntityState(this.plebObj, (byte) 12);
		}
	}

	private boolean checkSufficientDoorsPresentForNewVillager() {
		if (!this.romanVillageObj.isMatingSeason()) {
			return false;
		} else {
			return this.romanVillageObj.getNumPlebs() < romanVillageObj
					.getMaxNumberOfPlebs();
		}
	}

	private void giveBirth() {
		EntityPleb entityPleb = this.plebObj.createPlebChild(this.mate);
		this.mate.setGrowingAge(6000);
		this.plebObj.setGrowingAge(6000);
		entityPleb.setGrowingAge(-24000);
		entityPleb.setLocationAndAngles(this.plebObj.posX, this.plebObj.posY,
				this.plebObj.posZ, 0.0F, 0.0F);
		this.worldObj.spawnEntityInWorld(entityPleb);
		this.worldObj.setEntityState(entityPleb, (byte) 12);
	}
}
