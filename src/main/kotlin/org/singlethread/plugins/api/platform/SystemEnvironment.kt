package org.singlethread.plugins.api.platform

import org.singlethread.plugins.api.event.STListener

object SystemEnvironment {

    fun getOS() : String = System.getProperty("os.name") ?: "Unknown"
    fun getJDKVersion(): String? = System.getProperty("java.version")

}