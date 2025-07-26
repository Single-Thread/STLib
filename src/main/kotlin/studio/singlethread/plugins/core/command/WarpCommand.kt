package studio.singlethread.plugins.core.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import studio.singlethread.plugins.STLib
import studio.singlethread.plugins.api.command.STCommand


object WarpCommand : STCommand<STLib>(STLib.library) {
    override fun command(): CommandAPICommand {



        return CommandAPICommand("test")
            .executes(CommandExecutor { sender, _ ->
                sender.sendMessage("pong!")
            })
    }

}