package org.spectrumflow.fortunebox.box

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.spectrumflow.fortunebox.FortuneBox.boxManager
import org.spectrumflow.fortunebox.FortuneBox.settings
import org.spectrumflow.fortunebox.api.BoxBuilder
import org.spectrumflow.fortunebox.api.objects.Box
import org.spectrumflow.fortunebox.api.objects.Prize
import org.spectrumflow.fortunebox.ui.BoxInventoryHolder
import taboolib.common.platform.function.submit
import taboolib.platform.util.buildItem
import taboolib.platform.util.giveItem

/**
 * @author ewsk
 * @date 2025/11/06
 */
class CSGOBox(
    val player: Player,
    val box: Box,
    val location: Location
): BoxBuilder() {

    val glasses = listOf(buildItem(Material.WHITE_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.ORANGE_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.MAGENTA_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.YELLOW_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.LIME_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.PINK_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.GRAY_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.CYAN_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.PURPLE_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.BLUE_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.BROWN_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.GREEN_STAINED_GLASS_PANE){name=settings.glassName},
        buildItem(Material.RED_STAINED_GLASS_PANE){name=settings.glassName})
    val slow = getSlow(120,15)

    val prizes = ArrayList<Prize>()

    private fun getSlow(full: Int,cut: Int): List<Int>{
        val list = ArrayList<Int>()
        var tempFull = full
        var tempCut = cut
        var index = full
        while (tempCut > 0) {
            if (tempFull <= index - tempCut || tempFull >= index - tempCut) {
                list.add(index)
                index -= tempCut
                tempCut--
            }
            tempFull--
        }
        return list
    }

    override fun open() {
        val inv = buildGui()
        this.player.openInventory(inv)
        var time = 0
        var full = 0
        var open = 0
        var animation = 0
        submit(delay = 0L, period = 1L){
            if (full <= 50){
                animate(inv,animation)
                box.playSound("Cycle",location)
                animation++
            }
            open++
            if (open >= 5){
                player.openInventory(inv)
                open = 0
            }
            if (full > 50){
                time++
                if (slow.contains(time)){
                    animate(inv,animation)
                    box.playSound("Cycle",location)
                    animation++
                }
                if (time == 60){
                    val item = buildItem(Material.WHITE_STAINED_GLASS_PANE){name=settings.glassName}
                    inv.setItem(4,item)
                    inv.setItem(22,item)
                    val prize = prizes[prizes.size - 5]
                    val items = prize.getPrizeItems()
                    items?.apply {
                        player.giveItem(this)
                    }
                    boxManager.removeOpeningList(player)
                    box.executeCompleted(player,prize)
                    box.playSound("Stop",location)
                    submit(delay = 40L) {
                        player.closeInventory()
                    }
                    cancel()
                }else if (time > 60){
                    cancel()
                }
            }
            full++
        }
    }
    private fun buildGui(): Inventory{
        val inv = Bukkit.createInventory(BoxInventoryHolder(location),27,this.box.animation)
        for (i in 0..8){
            inv.setItem(i, glasses[i])
            inv.setItem(i+18,glasses[i])
        }
        /*val item = buildItem(Material.WHITE_STAINED_GLASS_PANE){name=settings.glassName}
        inv.setItem(4,item)
        inv.setItem(22,item)*/
        for (i in 9..17){
            val prize = box.randomPrize()
            prize?.apply {
                inv.setItem(i,this.getDisplayItem())
                prizes.add(this)
            }
        }

        return inv
    }
    private fun animate(inventory: Inventory, animation: Int){
        val prize = box.randomPrize()
        prize?.apply {
            prizes.add(this)
        }
        for (i in 9..17){
            inventory.setItem(i,prizes[prizes.size - 18 + i].getDisplayItem())
        }
        for (i in 0..8){
            inventory.setItem(i,glasses[(animation + 1 + i) % 14])
            inventory.setItem(i+18,glasses[(animation + 1 + i) % 14])
        }
    }


}