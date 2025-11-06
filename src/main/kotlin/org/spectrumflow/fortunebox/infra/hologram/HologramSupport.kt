package org.spectrumflow.fortunebox.infra.hologram

import org.bukkit.Location
import org.spectrumflow.fortunebox.api.objects.Box

abstract class HologramSupport {
    abstract fun createHologram(location: Location,box: Box,name: String)
    abstract fun removeHologram(name: String)
    abstract fun removeAll()
    abstract fun exists(name: String): Boolean
}