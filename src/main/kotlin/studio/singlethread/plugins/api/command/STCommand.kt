package studio.singlethread.plugins.api.command

import dev.jorel.commandapi.CommandAPICommand
import studio.singlethread.plugins.api.STPlugin

abstract class STCommand<T : STPlugin>(plugin: T) {

    abstract fun command() : CommandAPICommand

}
