package toldea.romecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.world.World;
import toldea.romecraft.entity.EntityLegionary;
import toldea.romecraft.entity.EntityLegionary.LEGIONARY_EQUIPMENT;

public class EntityAIMeleeAttack2 extends EntityAIBase {
	World worldObj;
	EntityLegionary legionary;

	/**
	 * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
	 */
	int attackTick;

	/** The speed with which the mob will approach the target */
	double speedTowardsTarget;

	/** The PathEntity of our entity. */
	PathEntity entityPathEntity;
	private int attackTimer;

	//private int failedPathFindingPenalty;

	private double engageRangeSquared;

	public EntityAIMeleeAttack2(EntityLegionary legionary, double speed, double engageRange) {
		this.legionary = legionary;
		this.worldObj = legionary.worldObj;
		this.speedTowardsTarget = speed;
		this.engageRangeSquared = engageRange * engageRange;
		this.setMutexBits(0);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = legionary.getAttackTarget();

		if (entitylivingbase == null) {
			return false;
		} else if (!entitylivingbase.isEntityAlive()) {
			return false;
		} else if (legionary.getAttackTarget().getDistanceSqToEntity(legionary) > engageRangeSquared) {
			return false;
		} else {
			if (--this.attackTimer <= 0) {
				this.entityPathEntity = legionary.getNavigator().getPathToEntityLiving(entitylivingbase);
				this.attackTimer = 4 + legionary.getRNG().nextInt(7);
			}
			legionary.equipItem(LEGIONARY_EQUIPMENT.GLADIUS);
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		EntityLivingBase entitylivingbase = legionary.getAttackTarget();
		return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : !legionary.getNavigator().noPath());
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		legionary.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
		this.attackTimer = 0;
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		legionary.getNavigator().clearPathEntity();
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		EntityLivingBase entitylivingbase = legionary.getAttackTarget();
		legionary.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);

		if (legionary.getEntitySenses().canSee(entitylivingbase) && --this.attackTimer <= 0) {
			this.attackTimer = /*failedPathFindingPenalty +*/ 4 + legionary.getRNG().nextInt(7);

			legionary.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget);
			//if (legionary.getNavigator().getPath() != null) {
				//PathPoint finalPathPoint = legionary.getNavigator().getPath().getFinalPathPoint();
				//if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < 1) {
					//failedPathFindingPenalty = 0;
				//} else {
					//failedPathFindingPenalty += 10;
				//}
			//} //else {
				//failedPathFindingPenalty += 10;
			//}
		}

		this.attackTick = Math.max(this.attackTick - 1, 0);
		double d0 = (double) (legionary.width * 2.0F * legionary.width * 2.0F + entitylivingbase.width);

		if (legionary.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY, entitylivingbase.posZ) <= d0) {
			if (this.attackTick <= 0) {
				this.attackTick = 20;

				if (legionary.getHeldItem() != null) {
					legionary.swingItem();
				}

				legionary.attackEntityAsMob(entitylivingbase);
			}
		}
	}
}
