package toldea.romecraft.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.managers.TickManager;
import toldea.romecraft.romanvillage.RomanVillage;
import toldea.romecraft.romanvillage.RomanVillageBloomeryInfo;

public class EntityAIMoveTowardsBloomery extends EntityAIBase {
	private EntityPleb entityPleb;
	private RomanVillageBloomeryInfo bloomeryInfo;
	private int bloomeryPosX = -1;
	private int bloomeryPosZ = -1;

	public EntityAIMoveTowardsBloomery(EntityPleb entityPlebj) {
		this.entityPleb = entityPlebj;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		// Only Blacksmith plebs should execute tihs behavior.
		if (this.entityPleb.getProfession() != 1) {
			System.out.println("EntityAIMoveTowardsBloomery.shouldExecute - Profession is not blacksmithg!");
			return false;
		}
		if (this.entityPleb.worldObj.isDaytime() && !this.entityPleb.worldObj.isRaining() && !this.entityPleb.worldObj.provider.hasNoSky) {
			if (this.entityPleb.getRNG().nextInt(50) != 0) {
				return false;
			} else if (this.bloomeryPosX != -1
					&& this.entityPleb.getDistanceSq((double) this.bloomeryPosX, this.entityPleb.posY, (double) this.bloomeryPosZ) < 4.0D) {
				System.out.println("EntityAIMoveTowardsBloomery.shouldExecute - Too close to bloomery!");
				return false;
			} else {
				RomanVillage village = TickManager.romanVillageCollection.findNearestVillage(MathHelper.floor_double(this.entityPleb.posX),
						MathHelper.floor_double(this.entityPleb.posY), MathHelper.floor_double(this.entityPleb.posZ), 14);
				if (village == null) {
					System.out.println("EntityAIMoveTowardsBloomery.shouldExecute - Couldn't find village!");
					return false;
				} else {
					this.bloomeryInfo = (RomanVillageBloomeryInfo) village.findNearestObjectForInfoList(village.getBloomeryInfoList(),
							MathHelper.floor_double(this.entityPleb.posX), MathHelper.floor_double(this.entityPleb.posY),
							MathHelper.floor_double(this.entityPleb.posZ));
					if (bloomeryInfo == null) {
						System.out.println("EntityAIMoveTowardsBloomery.shouldExecute - Couldn't find bloomery!");
					}
					return this.bloomeryInfo != null;
				}
			}
		} else {
			System.out.println("EntityAIMoveTowardsBloomery.shouldExecute - Obey!");
			return false;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return !this.entityPleb.getNavigator().noPath();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		System.out.println("startExecuting");
		this.bloomeryPosX = -1;

		if (this.entityPleb.getDistanceSq((double) this.bloomeryInfo.posX, (double) this.bloomeryInfo.posY, (double) this.bloomeryInfo.posZ) > 256.0D) {
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityPleb, 14, 3, this.entityPleb.worldObj.getWorldVec3Pool()
					.getVecFromPool((double) this.bloomeryInfo.posX + 0.5D, (double) this.bloomeryInfo.posY, (double) this.bloomeryInfo.posZ + 0.5D));

			if (vec3 != null) {
				this.entityPleb.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D);
			}
		} else {
			this.entityPleb.getNavigator().tryMoveToXYZ((double) this.bloomeryInfo.posX + 0.5D, (double) this.bloomeryInfo.posY,
					(double) this.bloomeryInfo.posZ + 0.5D, 1.0D);
		}
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.bloomeryPosX = this.bloomeryInfo.posX;
		this.bloomeryPosZ = this.bloomeryInfo.posZ;
		this.bloomeryInfo = null;
	}
}
