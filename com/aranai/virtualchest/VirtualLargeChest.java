/*This file is part of VirtualLargeChest .

VirtualLargeChest is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

VirtualLargeChest is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with VirtualLargeChest.  If not, see <http://www.gnu.org/licenses/>.*/
package com.aranai.virtualchest;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author Balor (aka Antoine Aflalo)
 */
public class VirtualLargeChest extends VirtualChest
{

    protected TileEntityVirtualChest chest2;
    protected InventoryLargeChest lc;

    public VirtualLargeChest(Player p)
    {
        super(p);
        chest2 = new TileEntityVirtualChest();
        chest2.setName(p.getName());
        lc = new InventoryLargeChest(p.getName(), chest, chest2);
    }

    /**
     * Open the chest for the owner
     */
    @Override
    public void openChest(Player p)
    {
        if (isBelongTo(p))
        {
            EntityPlayer eh = ((CraftPlayer) p).getHandle();
            eh.a(lc);
        } else
            p.sendMessage("You can't open this chest, it's not yours.");
    }
    /**
     * adding a ItemStack to the chest
     * @param is
     * @return
     */
    @Override
    public boolean addItemStack(ItemStack is)
    {
        if(!super.addItemStack(is))
            return chest2.addItemStack(is);
        return true;
    }
    /**
     * Empty chests
     */
    @Override
    public void emptyChest()
    {
        super.emptyChest();
        chest2.emptyChest();
    }
}
