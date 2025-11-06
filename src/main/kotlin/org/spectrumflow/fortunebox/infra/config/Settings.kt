package org.spectrumflow.fortunebox.infra.config

import taboolib.module.configuration.Configuration

class Settings(file: Configuration) {

    val language = file.getString(ConfigKeys.Language.key,"zh_CN")!!
    val onlyCheckInMainHand = file.getBoolean(ConfigKeys.OnlyCheckInMainHand.key,true)
    val glassName = file.getString(ConfigKeys.GlassName.key)
    val prefix = file.getString(ConfigKeys.Prefix.key)?:"FortuneBox"
}