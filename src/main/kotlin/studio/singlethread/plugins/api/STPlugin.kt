package studio.singlethread.plugins.api

import gg.flyte.twilight.scheduler.async
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import studio.singlethread.plugins.STLib
import studio.singlethread.plugins.api.command.STCommand
import studio.singlethread.plugins.api.event.STListener
import studio.singlethread.plugins.api.format.ComponentFormatter.mini
import studio.singlethread.plugins.api.platform.MinecraftVersion
import studio.singlethread.plugins.api.storage.Storage
import studio.singlethread.plugins.util.FeatherDI


abstract class STPlugin()  : JavaPlugin() {


    companion object {
        lateinit var storage: Storage
            private set
        lateinit var plugin: STPlugin
            private set
        lateinit var adventure: BukkitAudiences
            private set

    }

    private val listeners: MutableSet<STListener<out STPlugin>> = mutableSetOf()
    private val commands: MutableSet<STCommand<out STPlugin>> = mutableSetOf()
    private val version: String = "1.20.1"
    private val prefix: Component = mini("[${name}] ")

    abstract fun enable()
    abstract fun disable()
    abstract fun load()


    final override fun onEnable() {

        if (MinecraftVersion.isSupport(version)) {
            plugin = this
            adventure = BukkitAudiences.create(this)
        } else {
            async {
                Bukkit.getLogger().warning("Server version is unsupported version (< ${version}), Disabling this plugin...")
                Bukkit.getLogger().warning("서버 버전이 지원되지 않는 버전입니다 (< ${version}), 플러그인을 비활성화합니다...")
                Bukkit.getPluginManager().disablePlugin(this@STPlugin)
            }
            return
        }
        enable()
        for (plugin in this.description.softDepend) library().hookDependency(plugin)
        console("<green>Enable!</green>")
        library().loadPlugin(this)

    }
    final override fun onDisable() {

        disable()
        library().unloadPlugin(this)
        adventure.close()

    }
    final override fun onLoad() {


        load()

    }

    fun library() = STLib.library

    fun console(message: Component) {
        async { adventure.console().sendMessage(prefix.append(message)) }
    }
    fun console(minimessage: String) {
        console(mini(minimessage))
    }

    fun register(command: STCommand<out STPlugin>) {
        commands.add(command)
        command.command().register()
    }
    fun register(listener: STListener<out STPlugin>) {
        Bukkit.getPluginManager().registerEvents(listener, this)
    }
    fun register(name: String, permissionDefault: PermissionDefault) {
        Bukkit.getPluginManager().addPermission(Permission(name, permissionDefault))
    }

}