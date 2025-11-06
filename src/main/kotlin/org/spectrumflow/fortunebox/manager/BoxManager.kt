package org.spectrumflow.fortunebox.manager

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.spectrumflow.fortunebox.FortuneBox
import org.spectrumflow.fortunebox.FortuneBox.settings
import org.spectrumflow.fortunebox.api.enums.BoxConfig
import org.spectrumflow.fortunebox.api.enums.BoxType
import org.spectrumflow.fortunebox.api.objects.Box
import org.spectrumflow.fortunebox.box.CSGOBox
import org.spectrumflow.fortunebox.box.RouletteBox
import org.spectrumflow.fortunebox.infra.config.ConfigManager
import org.spectrumflow.fortunebox.infra.hologram.DecentHologramSupport
import org.spectrumflow.fortunebox.infra.hologram.HologramSupport
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFolder
import taboolib.common.platform.function.warning
import taboolib.module.configuration.Configuration
import taboolib.platform.util.sendLang
import java.io.File
import java.util.UUID

/**
 * @author ewsk
 * @date 2025/11/06
 */
class BoxManager {


    private val boxes = HashMap<String, Box>()
    private val boxMap = HashMap<Location, Box>()
    private val locationKey = HashMap<Location, String>()
    private val locationsData = ConfigManager.getLocations()
    private val openingBoxList = HashSet<Player>()
    private var hologramSupport: HologramSupport? = null
    fun loadHologram(){
        if (Bukkit.getPluginManager().isPluginEnabled("DecentHolograms")){
            hologramSupport = DecentHologramSupport()
        }

    }
    fun loadBoxes(){
        boxes.clear()
        boxMap.clear()
        locationKey.clear()

        hologramSupport?.removeAll()
        for (file: File in getBoxFiles()){
            try {
                val config = Configuration.loadFromFile(file)
                val boxType = BoxType.fromText(config.getString(BoxConfig.BoxType.key)?: "CSGO")?: BoxType.CSGO
                val fileName = file.name.removeSuffix(".yml")
                val box = Box(boxType,fileName,config)
                boxes[fileName] = box
            }catch (e: Exception){
                warning("There was an error while loading ${file.name} file. $e")
                e.printStackTrace()
            }
        }

        val section = locationsData.getConfigurationSection("Locations")
        section?.getKeys(false)?.forEach { key ->
            val name = section.getString("${key}.name")
            val world = section.getString("${key}.world")
            val x = section.getDouble("${key}.x")
            val y = section.getDouble("${key}.y")
            val z = section.getDouble("${key}.z")
            val box = boxes[name]
            if (box != null && world != null){
                val location = Location(Bukkit.getWorld(world),x,y,z)
                boxMap[location] = box
                locationKey[location] = key
                if (box.hologramEnable && hologramSupport != null){
                    hologramSupport!!.createHologram(location,box,key)
                }
            }else{
                warning("fail to load box instance: $name")
            }
        }
    }
    fun addBoxLocation(location: Location,box: Box){
        val key = UUID.randomUUID().toString()
        locationsData["Locations.${key}.name"] = box.fileName
        locationsData["Locations.${key}.world"] = location.world?.name
        locationsData["Locations.${key}.x"] = location.x
        locationsData["Locations.${key}.y"] = location.y
        locationsData["Locations.${key}.z"] = location.z
        locationsData.saveToFile()
        locationKey[location] = key
        boxMap[location] = box
        if (box.hologramEnable){
            hologramSupport?.createHologram(location,box,key)
        }
    }
    fun removeBoxLocation(location: Location){
        val key = locationKey[location]
        locationsData["Locations.$key"] = null
        locationsData.saveToFile()
        if (key != null){
            hologramSupport?.removeHologram(key)
        }
        boxMap.remove(location)
    }
    fun hasBox(location: Location) = boxMap[location] != null

    fun getBox(location: Location) = boxMap[location]

    fun getBoxFromName(name: String) = boxes[name]

    fun getBoxNames() = boxes.values.map { box -> box.fileName }

    fun getBoxFiles(): ArrayList<File>{
        val files = ArrayList<File>()
        val folder = File(getDataFolder(), "boxes")
        if (!folder.exists() || !folder.isDirectory){
            releaseResourceFolder("boxes/")
        }
        for (file: File in folder.listFiles()){
            if (file.name.endsWith(".yml")){
                files.add(file)
            }
        }
        return files
    }
    fun openBox(player: Player,box: Box,location: Location){
        if (hasOpeningList(player)){
            return
        }
        val userManager = FortuneBox.userManager
        if (!userManager.hasKeys(player.uniqueId,box.fileName,box.requireKeys,settings.onlyCheckInMainHand)){
            player.sendLang("no-key",box.name)
            return
        }
        if (!userManager.takeKeys(player.uniqueId,box.fileName,box.requireKeys,settings.onlyCheckInMainHand)){
            player.sendLang("no-key",box.name)
            return
        }
        val boxType = box.boxType
        val boxBuilder = when(boxType){
            BoxType.CSGO -> CSGOBox(player,box,location)
            BoxType.Roulette -> RouletteBox(player,box,location)
        }
        addOpeningList(player)
        box.executeOpening(player)
        boxBuilder.open()
    }
    fun unload(){
        boxes.clear()
        boxMap.clear()
        locationKey.clear()
    }
    fun addOpeningList(player: Player){
        openingBoxList.add(player)
    }
    fun hasOpeningList(player: Player) = openingBoxList.contains(player)
    fun removeOpeningList(player: Player) = openingBoxList.remove(player)

}