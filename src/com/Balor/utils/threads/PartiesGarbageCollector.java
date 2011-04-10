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

import java.util.TreeMap;

import com.Balor.bukkit.GiftPost.GiftPostWorker;
import com.aranai.virtualchest.VirtualChest;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class PartiesGarbageCollector extends Thread {
	private boolean stop = false;
	private int wait = 5 * 1000 * 60;
	private GiftPostWorker gpw;

	public PartiesGarbageCollector(GiftPostWorker worker) {
		gpw = worker;
	}

	private void garbageCollector() {
		if (!gpw.getParties().isEmpty()) {
			TreeMap<String, VirtualChest> tmp = new TreeMap<String, VirtualChest>();
			for (String party : GiftPostWorker.getmcMMO().getParties()) {
				if (!tmp.containsKey(party)) {
					tmp.put(party, gpw.getParties().get(party));
				}
			}
			gpw.getParties().clear();
			if (!tmp.isEmpty())
				gpw.getParties().putAll(tmp);
		}
	}

	public void run() {
		boolean fin = false;
		while (!fin) {
			try {
				garbageCollector();
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
		this.stop = true;
	}
}
