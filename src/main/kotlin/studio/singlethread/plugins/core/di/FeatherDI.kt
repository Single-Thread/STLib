package studio.singlethread.plugins.core.di

object FeatherDI {

    private val beans = mutableMapOf<Class<*>, Any>()

    fun <T : Any> register(clazz: Class<T>, instance: T) {
        beans[clazz] = instance
    }
    fun <T : Any> register(clazz: Class<T>) {
        beans[clazz] = clazz.getDeclaredConstructor().newInstance()
    }

    fun <T : Any> getBean(clazz: Class<T>): T {
        return beans[clazz] as T
    }

}