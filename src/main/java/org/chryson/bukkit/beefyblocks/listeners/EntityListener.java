package org.chryson.bukkit.listeners;

public class EntityListener implements Listener {
	private BeefyBlocks beefy;
	
	public EntityListener(BeefyBlocks bb) {
		beefy = bb;
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onEntityBreakDoor(EntityBreakDoorEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onEntityExplode(EntityExplodeEvent event) {
		
	}
}
