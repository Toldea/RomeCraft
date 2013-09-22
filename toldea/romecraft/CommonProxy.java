package toldea.romecraft;

import net.minecraft.world.World;

public class CommonProxy {

	// Client stuff
	public void registerRenderers() {
		// Nothing here as the server doesn't render graphics or entities!
	}

	public World getClientWorld() {
		return null;
	}
}