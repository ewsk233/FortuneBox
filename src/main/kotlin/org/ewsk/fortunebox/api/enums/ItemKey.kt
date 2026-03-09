package org.ewsk.fortunebox.api.enums

import org.bukkit.NamespacedKey
import org.ewsk.fortunebox.FortuneBox

enum class ItemKey(val key: String) {

    BoxKey("BoxKey");
    private val plugin = FortuneBox.getJavaPlugin()
    fun getNamespacedKey() = NamespacedKey(this.plugin,"${this.plugin.name.lowercase()}_${this.key}")
}