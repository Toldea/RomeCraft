package toldea.romecraft.managers;

import java.io.File;

import net.minecraftforge.common.Configuration;

public class ConfigManager {
	public static int blockRomanBricksId;
	public static int blockRomanBricksHalfSlabId;
	public static int blockRomanBricksDoubleSlabId;
	public static int blockRomanBricksStairsId;
	public static int blockRomanBricksChiseledId;
	public static int blockMarbleId;
	public static int blockMarbleMosaicId;
	public static int blockMarbleMosaicHalfSlabId;
	public static int blockMarbleMosaicDoubleSlabId;
	public static int blockMarbleMosaicStairsId;
	public static int blockRomanVillageForumId;
	public static int blockBloomeryId;
	public static int blockBellowsId;
	public static int blockRomanAnvilId;
	
	public static int itemGladiusId;
	public static int itemScutumId;
	public static int itemGaleaId;
	public static int itemLoricaSegmentataId;
	public static int itemCingulumId;
	public static int itemCaligaeId;
	public static int itemPilumId;
	public static int itemVerutumId;
	public static int itemPugioId;
	public static int itemSarcinaId;
	public static int itemSudisId;
	public static int itemLegionaryEquipmentId;
	public static int itemBlacksmithEquipmentId;
	public static int itemScepterId;
	public static int itemIronBloomId;
	
	private ConfigManager() {
	}

	public static void loadConfig(File file) {
		Configuration config = new Configuration(file);

		config.load();
		
		blockRomanBricksId = config.getBlock("blockRomanBricks", 1550).getInt();
		blockRomanBricksHalfSlabId = config.getBlock("blockRomanBricksHalfSlab", 1551).getInt();
		blockRomanBricksDoubleSlabId = config.getBlock("blockRomanBricksDoubleSlab", 1552).getInt();
		blockRomanBricksStairsId = config.getBlock("blockRomanBricksStairs", 1553).getInt();
		blockRomanBricksChiseledId = config.getBlock("blockRomanBricksChiseled", 1554).getInt();
		blockMarbleId = config.getBlock("blockMarble", 1555).getInt();
		blockMarbleMosaicId = config.getBlock("blockMarbleMosaic", 1556).getInt();
		blockMarbleMosaicHalfSlabId = config.getBlock("blockMarbleMosaicHalfSlab", 1557).getInt();
		blockMarbleMosaicDoubleSlabId = config.getBlock("blockMarbleMosaicDoubleSlab", 1558).getInt();
		blockMarbleMosaicStairsId = config.getBlock("blockMarbleMosaicStairs", 1559).getInt();
		blockRomanVillageForumId = config.getBlock("blockRomanVillageForum", 1560).getInt();
		blockBloomeryId = config.getBlock("blockBloomery", 1561).getInt();
		blockBellowsId = config.getBlock("blockBellows", 1562).getInt();
		blockRomanAnvilId = config.getBlock("blockRomanAnvil", 1563).getInt();
		
		itemGladiusId = config.getItem("itemGladius", 1650).getInt();
		itemScutumId = config.getItem("itemScutum", 1651).getInt();
		itemGaleaId = config.getItem("itemGalea", 1652).getInt();
		itemLoricaSegmentataId = config.getItem("itemLoricaSegmentata", 1653).getInt();
		itemCingulumId = config.getItem("itemCingulum", 1654).getInt();
		itemCaligaeId = config.getItem("itemCaligae", 1655).getInt();
		itemPilumId = config.getItem("itemPilum", 1656).getInt();
		itemVerutumId = config.getItem("itemVerutum", 1657).getInt();
		itemPugioId = config.getItem("itemPugio", 1658).getInt();
		itemSarcinaId = config.getItem("itemSarcina", 1659).getInt();
		itemSudisId = config.getItem("itemSudis", 1660).getInt();
		itemLegionaryEquipmentId = config.getItem("itemLegionaryEquipment", 1661).getInt();
		itemBlacksmithEquipmentId = config.getItem("itemBlacksmithEquipment", 1662).getInt();
		itemScepterId = config.getItem("itemScepter", 1663).getInt();
		itemIronBloomId = config.getItem("itemIronBloom", 1664).getInt();
		
		config.save();
	}
}

