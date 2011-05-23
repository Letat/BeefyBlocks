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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CacheStrategy;

@CacheStrategy(readOnly=true)
@Entity
@Table(name="bb_player_settings")
public class PlayerSettings {
	@Id
	private String playerName;
	
	private DisplayPreference display;
	
	public PlayerSettings() {}
	
	public PlayerSettings(String playerName, DisplayPreference display) {
		setPlayerName(playerName);
		setDisplay(display);
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public DisplayPreference getDisplay() {
		return display;
	}
	
	public void setDisplay(DisplayPreference display) {
		this.display = display;
	}
}

