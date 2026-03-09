package org.ewsk.fortunebox.event

import org.ewsk.fortunebox.FortuneBox.settings
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.lang.event.PlayerSelectLocaleEvent
import taboolib.module.lang.event.SystemSelectLocaleEvent

@SubscribeEvent
fun lang(event: PlayerSelectLocaleEvent) {
    event.locale = settings.language
}

@SubscribeEvent
fun lang(event: SystemSelectLocaleEvent) {
    event.locale = settings.language
}