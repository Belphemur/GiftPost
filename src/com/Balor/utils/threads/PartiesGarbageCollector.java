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
package com.Balor.utils.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.aranai.virtualchest.VirtualChest;
import com.aranai.virtualchest.VirtualLargeChest;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class PartiesGarbageCollector extends Thread {
	private boolean stop = false;
	private int wait;
	private GiftPostWorker gpw;

	public PartiesGarbageCollector(GiftPostWorker worker) {
		gpw = worker;
		this.wait = gpw.getConfig().getInt("auto-save-time", 10) * 1000 * 60;
	}

	private void garbageCollectorAndSave() {
		if (!gpw.getParties().isEmpty()) {
			TreeMap<String, VirtualChest> tmp = new TreeMap<String, VirtualChest>();
			List<String> names = new ArrayList<String>();
			List<String> types = new ArrayList<String>();
			for (String party : GiftPostWorker.getmcMMO().getParties()) {
				if (party != null && !tmp.containsKey(party) && gpw.getParties().containsKey(party)) {
					VirtualChest v = gpw.getParties().get(party);
					names.add(party);
					tmp.put(party, v);
					if (v instanceof VirtualLargeChest)
						types.add("large");
					else
						types.add("normal");
				}
			}
			gpw.getParties().clear();
			gpw.getFileMan().createPartyFile(names, types);
			if (!tmp.isEmpty())
				gpw.getParties().putAll(tmp);
			gpw.saveParties();
		}
	}

	public void run() {
		boolean fin = false;
		gpw.loadParties();
		while (!fin) {
			try {
				garbageCollectorAndSave();
				synchronized (this) {
					Thread.yield();
					fin = this.stop;
				}
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void stopIt() {
		garbageCollectorAndSave();
		this.stop = true;
	}
}
