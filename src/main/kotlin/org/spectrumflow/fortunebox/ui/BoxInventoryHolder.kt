package org.spectrumflow.fortunebox.ui

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class BoxInventoryHolder(val location: Location): InventoryHolder {
    private var inv: Inventory? = null

    override fun getInventory(): Inventory {
        if (inv == null) {
            inv = Bukkit.createInventory(this, 9*3, "FortuneBox")
        }
        return inv!!
    }
}