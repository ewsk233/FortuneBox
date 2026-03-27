package org.ewsk.fortunebox.network

import net.minecraft.core.BlockPosition
import net.minecraft.network.protocol.game.PacketPlayOutBlockAction
import net.minecraft.world.level.block.Blocks
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import taboolib.module.nms.sendPacket
import kotlin.collections.forEach

class NMSChestAnimationImpl: NMSChestAnimation() {
    override fun sendAnimation(
        block: Block,
        open: Boolean,
        players: Collection<Player>?
    ) {
        val location = block.location
        val world = block.world
        val pos = BlockPosition(location.x.toInt(), location.y.toInt(), location.z.toInt())
        val packet = when(block.type){
            Material.ENDER_CHEST -> PacketPlayOutBlockAction(pos, Blocks.ENDER_CHEST, 1, if (open) 1 else 0)
            Material.TRAPPED_CHEST -> PacketPlayOutBlockAction(pos, Blocks.TRAPPED_CHEST, 1, if (open) 1 else 0)
            Material.CHEST -> PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, if (open) 1 else 0)
            else -> null
        }
        if (packet != null){
            if (players != null){
                players.forEach {
                    it.sendPacket(packet)
                }
            }else{
                world.players.forEach {
                    it.sendPacket(packet)
                }
            }
        }
    }
}