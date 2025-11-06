package org.spectrumflow.fortunebox.event

import org.bukkit.block.Block
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPistonExtendEvent
import org.bukkit.event.block.BlockPistonRetractEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.spectrumflow.fortunebox.FortuneBox.boxManager
import org.spectrumflow.fortunebox.api.events.BoxInteractEvent
import org.spectrumflow.fortunebox.ui.BoxInventoryHolder
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.sendLang


@SubscribeEvent
fun onPlayerInteractEvent(event: PlayerInteractEvent){
    if (event.hand == EquipmentSlot.OFF_HAND) return
    val block = event.clickedBlock
    if (block == null || block.isEmpty) return
    val location = block.location
    if (boxManager.hasBox(location)){
        if (event.player.isOp && event.player.isSneaking && event.action == Action.LEFT_CLICK_BLOCK){
            return
        }
        event.isCancelled = true
        BoxInteractEvent(event,event.action,location,boxManager.getBox(location)!!).call()
    }
}
@SubscribeEvent
fun onPlayerClickUI(event: InventoryClickEvent){
    if (event.inventory.holder is BoxInventoryHolder){
        event.isCancelled = true
    }
}
@SubscribeEvent
fun onBlockBreak(event: BlockBreakEvent){
    val location = event.block.location
    if (boxManager.hasBox(location)){
        boxManager.removeBoxLocation(location)
        event.player.sendLang("break-box-success")
    }
}
@SubscribeEvent
fun onPistonExtend(event: BlockPistonExtendEvent){
    for (block: Block in event.blocks){
        val location = block.location
        if (boxManager.hasBox(location)){
            event.isCancelled = true
            return
        }
    }
}
@SubscribeEvent
fun onPistonRetract(event: BlockPistonRetractEvent){
    for (block: Block in event.blocks){
        val location = block.location
        if (boxManager.hasBox(location)){
            event.isCancelled = true
            return
        }
    }
}
