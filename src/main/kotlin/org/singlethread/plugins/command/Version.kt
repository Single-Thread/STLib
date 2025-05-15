package org.singlethread.plugins.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.singlethread.plugins.STLib
import org.singlethread.plugins.api.command.STCommand

class Version : STCommand(STLib.plugin, "version","버전") {

    override fun execute(args: Array<String>) {
        TODO("Not yet implemented")
    }

    override fun tab(args: Array<String>): List<String> {
        TODO("Not yet implemented")
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        TODO("Not yet implemented")
    }


}