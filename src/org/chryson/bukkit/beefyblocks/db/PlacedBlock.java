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

package org.chryson.bukkit.beefyblocks.db;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.avaje.ebean.annotation.CacheStrategy;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

//@CacheStrategy(readOnly=true)
@Entity
@Table(name="bb_placed_blocks")
public class PlacedBlock {
	@EmbeddedId
	private BlockLocation location;
	
	@Basic
	@NotNull
	@NotEmpty
	private String materialName;
	
	@Basic
	@NotNull
	@NotEmpty
	private String playerName;
	
	@Basic
	private Timestamp timestamp;
	
	@Basic
	private boolean permanent;
	
	@Basic
	private int reinforcement;
	
	public PlacedBlock() {}
	
	public PlacedBlock(Player p, Block b) {
		this(p, b, false);
	}
	
	public PlacedBlock(Player p, Block b, boolean permanent) {
		this(p.getName(), b, permanent);
	}
	
	public PlacedBlock(String pName, Block b, boolean permanent) {
		setPlayerName(pName);
		setMaterial(b.getType());
		setLocation(new BlockLocation(b.getLocation()));
		setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
		setPermanent(permanent);
		setReinforcement(0);
	}
	
	public BlockLocation getLocation() {
		return location;
	}
	
	public void setLocation(BlockLocation loc) {
		this.location = location;
	}
	
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	public String getMaterialName() {
		return materialName;
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
	
	public boolean isPermanent() {
		return permanent;
	}
	
	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}
	
	public int getReinforcement() {
		return reinforcement;
	}
	
	public void setReinforcement(int reinforcement) {
		this.reinforcement = reinforcement;
	}
	
	public Material getMaterial() {
		return Material.getMaterial(materialName);
	}
	
	public void setMaterial(Material material) {
		setMaterialName(material.toString());
	}
}
