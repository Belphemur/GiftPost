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
import com.aranai.virtualchest.VirtualChest;
import com.nijiko.permissions.PermissionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * 
 * @author Balor
 */
public class GiftPostWorker {

	private HashMap<String, VirtualChest> chests;
	private PermissionHandler Perm;
	private List<GPCommand> commands;

	public GiftPostWorker(PermissionHandler Perm) {
		chests = new HashMap<String, VirtualChest>();
		this.Perm = Perm;
		commands = new ArrayList<GPCommand>();
	}

	public VirtualChest getChest(String name) {
		if (chests.containsKey(name))
			return chests.get(name);
		else {
			VirtualChest tmp = new VirtualChest(name);
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
	 * Check the permissions
	 * 
	 * @param player
	 * @param perm
	 * @return boolean
	 */
	public boolean hasPerm(Player player, String perm) {
		if (Perm == null)
			return true;
		else if (Perm.has(player, perm))
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
}
