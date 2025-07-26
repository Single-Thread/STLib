package studio.singlethread.plugins.api.config

import com.google.common.base.Throwables
import com.google.common.collect.ImmutableMap
import gg.flyte.twilight.scheduler.async
import org.bukkit.Bukkit
import org.simpleyaml.configuration.ConfigurationSection
import org.simpleyaml.configuration.comments.CommentType
import org.simpleyaml.configuration.file.YamlFile
import studio.singlethread.plugins.api.STPlugin
import studio.singlethread.plugins.api.platform.FileResource
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.util.logging.Level


open class STConfiguration<T : STPlugin>(
    val plugin: T,
    folder: String = "configs",
    name: String,
    val version: Int = 1
) {

    constructor(plugin: T, name: String, version: Int = 1) : this(plugin, "configs", name, version)

    private val HEADER = """
        This is the configuration for %s.
        If you have any questions or need assistance,
        please join our Discord server and ask for help from %s!

        이것은 %s의 구성입니다.
        질문이 있거\uub098 도움이 필요하시면,
        저희 Discord 서버에 가입하셔서 %s에게 도움을 요청해 주세요!"""

    val file: File? = FileResource.createFileCopy(plugin, folder, name)
    val config: YamlFile = YamlFile(file)
    var changed: Boolean = false
        private set

    private var instance: STConfiguration<T>? = null

    init {
        load()
        set("version", version)
        val id = plugin.name
        val author = plugin.description.authors.joinToString(" & ")
        config.options().header(String.format(HEADER, id, author, id, author))
        config.options().copyDefaults(true)
    }

    companion object {
        fun toMap(section: ConfigurationSection?): Map<String, Any> {
            val builder = ImmutableMap.builder<String, Any>()
            section?.getKeys(false)?.forEach { key ->
                section.get(key)?.let {
                    builder.put(key, if (it is ConfigurationSection) toMap(it) else it)
                }
            }
            return builder.build()
        }
    }

    fun setup(instance: STConfiguration<T>) {
        this.instance = instance
        init()
        save()
    }

    protected fun log(level: Level, message: String) {
        Bukkit.getLogger().log(level,message)
    }

    open fun reload() {
        load()
        init()
    }

    fun load() {
        changed = false
        try {
            val previous = config.dump()
            config.loadWithComments()
            val dump = config.dump()
            if (previous.isNotEmpty() && !previous.equals(dump, ignoreCase = true)) {
                changed = true
            }
        } catch (_: IOException) {
        } catch (ex: Exception) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load ${file?.name}, please correct your syntax errors", ex)
            Throwables.throwIfUnchecked(ex)
        }
    }

    private fun init() {
        this::class.java.declaredMethods
            .filter { it.modifiers and Modifier.PRIVATE != 0 && it.parameterCount == 0 && it.returnType == Void.TYPE }
            .forEach { method ->
                runCatching {
                    method.isAccessible = true
                    method.invoke(instance)
                }.onFailure { ex ->
                    val cause = (ex as? InvocationTargetException)?.cause ?: ex
                    Throwables.throwIfUnchecked(cause)
                    Bukkit.getLogger().log(Level.SEVERE, "Error invoking $method", cause)
                }
            }
    }

    fun save() {
        if (plugin.isEnabled) {
            async { saveFile() }
        } else {
            saveFile()
        }
    }

    private fun saveFile() {
        try {
            config.save(file)
        } catch (ex: IOException) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save $file", ex)
        }
    }

    // 기본 setter
    protected fun set(path: String, value: Any, vararg comment: String) {
        config.addDefault(path, value)
        if (comment.isNotEmpty()) setComment(path, *comment)
        config.set(path, value)
    }

    // 기본 getter들
    protected fun getString(path: String, def: String, vararg comment: String): String {
        config.addDefault(path, def)
        if (comment.isNotEmpty()) setComment(path, *comment)
        return config.getString(path, config.getString(path)) ?: def
    }

    protected fun getBoolean(path: String, def: Boolean, vararg comment: String): Boolean {
        config.addDefault(path, def)
        if (comment.isNotEmpty()) setComment(path, *comment)
        return config.getBoolean(path, config.getBoolean(path))
    }

    protected fun getDouble(path: String, def: Double, vararg comment: String): Double {
        config.addDefault(path, def)
        if (comment.isNotEmpty()) setComment(path, *comment)
        return config.getDouble(path, config.getDouble(path))
    }

    protected fun getInt(path: String, def: Int, vararg comment: String): Int {
        config.addDefault(path, def)
        if (comment.isNotEmpty()) setComment(path, *comment)
        return config.getInt(path, config.getInt(path))
    }

    protected fun getLong(path: String, def: Long, vararg comment: String): Long {
        config.addDefault(path, def)
        if (comment.isNotEmpty()) setComment(path, *comment)
        return config.getLong(path, config.getLong(path))
    }

    protected fun <E> getList(path: String, def: List<E>, vararg comment: String): List<E> {
        config.addDefault(path, def)
        if (comment.isNotEmpty()) setComment(path, *comment)
        return config.getList(path, config.getList(path)) as? List<E> ?: def
    }

    protected fun getStringList(path: String, def: List<String>, vararg comment: String): List<String> =
        getList(path, def, *comment)

    protected fun getBooleanList(path: String, def: List<Boolean>, vararg comment: String): List<Boolean> =
        getList(path, def, *comment)

    protected fun getFloatList(path: String, def: List<Float>, vararg comment: String): List<Float> =
        getList(path, def, *comment)

    protected fun getDoubleList(path: String, def: List<Double>, vararg comment: String): List<Double> =
        getList(path, def, *comment)

    protected fun getIntegerList(path: String, def: List<Int>, vararg comment: String): List<Int> =
        getList(path, def, *comment)

    protected fun getLongList(path: String, def: List<Long>, vararg comment: String): List<Long> =
        getList(path, def, *comment)

    protected fun getMap(path: String, def: Map<String, Any>, vararg comment: String): Map<String, Any> {
        if (config.getConfigurationSection(path) == null) {
            config.addDefault(path, def)
            if (comment.isNotEmpty()) setComment(path, *comment)
            return def
        }
        return toMap(config.getConfigurationSection(path))
    }

    fun getDefaultSection(): ConfigurationSection = config.defaultSection

    fun getConfigurationSection(path: String, vararg comment: String): ConfigurationSection? {
        if (comment.isNotEmpty()) setComment(path, *comment)
        return config.getConfigurationSection(path)
    }

    private fun setComment(path: String, vararg comment: String) {
        config.setComment(path, comment.joinToString("\n"), CommentType.BLOCK)
    }
}
