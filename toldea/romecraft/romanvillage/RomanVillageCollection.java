package toldea.romecraft.romanvillage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import toldea.romecraft.managers.BlockManager;
import toldea.romecraft.tileentity.TileEntityBloomery;

public class RomanVillageCollection extends WorldSavedData {
	public static final String key = "romanVillages";

	private World worldObj;

	private final List villageList = new ArrayList();
	private final List villagerPositionsList = new ArrayList();

	private final List newDoors = new ArrayList();
	private final List newBloomeries = new ArrayList();

	private int tickCounter;

	public RomanVillageCollection(String par1Str) {
		super(key);
	}

	public RomanVillageCollection(World par1World) {
		super(key);
		this.worldObj = par1World;
		this.markDirty();
	}

	/**
	 * Links the world to this RomanVillageCollection and all registered RomanVillage objects.
	 */
	public void linkWorld(World par1World) {
		System.out.println("RomanVillageCollection.linkWorld");
		this.worldObj = par1World;
		Iterator iterator = this.villageList.iterator();

		while (iterator.hasNext()) {
			RomanVillage village = (RomanVillage) iterator.next();
			village.linkWorld(par1World);
		}
	}

	public void addVillagerPosition(int par1, int par2, int par3) {
		if (this.villagerPositionsList.size() <= 64) {
			if (!this.isVillagerPositionPresent(par1, par2, par3)) {
				this.villagerPositionsList.add(new ChunkCoordinates(par1, par2, par3));
			}
		}
	}

	/**
	 * Runs a single tick for the village collection
	 */
	public void tick() {
		++this.tickCounter;
		Iterator iterator = this.villageList.iterator();

		while (iterator.hasNext()) {
			RomanVillage village = (RomanVillage) iterator.next();
			village.tick(this.tickCounter);
			this.addUnassignedVillageObjectsAroundToNewObjectsList(village.getCenter());
		}

		if (this.villageList.size() > 0) {
			this.removeAnnihilatedVillages();
			this.dropOldestVillagerPosition();
		}

		this.addNewObjectsToVillage(newDoors);
		this.addNewObjectsToVillage(newBloomeries);

		if (this.tickCounter % 400 == 0) {
			this.markDirty();
		}
	}

	private void removeAnnihilatedVillages() {
		Iterator iterator = this.villageList.iterator();

		while (iterator.hasNext()) {
			RomanVillage village = (RomanVillage) iterator.next();

			if (village.isAnnihilated()) {
				System.out.println("RomanVillageCollection.removeAnnihilatedVillages is removing an annihilated village.");
				iterator.remove();
				this.markDirty();
				System.out.println("New villageList count: " + villageList.size());
			}
		}
	}

	/**
	 * Get a list of villages.
	 */
	public List getVillageList() {
		return this.villageList;
	}

	/**
	 * Finds the nearest village, but only the given coordinates are withing it's bounding box plus the given the distance.
	 */
	public RomanVillage findNearestVillage(int par1, int par2, int par3, int par4) {
		RomanVillage village = null;
		float f = Float.MAX_VALUE;
		Iterator iterator = this.villageList.iterator();

		while (iterator.hasNext()) {
			RomanVillage village1 = (RomanVillage) iterator.next();
			float f1 = village1.getCenter().getDistanceSquared(par1, par2, par3);

			if (f1 < f) {
				float f2 = (float) (par4 + village1.getVillageRadius());

				if (f1 <= f2 * f2) {
					village = village1;
					f = f1;
				}
			}
		}

		return village;
	}

	private void dropOldestVillagerPosition() {
		if (!this.villagerPositionsList.isEmpty()) {
			this.addUnassignedVillageObjectsAroundToNewObjectsList((ChunkCoordinates) this.villagerPositionsList.remove(0));
		}
	}

