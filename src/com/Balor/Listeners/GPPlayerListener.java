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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.Balor.commands.Chest;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class GPPlayerListener extends PlayerListener {
	private GiftPostWorker worker;

	public GPPlayerListener(GiftPostWorker gpw) {
		worker = gpw;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (worker.getChest(event.getPlayer().getName()) != null
				&& !worker.getChest(event.getPlayer().getName()).isEmpty()) {
			worker.getFileMan().openOfflineFile(event.getPlayer());
			event.getPlayer().sendMessage(
					ChatColor.GOLD + "(command" + ChatColor.RED + " /gp c"
							+ ChatColor.GOLD + " to see your chest.)");
		}
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (worker.getConfig().getString("allow-offline", "true")
				.matches("true"))
			worker.getFileMan().createWorldFile(event.getPlayer());
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer().getItemInHand().getType().equals(Material.CHEST)
				&& (event.getAction().equals(Action.LEFT_CLICK_AIR) || event
						.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
			new Chest().execute(worker, event.getPlayer(), null);
		}
	}
}
