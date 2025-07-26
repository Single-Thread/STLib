package studio.singlethread.plugins.api.event

import org.bukkit.event.Listener
import studio.singlethread.plugins.api.STPlugin

abstract class STListener<T : STPlugin>(plugin: T) : Listener {

}