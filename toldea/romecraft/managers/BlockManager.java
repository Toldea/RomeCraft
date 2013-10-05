package toldea.romecraft.managers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import toldea.romecraft.block.BlockBellows;
import toldea.romecraft.block.BlockBloomery;
import toldea.romecraft.block.BlockRomanBrickSlab;
import toldea.romecraft.block.BlockRomanVillageForum;
import toldea.romecraft.block.RomeCraftBlock;
import toldea.romecraft.block.RomeCraftBlockHalfSlab;
import toldea.romecraft.block.RomeCraftBlockStairs;
import toldea.romecraft.block.RomeCraftMultiSidedBlock;
import toldea.romecraft.item.RomeCraftItemSlab;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BlockManager {
	private static int nextBlockID = 500;

	public static Block blockRomanBricks = null;
	public static RomeCraftBlockHalfSlab blockRomanBricksHalfSlab = null;
	public static RomeCraftBlockHalfSlab blockRomanBricksDoubleSlab = null;
	public static Block blockRomanBricksStairs = null;

	public static Block blockMarble = null;

	public static Block romanVillageForum = null;
	
	public static Block blockBloomery = null;
	public static Block blockBellows = null;

	public static void registerBlocks() {
		// Roman Bricks
		blockRomanBricks = new RomeCraftBlock(nextBlockID++, Material.rock).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep)
				.setUnlocalizedName("blockRomanBricks").setTextureName("romecraft:romanbrick");
		GameRegistry.registerBlock(blockRomanBricks, "blockRomanBricks");
		LanguageRegistry.addName(blockRomanBricks, "Roman Bricks");

		blockRomanBricksHalfSlab = (RomeCraftBlockHalfSlab) new BlockRomanBrickSlab(nextBlockID++, false).setHardness(2.0F).setResistance(10.0F)
				.setStepSound(Block.soundStoneFootstep).setUnlocalizedName("blockRomanBricksSlab").setTextureName("romecraft:romanbrick");
		GameRegistry.registerBlock(blockRomanBricksHalfSlab, "blockRomanBricksHalfSlab");
		LanguageRegistry.instance().addStringLocalization(((BlockRomanBrickSlab) blockRomanBricksHalfSlab).getFullSlabName(0) + ".name", "Roman Bricks Slab");
		
		blockRomanBricksDoubleSlab = (RomeCraftBlockHalfSlab) new BlockRomanBrickSlab(nextBlockID++, true).setHardness(2.0F).setResistance(10.0F)
				.setStepSound(Block.soundStoneFootstep).setUnlocalizedName("blockRomanBricksSlab").setTextureName("romecraft:romanbrick");
		GameRegistry.registerBlock(blockRomanBricksDoubleSlab, "blockRomanBricksDoubleSlab");
		LanguageRegistry.addName(blockRomanBricksDoubleSlab, "Roman Bricks Double Slab");
		
		Item.itemsList[blockRomanBricksHalfSlab.blockID] = (new RomeCraftItemSlab(blockRomanBricksHalfSlab.blockID - 256,
				(RomeCraftBlockHalfSlab) blockRomanBricksHalfSlab, (RomeCraftBlockHalfSlab) blockRomanBricksDoubleSlab, false));
		
		blockRomanBricksStairs = (new RomeCraftBlockStairs(nextBlockID++, blockRomanBricks, 0)).setUnlocalizedName("blockRomanBricksStairs");
		GameRegistry.registerBlock(blockRomanBricksStairs, "blockRomanBricksStairs");
		LanguageRegistry.addName(blockRomanBricksStairs, "Roman Brick Stairs");

		// Marble Block
		blockMarble = new RomeCraftMultiSidedBlock(nextBlockID++, Material.rock).setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep)
				.setUnlocalizedName("blockMarble").setTextureName("romecraft:marble_block");
		GameRegistry.registerBlock(blockMarble, "blockMarble");
		LanguageRegistry.addName(blockMarble, "Marble");

		// RomanVillageForum Block
		romanVillageForum = new BlockRomanVillageForum(nextBlockID++, Material.rock).setUnlocalizedName("romanVillageForum").setTextureName("romecraft:forum");
		GameRegistry.registerBlock(romanVillageForum, "blockRomanVillageForum");
		LanguageRegistry.addName(romanVillageForum, "Forum");

		// Bloomery Block
		blockBloomery = new BlockBloomery(nextBlockID++, Material.clay).setHardness(0.5F).setStepSound(Block.soundGravelFootstep)
				.setUnlocalizedName("bloomery").setTextureName("romecraft:bloomery");
		GameRegistry.registerBlock(blockBloomery, "blockBloomery");
		LanguageRegistry.addName(blockBloomery, "Bloomery Block");
		
		// Bellows Block
		blockBellows = new BlockBellows(nextBlockID++, Material.wood).setUnlocalizedName("blockBellows").setTextureName("romecraft:bellows");
		GameRegistry.registerBlock(blockBellows, "blockBellows");
		LanguageRegistry.addName(blockBellows, "Bellows");
	}
}