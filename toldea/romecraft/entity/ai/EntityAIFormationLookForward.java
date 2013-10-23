package toldea.romecraft.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import toldea.romecraft.entity.EntityLegionary;
import toldea.romecraft.entity.ai.Contubernium.Facing;

public class EntityAIFormationLookForward extends EntityAIBase {
	private EntityLegionary legionary;

	public EntityAIFormationLookForward(EntityLegionary legionary) {
		this.legionary = legionary;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	public void updateTask() {
		double x = legionary.posX;
		double z = legionary.posZ;
		Facing facing = SquadManager.getContubernium(legionary.getContuberniumId()).getFacing();
		switch (facing) {
		case NORTH:
			z -= 1.0d;
			break;
		case SOUTH:
			z += 1.0d;
			break;
		case EAST:
			x += 1.0d;
			break;
		case WEST:
			x -= 1.0d;
			break;
		default:
			break;
		}
		legionary.getLookHelper().setLookPosition(x, legionary.posY + legionary.getEyeHeight(), z, 10.0F, (float) legionary.getVerticalFaceSpeed());
	}
}
