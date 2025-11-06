package org.spectrumflow.fortunebox.box

import org.bukkit.Location
import org.bukkit.entity.Player
import org.spectrumflow.fortunebox.api.BoxBuilder
import org.spectrumflow.fortunebox.api.objects.Box

class RouletteBox(
    val player: Player,
    val box: Box,
    val location: Location
): BoxBuilder() {
    override fun open() {
        TODO()
    }
}