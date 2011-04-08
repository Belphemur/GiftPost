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
package com.Balor.commands;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.aranai.virtualchest.VirtualChest;
import com.nijiko.coelho.iConomy.iConomy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author Balor (aka Antoine Aflalo)
 */
public class Chest implements GPCommand {

	/**
	 * Execute commands
	 * 
	 * @param gp
	 * @param sender
	 * @param args
	 */
	@Override
	public void execute(GiftPostWorker gpw, CommandSender sender, String[] args) {
		Player p = (Player) sender;
		if (iConomyCheck(gpw, p)) {
			VirtualChest v = gpw.getChest(p.getName());
			if (v != null)
				v.openChest((Player) sender);
			else
				p.sendMessage(ChatColor.RED
						+ "You don't have a chest. To buy one type "
						+ ChatColor.GOLD + "/gp buy (large|normal)");
		}
	}

	/**
	 * Check if the plugin iConomy is present and if the player have enough
	 * money. After checked, substract the money.
	 * 
	 * @param gpw
	 * @param player
	 * @return
	 */
	private boolean iConomyCheck(GiftPostWorker gpw, Player player) {
		if (GiftPostWorker.getiConomy() != null
				&& gpw.getConfig().getString("iConomy", "false")
						.matches("true")) {
			if (iConomy.getBank().hasAccount(player.getName())) {
				if (iConomy.getBank().getAccount(player.getName()).getBalance() < gpw
						.getConfig().getDouble("iConomy-openchest-price", 1.0)) {
					player.sendMessage(ChatColor.RED + "You don't have enough "
							+ iConomy.getBank().getCurrency()
							+ " to pay the Chests Keeper !");
					return false;
				} else {
					iConomy.getBank()
							.getAccount(player.getName())
							.subtract(
									gpw.getConfig().getDouble(
											"iConomy-openchest-price", 1.0));
					player.sendMessage(gpw.getConfig().getDouble(
							"iConomy-openchest-price", 1.0)
							+ " "
							+ iConomy.getBank().getCurrency()
							+ ChatColor.DARK_GRAY
							+ " used to pay the Chests Keeper.");
					return true;
				}

			} else {
				player.sendMessage(ChatColor.RED
						+ "You must have an account to pay the post !");
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate a command to check if it should be executed
	 * 
	 * @param lwc
	 * @param command
	 * @param args
	 * @return
	 */
	@Override
	public boolean validate(GiftPostWorker gpw, CommandSender sender,
			String[] args) {
		return (gpw.hasFlag(args, "c") || gpw.hasFlag(args, "chest"))
				&& gpw.hasPerm((Player) sender, getPermName());
	}

	/**
	 * @return the name of the perm to add in the permFile.
	 */
	@Override
	public String getPermName() {
		return "giftpost.chest.open";
	}
}