	private void addNewObjectsToVillage(List infoList) {
		int i = 0;

		while (i < infoList.size()) {
			System.out.println("looping through new object #" + i);
			RomanVillageObjectInfo villageObjectInfo = (RomanVillageObjectInfo) infoList.get(i);
			Iterator iterator = this.villageList.iterator();

			while (true) {
				if (iterator.hasNext()) {
					RomanVillage village = (RomanVillage) iterator.next();
					System.out.println("Looping through village: " + village);
					int j = (int) village.getCenter().getDistanceSquared(villageObjectInfo.posX, villageObjectInfo.posY, villageObjectInfo.posZ);
					float k = 32f + village.getVillageRadius();
					if (j > k * k) {
						continue;
					}
					if (villageObjectInfo instanceof RomanVillageDoorInfo) {
						village.addVillageObjectInfoToInfoList(village.getVillageDoorInfoList(), villageObjectInfo);
					} else if (villageObjectInfo instanceof RomanVillageBloomeryInfo) {
						village.addVillageObjectInfoToInfoList(village.getBloomeryInfoList(), villageObjectInfo);
					}
				}
				++i;
				break;
			}
		}

		infoList.clear();
	}

	public void createNewVillage(int x, int y, int z) {
		System.out.println("RomanVillageCollection.createNewVillage");
		RomanVillage village = new RomanVillage(this.worldObj, new ChunkCoordinates(x, y, z));
		this.villageList.add(village);
		this.markDirty();
	}

	public RomanVillage getVillageAt(int par1, int par2, int par3) {
		Iterator iterator = this.villageList.iterator();
		RomanVillage village = null;
		ChunkCoordinates villagePos = new ChunkCoordinates(0, 0, 0);
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			village = (RomanVillage) iterator.next();
			villagePos = village.getVillageForumLocation();
		} while (villagePos.posX != par1 || villagePos.posZ != par3 || Math.abs(villagePos.posY - par2) > 1);

