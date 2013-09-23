package toldea.romecraft.ai;

import toldea.romecraft.entity.EntityLegionary;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIFormationMoveTowardsTarget extends EntityAIBase {
	private EntityLegionary entityLegionary;
	private EntityLivingBase targetEntity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private double speed;

	/**
	 * If the distance to the target entity is further than this, this AI task will not run.
	 */
	private float maxTargetDistance;

	public EntityAIFormationMoveTowardsTarget(EntityLegionary par1EntityLegionary, double par2, float par4) {
		this.entityLegionary = par1EntityLegionary;
		this.speed = par2;
		this.maxTargetDistance = par4;
		//this.setMutexBits(1);
		//this.setMutexBits(3);
		this.setMutexBits(0);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (!entityLegionary.isRegistered()) {
			return false;
		}
		int contuberniumId = entityLegionary.getContuberniumId();
		Contubernium contubernium = SquadManager.getContubernium(contuberniumId);
		this.targetEntity = contubernium.getTargetEntity();//this.entityLegionary.getAttackTarget();

		if (this.targetEntity == null) {
			return false;
		} else if (this.targetEntity.getDistanceSqToEntity(this.entityLegionary) > (double) (this.maxTargetDistance * this.maxTargetDistance)) {
			return false;
		} else {
			/*
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityLegionary, 16, 7, this.entityLegionary.worldObj.getWorldVec3Pool()
					.getVecFromPool(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));
			*/
			
			Vec3 vec3 = targetEntity.getPosition(1.0f);
			
			if (vec3 == null) {
				return false;
			} else {
				float offset = SquadManager.getFormationOffsetForContubernium(contubernium);
				this.movePosX = vec3.xCoord + (entityLegionary.getSquadIndex() % Contubernium.files * Contubernium.tightness) - (offset * Contubernium.tightness);
				this.movePosY = vec3.yCoord;
				this.movePosZ = vec3.zCoord + ((int)(entityLegionary.getSquadIndex() / Contubernium.files) * Contubernium.tightness);
				return true;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return !this.entityLegionary.getNavigator().noPath() && this.targetEntity.isEntityAlive()
				&& this.targetEntity.getDistanceSqToEntity(this.entityLegionary) < (double) (this.maxTargetDistance * this.maxTargetDistance);
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.targetEntity = null;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.entityLegionary.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
	}
}