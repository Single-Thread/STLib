package org.singlethread.plugins.api.command

import org.bukkit.command.CommandExecutor
import org.singlethread.plugins.api.STPlugin

abstract class STCommand(val plugin: STPlugin,vararg name: String) : CommandExecutor {


    abstract fun execute(args: Array<String>)
    abstract fun tab(args: Array<String>): List<String>


}