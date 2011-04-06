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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

import com.aranai.virtualchest.SerializedItemStack;
import com.aranai.virtualchest.VirtualChest;

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

	/**
	 * Open the file and return the Configuration object
	 * 
	 * @param directory
	 * @param fileName
	 * @return the configuration file
	 */
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

	/**
	 * Accessor to deleteFile()
	 * 
	 * @param p
	 */
	public void deletePlayerFile(Player p) {
		deleteFile("Players", p.getName() + ".yml");
	}

	/**
	 * Delete the given file
	 * 
	 * @param directory
	 * @param fileName
	 */
	private void deleteFile(String directory, String fileName) {
		File toDel = new File(path + File.separator + directory
				+ File.separator + fileName);
		if (toDel.exists())
			try {
				toDel.delete();
			} catch (SecurityException ex) {
				System.out.println("cannot delete file " + toDel.getPath());
			}
	}

	/**
	 * Open the file and say to the player what was send and delete it after
	 * read.
	 * 
	 * @param p
	 */
	public void openPlayerFile(Player p) {
		Configuration conf = this.getFile("Players", p.getName() + ".yml");
		List<String> playerNames = new ArrayList<String>();
		List<String> itemsNames = new ArrayList<String>();
		List<Integer> itemsAmount = new ArrayList<Integer>();
		playerNames = conf.getStringList("Players", null);
		if (playerNames != null) {
			for (String name : playerNames) {
				itemsNames = conf.getStringList("From." + name + ".Items",
						itemsNames);
				itemsAmount = conf.getIntList("From." + name + ".Amount",
						itemsAmount);
				String msg = ChatColor.GREEN + name + ChatColor.WHITE
						+ " send you : " + ChatColor.GOLD;
				for (int i = 0; i < itemsNames.size(); i++)
					msg += itemsAmount.toArray()[i] + " "
							+ itemsNames.toArray()[i] + ", ";
				p.sendMessage(msg.subSequence(0, msg.length() - 2).toString());
			}
		}
		deleteFile("Players", p.getName() + ".yml");
	}

	/**
	 * Save all the chest.
	 * 
	 * @param chest
	 */
	public void saveChests(HashMap<String, VirtualChest> chest, String fileName) {
		String filename = this.path + File.separator + fileName;
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		ArrayList<SerializedItemStack> itemstacks = new ArrayList<SerializedItemStack>();
		HashMap<String, ArrayList<SerializedItemStack>> saved = new HashMap<String, ArrayList<SerializedItemStack>>();
		for (VirtualChest v : chest.values()) {
			for (ItemStack is : v.getContents()) {
				if (is != null)
					itemstacks.add(new SerializedItemStack(is.id, is.count,
							is.damage));
			}
			saved.put(v.getOwnerName(), new ArrayList<SerializedItemStack>(
					itemstacks));
			itemstacks = new ArrayList<SerializedItemStack>();
		}
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(saved);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Load the saved chest file.
	 * 
	 * @param fileName
	 * @return an HashMap with all the chests
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, VirtualChest> loadChests(String fileName) {
		String filename = this.path + File.separator + fileName;
		HashMap<String, VirtualChest> chests = new HashMap<String, VirtualChest>();
		HashMap<String, ArrayList<SerializedItemStack>> saved = null;
		if (new File(filename).exists()) {
			FileInputStream fis = null;
			ObjectInputStream in = null;

			try {
				fis = new FileInputStream(filename);
				in = new ObjectInputStream(fis);
				saved = (HashMap<String, ArrayList<SerializedItemStack>>) in
						.readObject();
				in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
			if (saved != null) {
				Set<String> names = saved.keySet();
				int i = 0;
				for (ArrayList<SerializedItemStack> al : saved.values()) {
					VirtualChest v = new VirtualChest(
							(String) names.toArray()[i]);
					for (SerializedItemStack sis : al)
						v.addItemStack(new ItemStack(sis.id, sis.count,
								sis.damage));
					chests.put((String)names.toArray()[i],new VirtualChest(v));
					i++;
				}

				return chests;
			}
			return null;
		} else
			return null;
	}
}
