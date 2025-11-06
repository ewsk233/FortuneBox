package org.spectrumflow.fortunebox.command

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.spectrumflow.fortunebox.FortuneBox.boxManager
import org.spectrumflow.fortunebox.FortuneBox.userManager
import org.spectrumflow.fortunebox.infra.config.ConfigManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.int
import taboolib.common.platform.command.player
import taboolib.common.platform.command.subCommand
import taboolib.common.util.isPlayer
import taboolib.module.lang.sendLang


@CommandHeader("fortunebox",["fb"], permissionDefault = PermissionDefault.TRUE)
object MainCommand {
    @CommandBody(permission = "fortunebox.command.reload", permissionDefault = PermissionDefault.OP)
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            ConfigManager.reloadAll()
            boxManager.loadBoxes()
            sender.sendLang("reload-success")
        }
    }
    @CommandBody(permission = "fortunebox.command.set", permissionDefault = PermissionDefault.OP)
    val set = subCommand {
        dynamic("name"){
            suggestion<ProxyCommandSender>(uncheck = true) { _,_ ->
                boxManager.getBoxNames()
            }
            execute<ProxyCommandSender>{sender, context, _ ->
                if (!sender.isPlayer()){
                    sender.sendLang("must-be-player")
                    return@execute
                }
                val player = sender.cast<Player>()
                val block = player.getTargetBlock(null, 5)
                if (block.isEmpty || block.isLiquid){
                    sender.sendLang("must-be-looking-at-block")
                    return@execute
                }
                val name = context["name"]
                val box = boxManager.getBoxFromName(name)
                if (box == null){
                    sender.sendLang("no-box",name)
                    return@execute
                }
                val location = block.location
                boxManager.addBoxLocation(location,box)
                sender.sendLang("set-success")
            }
        }

    }
    @CommandBody(permission = "fortunebox.command.give", permissionDefault = PermissionDefault.OP)
    val give = subCommand {
        player{
            dynamic("box"){
                suggestion<ProxyCommandSender>(uncheck = true) { _,_ ->
                    boxManager.getBoxNames()
                }
                int("amount") {
                    execute<ProxyCommandSender> { sender, context, _ ->
                        val s = context["player"]
                        val player = Bukkit.getPlayer(s)
                        if (player != null){
                            val amount = context["amount"].toInt()
                            val box = context["box"]
                            userManager.addKeys(player.uniqueId,box,amount)
                            sender.sendLang("give-key-success",player.name,amount,box)
                        }else{
                            sender.sendLang("player-not-exist")
                        }
                    }
                }
            }
        }
    }
    @CommandBody(permission = "fortunebox.command.get", permissionDefault = PermissionDefault.OP)
    val get = subCommand {
        dynamic("box"){
            suggestion<ProxyCommandSender>(uncheck = true) { _,_ ->
                boxManager.getBoxNames()
            }
            int("amount") {
                execute<ProxyCommandSender> { sender, context, _ ->
                    if (!sender.isPlayer()){
                        sender.sendLang("must-be-player")
                        return@execute
                    }
                    val player = sender.cast<Player>()
                    val box = context["box"]
                    userManager.addKeys(player.uniqueId,box,context["amount"].toInt())
                    sender.sendLang("get-key-success",box)
                }
            }
        }
    }

}