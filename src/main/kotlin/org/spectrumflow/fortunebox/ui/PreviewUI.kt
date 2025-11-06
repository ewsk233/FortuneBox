package org.spectrumflow.fortunebox.ui

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.spectrumflow.fortunebox.api.objects.Prize
import taboolib.library.xseries.XMaterial
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.PageableChest
import taboolib.platform.util.asLangText
import taboolib.platform.util.buildItem

object PreviewUI {
    fun preview(player: Player,previewName: String,prizes: List<Prize>){
        player.openMenu<PageableChest<Prize>>(previewName){
            map(
                "@@@@@@@@@",
                "@@@@@@@@@",
                "@@@@@@@@@",
                "P#######N"
            )
            slotsBy('@')
            elements {prizes}
            onGenerate(async = true){_, element, _, _ ->
                element.getDisplayItem()
            }
            onClick { it.isCancelled = true }


            set('#', buildItem(XMaterial.WHITE_STAINED_GLASS_PANE) { name = " " })
            setNextPage(getFirstSlot('N')) { _, hasNext -> buildNavigationItem(hasNext, player.asLangText("next-page-button")) }
            setPreviousPage(getFirstSlot('P')) { _, hasPrev -> buildNavigationItem(hasPrev, player.asLangText("prev-page-button")) }
        }
    }
    private fun buildNavigationItem(hasPage: Boolean, name: String): ItemStack {
        return buildItem(if (hasPage) XMaterial.SPECTRAL_ARROW else XMaterial.ARROW) {
            this.name = if (hasPage) "§f$name" else "§7$name"
        }
    }
}