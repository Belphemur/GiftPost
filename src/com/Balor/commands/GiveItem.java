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

import static com.Balor.utils.Display.chestKeeper;

import net.minecraft.server.ItemStack;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.aranai.virtualchest.VirtualChest;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class GiveItem implements GPCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.Balor.commands.GPCommand#execute(com.Balor.bukkit.GiftPost.GiftPostWorker
	 * , org.bukkit.command.CommandSender, java.lang.String[])
	 */
	public void execute(GiftPostWorker gpw, CommandSender sender, String[] args) {
		if (args.length < 3) {
			sender.sendMessage(getHelp());
			return;
		}
		Player p = (Player) sender;
		VirtualChest v;
		if ((v = gpw.getSendChest(p.getName())) != null) {
			Material m = checkMaterial(args[1], p);
			if (m == null)
				return;
			int nb;
			try {
				nb = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				p.sendMessage(chestKeeper() + ChatColor.RED + args[2] + " is not a number.");
				return;
			}
			v.addItemStack(new ItemStack(m.getId(), nb, 0));
			p.sendMessage(chestKeeper() + ChatColor.WHITE + "Successfuly added " + ChatColor.GOLD
					+ nb + " " + m.name() + ChatColor.WHITE + " to your send chest ("
					+ ChatColor.GREEN + v.getName() + ChatColor.WHITE + ")");
		} else
			p.sendMessage(chestKeeper() + ChatColor.RED
					+ "You don't have a chest. To buy one type " + ChatColor.GOLD
					+ "/gp buy (large|normal) nameOfTheChest");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#validate(com.Balor.bukkit.GiftPost.
	 * GiftPostWorker, org.bukkit.command.CommandSender, java.lang.String[])
	 */
	public boolean validate(GiftPostWorker gpw, CommandSender sender, String[] args) {
		return ((gpw.hasFlag(args, "i") || gpw.hasFlag(args, "item")))
				&& gpw.hasPerm((Player) sender, getPermName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#getPermName()
	 */
	public String getPermName() {
		return "giftpost.admin.item";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.Balor.commands.GPCommand#getHelp()
	 */
	public String getHelp() {
		return ChatColor.GOLD + "/gp i (id|name) numberOfItems" + ChatColor.WHITE
				+ ": add the item to your send chest\n";
	}

	/**
	 * Translate the id or name to a material
	 * 
	 * @param mat
	 * @return Material
	 */
	private Material checkMaterial(String mat, Player player) {
		Material m = null;
		try {
			int id = Integer.parseInt(mat);
			m = Material.getMaterial(id);
		} catch (NumberFormatException e) {
			m = Material.matchMaterial(mat);
		}
		if (m == null)
			player.sendMessage(chestKeeper() + ChatColor.RED + "Unknown material: "
					+ ChatColor.WHITE + mat);
		return m;

	}

}
