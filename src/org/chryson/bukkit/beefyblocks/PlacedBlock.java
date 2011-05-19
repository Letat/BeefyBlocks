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

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.avaje.ebean.annotation.CacheStrategy;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@CacheStrategy(readOnly=true)
@Entity
@Table(name="bb_placed_blocks")
public class PlacedBlock {
	@EmbeddedId
	private PlacedBlockLocation loc;
	
	@Basic
	private int blockTypeId;
	
	@Basic
	@NotNull
	@NotEmpty
	private String playerName;
	
	@Basic
	private Timestamp timestamp;
	
	public PlacedBlock() {}
	
	public PlacedBlock(Player p, Block b) {
		setPlayerName(p.getName());
		setBlockTypeId(b.getTypeId());
		setLoc(new PlacedBlockLocation(b.getLocation()));
		setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
	}
	
	public PlacedBlockLocation getLoc() {
		return loc;
	}
	
	public void setLoc(PlacedBlockLocation loc) {
		this.loc = loc;
	}
	
	public int getBlockTypeId() {
		return blockTypeId;
	}
	
	public void setBlockTypeId(int blockTypeId) {
		this.blockTypeId = blockTypeId;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
