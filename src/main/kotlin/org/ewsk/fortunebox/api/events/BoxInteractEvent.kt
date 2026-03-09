package org.ewsk.fortunebox.api.events

import org.bukkit.Location
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.ewsk.fortunebox.api.objects.Box
import taboolib.platform.type.BukkitProxyEvent

data class BoxInteractEvent(
    val bukkitEvent: PlayerInteractEvent,
    val action: Action,
    val location: Location,
    val box: Box
): BukkitProxyEvent()
