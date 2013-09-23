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

	private List<EntityLegionary> squadMembersList;
	private Vec3 targetLocation = null;
	private EntityLivingBase targetEntity = null;
	private boolean shouldFollowPlayer = true;

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

	public void setTargetLocation(Vec3 newTargetLocation) {
		targetLocation = newTargetLocation;
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
	
	public void setTargetEntity(EntityLivingBase entity) {
		this.targetEntity = entity;
	}
	public EntityLivingBase getTargetEntity() {
		return this.targetEntity;
	}

	public void saveNBTData(NBTTagCompound compound) {
		compound.setBoolean("shouldFollowPlayer", shouldFollowPlayer);
		
		if (targetLocation != null) {
			compound.setDouble("targetLocationX", targetLocation.xCoord);
			compound.setDouble("targetLocationY", targetLocation.yCoord);
			compound.setDouble("targetLocationZ", targetLocation.zCoord);
		}
	}

	public void loadNBTData(NBTTagCompound compound) {
		if (compound != null) {
			shouldFollowPlayer = compound.getBoolean("shouldFollowPlayer");
			
			if (compound.hasKey("targetLocationX")) {
				Double xCoord = compound.getDouble("targetLocationX");
				Double yCoord = compound.getDouble("targetLocationY");
				Double zCoord = compound.getDouble("targetLocationZ");
				targetLocation = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
			}
		}
	}
}
