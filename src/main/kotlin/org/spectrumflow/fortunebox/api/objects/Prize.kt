package org.spectrumflow.fortunebox.api.objects

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XEnchantment
import taboolib.platform.util.buildItem
import top.maplex.arim.Arim
import kotlin.collections.set

/**
 * @author ewsk
 * @date 2025/11/06
 */
class Prize(
    private val section: ConfigurationSection,
    private val weight: Int
) {
    private val prizeName = section.getString("Name")?:"LuckBoxPrize"
    private val displayName = section.getString("DisplayName")
    private val displayLore = section.getStringList("DisplayLore")
    private val displayAmount = section.getInt("DisplayAmount",1)
    private val glowing = section.getBoolean("Glowing")
    private val enchants = section.getConfigurationSection("DisplayEnchants")
    private val displayCustomModelData = section.getInt("DisplayCustomModelData",-1)
    private val displayModelNamespace = section.getString("DisplayModel.Namespace")
    private val displayModelKey = section.getString("DisplayModel.Key")
    private val displayRate = section.getString("DisplayRate")
    private val displayItem = section.getString("DisplayItem")?.parseToItem()?.let {
        buildItem(it){
            displayName?.apply { name = displayName }
            if (displayLore.isNotEmpty()) lore += displayLore
            displayRate?.apply { lore += displayRate }
            amount = displayAmount
            if (glowing) shiny()
            val keys = this@Prize.enchants?.getKeys(false)
            if (keys != null){
                for (key: String in keys){
                    val level = this@Prize.enchants.getInt(key,0)
                    val enchantment = XEnchantment.of(key).get().get()
                    if (enchantment != null){
                        enchants[enchantment] = level
                    }
                }
            }

            if (this@Prize.displayCustomModelData != -1) customModelData = this@Prize.displayCustomModelData
            if (!displayModelNamespace.isNullOrBlank() && !displayModelKey.isNullOrBlank()){
                itemModel = NamespacedKey(displayModelNamespace,displayModelKey)
            }
        }
    }

    private val items = section.getList("Items")?.mapNotNull { e ->
        val map = e as Map<*, *>
        val item = map["Item"]
        val name = map["Name"]
        val lore = map["Lore"]
        val amount = map["Amount"]
        val customModelData = map["CustomModelData"]
        val model = map["Model"]
        val enchants = map["Enchants"]
        val itemStack = item?.let { i ->
            buildItem((i as String).parseToItem()) {
                name?.apply { this@buildItem.name = this as String }
                lore?.apply { this@buildItem.lore += (this as List<*>).map { s -> s as String } }
                amount?.apply { this@buildItem.amount = this as Int }
                customModelData?.apply { this@buildItem.customModelData = this as Int }
                model?.apply {
                    val m = this as Map<*, *>
                    val namespace = m["Namespace"]
                    val key = m["Key"]
                    if (namespace != null && key != null && (namespace as String).isNotBlank() && (key as String).isNotBlank()) {
                        this@buildItem.itemModel = NamespacedKey(namespace, key)
                    }
                }
                enchants?.apply {
                    val m = this as Map<*,*>
                    m.keys.forEach { key ->
                        val level = (m[key] as Number).toInt()
                        val enchantment = XEnchantment.of(key as String).get().get()
                        if (enchantment != null){
                            this@buildItem.enchants[enchantment] = level
                        }
                    }
                }
            }
        }
        itemStack
    }

    private val commands = section.getStringList("Commands")
    fun getDisplayItem(): ItemStack{
        return this.displayItem?: buildItem(Material.STONE){name = "error"}
    }

    fun getPrizeItems(): List<ItemStack>?{
        return this.items
    }
    fun getPrizeName() = prizeName
    fun getCommands() = this.commands
    fun getWeight() = this.weight
    private fun String.parseToItem(): ItemStack{
        return Arim.itemManager.parse2ItemStack(this).itemStack
        /*if (this.contains(':')){
            val split = this.split(':')
            val type = split[0]
            val id = split[1]
            val stone = buildItem(Material.STONE)
            when(type){
                "MM" -> {
                    if (Mythic.isLoaded()){
                        return Mythic.API.getItemStack(id)?:stone
                    }
                    return stone
                }
                "AF" -> {
                    if (pm.getPlugin("AzureFlow") != null){
                        return AzureFlowAPI.getFactory(id)?.build()?.itemStack()?:stone
                    }
                    return stone
                }
                else -> {
                    return Material.getMaterial(this.uppercase().replace(':','_').replace(Regex("\\W"),""))
                        ?.let { buildItem(it) }?: stone
                }
            }
        }else{
            return this.parseToItemStack()
        }*/
    }


}