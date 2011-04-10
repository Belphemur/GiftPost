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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.Balor.commands.GPCommand;
import com.aranai.virtualchest.VirtualChest;
import com.aranai.virtualchest.VirtualLargeChest;
import com.gmail.nossr50.mcMMO;
import com.nijiko.coelho.iConomy.iConomy;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class BuyPartyChest implements GPCommand {

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
		String type = args[1].toLowerCase();
		if (GiftPostWorker.getmcMMO() != null) {
			if (mcMMO.inParty(player)) {
				if (!gpw.getParties().containsKey(mcMMO.getPartyName(player))) {
					if (type.matches("normal") || type.matches("large")) {
						if (iConomyCheck(gpw, player, type)) {
							if (type.matches("normal"))
								gpw.getParties().put(mcMMO.getPartyName(player),
										new VirtualChest(mcMMO.getPartyName(player)));
							if (type.matches("large"))
								gpw.getParties().put(mcMMO.getPartyName(player),
										new VirtualLargeChest(mcMMO.getPartyName(player)));
							McParty.getInstance().sendMessage(
									player,
									"[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
											+ player.getName() + " bought a Virtual " + type
											+ " Chest for the party (" + ChatColor.GOLD + "command /pchest"
											+ ChatColor.WHITE + " to open it)");
						}

					} else
						sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
								+ ChatColor.RED + "There is only 2 type of Chests : large and normal");
				} else
					sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
							+ ChatColor.RED + "Your party have already a chest : " + ChatColor.GOLD
							+ "/pchest" + ChatColor.RED + " to see it.");
			} else
				sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
						+ ChatColor.DARK_RED + "You must be in a party to buy a party chest");
		} else
			sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper" + ChatColor.WHITE + "] "
					+ ChatColor.DARK_RED + "You don't have mcMMO installed !");

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
		return (GiftPostWorker.getmcMMO() != null && args.length == 2 && (gpw.hasFlag(args, "party") || gpw
				.hasFlag(args, "p"))) && gpw.hasPerm((Player) sender, getPermName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#getPermName()
	 */
	@Override
	public String getPermName() {
		return "mcmmo.commands.party";
	}

}
