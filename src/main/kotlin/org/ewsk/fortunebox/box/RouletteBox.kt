package org.ewsk.fortunebox.box

import org.bukkit.Location
import org.bukkit.entity.Player
import org.ewsk.fortunebox.api.BoxBuilder
import org.ewsk.fortunebox.api.objects.Box

class RouletteBox(
    val player: Player,
    val box: Box,
    val location: Location
): BoxBuilder() {
    override fun open() {
        TODO()
    }
}