		return village;
	}

	private void addUnassignedVillageObjectsAroundToNewObjectsList(ChunkCoordinates par1ChunkCoordinates) {
		// System.out.println("RomanVillageCollection.addUnassignedWoodenDoorsAroundToNewDoorsList");
		byte b0 = 16;
		byte b1 = 4;
		byte b2 = 16;

		for (int i = par1ChunkCoordinates.posX - b0; i < par1ChunkCoordinates.posX + b0; ++i) {
			for (int j = par1ChunkCoordinates.posY - b1; j < par1ChunkCoordinates.posY + b1; ++j) {
				for (int k = par1ChunkCoordinates.posZ - b2; k < par1ChunkCoordinates.posZ + b2; ++k) {
					if (this.isWoodenDoorAt(i, j, k)) {
						RomanVillageObjectInfo villageObjectInfo = this.getVillageObjectAt(this.newDoors, i, j, k);

						if (villageObjectInfo == null) {
							this.addDoorToNewListIfAppropriate(i, j, k);
						} else if (villageObjectInfo instanceof RomanVillageDoorInfo) {
							((RomanVillageDoorInfo) villageObjectInfo).lastActivityTimestamp = this.tickCounter;
						}
					} else if (this.isBloomeryAt(i, j, k)) {
						RomanVillageObjectInfo villageObjectInfo = this.getVillageObjectAt(this.newBloomeries, i, j, k);

						if (villageObjectInfo == null) {
							this.addBloomeryToNewListIfAppropriate(i, j, k);
						}/* else if (villageObjectInfo instanceof RomanVillageBloomeryInfo) {
							((RomanVillageDoorInfo) villageObjectInfo).lastActivityTimestamp = this.tickCounter;
						}*/
					}
				}
			}
		}
	}

	private RomanVillageObjectInfo getVillageObjectAt(List objectInfoList, int x, int y, int z) {
		Iterator iterator = objectInfoList.iterator();
		RomanVillageObjectInfo villageObjectInfo;

		do {
			if (!iterator.hasNext()) {
				iterator = this.villageList.iterator();
				RomanVillageObjectInfo villageObjectInfo1;

				do {
					if (!iterator.hasNext()) {
						return null;
					}

					RomanVillage village = (RomanVillage) iterator.next();
					villageObjectInfo1 = (RomanVillageObjectInfo) village.getVillageObjectAt(x, y, z);
				} while (villageObjectInfo1 == null);

				return villageObjectInfo1;
			}

			villageObjectInfo = (RomanVillageObjectInfo) iterator.next();
		} while (villageObjectInfo.posX != x || villageObjectInfo.posZ != z || Math.abs(villageObjectInfo.posY - y) > 1);

		return villageObjectInfo;
	}

	private void addDoorToNewListIfAppropriate(int par1, int par2, int par3) {
		int l = ((BlockDoor) Block.doorWood).getDoorOrientation(this.worldObj, par1, par2, par3);
		int i1;
		int j1;

		if (l != 0 && l != 2) {
			i1 = 0;

			for (j1 = -5; j1 < 0; ++j1) {
				if (this.worldObj.canBlockSeeTheSky(par1, par2, par3 + j1)) {
					--i1;
				}
			}
			for (j1 = 1; j1 <= 5; ++j1) {
				if (this.worldObj.canBlockSeeTheSky(par1, par2, par3 + j1)) {
					++i1;
				}
			}

			if (i1 != 0) {
				System.out.println("RomanVillageCollection.addDoorToNewListIfAppropriate adding new door!");
				this.newDoors.add(new RomanVillageDoorInfo(par1, par2, par3, 0, i1 > 0 ? -2 : 2, this.tickCounter));
			}
		} else {
			i1 = 0;

			for (j1 = -5; j1 < 0; ++j1) {
				if (this.worldObj.canBlockSeeTheSky(par1 + j1, par2, par3)) {
					--i1;
				}
			}

			for (j1 = 1; j1 <= 5; ++j1) {
				if (this.worldObj.canBlockSeeTheSky(par1 + j1, par2, par3)) {
					++i1;
				}
			}

			if (i1 != 0) {
				System.out.println("RomanVillageCollection.addDoorToNewListIfAppropriate adding new door!");
				this.newDoors.add(new RomanVillageDoorInfo(par1, par2, par3, i1 > 0 ? -2 : 2, 0, this.tickCounter));
			}
		}
	}

	private void addBloomeryToNewListIfAppropriate(int x, int y, int z) {
		TileEntityBloomery bloomery = (TileEntityBloomery) this.worldObj.getBlockTileEntity(x, y, z);
		if (bloomery != null && bloomery.getIsValid() && bloomery.getIsMaster()) {
			System.out.println("RomanVillageCollection.addBloomeryToNewListIfAppropriate adding new Bloomery!");
			this.newBloomeries.add(new RomanVillageBloomeryInfo(x, y, z));
		}
	}

	private boolean isVillagerPositionPresent(int par1, int par2, int par3) {
		Iterator iterator = this.villagerPositionsList.iterator();
		ChunkCoordinates chunkcoordinates;

		do {
			if (!iterator.hasNext()) {
				return false;
			}

			chunkcoordinates = (ChunkCoordinates) iterator.next();
		} while (chunkcoordinates.posX != par1 || chunkcoordinates.posY != par2 || chunkcoordinates.posZ != par3);

		return true;
	}

	private boolean isWoodenDoorAt(int x, int y, int z) {
		int l = this.worldObj.getBlockId(x, y, z);
		return l == Block.doorWood.blockID;
	}
	
	private boolean isBloomeryAt(int x, int y, int z) {
		int l = this.worldObj.getBlockId(x, y, z);
		return l == BlockManager.blockBloomery.blockID;
	}

	/**
	 * reads in data from the NBTTagCompound into this MapDataBase
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		System.out.println("RomanVillageCollection.readFromNBT");
		this.tickCounter = par1NBTTagCompound.getInteger("Tick");
		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Villages");

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			RomanVillage village = new RomanVillage();
			village.readVillageDataFromNBT(nbttagcompound1);
			this.villageList.add(village);
		}
	}

	/**
	 * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		System.out.println("RomanVillageCollection.writeToNBT");
		par1NBTTagCompound.setInteger("Tick", this.tickCounter);
		NBTTagList nbttaglist = new NBTTagList("Villages");
		Iterator iterator = this.villageList.iterator();

		while (iterator.hasNext()) {
			RomanVillage village = (RomanVillage) iterator.next();
			NBTTagCompound nbttagcompound1 = new NBTTagCompound("Village");
			village.writeVillageDataToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		par1NBTTagCompound.setTag("Villages", nbttaglist);
	}
}