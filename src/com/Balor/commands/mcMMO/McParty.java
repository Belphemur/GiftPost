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
package com.Balor.commands.mcMMO;

import org.bukkit.entity.Player;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.mcParty;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class McParty extends mcParty {

	/**
	 * @param instance
	 */
	public McParty(mcMMO instance) {
		super(GiftPostWorker.getmcMMO());
	}

	private static volatile McParty myInstance;

	public static McParty getInstance() {
		if (myInstance == null) {
			myInstance = new McParty(null);
		}
		return myInstance;
	}

	public void sendMessage(Player player, String message) {
		Player[] players = GiftPostWorker.getmcMMO().getServer().getOnlinePlayers();
		int x = 0;
		for (Player p : players) {
			if (player != null && p != null) {
				if (inSameParty(player, p)) {
					p.sendMessage(message);
					x++;
				}
			}
		}
	}

}
