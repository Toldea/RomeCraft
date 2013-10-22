package toldea.romecraft.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import toldea.romecraft.entity.EntityLegionary;
import toldea.romecraft.entity.ai.Contubernium.Facing;

public class EntityAIFormationMoveTowardsLocation extends EntityAIBase {
	World worldObj;
	EntityLegionary entityLegionary;

	private float distanceFromEntity;

	private double xPosition;
	private double yPosition;
	private double zPosition;
	private double speed;

	public EntityAIFormationMoveTowardsLocation(EntityLegionary par1Legionary, double par2Speed) {
		entityLegionary = par1Legionary;
		worldObj = entityLegionary.worldObj;
		speed = par2Speed;
	}

	@Override
	public boolean shouldExecute() {
		if (!entityLegionary.isRegistered()) {
			return false;
		}
		int contuberniumId = entityLegionary.getContuberniumId();
		Contubernium contubernium = SquadManager.getContubernium(contuberniumId);
		// If we have an active attack target, prioritize that over this behavior.
		if (contubernium == null || contubernium.getTargetEntity() != null) {
			return false;
		}

		Vec3 vec3 = contubernium.getTargetLocation();

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

	public boolean continueExecuting() {
		return !this.entityLegionary.getNavigator().noPath();
	}

	public void startExecuting() {
		EntityAIHelper.moveTowardsTargetPosition(entityLegionary, xPosition, yPosition, zPosition, 3d, speed);
	}
}
