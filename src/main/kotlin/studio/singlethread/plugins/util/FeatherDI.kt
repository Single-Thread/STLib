package studio.singlethread.plugins.util

object FeatherDI {

    private val beans = mutableMapOf<Class<*>, Any>()

    fun <T : Any> register(clazz: Class<T>, instance: T) {
        beans[clazz] = instance
    }

    fun <T : Any> getBean(clazz: Class<T>): T {
        return beans[clazz] as T
    }

}