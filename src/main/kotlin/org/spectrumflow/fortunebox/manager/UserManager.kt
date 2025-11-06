package org.spectrumflow.fortunebox.manager

import org.bukkit.entity.Player
import java.util.UUID

abstract class UserManager {
    abstract fun getUser(uuid: UUID): Player?
    abstract fun addKeys(uuid: UUID,boxName: String,amount: Int): Boolean
    abstract fun hasKeys(uuid: UUID,boxName: String,amount: Int,checkHand: Boolean): Boolean
    abstract fun takeKeys(uuid: UUID, boxName: String, amount: Int, onlyInHand: Boolean): Boolean
}