package org.ewsk.fortunebox.event

import org.bukkit.event.block.Action
import org.ewsk.fortunebox.FortuneBox.boxManager
import org.ewsk.fortunebox.api.events.BoxInteractEvent
import taboolib.common.platform.event.SubscribeEvent


@SubscribeEvent
fun onBoxInteract(event: BoxInteractEvent){
    val action = event.action
    val box = event.box
    if (action == Action.LEFT_CLICK_BLOCK){
        if (box.isPreviewEnable()){
            box.preview(event.bukkitEvent.player)
        }
    }else{
        boxManager.openBox(event.bukkitEvent.player,box,event.location)
    }
}