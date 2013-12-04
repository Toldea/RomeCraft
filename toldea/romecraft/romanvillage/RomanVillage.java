package toldea.romecraft.romanvillage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import toldea.romecraft.entity.EntityPleb;
import toldea.romecraft.tileentity.TileEntityBloomery;
import toldea.romecraft.tileentity.TileEntityRomanAnvil;

public class RomanVillage {
	private World worldObj;

	// TODO: Rewrite this whole different lists system to something more elegant.
	private final List plebDoorInfoList = new ArrayList();
	private final List bloomeryInfoList = new ArrayList();
	private final List romanAnvilInfoList = new ArrayList();

	private final ChunkCoordinates centerHelper = new ChunkCoordinates(0, 0, 0);
	private final ChunkCoordinates center = new ChunkCoordinates(0, 0, 0);
	private ChunkCoordinates villageForumLocation = new ChunkCoordinates(0, 0, 0);

	private int villageRadius;
	private int lastAddDoorTimestamp;
	private int tickCounter;
	private int numPlebs;

	/** Timestamp of tick count when villager last bred */
	private int noBreedTicks;

	/** List of player reputations with this village */
	private TreeMap playerReputation = new TreeMap();
	private List villageAgressors = new ArrayList();

	private boolean shouldBeAnnihilated = false;

	private RomanVillageMap villageMapData;

	public RomanVillage() {
	}

	public RomanVillage(World par1World) {
		this.worldObj = par1World;
	}

	public RomanVillage(World world, ChunkCoordinates par2VillageForumLocation) {
		this.worldObj = world;
		villageForumLocation = par2VillageForumLocation;
		updateVillageRadiusAndCenter();

		String mapName = new String("romanvillagemap" + villageForumLocation.posX + "." + villageForumLocation.posY + "." + villageForumLocation.posZ);
		villageMapData = new RomanVillageMap(mapName, RomanVillageMap.DEFAULT_ROMAN_MAP_SIZE);
		world.setItemData(mapName, villageMapData);
		villageMapData.scale = 0;
		int i = RomanVillageMap.DEFAULT_ROMAN_MAP_SIZE * (1 << villageMapData.scale);
		villageMapData.xCenter = (int) (Math.round(villageForumLocation.posX / (double) i) * (long) i);
		villageMapData.zCenter = (int) (Math.round(villageForumLocation.posZ / (double) i) * (long) i);
		villageMapData.dimension = (byte) world.provider.dimensionId;
		updateMapData(world);
	}
	
	public void updateMapData(World world) {
		villageMapData.updateMapData(world);
		villageMapData.markDirty();
	}

	public void linkWorld(World par1World) {
		this.worldObj = par1World;
	}

	/**
	 * Called periodically by VillageCollection
	 */
	public void tick(int par1) {
		this.tickCounter = par1;
		this.removeDeadAndOutOfRangeObjectsForList(plebDoorInfoList);
		this.removeDeadAndOutOfRangeObjectsForList(bloomeryInfoList);
		this.removeDeadAndOutOfRangeObjectsForList(romanAnvilInfoList);
		// this.removeDeadAndOldAgressors();

		if (par1 % 2000 == 0) {
			this.trySpawnPlebs();
		}

		if (par1 % 20 == 0) {
			this.updateNumPlebs();
		}

		int j = this.numPlebs / 10;
	}

	public RomanVillageMap getVillageMapData() {
		return this.villageMapData;
	}

	public int getMaxNumberOfPlebs() {
		return (int) ((double) ((float) this.getNumVillageDoors()) * 0.35D);
	}

