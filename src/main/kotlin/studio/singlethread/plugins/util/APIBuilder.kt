package studio.singlethread.plugins.util

import gg.flyte.twilight.Twilight
import gg.flyte.twilight.data.Redis
import gg.flyte.twilight.twilight
import studio.singlethread.plugins.api.STPlugin

class APIBuilder constructor(val plugin: STPlugin) {

    fun setRedis(authentication: Redis.Authentication,host:String,port: Int,timeout: Int,username: String,password: String,url: String) : APIBuilder {

//        authentication: USERNAME_PASSWORD
//        host: "host"
//        port: 6379 # Default Redis Port
//        timeout: 500 # 500 Milliseconds Timeout
//        username: "username"
//        password: "password"
//        url: "url"


        return this
    }

    fun build() : Twilight {

        return twilight(plugin) {
        }

    }

}