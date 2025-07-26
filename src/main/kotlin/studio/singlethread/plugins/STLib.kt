package studio.singlethread.plugins

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import gg.flyte.twilight.Twilight
import gg.flyte.twilight.scheduler.async
import org.bukkit.Bukkit
import studio.singlethread.plugins.api.STPlugin
import studio.singlethread.plugins.api.config.setting.SettingConfiguration
import studio.singlethread.plugins.core.command.WarpCommand
import studio.singlethread.plugins.util.APIBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import studio.singlethread.plugins.util.FeatherDI
import studio.singlethread.plugins.util.Libraries


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
    private val libraries: Libraries

    init {
        libraries = Libraries(this)
        libraries.load("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.23")
        libraries.load("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
        libraries.load("org.jetbrains.exposed:exposed-core:0.49.0")
        libraries.load("org.jetbrains.exposed:exposed-dao:0.49.0")
        libraries.load("org.jetbrains.exposed:exposed-jdbc:0.49.0")
        libraries.load("org.xerial:sqlite-jdbc:3.45.2.0")
        libraries.load("com.mysql:mysql-connector-j:9.1.0")

        libraries.load("net.kyori:adventure-text-minimessage:4.22.0")
    }

    override fun enable() {

        library = FeatherDI.getBean(STLib::class.java)
        api = APIBuilder(library).build()
        CommandAPI.onEnable()
        SettingConfiguration(library).reload()
        register(WarpCommand)

    }
    override fun disable() {

        CommandAPI.onDisable()

    }
    override fun load() {

        FeatherDI.register(STLib::class.java,this)
        CommandAPI.onLoad(CommandAPIBukkitConfig(this@STLib).silentLogs(true).skipReloadDatapacks(true))

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
