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

//GiftPost
import com.Balor.commands.GPCommand;
import com.Balor.utils.FilesManager;
import com.aranai.virtualchest.VirtualChest;
import com.aranai.virtualchest.VirtualLargeChest;
//Plugins
import com.gmail.nossr50.mcMMO;
import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.permissions.PermissionHandler;
//Java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
//Bukkit

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

/**
 * 
 * @author Balor
 */
public class GiftPostWorker {

	private HashMap<String, HashMap<String, VirtualChest>> chests = new HashMap<String, HashMap<String, VirtualChest>>();
	private HashMap<String, VirtualChest> defaultChests = new HashMap<String, VirtualChest>();
	private HashMap<String, VirtualChest> sendReceiveChests = new HashMap<String, VirtualChest>();;
	private static PermissionHandler permission = null;
	private List<GPCommand> commands = new ArrayList<GPCommand>();
	private Configuration config;
	private FilesManager fManager;
	public static final Logger log = Logger.getLogger("Minecraft");
	private static iConomy iConomy = null;
	private static mcMMO mcMMO = null;
	private HashMap<String, VirtualChest> parties = new HashMap<String, VirtualChest>();
	private HashMap<String, HashMap<String, Boolean>> permissions = new HashMap<String, HashMap<String, Boolean>>();
	private static GiftPostWorker instance;

	private GiftPostWorker() {
	}

