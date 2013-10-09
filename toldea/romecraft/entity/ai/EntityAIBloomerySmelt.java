package toldea.romecraft.entity.ai;

import toldea.romecraft.entity.EntityPleb;

public class EntityAIBloomerySmelt extends EntityAIBloomeryInteract {

	boolean redstoneState;
	int delayTimer;

	public EntityAIBloomerySmelt(EntityPleb entityPleb, boolean par2) {
		super(entityPleb);
		this.entityPleb = entityPleb;
		this.redstoneState = par2;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.redstoneState && this.delayTimer > 0 && super.continueExecuting();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.delayTimer = 20;
		// this.targetBloomery.onPoweredBlockChange(this.entityPleb.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, true);
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		if (this.redstoneState) {
			// this.targetBloomery.onPoweredBlockChange(this.entityPleb.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ, false);
		}
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		--this.delayTimer;
		super.updateTask();
	}

}
