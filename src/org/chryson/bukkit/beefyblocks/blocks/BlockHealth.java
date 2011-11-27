package org.chryson.bukkit.beefyblocks.blocks;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.chryson.bukkit.beefyblocks.PersistenceManager;
import org.chryson.bukkit.beefyblocks.db.DisplayPreference;

public class BlockHealth {
    // the break counts for each block type
    public static byte[] naturalHealths; // max 127
    public static byte[] placedHealths;
    
    public static Map blockHealths;
    
	private short startingHealth;
	private short remainingHealth;
	
	public BlockHealth(short health) {
		setHealth(health);
	}
	
	public BlockHealth(BeefyBlock block) {
		short health = getMaxHealth(block.getType(), block.isPlaced());
		health += block.getReinforcement();
		setHealth(health);
	}
	
	private void setHealth(short health) {
		setStartingHealth(health);
		setRemainingHealth(health);
	}
	
	public static void setMaxHealth(Material mat, boolean placed, byte health) {
		if (placed)
			placedHealths[mat.getId()] = health;
		else
			naturalHealths[mat.getId()] = health;
	}
	
	public static byte getMaxHealth(Material mat, boolean placed) {
		byte health = 1;
		if (placed)
			health = placedHealths[mat.getId()];
		else
			health = naturalHealths[mat.getId()];
		return health;
	}
	
	public void setStartingHealth(short health) {
		startingHealth = health;
	}
	
	public short getStartingHealth() {
		return startingHealth;
	}
	
	public void setRemainingHealth(short health) {
		remainingHealth = health;
	}
	
	public short getRemainingHealth() {
		return remainingHealth;
	}
	
	public void lose(short amount) {
		remainingHealth -= amount;
	}
	
	public void gain(short amount) {
		remainingHealth += amount;
	}
	
	public float percentBroken() {
		float lostHealth = (float)(startingHealth - remainingHealth);
		return lostHealth / (float)startingHealth;
	}
	
	public boolean isGone() {
		return (remainingHealth <= 0);
	}
	
	public boolean isNotifiable(DisplayPreference pref) {
		if (pref == DisplayPreference.ALL)
			return true;
		if (pref == DisplayPreference.NONE)
			return false;
		short numNotifications = 3;
		short interval = (short) (startingHealth / numNotifications);
		short lostHealth = (short) (startingHealth - remainingHealth);
		return (lostHealth % interval == 0);
	}
	
	public void notify(Player player) {
		if (isNotifiable(PersistenceManager.getDisplayPref(player)))
			player.sendMessage(toString());
	}

	public String toString() {
		//return String.format("%d/%d intact", remainingHealth, startingHealth);
		return String.format("%.0f intact", (float)remainingHealth / (float)startingHealth);
	}
}
