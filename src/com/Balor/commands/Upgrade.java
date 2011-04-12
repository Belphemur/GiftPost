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
package com.Balor.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.aranai.virtualchest.VirtualChest;
import com.aranai.virtualchest.VirtualLargeChest;
import com.nijiko.coelho.iConomy.iConomy;

/**
 * @author Antoine
 * 
 */
public class Upgrade implements GPCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.Balor.commands.GPCommand#execute(com.Balor.bukkit.GiftPost.GiftPostWorker
	 * , org.bukkit.command.CommandSender, java.lang.String[])
	 */
	@Override
	public void execute(GiftPostWorker gpw, CommandSender sender, String[] args) {
		Player player = (Player) sender;
		VirtualChest v;
		if (args.length == 2)
			v = gpw.getChest(player.getName(), args[1].toLowerCase());
		else
			v = gpw.getDefaultChest(player.getName());
		if (v != null) {
			if ((v instanceof VirtualChest) && !(v instanceof VirtualLargeChest)) {
				if (iConomyCheck(gpw, player, "large")) {
					if (gpw.getFileMan().upgradeChest(player, v.getName())) {
						gpw.addChest(player, new VirtualLargeChest(v));

						sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
								+ v.getName() + " is now a Large Chest.");
					} else
						sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
								+ "A problem happend when you tried to upgrade your chest .");
				}
			} else
				sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
						+ ChatColor.RED + "You can't upgrade a Large chest !");
		} else
			sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] " + ChatColor.RED
					+ "You don't have a chest to upgrade.");

	}

	/**
	 * Check if the plugin iConomy is present and if the player have enough
	 * money. After checked, substract the money.
	 * 
	 * @param gpw
	 * @param player
	 * @return
	 */
	private boolean iConomyCheck(GiftPostWorker gpw, Player player, String type) {
		if (GiftPostWorker.getiConomy() != null
				&& gpw.getConfig().getString("iConomy", "false").matches("true")) {
			if (iConomy.getBank().hasAccount(player.getName())) {
				if (iConomy.getBank().getAccount(player.getName()).getBalance() < gpw.getConfig().getDouble(
						"iConomy-" + type + "Chest-price", 10.0)) {
					player.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
							+ ChatColor.RED + "You don't have enough " + iConomy.getBank().getCurrency()
							+ " to pay the Chests Keeper !");
					return false;
				} else {
					iConomy.getBank().getAccount(player.getName())
							.subtract(gpw.getConfig().getDouble("iConomy-" + type + "Chest-price", 10.0));
					player.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
							+ gpw.getConfig().getDouble("iConomy-" + type + "Chest-price", 10.0) + " "
							+ iConomy.getBank().getCurrency() + ChatColor.DARK_GRAY
							+ " used to pay the Chests Keeper.");
					return true;
				}

			} else {
				player.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
						+ ChatColor.RED + "You must have an account to pay the Chests Keeper !");
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#validate(com.Balor.bukkit.GiftPost.
	 * GiftPostWorker, org.bukkit.command.CommandSender, java.lang.String[])
	 */
	@Override
	public boolean validate(GiftPostWorker gpw, CommandSender sender, String[] args) {
		return (args.length >= 1 && (gpw.hasFlag(args, "u") || gpw.hasFlag(args, "upgrade")))
				&& gpw.hasPerm((Player) sender, getPermName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#getPermName()
	 */
	@Override
	public String getPermName() {
		return "giftpost.chest.upgrade";
	}

}
