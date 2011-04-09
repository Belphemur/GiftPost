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
package com.gmail.nossr50;

import org.bukkit.entity.Player;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class McParty extends mcParty {

	/**
	 * @param instance
	 */
	public McParty(mcMMO instance) {
		super(instance);
	}

	private static volatile McParty instance;
	private static mcMMO plugin;

	public static McParty getInstance() {
		if (instance == null) {
			instance = new McParty(plugin);
		}
		return instance;
	}

	public void sendMessage(Player player, String message) {
		Player[] players = plugin.getServer().getOnlinePlayers();
		int x = 0;
		for (Player p : players) {
			if (player != null && p != null) {
				if (inSameParty(player, p) && !p.getName().equals(player.getName())) {
					p.sendMessage(message);
					x++;
				}
			}
		}
	}

}