	/**
	 * Try and spawn a new Pleb if there are less than 4 in the village and there are enough doors to house them. Any villager past 4 should be created through
	 * breeding.
	 */
	private void trySpawnPlebs() {
		int numPlebs = getNumPlebs();
		int maxNumPlebs = getMaxNumberOfPlebs();
		if (numPlebs < 4 && numPlebs < maxNumPlebs) {
			Vec3 spawnLocation = tryGetPlebSpawningLocation(MathHelper.floor_float((float) this.center.posX), MathHelper.floor_float((float) this.center.posY),
					MathHelper.floor_float((float) this.center.posZ), 1, 2, 1);
			if (spawnLocation != null) {
				EntityPleb entityPleb = new EntityPleb(this.worldObj);
				entityPleb.onSpawnWithEgg((EntityLivingData) null);
				// this.mate.setGrowingAge(6000);
				// this.villagerObj.setGrowingAge(6000);
				entityPleb.setGrowingAge(-24000);
				entityPleb.setLocationAndAngles(spawnLocation.xCoord, spawnLocation.yCoord, spawnLocation.zCoord, 0.0F, 0.0F);
				this.worldObj.spawnEntityInWorld(entityPleb);
				this.worldObj.setEntityState(entityPleb, (byte) 12);

				this.updateNumPlebs();
			}
		}
	}

	private Vec3 tryGetPlebSpawningLocation(int par1, int par2, int par3, int par4, int par5, int par6) {
		for (int k1 = 0; k1 < 10; ++k1) {
			int l1 = par1 + this.worldObj.rand.nextInt(16) - 8;
			int i2 = par2 + this.worldObj.rand.nextInt(6) - 3;
			int j2 = par3 + this.worldObj.rand.nextInt(16) - 8;

			if (this.isInRange(l1, i2, j2) && this.isValidEntitySpawningLocation(l1, i2, j2, par4, par5, par6)) {
				return this.worldObj.getWorldVec3Pool().getVecFromPool((double) l1, (double) i2, (double) j2);
			}
		}

		return null;
	}

	/**
	 * Tries up to 10 times to get a valid spawning location before eventually failing and returning null.
	 */
	/*
	 * private Vec3 tryGetIronGolemSpawningLocation(int par1, int par2, int par3, int par4, int par5, int par6) { for (int k1 = 0; k1 < 10; ++k1) { int l1 =
	 * par1 + this.worldObj.rand.nextInt(16) - 8; int i2 = par2 + this.worldObj.rand.nextInt(6) - 3; int j2 = par3 + this.worldObj.rand.nextInt(16) - 8;
	 * 
	 * if (this.isInRange(l1, i2, j2) && this.isValidEntitySpawningLocation(l1, i2, j2, par4, par5, par6)) { return
	 * this.worldObj.getWorldVec3Pool().getVecFromPool((double) l1, (double) i2, (double) j2); } }
	 * 
	 * return null; }
	 */
	private boolean isValidEntitySpawningLocation(int par1, int par2, int par3, int par4, int par5, int par6) {
		if (!this.worldObj.doesBlockHaveSolidTopSurface(par1, par2 - 1, par3)) {
			return false;
		} else {
			int k1 = par1 - par4 / 2;
			int l1 = par3 - par6 / 2;

			for (int i2 = k1; i2 < k1 + par4; ++i2) {
				for (int j2 = par2; j2 < par2 + par5; ++j2) {
					for (int k2 = l1; k2 < l1 + par6; ++k2) {
						if (this.worldObj.isBlockNormalCube(i2, j2, k2)) {
							return false;
						}
					}
				}
			}

			return true;
		}
	}

