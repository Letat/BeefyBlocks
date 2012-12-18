package org.chryson.bukkit.listeners;

public class HangingListener implements Listener {
	private BeefyBlocks beefy;
	
	public HangingListener(BeefyBlocks bb) {
		beefy = bb;
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onHangingBreak(HangingBreakEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onHangingPlace(HangingPlaceEvent event) {
		
	}
}