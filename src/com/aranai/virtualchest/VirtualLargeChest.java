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

    protected TileEntityVirtualChest subChest2;
    protected InventoryLargeChest lc;

    public VirtualLargeChest(Player p)
    {
        super(p);
        subChest2 = new TileEntityVirtualChest();
        subChest2.setName(p.getName());
        lc = new InventoryLargeChest(p.getName(), chest, subChest2);
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
        if (!super.addItemStack(is))
            return subChest2.addItemStack(is);
        return true;
    }
    /**
     * is Chest Full
     * @return
     */
    @Override
    public boolean isFull()
    {
    	return chest.isFull() && subChest2.isFull();
    }
    /**
     * Empty chests
     */
    @Override
    public void emptyChest()
    {
        super.emptyChest();
        subChest2.emptyChest();
    }

    /**
     * get all the itemStacks that compose the chest
     * @return
     */
    @Override
    public ItemStack[] getContents()
    {
        return lc.getContents();
    }

    @Override
    public boolean removeItemStack(ItemStack is)
    {
        if (!super.removeItemStack(is))
        {
            for (int i = 0; i < subChest2.getContents().length; i++)
                if (this.getContents()[i].equals(is))
                {
                    subChest2.removeItemStack(i);
                    return true;
                }
            return false;
        }
        return true;
    }

    @Override
    public void removeItemStack(int i)
    {
        if (i > chest.q_())
            subChest2.removeItemStack(i - chest.q_());
        else
            super.removeItemStack(i);
    }
}
