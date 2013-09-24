package toldea.romecraft.ai;

import java.util.ArrayList;
import java.util.List;

import toldea.romecraft.entity.EntityLegionary;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Contubernium {
	public static final int maxSize = 8;
	public static final int ranks = 2;
	public static final int files = 6;
	public static final float tightness = 2f;

	public enum Facing {
		NORTH(0), SOUTH(1), EAST(2), WEST(3);
		private final int direction;
		Facing(int par1Direction) {
			direction = par1Direction;
		}
		public final int getIntValue() {
			return direction;
		}
		public static Facing getFacingForIntValue(int intValue) {
			// Loop over all values and return the Facing that matches the parsed intValue.
			for (Facing f : Facing.values()) {
				if (f.getIntValue() == intValue) {
					return f;
				}
			}
			// Default to North.
			return NORTH;
		}
	}
	
	private List<EntityLegionary> squadMembersList;
	private Vec3 targetLocation = null;
	private EntityLivingBase targetEntity = null;
	private boolean shouldFollowPlayer = true;
	private Facing facing = Facing.NORTH;

	public Contubernium() {
		squadMembersList = new ArrayList<EntityLegionary>();
	}

	public void registerSquadMember(EntityLegionary squadMember) {
		if (!squadMembersList.contains(squadMember)) {
			squadMembersList.add(squadMember);
		}
	}

	public void removeSquadMember(EntityLegionary squadMember) {
		if (squadMembersList.contains(squadMember)) {
			squadMembersList.remove(squadMember);
		}
	}

	public void validateSquadMembers() {
		int size = squadMembersList.size();
		for (int i = 0; i < size; i++) {
			if (squadMembersList.get(i) == null || squadMembersList.get(i).isDead) {
				squadMembersList.remove(i);
				size--;
			}
		}
	}

	public int getSquadSize() {
		return squadMembersList.size();
	}

	public int getSquadIndex(EntityLegionary squadMember) {
		validateSquadMembers();

		if (squadMembersList.contains(squadMember)) {
			return squadMembersList.indexOf(squadMember);
		} else
			return -1;
	}

	public void setShouldFollowPlayer(boolean par1shouldFollowPlayer) {
		shouldFollowPlayer = par1shouldFollowPlayer;
	}

	/**
	 * Set the new target location for this Contubernium. If the location is valid, also remove all current squad member paths and update the Contubernium's
	 * facing.
	 * 
	 * @param newTargetLocation
	 *            The new Vec3 location for the Contubernium to move to.
	 */
	public void setTargetLocation(Vec3 newTargetLocation) {
		if (newTargetLocation != null) {
			// Clear all current paths for all squad members.
			for (EntityLegionary legionary : squadMembersList) {
				legionary.getNavigator().clearPathEntity();
			}
			// Calculate the new facing direction based on the delta x and z between our center and the new target location.
			faceTargetLocation(newTargetLocation);
		}
		// Finally update the target location to the new one.
		targetLocation.xCoord = newTargetLocation.xCoord;
		targetLocation.yCoord = newTargetLocation.yCoord;
		targetLocation.zCoord = newTargetLocation.zCoord;
	}

	public Vec3 getTargetLocation() {
		validateSquadMembers();
		if (squadMembersList.size() <= 0) {
			return null;
		} else {
			if (targetLocation == null) {
				EntityLegionary legionary = squadMembersList.get(0);
				if (legionary == null) {
					return null;
				}
				if (shouldFollowPlayer) {
					if (legionary.worldObj.playerEntities.size() == 0) {
						return null;
					}
					EntityPlayer player = ((EntityPlayer) legionary.worldObj.playerEntities.get(0));
					if (player == null) {
						return null;
					}
					Vec3 vec = player.getPosition(1.0f);
					vec.zCoord = vec.zCoord + 5;
					return vec;
				} else {
					return null;
				}
			} else {
				return targetLocation;
			}
		}
	}

	/**
	 * Get the current center and compare it's x and z coordinates to the new target location. Update the facing according to which of the axis is the
	 * 'greatest' (largest absolute difference).
	 * 
	 * @param targetLocation
	 *            The Vec3 target location to face.
	 */
	private void faceTargetLocation(Vec3 targetLocation) {
		Vec3 currentCenter = getCenter();
		double dx = currentCenter.xCoord - targetLocation.xCoord;
		double dz = currentCenter.zCoord - targetLocation.zCoord;
		if (Math.abs(dx) > Math.abs(dz)) {
			setFacing(dx > .0d ? Facing.WEST : Facing.EAST);
		} else {
			setFacing(dz > .0d ? Facing.NORTH : Facing.SOUTH);
		}
	}

	/**
	 * Sets the Contubernium's attack target. If it's a valid target it also clears all current squad member paths and updates the Contubernium's facing.
	 * 
	 * @param entity
	 */
	public void setTargetEntity(EntityLivingBase entity) {
		if (entity != null) {
			// Update the 'target location' to this entity's current position. When the entity is disposed, the squad will move to this position.
			// This also clears the squad's active paths and updates the Contubernium's facing towards the entity.
			setTargetLocation(entity.getPosition(1.0f));
		}
		this.targetEntity = entity;
	}

	public EntityLivingBase getTargetEntity() {
		if (this.targetEntity != null && !this.targetEntity.isEntityAlive()) {
			this.targetEntity = null;
		}
		return this.targetEntity;
	}

	/**
	 * Calculates and returns the Vec3 center of this Contubernium. (All squad member's positions combined divided by squad size.)
	 * @return Returns average position of all squad members.
	 */
	public Vec3 getCenter() {
		Vec3 center = Vec3.createVectorHelper(0d, 0d, 0d);
		Vec3 vec;
		double squadSize = getSquadSize();
		for (EntityLegionary legionary : squadMembersList) {
			vec = legionary.getPosition(1.0f);
			center = center.addVector(vec.xCoord, vec.yCoord, vec.zCoord);
		}
		center.xCoord = center.xCoord / squadSize;
		center.yCoord = center.yCoord / squadSize;
		center.zCoord = center.zCoord / squadSize;
		return center;
	}

	public Facing getFacing() {
		return this.facing;
	}
	public void setFacing(Facing par1Facing) {
		this.facing = par1Facing;
	}

	public void saveNBTData(NBTTagCompound compound) {
		compound.setBoolean("shouldFollowPlayer", shouldFollowPlayer);
		compound.setInteger("facingInt", this.facing.getIntValue());

		if (targetLocation != null) {
			compound.setDouble("targetLocationX", targetLocation.xCoord);
			compound.setDouble("targetLocationY", targetLocation.yCoord);
			compound.setDouble("targetLocationZ", targetLocation.zCoord);
		}
	}

	public void loadNBTData(NBTTagCompound compound) {
		if (compound != null) {
			shouldFollowPlayer = compound.getBoolean("shouldFollowPlayer");
			facing = Facing.getFacingForIntValue(compound.getInteger("facingInt"));

			if (compound.hasKey("targetLocationX")) {
				Double xCoord = compound.getDouble("targetLocationX");
				Double yCoord = compound.getDouble("targetLocationY");
				Double zCoord = compound.getDouble("targetLocationZ");
				targetLocation = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
			}
		}
	}
}
