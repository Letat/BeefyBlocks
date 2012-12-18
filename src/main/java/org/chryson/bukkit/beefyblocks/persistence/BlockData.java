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
@Table(name="bb_block_data")
public class Block {
	@EmbeddedId
	private BlockLocation location;
	
	@Basic
	@NotNull
	@NotEmpty
	private String materialName;
	
	@Basic
	@NotNull
	@NotEmpty
	private String ownerName;
	
	@Basic
	private Timestamp timestamp;
	
	@Basic
	private boolean permanent;
	
	@Basic
	private int reinforcement;
	
	public Block() {}
	
	public Block(Player owner, Block block) {
		this(owner, block, false);
	}
	
	public Block(Player owner, Block block, boolean permanent) {
		this(owner.getName(), block, permanent);
	}
	
	public Block(String ownerName, Block block, boolean permanent) {
		setOwnerName(ownerName);
		setMaterial(block.getType());
		setLocation(new BlockLocation(block.getLocation()));
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
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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
