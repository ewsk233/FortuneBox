package org.spectrumflow.fortunebox.api


/**
 * 以下方法需要在open中执行
 * boxManager.removeOpeningList(player)
 * box.executeCompleted(player,prize)
 */
abstract class BoxBuilder {
    abstract fun open()
}