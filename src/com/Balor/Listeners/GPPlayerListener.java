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
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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

	public GPPlayerListener() {
		worker = GiftPostWorker.getInstance();
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (worker.getConfig().getString("message-of-the-day", "true").matches("true"))
			event.getPlayer().sendMessage(
					"Virtual Chest is installed : /gp help to see all commands");
		if (worker.getDefaultChest(event.getPlayer().getName()) != null
				&& !worker.getDefaultChest(event.getPlayer().getName()).isEmpty()) {
			worker.getFileManager().openOfflineFile(event.getPlayer());
			if (worker.getConfig().getString("message-of-the-day", "true").matches("true"))
				event.getPlayer().sendMessage(
						ChatColor.GOLD + "(command" + ChatColor.RED + " /gp c" + ChatColor.GOLD
								+ " to see your chest.)");
		}
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (worker.getConfig().getString("allow-offline", "true").matches("true"))
			worker.getFileManager().createWorldFile(event.getPlayer());
		worker.removePermissionNode(event.getPlayer().getName());
	}

	public void onSign(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = event.getClickedBlock();
			if (block.getType().equals(Material.WALL_SIGN)
					|| block.getType().equals(Material.SIGN_POST)) {
				Sign sign = (Sign) block.getState();
				if (sign.getLine(0).indexOf("[Chest Keeper]") == 0
						&& sign.getLine(0).indexOf("]") != -1
						&& worker.hasPerm(event.getPlayer(), "giftpost.chest.open")) {
					new Chest().execute(worker, event.getPlayer(), null);
				}
			}
		}
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		onSign(event);
		if (worker.getConfig().getString("use-wand", "true").matches("true")
				&& (event.getPlayer().getItemInHand().getType().getId() == worker.getConfig()
						.getInt("wand-item-id", Material.CHEST.getId()))
				&& (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(
						Action.LEFT_CLICK_BLOCK))
				&& worker.hasPerm(event.getPlayer(), "giftpost.chest.everywhere")) {
			new Chest().execute(worker, event.getPlayer(), null);
		}
	}
}
