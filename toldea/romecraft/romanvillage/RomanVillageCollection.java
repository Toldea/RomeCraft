package toldea.romecraft.romanvillage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class RomanVillageCollection extends WorldSavedData {
	public static final String key = "romanVillages";

	private World worldObj;

	/**
	 * This is a black hole. You can add data to this list through a public interface, but you can't query that information in any way and it's not used
	 * internally either.
	 */
	private final List villagerPositionsList = new ArrayList();
	private final List newDoors = new ArrayList();
	private final List villageList = new ArrayList();
	private int tickCounter;

	public RomanVillageCollection(String par1Str) {
		super(key);
	}

	public RomanVillageCollection(World par1World) {
		super(key);
		System.out.println("RomanVillageCollection.RomanVillageCollection");
		this.worldObj = par1World;
		this.markDirty();
	}

	public void linkWorld(World par1World) {
		System.out.println("RomanVillageCollection.linkWorld");
		this.worldObj = par1World;
		Iterator iterator = this.villageList.iterator();

		while (iterator.hasNext()) {
			RomanVillage village = (RomanVillage) iterator.next();
			village.linkWorld(par1World);
		}
	}

	/**
	 * This is a black hole. You can add data to this list through a public interface, but you can't query that information in any way and it's not used
	 * internally either.
	 */
	public void addVillagerPosition(int par1, int par2, int par3) {
		System.out.println("RomanVillageCollection.addVillagerPosition");
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
		//System.out.println("RomanVillageCollection.tick");
		++this.tickCounter;
		Iterator iterator = this.villageList.iterator();

		while (iterator.hasNext()) {
			RomanVillage village = (RomanVillage) iterator.next();
			village.tick(this.tickCounter);
			
			//
			this.addUnassignedWoodenDoorsAroundToNewDoorsList(village.getCenter());
		}

		this.removeAnnihilatedVillages();
		//this.dropOldestVillagerPosition();
		this.addNewDoorsToVillageOrCreateVillage(); // TODO: Rewrite this so it doesn't recreate villages but only works for existing ones!

		if (this.tickCounter % 400 == 0) {
			this.markDirty();
		}
	}

	private void removeAnnihilatedVillages() {
		//System.out.println("RomanVillageCollection.removeAnnihilatedVillages");
		Iterator iterator = this.villageList.iterator();

		while (iterator.hasNext()) {
			RomanVillage village = (RomanVillage) iterator.next();

			if (village.isAnnihilated()) {
				System.out.println("RomanVillageCollection.removeAnnihilatedVillages is devouring a village! Ow noes! D:");
				iterator.remove();
				this.markDirty();
			}
		}
	}

	/**
	 * Get a list of villages.
	 */
	public List getVillageList() {
		System.out.println("RomanVillageCollection.getVillageList");
		return this.villageList;
	}

	/**
	 * Finds the nearest village, but only the given coordinates are withing it's bounding box plus the given the distance.
	 */
	public RomanVillage findNearestVillage(int par1, int par2, int par3, int par4) {
		System.out.println("RomanVillageCollection.findNearestVillage");
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
		//System.out.println("RomanVillageCollection.dropOldestVillagerPosition");
		if (!this.villagerPositionsList.isEmpty()) {
			System.out.println("RomanVillageCollection.dropOldestVillagerPosition: villagerPositionsList not empty :D");
			this.addUnassignedWoodenDoorsAroundToNewDoorsList((ChunkCoordinates) this.villagerPositionsList.remove(0));
		}
	}

	private void addNewDoorsToVillageOrCreateVillage() {
		//System.out.println("RomanVillageCollection.addNewDoorsToVillageOrCreateVillage");
		int i = 0;

		while (i < this.newDoors.size()) {
			System.out.println("looping through new door #" + i);
			RomanVillageDoorInfo villagedoorinfo = (RomanVillageDoorInfo) this.newDoors.get(i);
			boolean flag = false;
			Iterator iterator = this.villageList.iterator();

			while (true) {
				if (iterator.hasNext()) {
					RomanVillage village = (RomanVillage) iterator.next();
					System.out.println("Looping through village: " + village);
					int j = (int) village.getCenter().getDistanceSquared(villagedoorinfo.posX, villagedoorinfo.posY, villagedoorinfo.posZ);
					float k = 32f + village.getVillageRadius();

					if (j > k * k) {
						continue;
					}

					village.addVillageDoorInfo(villagedoorinfo);
					flag = true;
				}

				if (!flag) {					
					RomanVillage village1 = new RomanVillage(this.worldObj);
					
					System.out.println("Creating new village!: " + village1);
					
					System.out.println("-- Roman Village Data --");
					System.out.println("Village Center: (" + village1.getCenter().posX + ", " + village1.getCenter().posY + ", " + village1.getCenter().posZ + ")");
					System.out.println("Number of doors: " + village1.getNumVillageDoors());
					System.out.println("Number of villagers: " + village1.getNumVillagers());
					System.out.println("Village radius: " + village1.getVillageRadius());
					
					village1.addVillageDoorInfo(villagedoorinfo);
					this.villageList.add(village1);
					this.markDirty();
				}

				++i;
				break;
			}
		}

		this.newDoors.clear();
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
		ChunkCoordinates villagePos = new ChunkCoordinates(0,0,0);
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			village = (RomanVillage) iterator.next();
			villagePos = village.getVillageForumLocation();
		} while (villagePos.posX != par1 || villagePos.posZ != par3 || Math.abs(villagePos.posY - par2) > 1);

		return village;
	}

	private void addUnassignedWoodenDoorsAroundToNewDoorsList(ChunkCoordinates par1ChunkCoordinates) {
		//System.out.println("RomanVillageCollection.addUnassignedWoodenDoorsAroundToNewDoorsList");
		byte b0 = 16;
		byte b1 = 4;
		byte b2 = 16;

		for (int i = par1ChunkCoordinates.posX - b0; i < par1ChunkCoordinates.posX + b0; ++i) {
			for (int j = par1ChunkCoordinates.posY - b1; j < par1ChunkCoordinates.posY + b1; ++j) {
				for (int k = par1ChunkCoordinates.posZ - b2; k < par1ChunkCoordinates.posZ + b2; ++k) {
					if (this.isWoodenDoorAt(i, j, k)) {
						RomanVillageDoorInfo villagedoorinfo = this.getVillageDoorAt(i, j, k);

						if (villagedoorinfo == null) {
							this.addDoorToNewListIfAppropriate(i, j, k);
						} else {
							villagedoorinfo.lastActivityTimestamp = this.tickCounter;
						}
					}
				}
			}
		}
	}

	private RomanVillageDoorInfo getVillageDoorAt(int par1, int par2, int par3) {
		//System.out.println("RomanVillageCollection.getVillageDoorAt");
		Iterator iterator = this.newDoors.iterator();
		RomanVillageDoorInfo villagedoorinfo;

		do {
			if (!iterator.hasNext()) {
				iterator = this.villageList.iterator();
				RomanVillageDoorInfo villagedoorinfo1;

				do {
					if (!iterator.hasNext()) {
						return null;
					}

					RomanVillage village = (RomanVillage) iterator.next();
					villagedoorinfo1 = village.getVillageDoorAt(par1, par2, par3);
				} while (villagedoorinfo1 == null);

				return villagedoorinfo1;
			}

			villagedoorinfo = (RomanVillageDoorInfo) iterator.next();
		} while (villagedoorinfo.posX != par1 || villagedoorinfo.posZ != par3 || Math.abs(villagedoorinfo.posY - par2) > 1);

		return villagedoorinfo;
	}

	private void addDoorToNewListIfAppropriate(int par1, int par2, int par3) {
		//System.out.println("RomanVillageCollection.addDoorToNewListIfAppropriate");
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
	
	private boolean isVillagerPositionPresent(int par1, int par2, int par3) {
		System.out.println("RomanVillageCollection.isVillagerPositionPresent");
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
	
	private boolean isWoodenDoorAt(int par1, int par2, int par3) {
		int l = this.worldObj.getBlockId(par1, par2, par3);
		return l == Block.doorWood.blockID;
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