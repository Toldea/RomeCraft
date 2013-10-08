package toldea.romecraft.romanvillage;

public class RomanVillageObjectInfo {
	public final int posX;
	public final int posY;
	public final int posZ;
	
	public RomanVillageObjectInfo(int x, int y, int z) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}
	
	/**
	 * Returns the squared distance between this door and the given coordinate.
	 */
	public int getDistanceSquared(int par1, int par2, int par3) {
		int l = par1 - this.posX;
		int i1 = par2 - this.posY;
		int j1 = par3 - this.posZ;
		return l * l + i1 * i1 + j1 * j1;
	}
}