	/*
	 * private void updateNumIronGolems() { List list = this.worldObj.getEntitiesWithinAABB( EntityIronGolem.class, AxisAlignedBB.getAABBPool().getAABB((double)
	 * (this.center.posX - this.villageRadius), (double) (this.center.posY - 4), (double) (this.center.posZ - this.villageRadius), (double) (this.center.posX +
	 * this.villageRadius), (double) (this.center.posY + 4), (double) (this.center.posZ + this.villageRadius))); this.numIronGolems = list.size(); }
	 */
	private void updateNumPlebs() {
		/*
		 * List list = this.worldObj.getEntitiesWithinAABB( EntityVillager.class, AxisAlignedBB.getAABBPool().getAABB((double) (this.center.posX -
		 * this.villageRadius), (double) (this.center.posY - 4), (double) (this.center.posZ - this.villageRadius), (double) (this.center.posX +
		 * this.villageRadius), (double) (this.center.posY + 4), (double) (this.center.posZ + this.villageRadius)));
		 */
		List list = this.worldObj.getEntitiesWithinAABB(
				EntityPleb.class,
				AxisAlignedBB.getAABBPool().getAABB((double) (this.center.posX - this.villageRadius), (double) (this.center.posY - 4),
						(double) (this.center.posZ - this.villageRadius), (double) (this.center.posX + this.villageRadius), (double) (this.center.posY + 4),
						(double) (this.center.posZ + this.villageRadius)));
		this.numPlebs = list.size();

		if (this.numPlebs == 0) {
			this.playerReputation.clear();
		}
	}

	public ChunkCoordinates getCenter() {
		return this.center;
	}

	public ChunkCoordinates getVillageForumLocation() {
		return villageForumLocation;
	}

	public int getVillageRadius() {
		return this.villageRadius;
	}

	/**
	 * Actually get num village door info entries, but that boils down to number of doors. Called by EntityAIVillagerMate and VillageSiege
	 */
	public int getNumVillageDoors() {
		return this.plebDoorInfoList.size();
	}

	public int getNumBloomeries() {
		return this.bloomeryInfoList.size();
	}
	
	public int getNumRomanAnvils() {
		return this.romanAnvilInfoList.size();
	}

	public int getTicksSinceLastDoorAdding() {
		return this.tickCounter - this.lastAddDoorTimestamp;
	}

	public int getNumPlebs() {
		return this.numPlebs;
	}

	/**
	 * Returns true, if the given coordinates are within the bounding box of the village.
	 */
	public boolean isInRange(int par1, int par2, int par3) {
		return this.center.getDistanceSquared(par1, par2, par3) < (float) (this.villageRadius * this.villageRadius);
		// return this.center.getDistanceSquared(par1, par2, par3) < (float) (this.villageRadius * this.villageRadius);
	}

	/**
	 * called only by class EntityAIMoveThroughVillage
	 */
	public List getVillageDoorInfoList() {
		return this.plebDoorInfoList;
	}

	public List getBloomeryInfoList() {
		return this.bloomeryInfoList;
	}
	
	public List getRomanAnvilInfoList() {
		return this.romanAnvilInfoList;
	}

	public RomanVillageObjectInfo findNearestObjectForInfoList(List infoList, int par1, int par2, int par3) {
		RomanVillageObjectInfo objectInfo = null;
		int l = Integer.MAX_VALUE;
		Iterator iterator = infoList.iterator();

		while (iterator.hasNext()) {
			RomanVillageObjectInfo objectInfo1 = (RomanVillageObjectInfo) iterator.next();
			int i1 = objectInfo1.getDistanceSquared(par1, par2, par3);

			if (i1 < l) {
				objectInfo = objectInfo1;
				l = i1;
			}
		}

		return objectInfo;
	}

	/**
	 * Find a door suitable for shelter. If there are more doors in a distance of 16 blocks, then the least restricted one (i.e. the one protecting the lowest
	 * number of villagers) of them is chosen, else the nearest one regardless of restriction.
	 */
	public RomanVillageDoorInfo findNearestDoorUnrestricted(int par1, int par2, int par3) {
		RomanVillageDoorInfo villagedoorinfo = null;
		int l = Integer.MAX_VALUE;
		Iterator iterator = this.plebDoorInfoList.iterator();

		while (iterator.hasNext()) {
			RomanVillageDoorInfo villagedoorinfo1 = (RomanVillageDoorInfo) iterator.next();
			int i1 = villagedoorinfo1.getDistanceSquared(par1, par2, par3);

			if (i1 > 256) {
				i1 *= 1000;
			} else {
				i1 = villagedoorinfo1.getDoorOpeningRestrictionCounter();
			}

			if (i1 < l) {
				villagedoorinfo = villagedoorinfo1;
				l = i1;
			}
		}

		return villagedoorinfo;
	}

