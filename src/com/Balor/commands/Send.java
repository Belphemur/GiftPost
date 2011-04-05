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
		String targetName=args[1];
		Player target = sender.getServer().getPlayer(targetName);
		Player player = (Player) sender;
		if (gpw.getChest(player.getName()).isEmpty())
			sender.sendMessage(ChatColor.DARK_GRAY
					+ "Your chest is empty, nothing to send");
		else if (gpw.getChest(targetName).isFull())
			sender.sendMessage(ChatColor.RED + "The chest of " + ChatColor.BLUE
					+ targetName + ChatColor.RED + " is full !");
		else if (gpw.getChest(targetName).leftCases() < gpw.getChest(
				player.getName()).usedCases())
			sender.sendMessage(ChatColor.RED
					+ "There isn't enough place in the " + ChatColor.BLUE
					+ targetName + ChatColor.RED + "'s chest !");
		else 
		{
			if(target !=null)
			{
				target.sendMessage(ChatColor.GREEN+player.getName()+ChatColor.BLUE+" send you a gift, look in your chest (using command /gp c).");
				sender.sendMessage(ChatColor.BLUE+"Succefuly send your gift to "+ChatColor.GREEN+targetName);
				gpw.getChest(targetName).addItemStack(gpw.getChest(player.getName()).getContents());
				gpw.getChest(player.getName()).emptyChest();
			}
			else 
				if(gpw.getConfig().getString("allow-offline","false").equals("true"))
				{
					sender.sendMessage(ChatColor.BLUE+"Succefuly send your gift to "+ChatColor.GREEN+targetName+ChatColor.RED+" but he's offline, he'll receve it when he'll connect.");
					gpw.getChest(targetName).addItemStack(gpw.getChest(player.getName()).getContents());
					gpw.getChest(player.getName()).emptyChest();
				}
				else
					sender.sendMessage(targetName+ChatColor.RED+" is offline, you can't send him your gift.");
			
		}

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
