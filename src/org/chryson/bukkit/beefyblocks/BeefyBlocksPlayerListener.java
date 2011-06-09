/*  BeefyBlocks - A Bukkit plugin to beef-up blocks, making them last longer
 *  Copyright (C) 2011 Letat
 *  Copyright (C) 2011 Robert Sargant
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
*/    	

package org.chryson.bukkit.beefyblocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class BeefyBlocksPlayerListener extends PlayerListener {
	private BeefyBlocks parent;
	
	public BeefyBlocksPlayerListener(BeefyBlocks instance) {
		parent = instance;
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (parent.permBlockPlacers.contains(p.getName()))
			parent.permBlockPlacers.remove(p.getName());
	}
	
    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    	Player p = event.getPlayer();
    	String[] args = event.getMessage().split(" ");
    	
    	if (args[0].equals("/beefup")) {
	    	if (BeefyBlocks.hasPermission(p, "beefyblocks.beefup")) {
	    		if (args.length == 3) {
	    			try {
	        			int block = Material.valueOf(args[1].toUpperCase()).getId();
	        			int lives = Integer.parseInt(args[2]);
	        			parent.baseLives[block] = (byte) lives;
	        			parent.getConfiguration().setProperty("blocks.base." + args[1].toUpperCase(), lives);
	        			parent.getConfiguration().save();
	        			p.sendMessage(ChatColor.GREEN + "[Beefy] Block durability updated.");
	    			} catch (NumberFormatException e) {
		    			p.sendMessage(ChatColor.RED + "[Beefy] Invalid amount of breaks");
	    			} catch (IllegalArgumentException e) {
	    				p.sendMessage(ChatColor.RED + "[Beefy] Invalid block type");
	    			} catch (Exception e) {
	    				p.sendMessage(ChatColor.RED + "[Beefy] Unable to save changes.");
	    			}
	    		} else if (args.length == 4) {
	    			try {
	        			int block = Material.valueOf(args[2].toUpperCase()).getId();
	        			int lives = Integer.parseInt(args[3]);
	        			parent.placedLives[block] = (byte) lives;
	        			parent.getConfiguration().setProperty("blocks.placed." + args[2].toUpperCase(), lives);
	        			parent.getConfiguration().save();
	        			p.sendMessage(ChatColor.GREEN + "[Beefy] Block durability updated.");
	    			} catch (NumberFormatException e) {
		    			p.sendMessage(ChatColor.RED + "[Beefy] Invalid amount of breaks");
	    			} catch (IllegalArgumentException e) {
	    				p.sendMessage(ChatColor.RED + "[Beefy] Invalid block type");
	    			} catch (Exception e) {
	    				p.sendMessage(ChatColor.RED + "[Beefy] Unable to save changes.");
	    			}
	    		} else {
	    			p.sendMessage(ChatColor.YELLOW + "[Beefy] Insufficient amount of parameters.");
	    			p.sendMessage(ChatColor.YELLOW + "[Beefy] Usage: /beefup [p] dirt 3");
	    		}
	    	} else {
	    		p.sendMessage(ChatColor.RED + "[Beefy] You do not have the authority to do that.");
	    	}
			event.setCancelled(true);
    	} else if (args[0].equals("/beefyshow")) {
    		PlayerSettings ps = parent.getPlayerSettings(p.getName(), false);
    		if (ps == null) {
    			ps = new PlayerSettings(p.getName(), DisplayPreference.NONE);
    			p.sendMessage(ChatColor.GREEN + "[Beefy] Status messages disabled.");
    		} else {
    			if (ps.getDisplay() == DisplayPreference.ALL) {
    				ps.setDisplay(DisplayPreference.NONE);
    				p.sendMessage(ChatColor.GREEN + "[Beefy] Status messages disabled.");
    			} else {
    				ps.setDisplay(DisplayPreference.ALL);
    				p.sendMessage(ChatColor.GREEN + "[Beefy] Status messages enabled.");
    			}
    		}
    		parent.getDatabase().save(ps);
    		event.setCancelled(true);
    	} else if (args[0].equals("/beefyperm")) {
    		if (BeefyBlocks.hasPermission(p, "beefyblocks.perm")) {
	    		if (parent.permBlockPlacers.contains(p.getName())) {
	    			parent.permBlockPlacers.remove(p.getName());
	    			p.sendMessage(ChatColor.GREEN + "[Beefy] You are no longer placing permanent blocks.");
	    		} else {
	    			parent.permBlockPlacers.add(p.getName());
	    			p.sendMessage(ChatColor.GREEN + "[Beefy] You are now placing permanent blocks.");
	    		}
    		} else {
    			p.sendMessage(ChatColor.RED + "[Beefy] You do not have the authority to do that.");
    		}
    		event.setCancelled(true);
    	}
    }
}
