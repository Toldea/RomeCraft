package toldea.romecraft.entity.ai;

import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.tileentity.TileEntityBellows;

public class EntityAIBloomerySmelt extends EntityAIBloomeryInteract {
	int delayTimer;
	TileEntityBellows targetBellows;

	public EntityAIBloomerySmelt(EntityPleb entityPleb, boolean par2) {
		super(entityPleb);
		this.entityPleb = entityPleb;
		targetBellows = null;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.targetBellows != null && this.delayTimer > 0 && super.continueExecuting();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		System.out.println("EntityAIBloomerySmelt - startExecuting");
		this.delayTimer = 60;
		for (int i = 0; i < 4; i++) {
			TileEntityBellows bellows = this.targetBloomery.getAdjacentBellowsForDirection(i);
			if (bellows != null) {
				this.targetBellows = bellows;
				break;
			}
		}
		if (targetBellows != null) {
			System.out.println("EntityAIBloomerySmelt - pushing bellows!");
			targetBellows.pushBellows();
		}
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		targetBellows = null;
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		--this.delayTimer;
		super.updateTask();
	}

}