	/**
	 * Loops through all different object lists and returns any object found at that position or null if none are found.
	 */
	public RomanVillageObjectInfo getVillageObjectAt(int x, int y, int z) {
		RomanVillageObjectInfo objectInfo;
		objectInfo = getVillageObjectForInfoListAt(this.plebDoorInfoList, x, y, z);
		if (objectInfo != null) {
			return objectInfo;
		}
		objectInfo = getVillageObjectForInfoListAt(this.bloomeryInfoList, x, y, z);
		if (objectInfo != null) {
			return objectInfo;
		}
		objectInfo = getVillageObjectForInfoListAt(this.romanAnvilInfoList, x, y, z);
		if (objectInfo != null) {
			return objectInfo;
		}
		return null;
	}

	public RomanVillageObjectInfo getVillageObjectForInfoListAt(List infoList, int x, int y, int z) {
		if (this.center.getDistanceSquared(x, y, z) > (float) (this.villageRadius * this.villageRadius)) {
			return null;
		} else {
			Iterator iterator = infoList.iterator();
			RomanVillageObjectInfo objectInfo;

			do {
				if (!iterator.hasNext()) {
					return null;
				}

				objectInfo = (RomanVillageObjectInfo) iterator.next();
			} while (objectInfo.posX != x || objectInfo.posZ != z || Math.abs(objectInfo.posY - y) > 1);

			return objectInfo;
		}
	}

	public void addVillageObjectInfoToInfoList(List infoList, RomanVillageObjectInfo romanVillageObjectInfo) {
		infoList.add(romanVillageObjectInfo);
		this.centerHelper.posX += romanVillageObjectInfo.posX;
		this.centerHelper.posY += romanVillageObjectInfo.posY;
		this.centerHelper.posZ += romanVillageObjectInfo.posZ;
		this.updateVillageRadiusAndCenter();

		if (romanVillageObjectInfo instanceof RomanVillageDoorInfo) {
			this.lastAddDoorTimestamp = ((RomanVillageDoorInfo) romanVillageObjectInfo).lastActivityTimestamp;
		}
	}

	/**
	 * Returns true, if there is not a single village door left. Called by VillageCollection
	 */
	public boolean isAnnihilated() {
		return shouldBeAnnihilated; // TODO: Make sure current solution is working properly in all cases.
		// return this.villageDoorInfoList.isEmpty();
	}

	public void flagForAnnihilation() {
		this.shouldBeAnnihilated = true;
	}

	/*
	 * public void addOrRenewAgressor(EntityLivingBase par1EntityLivingBase) { Iterator iterator = this.villageAgressors.iterator(); VillageAgressor
	 * villageagressor;
	 * 
	 * do { if (!iterator.hasNext()) { this.villageAgressors.add(new VillageAgressor(this, par1EntityLivingBase, this.tickCounter)); return; }
	 * 
	 * villageagressor = (VillageAgressor)iterator.next(); } while (villageagressor.agressor != par1EntityLivingBase);
	 * 
	 * villageagressor.agressionTime = this.tickCounter; }
	 * 
	 * public EntityLivingBase findNearestVillageAggressor(EntityLivingBase par1EntityLivingBase) { double d0 = Double.MAX_VALUE; VillageAgressor
	 * villageagressor = null;
	 * 
	 * for (int i = 0; i < this.villageAgressors.size(); ++i) { VillageAgressor villageagressor1 = (VillageAgressor)this.villageAgressors.get(i); double d1 =
	 * villageagressor1.agressor.getDistanceSqToEntity(par1EntityLivingBase);
	 * 
	 * if (d1 <= d0) { villageagressor = villageagressor1; d0 = d1; } }
	 * 
	 * return villageagressor != null ? villageagressor.agressor : null; }
	 */
	public EntityPlayer func_82685_c(EntityLivingBase par1EntityLivingBase) {
		double d0 = Double.MAX_VALUE;
		EntityPlayer entityplayer = null;
		Iterator iterator = this.playerReputation.keySet().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();

			if (this.isPlayerReputationTooLow(s)) {
				EntityPlayer entityplayer1 = this.worldObj.getPlayerEntityByName(s);

				if (entityplayer1 != null) {
					double d1 = entityplayer1.getDistanceSqToEntity(par1EntityLivingBase);

					if (d1 <= d0) {
						entityplayer = entityplayer1;
						d0 = d1;
					}
				}
			}
		}

