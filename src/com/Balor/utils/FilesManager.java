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
package com.Balor.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

import net.minecraft.server.ItemStack;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class FilesManager {
	protected String path;

	/**
	 * Constructor
	 * 
	 * @param path
	 */
	public FilesManager(String path) {
		this.path = path;
		if (!new File(this.path).exists()) {
			new File(this.path).mkdir();
		}
	}

	private Configuration getFile(String directory, String fileName) {

		if (!new File(this.path + File.separator + directory).exists()) {
			new File(this.path + File.separator + directory).mkdir();
		}
		File file = new File(path + File.separator + directory + File.separator
				+ fileName);

		if (!file.exists()) {

			try {
				file.createNewFile();
			} catch (IOException ex) {
				System.out.println("cannot create file " + file.getPath());
			}
		}
		Configuration config = new Configuration(file);
		config.load();
		return config;

	}

	/**
	 * Create the offline file for the player
	 * 
	 * @param to
	 * @param items
	 * @param from
	 */
	public void createPlayerFile(String to, ItemStack[] items, String from) {
		Configuration conf = this.getFile("Players", to + ".yml");
		List<String> itemsNames = new ArrayList<String>();
		List<Integer> itemsAmount = new ArrayList<Integer>();
		List<String> playerNames = new ArrayList<String>();
		for (ItemStack is : items)
			if (is != null) {
				itemsNames.add(Material.getMaterial(is.id).toString()
						.toLowerCase().replace('_', ' '));
				itemsAmount.add(is.count);
			}

		if (conf.getProperty("From." + from) == null) {
			playerNames.add(from);
			if (conf.getProperty("Players") == null)
				conf.setProperty("Players", playerNames);
			else {
				playerNames.addAll(conf.getStringList("Players", null));
				conf.setProperty("Players", playerNames);
			}
			conf.setProperty("From." + from, from);
			conf.setProperty("From." + from + ".Items", itemsNames);
			conf.setProperty("From." + from + ".Amount", itemsAmount);

		} else {
			List<String> list = new ArrayList<String>();
			List<Integer> list2 = new ArrayList<Integer>();
			list = conf.getStringList("From." + from + ".Items", list);
			list2 = conf.getIntList("From." + from + ".Amount", list2);
			itemsNames.addAll(list);
			itemsAmount.addAll(list2);
			conf.setProperty("From." + from + ".Items", itemsNames);
			conf.setProperty("From." + from + ".Amount", itemsAmount);
		}
		conf.save();

	}
	public void openPlayerFile(Player p)
	{
		Configuration conf = this.getFile("Players", p.getName() + ".yml");
	}
}
