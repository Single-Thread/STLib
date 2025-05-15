package org.singlethread.plugins.api.format

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.singlethread.plugins.STLib

object ComponentFormatter {

    fun mini(miniMessage: String): Component {
        return MiniMessage.miniMessage().deserialize(miniMessage)
    }

    fun mini(component: Component): String {
        return MiniMessage.miniMessage().serialize(component)
    }

    fun parse(miniMessage: String): Component {
        return parse(null, miniMessage)
    }

    fun parse(sender: CommandSender?, miniMessage: String): Component {
        val message = if (STLib.plugin.isEnabledDependency("PlaceholderAPI")) {
            val player = sender as? Player
            PlaceholderAPI.setPlaceholders(player, miniMessage)
        } else {
            miniMessage
        }
        return mini(message)
    }

    fun legacy(component: Component): String {
        return legacy(LegacyComponentSerializer.SECTION_CHAR, component)
    }

    fun legacy(legacyCharacter: Char, component: Component): String {
        return LegacyComponentSerializer.legacy(legacyCharacter).serialize(component)
    }

//    fun system(sender: CommandSender?, miniMessage: String): Component {
//        val lore = parse(sender, framework().modules.themeModule.systemMessage)
//        return parse(miniMessage).hoverEvent(HoverEvent.showText(lore))
//    }
//
//    fun system(sender: CommandSender?, component: Component): Component {
//        val lore = parse(sender, framework().modules.themeModule.systemMessage)
//        return component.hoverEvent(HoverEvent.showText(lore))
//    }

}