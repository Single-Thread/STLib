package studio.singlethread.plugins.api.platform

import org.bukkit.Bukkit


object MinecraftVersion {

    private val VERSION_STR: String = fromAPI(Bukkit.getBukkitVersion())
    private val VERSION: Version = Version(VERSION_STR)

    fun fromAPI(version: String): String {
        return version.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
    }

    /***
     * if you use Bukkit#getBukkitVersion, use fromAPI
     * @param minVersion ex) 1.14.0
     * @param maxVersion ex) 1.21.1
     * @return isSupportVersion
     */
    fun isSupport(minVersion: String, maxVersion: String): Boolean {
        val min = Version(minVersion)
        val max = Version(maxVersion)
        if (min.major > VERSION.major || max.major < VERSION.major) return false
        if (min.minor > VERSION.minor || max.minor < VERSION.minor) return false
        return min.patch <= VERSION.patch && max.patch >= VERSION.patch
    }

    fun isSupport(versions: MutableList<String?>): Boolean {
        return versions.contains(VERSION_STR)
    }

    /***
     * check server is higher than minVersion
     * @return isSupportVersion
     */
    fun isSupport(minVersion: String): Boolean {
        val min = Version(minVersion)
        if (min.major > VERSION.major) return false
        if (min.minor > VERSION.minor) return false
        return min.patch <= VERSION.patch
    }

    @Deprecated("")
    fun isLegacy(): Boolean {
        return hasClass("net.minecraft.server.MinecraftServer")
    }

    fun isPaper(): Boolean {
        return hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration")
    }

    fun isFolia(): Boolean {
        return hasClass("io.papermc.paper.threadedregions.RegionizedServer")
    }

    private fun hasClass(className: String?): Boolean {
        try {
            Class.forName(className)
            return true
        } catch (e: ClassNotFoundException) {
            return false
        }
    }
    override fun toString(): String {
        return VERSION_STR
    }

    fun get(): Version {
        return VERSION
    }

    fun getNMS(versionStr: String): String {
        val version = Version(versionStr)
        return when (version.minor) {
            17 -> "v1_17_R1"
            18 -> when (version.patch) {
                0, 1 -> "v1_18_R1"
                else -> "v1_18_R2"
            }

            19 -> when (version.patch) {
                0, 1, 2 -> "v1_19_R1"
                3 -> "v1_19_R2"
                else -> "v1_19_R3"
            }

            20 -> when (version.patch) {
                0, 1 -> "v1_20_R1"
                2 -> "v1_20_R2"
                3, 4 -> "v1_20_R3"
                else -> "v1_20_R4"
            }

            21 -> when (version.patch) {
                0, 1 -> "v1_21_R1"
                2, 3 -> "v1_21_R2"
                4 -> "v1_21_R3"
                else -> "v1_21_R4"
            }

            else -> "v1_21_R4"
        }
    }
    class Version(version: String) {
        val major: Int
        val minor: Int
        val patch: Int

        init {
            val numbers = version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val majorStr = if (numbers.size == 0) "0" else numbers[0]
            val minorStr = if (numbers.size <= 1) "0" else numbers[1]
            val patchStr = if (numbers.size <= 2) "0" else numbers[2]
            this.major = majorStr.toInt()
            this.minor = minorStr.toInt()
            this.patch = patchStr.toInt()
        }

        override fun toString(): String {
            return major.toString() + "." + minor + "." + patch
        }
    }
}