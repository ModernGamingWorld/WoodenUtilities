package com.lazy.woodenutilities.inventory.containers;

import com.lazy.woodenutilities.content.ModContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;

public class WoodenFurnaceContainer extends AbstractFurnaceContainer {

    public WoodenFurnaceContainer(int id, PlayerInventory playerInventory) {
        super(ModContainers.WOODEN_FURNACE_CONTAINER, IRecipeType.SMELTING, id, playerInventory);
    }

    public WoodenFurnaceContainer(int id, PlayerInventory playerInventory, IInventory furnaceInv, IIntArray data) {
        super(ModContainers.WOODEN_FURNACE_CONTAINER, IRecipeType.SMELTING, id, playerInventory, furnaceInv, data);
    }
}
