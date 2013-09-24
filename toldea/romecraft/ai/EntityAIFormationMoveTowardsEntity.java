package toldea.romecraft.ai;

import toldea.romecraft.ai.Contubernium.Facing;
import toldea.romecraft.entity.EntityLegionary;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIFormationMoveTowardsEntity extends EntityAIBase {
	private EntityLegionary entityLegionary;
	private EntityLivingBase targetEntity;
	private double xPosition;
	private double yPosition;
	private double zPosition;
	private double speed;

	/**
	 * If the distance to the target entity is further than this, this AI task will not run.
	 */
	private float maxTargetDistance;

	public EntityAIFormationMoveTowardsEntity(EntityLegionary par1EntityLegionary, double par2, float par4) {
		this.entityLegionary = par1EntityLegionary;
		this.speed = par2;
		this.maxTargetDistance = par4;
		// this.setMutexBits(1);
		// this.setMutexBits(3);
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
		this.targetEntity = contubernium.getTargetEntity();

		if (this.targetEntity == null) {
			return false;
		} else if (this.targetEntity.getDistanceSqToEntity(this.entityLegionary) > (double) (this.maxTargetDistance * this.maxTargetDistance)) {
			return false;
		} else {
			Vec3 vec3 = targetEntity.getPosition(1.0f);

			if (vec3 == null) {
				return false;
			} else {
				float offset = SquadManager.getFormationOffsetForContubernium(contubernium);
				Facing facing = contubernium.getFacing();
				switch (facing) {
				case NORTH:
					this.xPosition = vec3.xCoord + (offset * Contubernium.tightness)
							- (entityLegionary.getSquadIndex() % Contubernium.files * Contubernium.tightness);
					this.zPosition = vec3.zCoord + ((int) (entityLegionary.getSquadIndex() / Contubernium.files) * Contubernium.tightness);
					break;
				case SOUTH:
					this.xPosition = vec3.xCoord + (entityLegionary.getSquadIndex() % Contubernium.files * Contubernium.tightness)
							- (offset * Contubernium.tightness);
					this.zPosition = vec3.zCoord - ((int) (entityLegionary.getSquadIndex() / Contubernium.files) * Contubernium.tightness);
					break;
				case EAST:
					this.xPosition = vec3.xCoord - ((int) (entityLegionary.getSquadIndex() / Contubernium.files) * Contubernium.tightness);
					this.zPosition = vec3.zCoord + (offset * Contubernium.tightness)
							- (entityLegionary.getSquadIndex() % Contubernium.files * Contubernium.tightness);
					break;
				case WEST:
					this.xPosition = vec3.xCoord + ((int) (entityLegionary.getSquadIndex() / Contubernium.files) * Contubernium.tightness);
					this.zPosition = vec3.zCoord + (entityLegionary.getSquadIndex() % Contubernium.files * Contubernium.tightness)
							- (offset * Contubernium.tightness);
					break;
				default:
					break;
				}

				this.yPosition = vec3.yCoord;

				if (entityLegionary.getDistanceSq(xPosition, yPosition, zPosition) < 3d) {
					return false;
				}

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
		// public void updateTask() {
		this.entityLegionary.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
	}
}