package org.spectrumflow.fortunebox.api.objects

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.spectrumflow.fortunebox.FortuneBox.settings
import org.spectrumflow.fortunebox.api.enums.BoxConfig
import org.spectrumflow.fortunebox.api.enums.BoxType
import org.spectrumflow.fortunebox.api.enums.ItemKey
import org.spectrumflow.fortunebox.infra.config.ConfigManager
import org.spectrumflow.fortunebox.ui.PreviewUI
import taboolib.library.xseries.parseToItemStack
import taboolib.module.configuration.Configuration
import taboolib.platform.util.buildItem
import top.maplex.arim.Arim
import top.maplex.arim.tools.weightrandom.WeightRandom
/**
 * @author ewsk
 * @date 2025/11/06
 */
class Box(
    val boxType: BoxType,
    val fileName: String,
    val file: Configuration,
    ) {
    val name = file.getString(BoxConfig.Name.key)?: "FortuneBox"
    private val boardCastEnable = file.getBoolean(BoxConfig.BoardCastEnable.key,false)
    private val openingBoardCast = file.getString(BoxConfig.OpeningBoardCast.key)
    private val completedBoardCast = file.getString(BoxConfig.CompletedBoardCast.key)
    private val openingCommandEnable = file.getBoolean(BoxConfig.OpeningCommandEnable.key,false)
    private val openingCommands = file.getStringList(BoxConfig.OpeningCommands.key)
    private val previewEnable = file.getBoolean(BoxConfig.PreviewEnable.key,false)
    private val previewName = file.getString(BoxConfig.PreviewName.key)?:"Chest"
    val requireKeys = file.getInt(BoxConfig.RequireKeys.key,1)
    val animation = file.getString(BoxConfig.Animation.key)?: "rolling..."
    val hologramEnable = file.getBoolean(BoxConfig.HologramEnable.key,false)
    val hologramHeight = file.getDouble(BoxConfig.HologramHeight.key)
    val hologramUpdateInterval = file.getInt(BoxConfig.HologramUpdateInterval.key)
    val hologramRange = file.getInt(BoxConfig.HologramRange.key)
    val hologramContent = file.getStringList(BoxConfig.HologramContent.key)
    private val prizeMessage = file.getStringList(BoxConfig.PrizeMessage.key)
    private val prizeCommands = file.getStringList(BoxConfig.PrizeCommands.key)
    private val prizes = loadPrizes()


    private fun loadPrizes(): List<Prize>{
        val prizes = ArrayList<Prize>()
        val list = file.getList(BoxConfig.Prize.key)
        val items = ConfigManager.getItems()
        list?.map {
            val map = it as Map<*,*>
            val key = map["Key"] as String
            val weight = map["Weight"] as Int
            if (items.contains(key)) {
                val section = items.getConfigurationSection(key)
                section?.apply{
                    prizes.add(Prize(this, weight))
                }
            }
        }
        return prizes
    }
    private val key = file.getString(BoxConfig.KeyItem.key)?.parseToItemStack()?.let { buildItem(it){
        file.getString(BoxConfig.KeyName.key)?.apply { name = this }
        file.getStringList(BoxConfig.KeyLore.key).apply { lore += this }
        if (file.getBoolean(BoxConfig.KeyGlowing.key)) shiny()
        customModelData = file.getInt(BoxConfig.KeyCustomModelData.key)
        val namespace = file.getString(BoxConfig.KeyModelNamespace.key)
        val modelKey = file.getString(BoxConfig.KeyModelKey.key)
        if (namespace != null && modelKey != null && namespace.isNotBlank() && modelKey.isNotBlank()){
            itemModel = NamespacedKey(namespace,modelKey)
        }

    } }
    fun getKey(): ItemStack{
        val meta = key?.itemMeta
        meta?.persistentDataContainer?.set(ItemKey.BoxKey.getNamespacedKey(), PersistentDataType.STRING,fileName)
        meta?.apply {
            key.itemMeta = meta
        }
        return key?: buildItem(Material.STONE){name = "error"}
    }
    fun isPreviewEnable() = previewEnable
    fun preview(player: Player){
        PreviewUI.preview(player,previewName,prizes)
    }
    fun randomPrize(): Prize?{
        val collection = prizes.map {
            WeightRandom.WeightCategory(it,it.getWeight())
        }
        return Arim.weightRandom.getWeightRandom(collection)
    }

    private val soundMap = HashMap<String, SoundData>()

    fun playSound(type: String,location: Location){
        val soundData = soundMap[type]
        if (soundData == null){
            val section = file.getConfigurationSection(BoxConfig.Sound.key)
            val enable = section?.getBoolean("$type.Enable")
            if (enable == true){
                val value = section.getString("$type.Value")
                val volume = section.getDouble("$type.Volume",1.0)
                val pitch = section.getDouble("$type.Pitch",1.0)
                if (value != null && value.isNotBlank()){
                    location.world?.playSound(location,value, volume.toFloat(), pitch.toFloat())
                }
                soundMap[type] = SoundData(true,value,volume.toFloat(),pitch.toFloat())
            }else{
                soundMap[type] = SoundData(false,null,null,null)
            }
        }else{
            if (soundData.enable && soundData.value != null && soundData.volume != null && soundData.pitch != null){
                location.world?.playSound(location,soundData.value, soundData.volume, soundData.pitch)
            }

        }
    }
    fun executeOpening(player: Player){
        if (boardCastEnable){
            if (!openingBoardCast.isNullOrBlank()){
                val boardCast = openingBoardCast.replace("%prefix%",settings.prefix).replace("%player%",player.name)
                Bukkit.broadcast(boardCast,"fortunebox.boardcast.common")
            }
        }
        if (openingCommandEnable){
            if (openingCommands.isNotEmpty()){
                val commands = openingCommands.map { it.replace("%player%",player.name) }
                commands.forEach {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),it)
                }

            }
        }
    }
    fun executeCompleted(player: Player,prize: Prize){
        if (boardCastEnable){
            if (!completedBoardCast.isNullOrBlank()){
                val boardCast = completedBoardCast.replace("%prefix%",settings.prefix).replace("%player%",player.name).replace("%prize%",prize.getPrizeName())
                Bukkit.broadcast(boardCast,"fortunebox.boardcast.common")
            }
        }
        prizeMessage.forEach {
            val message = it.replace("%box%",name).replace("%prize%",prize.getPrizeName())
            player.sendMessage(message)
        }
        val commands = prizeCommands.map { it.replace("%player%",player.name) }
        commands.forEach {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),it)
        }
        val commandsPrize = prize.getCommands().map { it.replace("%player%",player.name) }
        commandsPrize.forEach {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),it)
        }
    }

}