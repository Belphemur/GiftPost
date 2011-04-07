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
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

/**
 * 
 * @author Balor
 */
public class GiftPostWorker {

	private HashMap<String, VirtualChest> chests;
	private static PermissionHandler permission= null;
	private List<GPCommand> commands;
	private Configuration config;
	private FilesManager fMan;
	public static final Logger log = Logger.getLogger("Minecraft");
	private static iConomy iConomy = null;

	public GiftPostWorker(Configuration config, String dataFolder) {
		chests = new HashMap<String, VirtualChest>();
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
	 * @param name
	 * @return
	 */
	public VirtualChest getChest(String name) {
		if (chests.containsKey(name))
			return chests.get(name);
		else {
			VirtualChest tmp = null;
			if (this.config.getString("chest-type", "normal").matches("normal"))
				tmp = new VirtualChest(name);
			else
				tmp = new VirtualLargeChest(name);
			chests.put(name, tmp);
			return tmp;
		}
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
		this.fMan.saveChests(chests, "chest.dat");
		log.info("[VirtualChest] Chests Saved !");
	}

	public synchronized void load() {
		if (this.config.getString("chest-type", "normal").matches("normal"))
			loadNormal();
		else
			loadLarge();
	}

	/**
	 * Load the normal chests.
	 */
	private void loadNormal() {
		HashMap<String, VirtualChest> loaded = this.fMan
				.loadChests("chest.dat");
		if (loaded != null) {
			chests = loaded;
		}
	}

	/**
	 * Load the large chest.
	 */
	private void loadLarge() {
		HashMap<String, VirtualLargeChest> loaded = this.fMan
				.loadLargeChests("chest.dat");
		if (loaded != null) {
			Set<String> names = loaded.keySet();
			int i = 0;
			for (VirtualLargeChest v : loaded.values()) {
				chests.put((String) names.toArray()[i], v);
				i++;
			}
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
			player.sendMessage(ChatColor.RED
					+ "You don't have the Permissions to do that "
					+ ChatColor.BLUE + "(" + perm + ")");
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
