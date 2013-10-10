package toldea.romecraft.block;

public class BlockHelper {
	private BlockHelper() {}
	
	public static final int MASK_DIR = 0x00000007;
	public static final int META_DIR_NORTH = 0x00000001;
	public static final int META_DIR_SOUTH = 0x00000002;
	public static final int META_DIR_EAST = 0x00000003;
	public static final int META_DIR_WEST = 0x00000000;
	
	public static byte getDirectionByteForInt(int direction) {
		switch (direction) {
		case 0:
			return META_DIR_EAST;
		case 1:
			return META_DIR_SOUTH;
		case 2:
			return META_DIR_NORTH;
		case 3:
			return META_DIR_WEST;
		default:
			return 0;
		}
	}
	
	public static byte getOppositeDirectionByteForInt(int direction) {
		switch (direction) {
		case 0:
			return META_DIR_WEST;
		case 1:
			return META_DIR_NORTH;
		case 2:
			return META_DIR_SOUTH;
		case 3:
			return META_DIR_EAST;
		default:
			return 0;
		}
	}
}
