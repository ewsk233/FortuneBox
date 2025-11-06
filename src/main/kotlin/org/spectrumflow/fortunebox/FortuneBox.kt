package org.spectrumflow.fortunebox

import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.spectrumflow.fortunebox.infra.config.ConfigManager
import org.spectrumflow.fortunebox.infra.config.Settings
import org.spectrumflow.fortunebox.manager.BoxManager
import org.spectrumflow.fortunebox.manager.BukkitUserManager
import org.spectrumflow.fortunebox.manager.UserManager
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.lang.Language
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin

/**
 * @author ewsk
 * @date 2025/11/06
 */
object FortuneBox : Plugin() {

    lateinit var settings: Settings
    lateinit var boxManager: BoxManager
    lateinit var userManager: UserManager

    override fun onEnable() {
        Language.enableSimpleComponent = true

        registerPermissions()
        settings = Settings(ConfigManager.getConfig())
        boxManager = BoxManager()
        boxManager.loadHologram()
        boxManager.loadBoxes()
        userManager = BukkitUserManager()
        console().sendMessage("[§dFortuneBox§f]§a load success")
        console().sendMessage("[§dFortuneBox§f]§a author's QQ: §e2962271068")
        Metrics(27879,"0.0.1", Platform.BUKKIT)
    }

    override fun onDisable() {
        boxManager.unload()
        unregisterPermissions()
    }
    fun getJavaPlugin() = BukkitPlugin.getProvidingPlugin(this::class.java)
    fun registerPermissions(){
        val permission = Permission("fortunebox.broadcast.common", "允许接收FortuneBox广播", PermissionDefault.TRUE)
        Bukkit.getPluginManager().addPermission(permission)
    }
    fun unregisterPermissions(){
        Bukkit.getPluginManager().removePermission("fortunebox.broadcast.common")
    }

}