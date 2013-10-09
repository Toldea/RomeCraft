package toldea.romecraft.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import toldea.romecraft.block.BlockBloomery;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class EntityAIBloomeryInteract extends EntityAIBase {
	protected EntityPleb entityPleb;
	protected int entityPosX;
	protected int entityPosY;
	protected int entityPosZ;
	protected BlockBloomery targetBloomery;

	/**
	 * If is true then the Entity has stopped Door Interaction and compoleted the task.
	 */
	boolean hasStoppedUsingBloomery;
	float entityPositionX;
	float entityPositionZ;

	public EntityAIBloomeryInteract(EntityPleb entityPleb) {
		this.entityPleb = entityPleb;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (!this.entityPleb.isCollidedHorizontally) {
			return false;
		} else {
			PathNavigate pathnavigate = this.entityPleb.getNavigator();
			PathEntity pathentity = pathnavigate.getPath();

			if (pathentity != null && !pathentity.isFinished() && pathnavigate.getCanBreakDoors()) {
				for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); ++i) {
					PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
					this.entityPosX = pathpoint.xCoord;
					this.entityPosY = pathpoint.yCoord + 1;
					this.entityPosZ = pathpoint.zCoord;

					if (this.entityPleb.getDistanceSq((double) this.entityPosX, this.entityPleb.posY, (double) this.entityPosZ) <= 2.25D) {
						this.targetBloomery = this.findUsableBloomery(this.entityPosX, this.entityPosY, this.entityPosZ);

						if (this.targetBloomery != null) {
							return true;
						}
					}
				}

				this.entityPosX = MathHelper.floor_double(this.entityPleb.posX);
				this.entityPosY = MathHelper.floor_double(this.entityPleb.posY + 1.0D);
				this.entityPosZ = MathHelper.floor_double(this.entityPleb.posZ);
				this.targetBloomery = this.findUsableBloomery(this.entityPosX, this.entityPosY, this.entityPosZ);
				return this.targetBloomery != null;
			} else {
				return false;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return !this.hasStoppedUsingBloomery;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.hasStoppedUsingBloomery = false;
		this.entityPositionX = (float) ((double) ((float) this.entityPosX + 0.5F) - this.entityPleb.posX);
		this.entityPositionZ = (float) ((double) ((float) this.entityPosZ + 0.5F) - this.entityPleb.posZ);
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		float f = (float) ((double) ((float) this.entityPosX + 0.5F) - this.entityPleb.posX);
		float f1 = (float) ((double) ((float) this.entityPosZ + 0.5F) - this.entityPleb.posZ);
		float f2 = this.entityPositionX * f + this.entityPositionZ * f1;

		if (f2 < 0.0F) {
			this.hasStoppedUsingBloomery = true;
		}
	}

	/**
	 * Determines if a door can be broken with AI.
	 */
	private BlockBloomery findUsableBloomery(int x, int y, int z) {
		TileEntity tileEntity = this.entityPleb.worldObj.getBlockTileEntity(x, y, z);
		if (tileEntity != null && tileEntity instanceof TileEntityBloomery) {
			TileEntityBloomery bloomery = (TileEntityBloomery) tileEntity;
			if (bloomery.getIsValid() && bloomery.getIsMaster()) {
				return (BlockBloomery) bloomery.getBlockType();
			}
		}
		return null;
	}
}
