package org.singlethread.plugins.api

import io.papermc.paper.plugin.loader.library.impl.JarLibrary
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import org.singlethread.plugins.STLib
import org.singlethread.plugins.api.command.STCommand
import org.singlethread.plugins.api.event.STListener
import org.singlethread.plugins.api.format.ComponentFormatter
import org.singlethread.plugins.api.format.ComponentFormatter.mini
import org.singlethread.plugins.api.platform.MinecraftVersion


abstract class STPlugin : JavaPlugin() {

    private lateinit var plugin: STPlugin
    private lateinit var prefix: Component
    private lateinit var adventure: BukkitAudiences

    abstract fun enable()
    abstract fun disable()
    abstract fun load()

    override fun onEnable() {

        if (MinecraftVersion.isSupport("1.19.1")) {
            plugin = this;
            adventure = BukkitAudiences.create(this);
        } else {
            Bukkit.getLogger().warning("Server version is unsupported version (< 1.19.1), Disabling this plugin...");
            Bukkit.getLogger().warning("서버 버전이 지원되지 않는 버전입니다 (< 1.19.1), 플러그인을 비활성화합니다...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        registers(plugin.name + ".reload", PermissionDefault.OP);
        enable()
        console("<green>Enable!</green>");
        STLib.plugin.loadPlugin(this);

    }
    override fun onDisable() {
        STLib.plugin.unloadPlugin(this);
        console("<red>Disable!</red>");
        adventure.close()
        disable();
    }
    override fun onLoad() {
        val text = "<gradient:#F8F8F8:#121212>[${name}] </gradient>"
        this.prefix = mini(text)
        load()
    }

    fun console(message: Component) {
        adventure.console().sendMessage(prefix.append(message))
    }
    fun console(minimessage: String) {
        console(ComponentFormatter.mini(minimessage))
    }


    protected fun registers(vararg command: STCommand) {

    }
    protected fun registers(vararg event: STListener) {
        event.forEach { Bukkit.getPluginManager().registerEvents(it, this) }
    }
    protected fun registers(name: String,default: PermissionDefault) {
        val permission = Permission(name, default)
        if (Bukkit.getPluginManager().getPermission(name) == null) {
            Bukkit.getPluginManager().addPermission(permission)
        }
    }

}