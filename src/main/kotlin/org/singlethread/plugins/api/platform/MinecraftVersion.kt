package org.singlethread.plugins.api.platform

import org.bukkit.Bukkit

object MinecraftVersion {

    private val VERSION_STR: String = fromAPI(Bukkit.getBukkitVersion())
    private val VERSION: Version = Version(VERSION_STR)

    fun fromAPI(version: String): String = version.split("-")[0]

    /**
     * if you use Bukkit#getBukkitVersion, use fromAPI
     * @param minVersion ex) 1.14.0
     * @param maxVersion ex) 1.21.0
     * @return isSupportVersion
     */
    fun isSupport(minVersion: String, maxVersion: String): Boolean {
        val min = Version(minVersion)
        val max = Version(maxVersion)
        return min.version <= VERSION.version && max.version >= VERSION.version
    }
    fun isSupport(versions: List<String>): Boolean {
        return versions.contains(VERSION_STR)
    }
    /**
     * check server is > 1.14.4
     * @return isSupportVersion
     */
    fun isSupport(minVersion: String): Boolean {
        val min = Version(minVersion)
        return min.version <= VERSION.version
    }

    @Deprecated("Use specific version checks instead")
    fun isLegacy(): Boolean = hasClass("net.minecraft.server.MinecraftServer")
    fun isPaper(): Boolean =
        hasClass("com.destroystokyo.paper.PaperConfig") ||
                hasClass("io.papermc.paper.configuration.Configuration")
    fun isFolia(): Boolean =
        hasClass("io.papermc.paper.threadedregions.RegionizedServer")

    private fun hasClass(className: String): Boolean {
        return try {
            Class.forName(className)
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    fun getAsText(): String = VERSION_STR
    fun get(): Version = VERSION
    fun getNMS(versionStr: String): String {
        return when (Version(versionStr).version) {
            1171 -> "v1_17_R1";
            1180, 1181 -> "v1_18_R1";
            1182 -> "v1_18_R2";
            1190, 1191, 1192 -> "v1_19_R1";
            1193 -> "v1_19_R2";
            1194 -> "v1_19_R3";
            1200, 1201 -> "v1_20_R1";
            1202 -> "v1_20_R2";
            1203, 1204 -> "v1_20_R3";
            1205, 1206 -> "v1_20_R4";
            1210, 1211 -> "v1_21_R1";
            1212, 1213 -> "v1_21_R2";
            1214 -> "v1_21_R3";
            1215 -> "v1_21_R4";
            else -> "v1_21_R4";
        }
    }

    data class Version(val raw: String) {
        val major: Int
        val minor: Int
        val patch: Int
        val version: Int

        init {
            val parts = raw.split(".")
            major = parts.getOrNull(0)?.toIntOrNull() ?: 0
            minor = parts.getOrNull(1)?.toIntOrNull() ?: 0
            patch = parts.getOrNull(2)?.toIntOrNull() ?: 0
            version = String.format("%d%02d%02d", major, minor, patch).toInt()
        }
    }
}