	public static GiftPostWorker getInstance() {
		if (instance == null)
			instance = new GiftPostWorker();
		return instance;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public void setfManager(String path) {
		this.fManager = new FilesManager(path);
	}

	public FilesManager getFileManager() {
		return fManager;
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
	 * check if the given chest already exists.
	 * 
	 * @param playerName
	 * @param chestName
	 * @return
	 */
	public boolean chestExists(Player player, String chestName) {
		return chests.containsKey(player.getName())
				&& chests.get(player.getName()).containsKey(chestName);
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
	 * Return all the chest of the selected player
	 * 
	 * @param p
	 * @return
	 */
	public HashMap<String, VirtualChest> listOfChest(Player p) {
		return chests.get(p.getName());
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
	 * Return the send chest.
	 * 
	 * @param playerName
	 * @return
	 */
	public VirtualChest getSendChest(String playerName) {
		if (sendReceiveChests.containsKey(playerName))
			return sendReceiveChests.get(playerName);
		else
			return getDefaultChest(playerName);
	}

	/**
	 * Set default chest for the player.
	 * 
	 * @param playerName
	 * @param v
	 * @return
	 */
	public boolean setDefaultChest(String playerName, String vChest) {
		VirtualChest v = getChest(playerName, vChest);
		return setDefaultChest(playerName, v);
	}

	public boolean setDefaultChest(String playerName, VirtualChest v) {
		if (chests.get(playerName).containsValue(v)) {
			defaultChests.put(playerName, v);
			fManager.createDefaultChest(playerName, v.getName());
			return true;
		}
		return false;
	}

	/**
	 * Set send chest for the player.
	 * 
	 * @param playerName
	 * @param v
	 * @return
	 */
	public boolean setSendChest(String playerName, String vChest) {
		VirtualChest v = getChest(playerName, vChest);
		if (v != null)
			return setSendChest(playerName, v);
		return false;
	}

	public boolean setSendChest(String playerName, VirtualChest v) {
		if (chests.containsKey(playerName) && chests.get(playerName).containsValue(v)) {
			sendReceiveChests.put(playerName, v);
			fManager.createSendReceiveChest(playerName, v.getName());
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
			fManager.createChestFile(player, vChest.getName(), "large");
		else
			fManager.createChestFile(player, vChest.getName(), "normal");
		if (numberOfChest(player) == 1)
			setDefaultChest(player.getName(), vChest);

	}

	public boolean removeChest(Player player, VirtualChest vChest) {
		String pName = player.getName();
		if (chests.containsKey(pName)) {
			HashMap<String, VirtualChest> playerChests = chests.get(pName);
			if (playerChests.remove(vChest.getName()) != null) {
				fManager.deleteChestFile(pName, vChest.getName());
				String newDefaultChest = fManager.openChestTypeFile(pName).names.get(0);
				if (defaultChests.containsValue(vChest)) {
					defaultChests.put(pName, getChest(player.getName(), newDefaultChest));
					fManager.createDefaultChest(pName, newDefaultChest);
				}
				if (sendReceiveChests.containsValue(vChest)) {
					sendReceiveChests.put(pName, defaultChests.get(pName));
					fManager.createSendReceiveChest(pName, newDefaultChest);
				}
				vChest = null;
				chests.remove(pName);
				chests.put(pName, playerChests);
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Rename a chest
	 * 
	 * @param player
	 * @param oldName
	 * @param newName
	 */
	public void renameChest(Player player, String oldName, String newName) {
		String playerName = player.getName();
		VirtualChest v = getChest(playerName, oldName);
		if (v != null) {
			v.setName(newName);
			chests.get(playerName).remove(oldName);
			chests.get(playerName).put(newName, v);
			fManager.renameChestFile(playerName, oldName, newName);
			if (defaultChests.containsValue(v))
				fManager.createDefaultChest(playerName, newName);
			if (sendReceiveChests.containsValue(v))
				fManager.createSendReceiveChest(playerName, newName);
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
		this.fManager.saveChests(chests, "chests.dat");
		log.info("[VirtualChest] Chests Saved !");
	}

	/**
	 * Save the parties
	 */
	public synchronized void saveParties() {
		this.fManager.saveParties(parties, "parties.dat");
		log.info("[VirtualChest] Parties Saved !");
	}

	/**
	 * load all the chests
	 */
	public synchronized void load() {
		this.config.load();
		this.fManager.loadChests("chests.dat", chests);
		TreeMap<String, String> tmp = fManager.getAllPlayerDefaultChest();
		TreeMap<String, String> tmp2 = fManager.getAllPlayerSendChest();
		String chestName;
		if (tmp != null)
			for (String player : tmp.keySet()) {
				defaultChests.put(player, getChest(player, tmp.get(player)));
				if ((chestName = tmp2.get(player)) == null)
					sendReceiveChests.put(player, defaultChests.get(player));
				else
					sendReceiveChests.put(player, getChest(player, chestName));
			}

	}

	/**
	 * load parties.
	 */
	public synchronized void loadParties() {
		this.config.load();
		HashMap<String, VirtualChest> loaded = this.fManager.loadParties("parties.dat");
		if (loaded != null) {
			parties.clear();
			parties.putAll(loaded);
		}
	}

	/**
	 * Transfer from an old save
	 * 
	 * @deprecated
	 */
	public synchronized void transfer() {
		HashMap<String, HashMap<String, VirtualChest>> loaded = this.fManager.transfer("chest.dat");
		if (loaded != null) {
			chests = loaded;
			TreeMap<String, String> tmp = fManager.getAllPlayerDefaultChest();
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
		return hasPerm(player, perm, true);
	}

	/**
	 * Check the permission with the possibility to disable the error msg
	 * 
	 * @param player
	 * @param perm
	 * @param errorMsg
	 * @return
	 */
	public boolean hasPerm(Player player, String perm, boolean errorMsg) {
		if (permission == null)
			return true;
		String playerName = player.getName();
		if (permissions.containsKey(playerName)) {
			if (permissions.get(playerName).containsKey(perm))
				return permissions.get(playerName).get(perm);

			if (permission.has(player, perm)) {
				permissions.get(playerName).put(perm, true);
				return true;
			} else {
				permissions.get(playerName).put(perm, false);
				if (errorMsg)
					player.sendMessage(ChatColor.RED + "You don't have the Permissions to do that "
							+ ChatColor.BLUE + "(" + perm + ")");
			}
		} else {
			permissions.put(playerName, new HashMap<String, Boolean>());
			if (permission.has(player, perm)) {
				permissions.get(playerName).put(perm, true);
				return true;
			} else {
				permissions.get(playerName).put(perm, false);
				if (errorMsg)
					player.sendMessage(ChatColor.RED + "You don't have the Permissions to do that "
							+ ChatColor.BLUE + "(" + perm + ")");
			}

		}

		return false;

	}

	/**
	 * Check if the command contain the flag
	 * 
	 * @param args
	 * @param checkFlag
	 * @return
	 */
	public boolean hasFlag(String[] args, String checkFlag) {
		if (args.length >= 1) {
			String flag = args[0].toLowerCase();
			return flag.equals(checkFlag) || flag.equals("-" + checkFlag);
		}
		return false;
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

	/**
	 * mcMMO plugin
	 * 
	 * @return
	 */
	public static mcMMO getmcMMO() {
		return mcMMO;
	}

	/**
	 * Set mcMMO Plugin
	 * 
	 * @param plugin
	 * @return
	 */
	public static boolean setmcMMO(mcMMO plugin) {
		if (mcMMO == null) {
			mcMMO = plugin;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Return all the parties (mcMMO) that have a virtual chest.
	 * 
	 * @return
	 */
	public HashMap<String, VirtualChest> getParties() {
		return parties;
	}

	/**
	 * 
	 * @return a keyset with all the name of the player owning a chest.
	 */
	public Set<String> getAllOwner() {
		return chests.keySet();
	}
}
