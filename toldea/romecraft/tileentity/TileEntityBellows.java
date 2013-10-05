package toldea.romecraft.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileEntityBellows extends TileEntity {
	private static float MAX_ROTATION = (float) (30f * Math.PI / 180f);
	private static float ROTATION_SPEED = .02f;

	private float bellowsRotation = 30f;
	private boolean contracting = false;
	private boolean recentlyPushed = false;

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			if (contracting) {
				bellowsRotation = Math.max(0f, bellowsRotation - ROTATION_SPEED);
				if (bellowsRotation <= 0f) {
					contracting = false;
				}
			} else if (bellowsRotation != MAX_ROTATION) {
				bellowsRotation = Math.min(MAX_ROTATION, bellowsRotation + ROTATION_SPEED);
				if (bellowsRotation >= MAX_ROTATION) {
					recentlyPushed = false;
				}
			}
		}
	}

	public void pushBellows() {
		if (worldObj.isRemote) {
			if (!recentlyPushed) {
				contracting = true;
				recentlyPushed = true;
			}
		}
	}

	public float getBellowsRotation() {
		return bellowsRotation;
	}
}
