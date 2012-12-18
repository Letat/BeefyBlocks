package org.chryson.bukkit.listeners;

public class BlockListener implements Listener {
	private BeefyBlocks beefy;
	
	public BlockListener(BeefyBlocks bb) {
		beefy = bb;
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onBlockBreak(BlockBreakEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onBlockBurn(BlockBurnEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onBlockFade(BlockFadeEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onBlockForm(BlockFormEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onBlockGrow(BlockGrowEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onBlockPistonRetract(BlockPistonRetractEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onBlockPlace(BlockPlaceEvent event) {
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onBlockSpread(BlockSpreadEvent event) {
		
	}
}
