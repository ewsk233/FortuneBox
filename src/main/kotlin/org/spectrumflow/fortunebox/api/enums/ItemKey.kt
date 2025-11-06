package org.spectrumflow.fortunebox.api.enums

import org.bukkit.NamespacedKey
import org.spectrumflow.fortunebox.FortuneBox

enum class ItemKey(val key: String) {

    BoxKey("BoxKey");
    private val plugin = FortuneBox.getJavaPlugin()
    fun getNamespacedKey() = NamespacedKey(this.plugin,"${this.plugin.name.lowercase()}_${this.key}")
}