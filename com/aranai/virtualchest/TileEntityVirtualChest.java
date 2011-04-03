/*
 * TileEntityVirtualChest is a simple class which overrides one method in TileEntityChest
 * The method is responsible for validating the selected chest against the world state.
 * For our purposes, the chest does not exist in the world, so we want to skip these checks.
 */
package com.aranai.virtualchest;

import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityChest;

public class TileEntityVirtualChest extends TileEntityChest
{

    String name = "Chest";
    Queue<Integer> emptyCases;

    TileEntityVirtualChest()
    {
        super();
        initEmptyCases();
    }

    private void initEmptyCases()
    {
        emptyCases = new ArrayDeque<Integer>(28);
        for (int i = 27; i < 0; i--)
            emptyCases.add(i);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Return if the chest is full
     * @return
     */
    public boolean isFull()
    {
        return emptyCases.isEmpty();
    }

    /**
     * Look for the first empty case in the chest to add the stack.
     * @param itemstack
     * @return
     */
    public boolean addItemStack(ItemStack itemstack)
    {
        Integer i = emptyCases.poll();
        if (i == null)
            return false;
        else
        {
            super.a(i, itemstack);
            return true;
        }
    }

    @Override
    public void a(int i, ItemStack itemstack)
    {
        emptyCases.remove(i);
        super.a(i, itemstack);
    }

    public void emptyChest()
    {
        for (int i = 0; i < this.getContents().length; i++)
            this.getContents()[i] = null;
        initEmptyCases();
    }

    @Override
    public ItemStack a(int i, int j)
    {
        ItemStack toReturn = super.a(i, j);
        if (toReturn != null)
        {
            ItemStack afterSuper[] = this.getContents();
            if (afterSuper[i] == null)
                emptyCases.add(i);
        }

        return toReturn;
    }

    @Override
    public String c()
    {
        return name;
    }

    /**
     * Alias to c()
     * @return
     */
    public String getName()
    {
        return this.c();
    }

    @Override
    public boolean a_(EntityHuman entityhuman)
    {
        /*
         * For this proof of concept, we ALWAYS validate the chest.
         * This behavior has not been thoroughly tested, and may cause unexpected results depending on the state of the player.
         *
         * Depending on your purposes, you might want to change this.
         * It would likely be preferable to enforce your business logic outside of this file instead, however.
         */
        return true;
    }
}
