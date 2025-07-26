package studio.singlethread.plugins.api.platform

import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.logging.Level;

object FileResource {

    @JvmStatic
    fun copy(plugin: Plugin, sourceFile: String): File = copy(plugin, null, sourceFile)
    @JvmStatic
    fun copy(plugin: Plugin, sourceFolder: String?, sourceFile: String): File {
        val targetFolder = if (sourceFolder == null) { createFolder(plugin.dataFolder)
        } else { createFolder(File(plugin.dataFolder, sourceFolder)) }

        val resultFile = File(targetFolder, sourceFile)
        if (!resultFile.exists()) {
            try {
                resultFile.createNewFile()
                val resourcePath = if (sourceFolder == null) sourceFile else "$sourceFolder/$sourceFile"
                plugin.getResource(resourcePath)?.use { `in` ->
                    FileOutputStream(resultFile).use { out ->
                        ByteStreams.copy(`in`, out)
                    }
                }
            } catch (e: Exception) {
                Bukkit.getLogger().log(Level.WARNING, "Error copying file $sourceFolder/$sourceFile", e)
            }
        }
        return resultFile
    }

    @JvmStatic
    fun createFileCopy(plugin: Plugin, sourceFolder: String, sourceFile: String): File? {
        val targetFolder = createFolder(File(plugin.dataFolder, sourceFolder))
        val resultFile = File(targetFolder, sourceFile)
        if (resultFile.exists()) return resultFile
        return try {
            resultFile.createNewFile()
            plugin.getResource("$sourceFolder/$sourceFile")?.use { `in` ->
                FileOutputStream(resultFile).use { out ->
                    ByteStreams.copy(`in`, out)
                }
            }
            resultFile
        } catch (e: Exception) {
            Bukkit.getLogger().log(Level.WARNING, "Error copying file $sourceFolder/$sourceFile", e)
            null
        }
    }

    @JvmStatic
    fun getResourceWithoutNew(folder: String, file: String): File? {
        val resource = File(folder, file)
        return if (resource.exists()) resource else null
    }

    @JvmStatic
    fun createFolder(folder: String): File = createFolder(File(folder))
    @JvmStatic
    fun createFolder(folder: File): File {
        if (!folder.exists()) {
            try {
                folder.mkdirs()
            } catch (e: Exception) {
                Bukkit.getLogger().log(Level.WARNING, "Error creating folder $folder", e)
            }
        }
        return folder
    }

    @JvmStatic
    fun createFile(folder: String, file: String): File = createFile(File(folder), file)
    @JvmStatic
    fun createFile(folder: File, file: String): File {
        if (!folder.exists()) {
            try {
                folder.mkdirs()
            } catch (e: Exception) {
                Bukkit.getLogger().log(Level.WARNING, "Error creating folder $folder", e)
            }
        }
        val resourceFile = File(folder, file)
        if (!resourceFile.exists()) {
            try {
                resourceFile.createNewFile()
            } catch (e: Exception) {
                Bukkit.getLogger().log(Level.WARNING, "Error creating file $file", e)
            }
        }
        return resourceFile
    }


}