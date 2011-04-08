/*This file is part of GiftPost .

    GiftPost is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GiftPost is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GiftPost.  If not, see <http://www.gnu.org/licenses/>.*/
package com.Balor.bukkit.GiftPost;

import com.Balor.commands.GPCommand;
import com.Balor.utils.FilesManager;
import com.aranai.virtualchest.VirtualChest;
import com.aranai.virtualchest.VirtualLargeChest;
import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.permissions.PermissionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

/**
 * 
 * @author Balor
 */
public class GiftPostWorker {

	private HashMap<String, HashMap<String, VirtualChest>> chests;
	private HashMap<String, VirtualChest> defaultChests;
	private static PermissionHandler permission = null;
	private List<GPCommand> commands;
	private Configuration config;
	private FilesManager fMan;
	public static final Logger log = Logger.getLogger("Minecraft");
	private static iConomy iConomy = null;

	public GiftPostWorker(Configuration config, String dataFolder) {
		chests = new HashMap<String, HashMap<String, VirtualChest>>();
		defaultChests = new HashMap<String, VirtualChest>();
		commands = new ArrayList<GPCommand>();
		this.config = config;
		fMan = new FilesManager(dataFolder);
	}

	public FilesManager getFileMan() {
		return fMan;
	}

	public Configuration getConfig() {
		return config;
	}

	/**
	 * Return the chest, create it if not exist
	 * 
	 * @param playerName
	 * @return
	 */
	public VirtualChest getChest(String playerName, String chestName) {
		if (chests.containsKey(playerName) && chests.get(playerName).containsKey(chestName))
			return chests.get(playerName).get(chestName);
		else
			return null;
	}

	/**
	 * Return the number of owned chest
	 * 
	 * @param p
	 * @return
	 */
	public int numberOfChest(Player p) {
		if (chests.containsKey(p.getName()))
			return chests.get(p.getName()).size();
		else
			return 0;
	}

	/**
	 * Return the default chest.
	 * 
	 * @param playerName
	 * @return
	 */
	public VirtualChest getDefaultChest(String playerName) {
		return defaultChests.get(playerName);
	}
	/**
	 * Set default chest for the player.
	 * @param playerName
	 * @param v
	 * @return
	 */
	public boolean setDefaultChest(String playerName, String vChest)
	{
		VirtualChest v = getChest(playerName, vChest);
		return setDefaultChest(playerName, v);
	}
	public boolean setDefaultChest(String playerName, VirtualChest v) {
		if (chests.get(playerName).containsValue(v))
		{
			defaultChests.put(playerName, v);
			fMan.createDefaultChest(playerName, v.getName());
			return true;
		}
		return false;
	}

	/**
	 * Add a new chest
	 * 
	 * @param player
	 * @param vChest
	 *            VirtualChest to add
	 * 
	 */
	public void addChest(Player player, VirtualChest vChest) {
		if (chests.containsKey(player.getName()))
			chests.get(player.getName()).put(vChest.getName(), vChest);
		else {
			HashMap<String, VirtualChest> tmp = new HashMap<String, VirtualChest>();
			tmp.put(vChest.getName(), vChest);
			chests.put(player.getName(), tmp);
		}
		if (vChest instanceof VirtualLargeChest)
			fMan.createChestFile(player, vChest.getName(), "large");
		else
			fMan.createChestFile(player, vChest.getName(), "normal");
		if(numberOfChest(player)==1)
			setDefaultChest(player.getName(), vChest);
			
	}

	/**
	 * Get a command represented by a specific class
	 * 
	 * @param clazz
	 * @return
	 */
	public GPCommand getCommand(Class<?> clazz) {
		for (GPCommand command : commands)
			if (command.getClass() == clazz)
				return command;

		return null;
	}

	/**
	 * @return the list of commands
	 */
	public List<GPCommand> getCommands() {
		return commands;
	}

	/**
	 * Save all the chests.
	 */
	public synchronized void save() {
		this.fMan.saveChests(chests, "chests.dat");
		log.info("[VirtualChest] Chests Saved !");
	}

	/**
	 * load all the chest
	 */
	public synchronized void load() {
		HashMap<String, HashMap<String, VirtualChest>> loaded = this.fMan.loadChests("chests.dat");
		if (loaded != null) {
			chests = loaded;
			TreeMap<String, String> tmp = fMan.getAllPlayerDefaultChest();
			if (tmp != null)
				for (String player : tmp.keySet())
					defaultChests.put(player, getChest(player, tmp.get(player)));
		}
	}

	/**
	 * Transfer from an old save
	 * 
	 * @deprecated
	 */
	public synchronized void transfer() {
		HashMap<String, HashMap<String, VirtualChest>> loaded = this.fMan.transfer("chest.dat");
		if (loaded != null) {
			chests = loaded;
			TreeMap<String, String> tmp = fMan.getAllPlayerDefaultChest();
			if (tmp != null)
				for (String player : tmp.keySet())
					defaultChests.put(player, getChest(player, tmp.get(player)));
		}
	}

	/**
	 * Check the permissions
	 * 
	 * @param player
	 * @param perm
	 * @return boolean
	 */
	public boolean hasPerm(Player player, String perm) {
		if (permission == null)
			return true;
		else if (permission.has(player, perm))
			return true;
		else {
			player.sendMessage(ChatColor.RED + "You don't have the Permissions to do that " + ChatColor.BLUE
					+ "(" + perm + ")");
			return false;
		}

	}

	/**
	 * Check if the command contain the flag
	 * 
	 * @param args
	 * @param checkFlag
	 * @return
	 */
	public boolean hasFlag(String[] args, String checkFlag) {
		String flag = args[0].toLowerCase();
		return flag.equals(checkFlag) || flag.equals("-" + checkFlag);
	}

	/**
	 * iConomy plugin
	 * 
	 * @return
	 */
	public static iConomy getiConomy() {
		return iConomy;
	}

	/**
	 * Set iConomy Plugin
	 * 
	 * @param plugin
	 * @return
	 */
	public static boolean setiConomy(iConomy plugin) {
		if (iConomy == null) {
			iConomy = plugin;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Permission plugin
	 * 
	 * @return
	 */
	public static PermissionHandler getPermission() {
		return permission;
	}

	/**
	 * Set iConomy Plugin
	 * 
	 * @param plugin
	 * @return
	 */
	public static boolean setPermission(PermissionHandler plugin) {
		if (permission == null) {
			permission = plugin;
		} else {
			return false;
		}
		return true;
	}
}
