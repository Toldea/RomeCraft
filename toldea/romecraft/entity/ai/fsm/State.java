package toldea.romecraft.entity.ai.fsm;


public abstract class State {
	/*
	protected ChunkCoordinates targetLocation = null; 
	
	public abstract void onStarted();
	public abstract void onExecute();
	public abstract void onFinished();
	
	private void moveTowardsTargetLocation() {
		if (currentNavigationTarget == currentTargetLocation && !entityPleb.getNavigator().noPath()) {
			return;
		}
		TileEntity targetEntity = getTileEntityForTargetLocation();
		if (targetEntity == null) {
			return;
		}
		double dist = entityPleb.getDistanceSq(targetEntity.xCoord, targetEntity.yCoord, targetEntity.zCoord);
		if (dist > IN_RANGE) {
			if (dist > 256.0d) {
				Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityPleb, 14, 3, this.entityPleb.worldObj.getWorldVec3Pool()
						.getVecFromPool((double) targetEntity.xCoord + .5d, (double) targetEntity.yCoord + .5d, (double) targetEntity.zCoord + .5d));
				if (vec3 != null) {
					this.entityPleb.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, .6D);
					currentNavigationTarget = currentTargetLocation;
					return;
				}
			} else {
				this.entityPleb.getNavigator().tryMoveToXYZ((double) targetEntity.xCoord + .5d, (double) targetEntity.yCoord + .5d,
						(double) targetEntity.zCoord + .5d, .6D);
				currentNavigationTarget = currentTargetLocation;
				return;
			}
		}
		return;
	}

	private boolean inRangeOfTargetLocation() {
		double dist = entityPleb.getDistanceSq(targetLocation.posX, targetLocation.posY, targetLocation.posZ);
		return (dist <= IN_RANGE);
	}
	*/
}
