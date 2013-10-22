package toldea.romecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIHelper {
	private EntityAIHelper() {
	}

	/**
	 * Try and move the entity towards the target entity. If the distance is more than 16 blocks it will calculate a path in that direction. If the distance is
	 * less than inRangeDistSquared it will do nothing.
	 */
	public static void moveTowardsTargetEntity(EntityCreature entity, EntityLivingBase targetEntity, double inRangeDistSquared, double speed) {
		double dist = entity.getDistanceSq(targetEntity.posX, targetEntity.posX, targetEntity.posZ);
		if (dist > inRangeDistSquared) {
			if (dist > 256.0d) {
				Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(entity, 14, 3,
						entity.worldObj.getWorldVec3Pool().getVecFromPool(targetEntity.posX, targetEntity.posY, targetEntity.posZ));
				if (vec3 != null) {
					entity.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, speed);
				}
			} else {
				entity.getNavigator().tryMoveToXYZ(targetEntity.posX, targetEntity.posY, targetEntity.posZ, speed);
			}
		}
	}

	/**
	 * Try and move the entity towards the target x y z. If the distance is more than 16 blocks it will calculate a path in that direction. If the distance is
	 * less than inRangeDistSquared it will do nothing.
	 */
	public static void moveTowardsTargetPosition(EntityCreature entity, double x, double y, double z, double inRangeDistSquared, double speed) {
		double dist = entity.getDistanceSq(x, y, z);
		if (dist > inRangeDistSquared) {
			// If the distance is too large (more than 16 blocks), calculate a path in that direction.
			if (dist > 256.0d) {
				// Calculate the delta x and z, get the magnitude of that vector and then try to move 15 blocks in that direction. 
				double dx = x - entity.posX;
				double dz = z - entity.posZ;
				double magnitude = Math.sqrt((dx * dx) + (dz * dz));
				//System.out.println("-----------------------------------");
				//System.out.println("Entity Location:  x: " + entity.posX + ", z: " + entity.posZ);
				//System.out.println("Target Location:  x: " + x + ", z: " + z);
				//System.out.println("Dynamic Location: x: " + (x + dx / length * 15d) + ", z: " + (z + dz / length * 15d));
				
				entity.getNavigator().tryMoveToXYZ(entity.posX + dx / magnitude * 15d, y, entity.posZ + dz / magnitude * 15d, speed);
				/*
				System.out.println("Distance too large for direct path, calculating path towards target location.");
				Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(entity, 16, 7, entity.worldObj.getWorldVec3Pool().getVecFromPool(x, y, z));
				if (vec3 != null) {
					System.out.println("moving in the general direction of the target location!");
					entity.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, speed);
				}*/
			} else {
				entity.getNavigator().tryMoveToXYZ(x, y, z, speed);
			}
		}
	}
}
