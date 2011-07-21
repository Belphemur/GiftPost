/************************************************************************
 * This file is part of GiftPost.									
 *																		
 * GiftPost is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by	
 * the Free Software Foundation, either version 3 of the License, or		
 * (at your option) any later version.									
 *																		
 * GiftPost is distributed in the hope that it will be useful,	
 * but WITHOUT ANY WARRANTY; without even the implied warranty of		
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the			
 * GNU General Public License for more details.							
 *																		
 * You should have received a copy of the GNU General Public License
 * along with GiftPost.  If not, see <http://www.gnu.org/licenses/>.
 ************************************************************************/
package com.Balor.Listeners;

import java.util.concurrent.ConcurrentMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.ItemStack;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.aranai.virtualchest.VirtualChest;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class DeathEntityListener extends EntityListener {
	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player p = (Player) event.getEntity();		
		if (GiftPostWorker.getInstance().numberOfChest(p) != 0
				&& GiftPostWorker.getInstance().getConfig().getString("drop-on-death", "false")
						.matches("true")) {
			Location deathLoc = p.getLocation();
			ConcurrentMap<String, VirtualChest> listOfChest = GiftPostWorker.getInstance().listOfChest(p);
			for (String chestName : listOfChest.keySet()) {
				VirtualChest v = listOfChest.get(chestName);
				for (ItemStack item : v.getContents())
					if (item != null)
						p.getWorld().dropItem(deathLoc, item);

				v.emptyChest();
			}
		}
	}

}
