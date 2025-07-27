package studio.singlethread.plugins.core

import studio.singlethread.plugins.api.STPlugin
import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;

class Libraries(plugin: STPlugin) {

    private val manager = BukkitLibraryManager(plugin, "libraries").apply {
        addMavenCentral()
        addJitPack()
    }

    fun load(dependency: String) {
        val (groupId, artifactId, version) = dependency.split(":")
        val lib = Library.builder()
            .groupId(groupId)
            .artifactId(artifactId)
            .version(version)
            .build()
        manager.loadLibrary(lib)
    }

    fun load(dependency: String, pattern: String, relocatedPattern: String) {
        val (groupId, artifactId, version) = dependency.split(":")
        val lib = Library.builder()
            .groupId(groupId)
            .artifactId(artifactId)
            .version(version)
            .relocate(pattern, relocatedPattern)
            .build()
        manager.loadLibrary(lib)
    }
}