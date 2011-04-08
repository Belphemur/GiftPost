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
import com.nijiko.coelho.iConomy.iConomy;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author Balor
 */
public class Send implements GPCommand {

	@Override
	public void execute(GiftPostWorker gpw, CommandSender sender, String[] args) {
		String targetName = args[1];
		Player target = sender.getServer().getPlayer(targetName);
		Player player = (Player) sender;
		if (gpw.getChest(player.getName()) == null)
			sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper"
					+ ChatColor.WHITE + "] " + ChatColor.RED
					+ "You don't have a chest. To buy one type "
					+ ChatColor.GOLD + "/gp buy (large|normal)");
		else if (gpw.getChest(targetName) == null)
			sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper"
					+ ChatColor.WHITE + "] " + ChatColor.RED + targetName
					+ " don't have a chest.");
		else if (player.getName().equals(targetName))
			sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper"
					+ ChatColor.WHITE + "] " + ChatColor.RED
					+ "You can't send a gift to yourself !");
		else if (gpw.getChest(player.getName()).isEmpty())
			sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper"
					+ ChatColor.WHITE + "] " + ChatColor.DARK_GRAY
					+ "Your chest is empty, nothing to send");
		else if (gpw.getChest(targetName).isFull())
			sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper"
					+ ChatColor.WHITE + "] " + ChatColor.RED + "The chest of "
					+ ChatColor.BLUE + targetName + ChatColor.RED
					+ " is full !");
		else if (gpw.getChest(targetName).leftCases() < gpw.getChest(
				player.getName()).usedCases())
			sender.sendMessage("[" + ChatColor.GOLD + "Chest Keeper"
					+ ChatColor.WHITE + "] " + ChatColor.RED
					+ "There isn't enough place in the " + ChatColor.BLUE
					+ targetName + ChatColor.RED + "'s chest !");
		else {
			if (target != null) {
				if (checkMaxRange(gpw, player, target)
						&& inSameWorld(gpw, player, target)) {
					if (iConomyCheck(gpw, player)) {
						gpw.getChest(targetName).addItemStack(
								gpw.getChest(player.getName()).getContents());
						gpw.getChest(player.getName()).emptyChest();
						target.sendMessage("["
								+ ChatColor.GOLD
								+ "Chest Keeper"
								+ ChatColor.WHITE
								+ "] "
								+ ChatColor.GREEN
								+ player.getName()
								+ ChatColor.GRAY
								+ " send you a gift, look in your chest (using command "
								+ ChatColor.GOLD + "/gp c" + ChatColor.GRAY
								+ ").");
						sender.sendMessage("[" + ChatColor.GOLD
								+ "Chest Keeper" + ChatColor.WHITE + "] "
								+ ChatColor.BLUE
								+ "Succefuly send your gift to "
								+ ChatColor.GREEN + targetName);
					}

				} else
					sender.sendMessage("["
							+ ChatColor.GOLD
							+ "Chest Keeper"
							+ ChatColor.WHITE
							+ "] "
							+ ChatColor.GRAY
							+ targetName
							+ ChatColor.RED
							+ " is to far away from you to send him your gift !");
			} else {
				if (gpw.getConfig().getString("allow-offline", "false")
						.matches("true")) {
					if (inSameWorld(gpw, player.getWorld().getName(), gpw
							.getFileMan().openWorldFile(targetName))) {
						if (iConomyCheck(gpw, player)) {
							sender.sendMessage("["
									+ ChatColor.GOLD
									+ "Chest Keeper"
									+ ChatColor.WHITE
									+ "] "
									+ ChatColor.BLUE
									+ "Succefuly send your gift to "
									+ ChatColor.GREEN
									+ targetName
									+ ChatColor.RED
									+ " but he's offline, he'll receve it when he'll connect.");
							gpw.getChest(targetName).addItemStack(
									gpw.getChest(player.getName())
											.getContents());
							gpw.getFileMan().createOfflineFile(
									targetName,
									gpw.getChest(player.getName())
											.getContents(), player.getName());
							gpw.getChest(player.getName()).emptyChest();
						}
					} else
						sender.sendMessage("["
								+ ChatColor.GOLD
								+ "Chest Keeper"
								+ ChatColor.WHITE
								+ "] "
								+ targetName
								+ ChatColor.RED
								+ " is offline, and he was in an another world when he quit.");
				} else
					sender.sendMessage(targetName + ChatColor.RED
							+ " is offline, you can't send him your gift.");
			}

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
						.getConfig().getDouble("iConomy-send-price", 1.0)) {
					player.sendMessage("[" + ChatColor.GOLD + "Chest Keeper"
							+ ChatColor.WHITE + "] " + ChatColor.RED
							+ "You don't have enough "
							+ iConomy.getBank().getCurrency()
							+ " to pay the post !");
					return false;
				} else {
					iConomy.getBank()
							.getAccount(player.getName())
							.subtract(
									gpw.getConfig().getDouble(
											"iConomy-send-price", 1.0));
					player.sendMessage("["
							+ ChatColor.GOLD
							+ "Chest Keeper"
							+ ChatColor.WHITE
							+ "] "
							+ gpw.getConfig().getDouble("iConomy-send-price",
									1.0) + " "
							+ iConomy.getBank().getCurrency()
							+ ChatColor.DARK_GRAY + " used to pay the post.");
					return true;
				}

			} else {
				player.sendMessage("[" + ChatColor.GOLD + "Chest Keeper"
						+ ChatColor.WHITE + "] " + ChatColor.RED
						+ "You must have an account to pay the post !");
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if the player are in the same world.
	 * 
	 * @param gpw
	 * @param player
	 * @param target
	 * @return
	 */
	private boolean inSameWorld(GiftPostWorker gpw, Player player, Player target) {
		if (gpw.getConfig().getString("world-check", "false ").matches("true")) {
			return player.getWorld().getName()
					.equals(target.getWorld().getName());
		}
		return true;
	}

	private boolean inSameWorld(GiftPostWorker gpw, String worldFrom,
			String worldTo) {
		if (gpw.getConfig().getString("world-check", "false ").matches("true")) {
			return worldFrom.equals(worldTo);
		}
		return true;
	}

	/**
	 * Function to check if the player don't exceed the max-range
	 * 
	 * @param gpw
	 * @param player
	 * @param target
	 * @return
	 */
	private boolean checkMaxRange(GiftPostWorker gpw, Player player,
			Player target) {
		if (gpw.getConfig().getString("use-max-range", "false ")
				.matches("true")) {
			int maxRadius = gpw.getConfig().getInt("max-range", 100);
			int totaldistance = 0;

			int x1 = player.getLocation().getBlockX();
			int y1 = player.getLocation().getBlockY();
			int z1 = player.getLocation().getBlockZ();
			int x2 = target.getLocation().getBlockX();
			int y2 = target.getLocation().getBlockY();
			int z2 = target.getLocation().getBlockZ();

			totaldistance = ((x1 - x2) ^ 2 + (y1 - y2) ^ 2 + (z1 - z2) ^ 2);
			if (!(totaldistance < (maxRadius ^ 2))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean validate(GiftPostWorker gpw, CommandSender sender,
			String[] args) {
		return ((gpw.hasFlag(args, "s") || gpw.hasFlag(args, "send")) && args.length == 2)
				&& gpw.hasPerm((Player) sender, getPermName());
	}

	@Override
	public String getPermName() {
		return "giftpost.chest.send";
	}

}
