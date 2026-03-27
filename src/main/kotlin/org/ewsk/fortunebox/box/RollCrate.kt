package org.ewsk.fortunebox.box

import org.bukkit.Location
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.ewsk.fortunebox.FortuneBox.boxManager
import org.ewsk.fortunebox.api.BoxBuilder
import org.ewsk.fortunebox.api.objects.Box
import org.ewsk.fortunebox.api.objects.Prize
import org.ewsk.fortunebox.network.NMSChestAnimation
import taboolib.common.platform.function.submit
import taboolib.module.nms.nmsProxy
import taboolib.platform.util.giveItem

class RollCrate(
    val player: Player,
    val box: Box,
    val location: Location,
    val key: String?
): BoxBuilder() {

    private val world = location.world
    private var display: Item? = null
    private val chestAnimation = nmsProxy<NMSChestAnimation>()
    override fun open() {
        chestAnimation.sendAnimation(location.block,true)
        val time = 8
        roll(time)
    }
    private fun roll(time: Int){
        submit(delay = 10L){
            val prize = generate()
            val displayItem = prize?.getDisplayItem()
            if (display == null && displayItem != null){
                display = world?.dropItem(location.clone().add(0.5,1.1,0.5),displayItem)
                display?.pickupDelay = Int.MAX_VALUE
                display?.owner = null
                display?.setGravity(false)
                display?.velocity = Vector(0,0,0)
                display?.isInvulnerable = true
                display?.isUnlimitedLifetime = true
            }else if (displayItem != null){
                display?.itemStack = displayItem
            }
            if (time <= 0){
                box.playSound("Stop",location)
                prize?.let { it.getPrizeItems()?.let { itemStacks -> player.giveItem(itemStacks) } }
                prize?.let { box.executeCompleted(player,it) }
                boxManager.removeOpeningList(player)

                submit(delay = 10L) {
                    display?.remove()
                    display = null
                    if (prize != null){
                        key?.let { boxManager.updateHologram(it,buildLines(prize.getPrizeName(),false)) }
                    }
                    chestAnimation.sendAnimation(location.block,false)
                }
            }else{
                if (prize != null){
                    key?.let { boxManager.updateHologram(it,buildLines(prize.getPrizeName(),true)) }
                }
                box.playSound("Cycle",location)
                roll(time-1)
            }
        }
    }

    private fun generate(): Prize?{
        val prize = box.randomPrize()
        val displayItem = prize?.getDisplayItem()
        if (displayItem != null){
            display?.itemStack = displayItem
        }
        return prize
    }
    private fun buildLines(prizeName: String,replace: Boolean): List<String>{
        return if (replace) box.hologramContent + box.animation.replace("{item}",prizeName) else box.hologramContent
    }
}