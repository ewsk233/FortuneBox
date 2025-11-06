package org.spectrumflow.fortunebox.manager

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.spectrumflow.fortunebox.FortuneBox
import org.spectrumflow.fortunebox.api.enums.ItemKey
import taboolib.platform.util.buildItem
import taboolib.platform.util.giveItem
import taboolib.platform.util.hasItem
import taboolib.platform.util.takeItem
import java.util.UUID

/**
 * @author ewsk
 * @date 2025/11/06
 */
class BukkitUserManager: UserManager(){
    val server = Bukkit.getServer()
    val boxManager = FortuneBox.boxManager
    override fun getUser(uuid: UUID): Player? {
        return this.server.getPlayer(uuid)
    }

    override fun addKeys(uuid: UUID, boxName: String, amount: Int): Boolean {
        val player = getUser(uuid)
        if (player == null) return false
        val box = boxManager.getBoxFromName(boxName)
        if (box == null) return false
        val keys = buildItem(box.getKey()){this.amount = amount}
        player.giveItem(keys)
        return true
    }

    override fun takeKeys(uuid: UUID, boxName: String, amount: Int, onlyInHand: Boolean): Boolean {
        val player = getUser(uuid)
        if (player == null) return false
        val inv = player.inventory
        if (onlyInHand){
            val item = inv.itemInMainHand
            val container = item.itemMeta?.persistentDataContainer
            val namespacedKey = ItemKey.BoxKey.getNamespacedKey()
            if (container?.has(namespacedKey,PersistentDataType.STRING) == true &&  container.get(namespacedKey, PersistentDataType.STRING) == boxName){
               if (item.amount >= amount){
                   item.amount -= amount
                   return true
               }else{
                   return false
               }
            }else{
                return false
            }
        }else{
            return inv.takeItem(amount){ item ->
                val container = item.itemMeta?.persistentDataContainer
                val namespacedKey = ItemKey.BoxKey.getNamespacedKey()
                container?.has(namespacedKey,PersistentDataType.STRING) == true &&  container.get(namespacedKey, PersistentDataType.STRING) == boxName
            }
        }

    }

    override fun hasKeys(uuid: UUID, boxName: String,amount: Int, checkHand: Boolean): Boolean {
        val player = getUser(uuid)
        if (player == null) return false
        val inv = player.inventory
        return if (checkHand){
            val item = inv.itemInMainHand
            val container = item.itemMeta?.persistentDataContainer
            if (container?.has(ItemKey.BoxKey.getNamespacedKey(), PersistentDataType.STRING) == true){
                container.get(ItemKey.BoxKey.getNamespacedKey(), PersistentDataType.STRING) == boxName && item.amount >= amount
            }else{
                false
            }
        }else{
            inv.hasItem(amount) { item ->
                val container = item.itemMeta?.persistentDataContainer
                if (container?.has(ItemKey.BoxKey.getNamespacedKey(),PersistentDataType.STRING) == true){
                    container.get(ItemKey.BoxKey.getNamespacedKey(), PersistentDataType.STRING) == boxName
                }else{
                    false
                }
            }
        }
    }



}