package org.singlethread.plugins

import org.bukkit.Bukkit
import org.singlethread.plugins.api.STPlugin


class STLib : STPlugin() {

    companion object {
        lateinit var plugin: STLib
    }

    private var hooks: MutableMap<String?, Boolean?> = HashMap<String?, Boolean?>()
    private val plugins: MutableMap<String?, STPlugin?> = HashMap<String?, STPlugin?>()
    override fun enable() {
    }

    override fun disable() {
    }

    override fun load() {
        plugin = this
    }

    fun loadPlugin(plugin: STPlugin) {
        console("loading STPlugin: ${plugin.name}")
        plugins.put(plugin.name, plugin);
    }
    fun unloadPlugin(plugin: STPlugin) {
        console("unloading STPlugin: ${plugin.name}")
        plugins.remove(plugin.name);
    }

    fun isEnabledDependency(dependencyName: String?): Boolean {
        return hooks.getOrDefault(dependencyName, false) == true
    }

    fun hookDependency(dependencyName: String) {
        hooks.put(dependencyName, Bukkit.getPluginManager().isPluginEnabled(dependencyName))
    }

}
