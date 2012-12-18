package org.chryson.bukkit.listeners;

public class WorldListener implements Listener {
	private BeefyBlocks beefy;
	
	public WorldListener(BeefyBlocks bb) {
		beefy = bb;
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onStructureGrow(StructureGrowEvent event) {
		
	}
}