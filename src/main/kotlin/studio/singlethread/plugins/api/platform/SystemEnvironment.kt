package studio.singlethread.plugins.api.platform

object SystemEnvironment {

    fun getOS() : String = System.getProperty("os.name") ?: "Unknown"
    fun getJDKVersion(): String? = System.getProperty("java.version")

}