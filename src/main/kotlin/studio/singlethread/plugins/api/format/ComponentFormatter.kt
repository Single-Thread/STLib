package studio.singlethread.plugins.api.format

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import studio.singlethread.plugins.STLib
import studio.singlethread.plugins.core.di.FeatherDI


object ComponentFormatter {

    private fun library() = STLib.library

    fun mini(miniMessage: String): Component {
        return MiniMessage.miniMessage().deserialize(miniMessage)
    }

    fun mini(component: Component): String {
        return MiniMessage.miniMessage().serialize(component)
    }

    fun parse(msg: String): Component {
        return parse(null, msg)
    }

    fun parse(sender: CommandSender?, miniMessage: String): Component {
        return ComponentFormatter.mini(
            if (library().isEnabledDependency("PlaceholderAPI")) PlaceholderAPI.setPlaceholders(
                if (sender is Player) sender else null,
                miniMessage
            ) else miniMessage
        )
    }

    fun legacy(component: Component): String {
        return legacy(LegacyComponentSerializer.SECTION_CHAR, component)
    }

    fun legacy(legacyCharacter: Char, component: Component): String {
        return LegacyComponentSerializer.legacy(legacyCharacter).serialize(component)
    }
}