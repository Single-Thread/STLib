package studio.singlethread.plugins

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import gg.flyte.twilight.Twilight
import org.bukkit.Bukkit
import studio.singlethread.plugins.api.STPlugin
import studio.singlethread.plugins.api.config.setting.SettingConfiguration
import studio.singlethread.plugins.command.WarpCommand
import studio.singlethread.plugins.util.APIBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class STLib : STPlugin() {



    companion object {
        lateinit var api: Twilight
            private set
        lateinit var library: STLib
            private set
    }

    private val plugins: MutableMap<String, STPlugin> = HashMap<String, STPlugin>()
    private val hooks: MutableMap<String, Boolean> = HashMap<String, Boolean>()
    private val log: Logger = LoggerFactory.getLogger("STLib")

    override fun enable() {
        CommandAPI.onEnable()

        library = this
        api = APIBuilder(this).build()
        SettingConfiguration(library).reload()
        register(WarpCommand)
    }
    override fun disable() {
        CommandAPI.onDisable()
    }
    override fun load() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true).skipReloadDatapacks(true))
    }

    fun loadPlugin(plugin: STPlugin) {
        log.info("loading STPlugin: ${plugin.name}")
        plugins.put(plugin.name, plugin)
    }

    fun unloadPlugin(plugin: STPlugin) {
        log.info("unloading STPlugin: ${plugin.name}")
        plugins.remove(plugin.name)
    }

    fun isEnabledDependency(dependencyName: String): Boolean {
        return hooks.getOrDefault(dependencyName, false)
    }

    fun hookDependency(dependencyName: String) {
        hooks.put(dependencyName, Bukkit.getPluginManager().isPluginEnabled(dependencyName))
    }


}
