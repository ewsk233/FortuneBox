package org.ewsk.fortunebox.network

import org.bukkit.block.Block
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.nmsProxy
import kotlin.getValue

abstract class NMSChestAnimation{
    abstract fun sendAnimation(block: Block, open: Boolean, players: Collection<Player>? = null)
    companion object {
        val instance by unsafeLazy { nmsProxy<NMSChestAnimation>() }
    }
}