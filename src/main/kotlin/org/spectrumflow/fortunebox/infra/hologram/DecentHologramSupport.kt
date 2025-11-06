package org.spectrumflow.fortunebox.infra.hologram

import eu.decentsoftware.holograms.api.DHAPI
import eu.decentsoftware.holograms.api.holograms.Hologram
import org.bukkit.Location
import org.spectrumflow.fortunebox.api.objects.Box

class DecentHologramSupport: HologramSupport() {

    private val holograms = HashMap<String, Hologram>()
    override fun createHologram(
        location: Location,
        box: Box,
        name: String
    ) {
        if (exists(name)) return
        val loc = Location(location.world,location.x + 0.5,location.y + 0.5 + box.hologramHeight,location.z + 0.5)
        val hologram = DHAPI.createHologram(name,loc,box.hologramContent)
        hologram.displayRange = box.hologramRange
        hologram.updateInterval = box.hologramUpdateInterval
        this.holograms[name] = hologram
    }

    override fun removeHologram(name: String) {
        DHAPI.removeHologram(name)
        this.holograms.remove(name)
    }

    override fun removeAll() {
        this.holograms.forEach { (key,value)->
            DHAPI.removeHologram(key)
            value.delete()
        }
        this.holograms.clear()
    }

    override fun exists(name: String): Boolean {
        return DHAPI.getHologram(name) != null
    }
}