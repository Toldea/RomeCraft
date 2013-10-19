package toldea.romecraft.romanvillage;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapCoord;
import net.minecraft.world.storage.MapData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RomanVillageMap extends MapData {
	public static final short DEFAULT_ROMAN_MAP_SIZE = 128;
	public short mapSize;

	public RomanVillageMap(String par1Str, short mapSize) {
		super(par1Str);
		this.mapSize = mapSize;
		colors = new byte[mapSize * mapSize];
	}

	public void updateMapData(World par1World) {
		if (par1World.provider.dimensionId == this.dimension) {
			short short1 = this.mapSize;
			short short2 = this.mapSize;
			int i = 1 << this.scale;
			int j = this.xCenter;
			int k = this.zCenter;
			int l = short1 / 2;
			int i1 = short2 / 2;
			int j1 = this.mapSize / i;

			if (par1World.provider.hasNoSky) {
				j1 /= 2;
			}

			for (int k1 = l - j1 + 1; k1 < l + j1; ++k1) {
				int l1 = 255;
				int i2 = 0;
				double d0 = 0.0D;

				for (int j2 = i1 - j1 - 1; j2 < i1 + j1; ++j2) {
					if (k1 >= 0 && j2 >= -1 && k1 < short1 && j2 < short2) {
						int k2 = k1 - l;
						int l2 = j2 - i1;
						boolean flag = k2 * k2 + l2 * l2 > (j1 - 2) * (j1 - 2);
						int i3 = (j / i + k1 - short1 / 2) * i;
						int j3 = (k / i + j2 - short2 / 2) * i;
						int[] aint = new int[Block.blocksList.length];
						Chunk chunk = par1World.getChunkFromBlockCoords(i3, j3);

						if (!chunk.isEmpty()) {
							int k3 = i3 & 15;
							int l3 = j3 & 15;
							int i4 = 0;
							double d1 = 0.0D;
							int j4;
							int k4;
							int l4;
							int i5;

							if (par1World.provider.hasNoSky) {
								j4 = i3 + j3 * 231871;
								j4 = j4 * j4 * 31287121 + j4 * 11;

								if ((j4 >> 20 & 1) == 0) {
									aint[Block.dirt.blockID] += 10;
								} else {
									aint[Block.stone.blockID] += 10;
								}

								d1 = 100.0D;
							} else {
								for (j4 = 0; j4 < i; ++j4) {
									for (k4 = 0; k4 < i; ++k4) {
										l4 = chunk.getHeightValue(j4 + k3, k4 + l3) + 1;
										int j5 = 0;

										if (l4 > 1) {
											boolean flag1;

											do {
												flag1 = true;
												j5 = chunk.getBlockID(j4 + k3, l4 - 1, k4 + l3);

												if (j5 == 0) {
													flag1 = false;
												} else if (l4 > 0 && j5 > 0 && Block.blocksList[j5].blockMaterial.materialMapColor == MapColor.airColor) {
													flag1 = false;
												}

												if (!flag1) {
													--l4;

													if (l4 <= 0) {
														break;
													}

													j5 = chunk.getBlockID(j4 + k3, l4 - 1, k4 + l3);
												}
											} while (l4 > 0 && !flag1);

											if (l4 > 0 && j5 != 0 && Block.blocksList[j5].blockMaterial.isLiquid()) {
												i5 = l4 - 1;
												boolean flag2 = false;
												int k5;

												do {
													k5 = chunk.getBlockID(j4 + k3, i5--, k4 + l3);
													++i4;
												} while (i5 > 0 && k5 != 0 && Block.blocksList[k5].blockMaterial.isLiquid());
											}
										}

										d1 += (double) l4 / (double) (i * i);
										++aint[j5];
									}
								}
							}

							i4 /= i * i;
							j4 = 0;
							k4 = 0;

							for (l4 = 0; l4 < Block.blocksList.length; ++l4) {
								if (aint[l4] > j4) {
									k4 = l4;
									j4 = aint[l4];
								}
							}

							double d2 = (d1 - d0) * 4.0D / (double) (i + 4) + ((double) (k1 + j2 & 1) - 0.5D) * 0.4D;
							byte b0 = 1;

							if (d2 > 0.6D) {
								b0 = 2;
							}

							if (d2 < -0.6D) {
								b0 = 0;
							}

							i5 = 0;

							if (k4 > 0) {
								MapColor mapcolor = Block.blocksList[k4].blockMaterial.materialMapColor;

								if (mapcolor == MapColor.waterColor) {
									d2 = (double) i4 * 0.1D + (double) (k1 + j2 & 1) * 0.2D;
									b0 = 1;

									if (d2 < 0.5D) {
										b0 = 2;
									}

									if (d2 > 0.9D) {
										b0 = 0;
									}
								}

								i5 = mapcolor.colorIndex;
							}

							d0 = d1;

							if (j2 >= 0 && k2 * k2 + l2 * l2 < j1 * j1 && (!flag || (k1 + j2 & 1) != 0)) {
								byte b1 = this.colors[k1 + j2 * short1];
								byte b2 = (byte) (i5 * 4 + b0);

								if (b1 != b2) {
									if (l1 > j2) {
										l1 = j2;
									}

									if (i2 < j2) {
										i2 = j2;
									}

									this.colors[k1 + j2 * short1] = b2;
								}
							}
						}
					}
				}

				if (l1 <= i2) {
					this.setColumnDirty(k1, l1, i2);
				}
			}
		}
	}

	/**
	 * reads in data from the NBTTagCompound into this MapDataBase
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		NBTBase dimension = par1NBTTagCompound.getTag("dimension");

		if (dimension instanceof NBTTagByte) {
			this.dimension = ((NBTTagByte) dimension).data;
		} else {
			this.dimension = ((NBTTagInt) dimension).data;
		}

		this.xCenter = par1NBTTagCompound.getInteger("xCenter");
		this.zCenter = par1NBTTagCompound.getInteger("zCenter");
		this.scale = par1NBTTagCompound.getByte("scale");

		if (this.scale < 0) {
			this.scale = 0;
		}

		if (this.scale > 4) {
			this.scale = 4;
		}

		this.mapSize = par1NBTTagCompound.getShort("mapsize");
		this.colors = par1NBTTagCompound.getByteArray("colors");
	}

	/**
	 * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setInteger("dimension", this.dimension);
		par1NBTTagCompound.setInteger("xCenter", this.xCenter);
		par1NBTTagCompound.setInteger("zCenter", this.zCenter);
		par1NBTTagCompound.setByte("scale", this.scale);
		par1NBTTagCompound.setShort("mapsize", this.mapSize);
		par1NBTTagCompound.setByteArray("colors", this.colors);
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Updates the client's map with information from other players in MP
	 */
	public void updateMPMapData(byte[] par1ArrayOfByte) {
		int i;

		if (par1ArrayOfByte[0] == 0) {
			i = par1ArrayOfByte[1] & 255;
			int j = par1ArrayOfByte[2] & 255;

			for (int k = 0; k < par1ArrayOfByte.length - 3; ++k) {
				this.colors[(k + j) * this.mapSize + i] = par1ArrayOfByte[k + 3];
			}

			this.markDirty();
		} else if (par1ArrayOfByte[0] == 1) {
			this.playersVisibleOnMap.clear();

			for (i = 0; i < (par1ArrayOfByte.length - 1) / 3; ++i) {
				byte b0 = (byte) (par1ArrayOfByte[i * 3 + 1] >> 4);
				byte b1 = par1ArrayOfByte[i * 3 + 2];
				byte b2 = par1ArrayOfByte[i * 3 + 3];
				byte b3 = (byte) (par1ArrayOfByte[i * 3 + 1] & 15);
				this.playersVisibleOnMap.put("icon-" + i, new MapCoord(this, b0, b1, b2, b3));
			}
		} else if (par1ArrayOfByte[0] == 2) {
			this.scale = par1ArrayOfByte[1];
		}
	}
}
