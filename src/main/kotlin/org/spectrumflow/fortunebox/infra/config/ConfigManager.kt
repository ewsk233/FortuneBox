package org.spectrumflow.fortunebox.infra.config

import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object ConfigManager {

    @Config
    private lateinit var config: Configuration

    @Config("prizes.yml")
    private lateinit var items: Configuration

    @Config("locations.yml")
    private lateinit var locations: Configuration

    fun reloadAll(){
        config.reload()
        items.reload()
        locations.reload()
    }

    fun getConfig(): Configuration = config
    fun getItems(): Configuration = items
    fun getLocations(): Configuration = locations
}