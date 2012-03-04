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
package com.Balor.party;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.MapMaker;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class PartyManager implements CommandExecutor {
	private final Map<String, Party> playerToParty = new MapMaker().makeMap();
	private final static PartyManager INSTANCE = new PartyManager();
	private static ConfigurationSection conf;
	private static JavaPlugin plugin;

	/**
	 * @return the iNSTANCE
	 */
	public static PartyManager getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 */
	private PartyManager() {
	}

	/**
	 * @param plugin
	 *            the plugin to set
	 */
	public static void setPlugin(JavaPlugin plugin) {
		PartyManager.plugin = plugin;
		PartyManager.plugin.getCommand("gp_party").setExecutor(getInstance());
		PartyManager.plugin.getCommand("gp_popen").setExecutor(getInstance());
		PartyManager.conf = plugin.getConfig();
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin,
				PartyManager.getInstance().new PartyTask(),
				PartyManager.conf.getLong("partyCheck") / 2,
				PartyManager.conf.getLong("partyCheck"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender
	 * , org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	private class PartyTask implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			for (Entry<String, Party> entry : playerToParty.entrySet()) {
				if (entry.getValue().asExpired(conf.getLong("partyExpiration"))) {
					playerToParty.remove(entry.getKey());
					continue;
				}
				entry.getValue().save();
			}
		}

	}

}