		return entityplayer;
	}

	/*
	 * private void removeDeadAndOldAgressors() { Iterator iterator = this.villageAgressors.iterator();
	 * 
	 * while (iterator.hasNext()) { VillageAgressor villageagressor = (VillageAgressor)iterator.next();
	 * 
	 * if (!villageagressor.agressor.isEntityAlive() || Math.abs(this.tickCounter - villageagressor.agressionTime) > 300) { iterator.remove(); } } }
	 */
	private void removeDeadAndOutOfRangeObjectsForList(List objectInfoList) {
		boolean flag = false;
		boolean flag1 = this.worldObj.rand.nextInt(50) == 0;
		Iterator iterator = objectInfoList.iterator();

		while (iterator.hasNext()) {
			RomanVillageObjectInfo villageObjectInfo = (RomanVillageObjectInfo) iterator.next();
			boolean removeObject = false;

			if (villageObjectInfo instanceof RomanVillageDoorInfo) {
				RomanVillageDoorInfo villageDoorInfo = (RomanVillageDoorInfo) villageObjectInfo;
				if (flag1) {
					villageDoorInfo.resetDoorOpeningRestrictionCounter();
				}
				if (!this.isBlockDoor(villageDoorInfo.posX, villageDoorInfo.posY, villageDoorInfo.posZ)
						|| Math.abs(this.tickCounter - villageDoorInfo.lastActivityTimestamp) > 1200) {
					removeObject = true;
				}
			} else if (villageObjectInfo instanceof RomanVillageBloomeryInfo) {
				RomanVillageBloomeryInfo villageBloomeryInfo = (RomanVillageBloomeryInfo) villageObjectInfo;
				if (!isBlockValidBloomery(villageBloomeryInfo.posX, villageBloomeryInfo.posY, villageBloomeryInfo.posZ)) {
					System.out.println("Removing invalid Bloomery from village!");
					removeObject = true;
				}
			} else if (villageObjectInfo instanceof RomanVillageAnvilInfo) {
				RomanVillageAnvilInfo villageAnvilInfo = (RomanVillageAnvilInfo) villageObjectInfo;
				if (!isBlockValidRomanAnvil(villageAnvilInfo.posX, villageAnvilInfo.posY, villageAnvilInfo.posZ)) {
					System.out.println("Removing invalid Anvil from village!");
					removeObject = true;
				}
			}

			if (removeObject) {
				this.centerHelper.posX -= villageObjectInfo.posX;
				this.centerHelper.posY -= villageObjectInfo.posY;
				this.centerHelper.posZ -= villageObjectInfo.posZ;
				flag = true;
				villageObjectInfo.isDetachedFromVillageFlag = true;
				iterator.remove();
			}
		}

		if (flag) {
			this.updateVillageRadiusAndCenter();
		}
	}

	private boolean isBlockDoor(int x, int y, int z) {
		int l = this.worldObj.getBlockId(x, y, z);
		return l <= 0 ? false : l == Block.doorWood.blockID;
	}

	private boolean isBlockValidBloomery(int x, int y, int z) {
		TileEntity tileEntity = this.worldObj.getBlockTileEntity(x, y, z);
		if (tileEntity == null || !(tileEntity instanceof TileEntityBloomery)) {
			return false;
		} else {
			TileEntityBloomery bloomery = (TileEntityBloomery) tileEntity;
			return (bloomery.getIsValid() && bloomery.getIsMaster());
		}
	}
	
	private boolean isBlockValidRomanAnvil(int x, int y, int z) {
		TileEntity tileEntity = this.worldObj.getBlockTileEntity(x, y, z);
		if (tileEntity == null || !(tileEntity instanceof TileEntityRomanAnvil)) {
			return false;
		} else {
			return true;
		}
	}

	private void updateVillageRadiusAndCenter() {
		int i = this.plebDoorInfoList.size() + this.bloomeryInfoList.size() + this.romanAnvilInfoList.size();

		if (i == 0) {
			this.center.set(villageForumLocation.posX, villageForumLocation.posY, villageForumLocation.posZ);
			this.villageRadius = 32;
		} else {
			this.center.set(this.centerHelper.posX / i, this.centerHelper.posY / i, this.centerHelper.posZ / i);
			int j = 0;

			RomanVillageObjectInfo villageObjectInfo;

			for (Iterator iterator = this.plebDoorInfoList.iterator(); iterator.hasNext(); j = Math.max(
					villageObjectInfo.getDistanceSquared(this.center.posX, this.center.posY, this.center.posZ), j)) {
				villageObjectInfo = (RomanVillageObjectInfo) iterator.next();
			}

			for (Iterator iterator = this.bloomeryInfoList.iterator(); iterator.hasNext(); j = Math.max(
					villageObjectInfo.getDistanceSquared(this.center.posX, this.center.posY, this.center.posZ), j)) {
				villageObjectInfo = (RomanVillageObjectInfo) iterator.next();
			}
			
			for (Iterator iterator = this.romanAnvilInfoList.iterator(); iterator.hasNext(); j = Math.max(
					villageObjectInfo.getDistanceSquared(this.center.posX, this.center.posY, this.center.posZ), j)) {
				villageObjectInfo = (RomanVillageObjectInfo) iterator.next();
			}

			this.villageRadius = Math.max(32, (int) Math.sqrt((double) j) + 1);
		}
	}

	/**
	 * Return the village reputation for a player
	 */
	public int getReputationForPlayer(String par1Str) {
		Integer integer = (Integer) this.playerReputation.get(par1Str);
		return integer != null ? integer.intValue() : 0;
	}

	/**
	 * Set the village reputation for a player.
	 */
	public int setReputationForPlayer(String par1Str, int par2) {
		int j = this.getReputationForPlayer(par1Str);
		int k = MathHelper.clamp_int(j + par2, -30, 10);
		this.playerReputation.put(par1Str, Integer.valueOf(k));
		return k;
	}

	/**
	 * Return whether this player has a too low reputation with this village.
	 */
	public boolean isPlayerReputationTooLow(String par1Str) {
		return this.getReputationForPlayer(par1Str) <= -15;
	}

	/**
	 * Read this village's data from NBT.
	 */
	public void readVillageDataFromNBT(NBTTagCompound par1NBTTagCompound) {
		this.numPlebs = par1NBTTagCompound.getInteger("PopSize");
		this.villageRadius = par1NBTTagCompound.getInteger("Radius");
		// this.numIronGolems = par1NBTTagCompound.getInteger("Golems");
		this.lastAddDoorTimestamp = par1NBTTagCompound.getInteger("Stable");
		this.tickCounter = par1NBTTagCompound.getInteger("Tick");
		this.noBreedTicks = par1NBTTagCompound.getInteger("MTick");
		this.center.posX = par1NBTTagCompound.getInteger("CX");
		this.center.posY = par1NBTTagCompound.getInteger("CY");
		this.center.posZ = par1NBTTagCompound.getInteger("CZ");
		this.centerHelper.posX = par1NBTTagCompound.getInteger("ACX");
		this.centerHelper.posY = par1NBTTagCompound.getInteger("ACY");
		this.centerHelper.posZ = par1NBTTagCompound.getInteger("ACZ");
		this.villageForumLocation.posX = par1NBTTagCompound.getInteger("FORUM_X");
		this.villageForumLocation.posY = par1NBTTagCompound.getInteger("FORUM_Y");
		this.villageForumLocation.posZ = par1NBTTagCompound.getInteger("FORUM_Z");

		NBTTagList doorTagList = par1NBTTagCompound.getTagList("Doors");

		for (int i = 0; i < doorTagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) doorTagList.tagAt(i);
			RomanVillageDoorInfo villagedoorinfo = new RomanVillageDoorInfo(nbttagcompound1.getInteger("X"), nbttagcompound1.getInteger("Y"),
					nbttagcompound1.getInteger("Z"), nbttagcompound1.getInteger("IDX"), nbttagcompound1.getInteger("IDZ"), nbttagcompound1.getInteger("TS"));
			this.plebDoorInfoList.add(villagedoorinfo);
		}

		NBTTagList bloomeryTagList = par1NBTTagCompound.getTagList("Bloomeries");

		for (int i = 0; i < bloomeryTagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) bloomeryTagList.tagAt(i);
			RomanVillageBloomeryInfo bloomeryInfo = new RomanVillageBloomeryInfo(nbttagcompound1.getInteger("X"), nbttagcompound1.getInteger("Y"),
					nbttagcompound1.getInteger("Z"));
			this.bloomeryInfoList.add(bloomeryInfo);
		}
		
		NBTTagList romanAnvilsTagList = par1NBTTagCompound.getTagList("RomanAnvils");

		for (int i = 0; i < romanAnvilsTagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) romanAnvilsTagList.tagAt(i);
			RomanVillageAnvilInfo anvilInfo = new RomanVillageAnvilInfo(nbttagcompound1.getInteger("X"), nbttagcompound1.getInteger("Y"),
					nbttagcompound1.getInteger("Z"));
			this.romanAnvilInfoList.add(anvilInfo);
		}

		NBTTagList nbttaglist1 = par1NBTTagCompound.getTagList("Players");

		for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
			NBTTagCompound nbttagcompound2 = (NBTTagCompound) nbttaglist1.tagAt(j);
			this.playerReputation.put(nbttagcompound2.getString("Name"), Integer.valueOf(nbttagcompound2.getInteger("S")));
		}
		if (par1NBTTagCompound.hasKey("MapData")) {
			if (villageMapData == null) {
				String mapName = new String("romanvillagemap" + villageForumLocation.posX + "." + villageForumLocation.posY + "." + villageForumLocation.posZ);
				villageMapData = new RomanVillageMap(mapName, RomanVillageMap.DEFAULT_ROMAN_MAP_SIZE);
			}
			this.villageMapData.readFromNBT(par1NBTTagCompound.getCompoundTag("MapData"));
		}
	}

	/**
	 * Write this village's data to NBT.
	 */
	public void writeVillageDataToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setInteger("PopSize", this.numPlebs);
		par1NBTTagCompound.setInteger("Radius", this.villageRadius);
		// par1NBTTagCompound.setInteger("Golems", this.numIronGolems);
		par1NBTTagCompound.setInteger("Stable", this.lastAddDoorTimestamp);
		par1NBTTagCompound.setInteger("Tick", this.tickCounter);
		par1NBTTagCompound.setInteger("MTick", this.noBreedTicks);
		par1NBTTagCompound.setInteger("CX", this.center.posX);
		par1NBTTagCompound.setInteger("CY", this.center.posY);
		par1NBTTagCompound.setInteger("CZ", this.center.posZ);
		par1NBTTagCompound.setInteger("ACX", this.centerHelper.posX);
		par1NBTTagCompound.setInteger("ACY", this.centerHelper.posY);
		par1NBTTagCompound.setInteger("ACZ", this.centerHelper.posZ);
		par1NBTTagCompound.setInteger("FORUM_X", this.villageForumLocation.posX);
		par1NBTTagCompound.setInteger("FORUM_Y", this.villageForumLocation.posY);
		par1NBTTagCompound.setInteger("FORUM_Z", this.villageForumLocation.posZ);

		NBTTagList tagListDoors = new NBTTagList("Doors");
		Iterator iterator = this.plebDoorInfoList.iterator();

		while (iterator.hasNext()) {
			RomanVillageDoorInfo villagedoorinfo = (RomanVillageDoorInfo) iterator.next();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound("Door");
			nbttagcompound1.setInteger("X", villagedoorinfo.posX);
			nbttagcompound1.setInteger("Y", villagedoorinfo.posY);
			nbttagcompound1.setInteger("Z", villagedoorinfo.posZ);
			nbttagcompound1.setInteger("IDX", villagedoorinfo.insideDirectionX);
			nbttagcompound1.setInteger("IDZ", villagedoorinfo.insideDirectionZ);
			nbttagcompound1.setInteger("TS", villagedoorinfo.lastActivityTimestamp);
			tagListDoors.appendTag(nbttagcompound1);
		}

		par1NBTTagCompound.setTag("Doors", tagListDoors);

		NBTTagList tagListBloomery = new NBTTagList("Bloomeries");
		iterator = this.bloomeryInfoList.iterator();

		while (iterator.hasNext()) {
			RomanVillageBloomeryInfo bloomeryInfo = (RomanVillageBloomeryInfo) iterator.next();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound("Bloomery");
			nbttagcompound1.setInteger("X", bloomeryInfo.posX);
			nbttagcompound1.setInteger("Y", bloomeryInfo.posY);
			nbttagcompound1.setInteger("Z", bloomeryInfo.posZ);
			tagListBloomery.appendTag(nbttagcompound1);
		}

		par1NBTTagCompound.setTag("Bloomeries", tagListBloomery);
		
		NBTTagList tagListAnvil = new NBTTagList("RomanAnvils");
		iterator = this.romanAnvilInfoList.iterator();

		while (iterator.hasNext()) {
			RomanVillageAnvilInfo anvilInfo = (RomanVillageAnvilInfo) iterator.next();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound("RomanAnvil");
			nbttagcompound1.setInteger("X", anvilInfo.posX);
			nbttagcompound1.setInteger("Y", anvilInfo.posY);
			nbttagcompound1.setInteger("Z", anvilInfo.posZ);
			tagListBloomery.appendTag(nbttagcompound1);
		}

		par1NBTTagCompound.setTag("RomanAnvils", tagListBloomery);

		NBTTagList nbttaglist1 = new NBTTagList("Players");
		Iterator iterator1 = this.playerReputation.keySet().iterator();

		while (iterator1.hasNext()) {
			String s = (String) iterator1.next();
			NBTTagCompound nbttagcompound2 = new NBTTagCompound(s);
			nbttagcompound2.setString("Name", s);
			nbttagcompound2.setInteger("S", ((Integer) this.playerReputation.get(s)).intValue());
			nbttaglist1.appendTag(nbttagcompound2);
		}

		par1NBTTagCompound.setTag("Players", nbttaglist1);

		if (this.villageMapData != null) {
			NBTTagCompound villageMapDataCompound = new NBTTagCompound("MapData");
			villageMapData.writeToNBT(villageMapDataCompound);
			par1NBTTagCompound.setCompoundTag("MapData", villageMapDataCompound);
		}
	}

	/**
	 * Prevent villager breeding for a fixed interval of time
	 */
	public void endMatingSeason() {
		this.noBreedTicks = this.tickCounter;
	}

	/**
	 * Return whether villagers mating refractory period has passed
	 */
	public boolean isMatingSeason() {
		return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
	}

	public void func_82683_b(int par1) {
		Iterator iterator = this.playerReputation.keySet().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			this.setReputationForPlayer(s, par1);
		}
	}
}
