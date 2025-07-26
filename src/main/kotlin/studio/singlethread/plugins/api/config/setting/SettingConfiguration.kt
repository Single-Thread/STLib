package studio.singlethread.plugins.api.config.setting

import studio.singlethread.plugins.api.STPlugin
import studio.singlethread.plugins.api.config.STConfiguration

class SettingConfiguration(plugin: STPlugin) : STConfiguration<STPlugin>(plugin, "setting.yml", 1) {
    private var verbose = false
    private var listener = true
    private var welcome = true
    private var prefix: String = ""
    //private var locale: String?
    //private var storage: StorageType = StorageType.JSON

    init {
        //this.locale = plugin.getLanguages().getFirst()
        setup(this)
    }

    private fun init() {
        verbose = getBoolean(
            "verbose", verbose, """
                Debug target
                디버그 옵션입니다
                """.trimIndent()
        )
        listener = getBoolean(
            "listener", listener, """
                When disabled, event listeners will be deactivated
                비활성화할시 이벤트 리스너가 비활성화됩니다
                """.trimIndent()
        )
        welcome = getBoolean(
            "welcome", welcome, """
                Controls whether to send MOTD to all players
                If disabled, MOTD will only be sent to operators
                MOTD 메세지를 플레이어에게 전송할지 조정합니다
                비활성화할시 관리자에게만 전송합니다
                """.trimIndent()
        )
        prefix = getString(
            "prefix", prefix, """
                Plugin message prefix. If left empty, the default prefix will be used.
                시스템 메세지의 접두사입니다. 비워두면 내장 접두사를 사용합니다.
                """.trimIndent()
        )
        //locale = getString(
        //    "locale", locale, """
        //        Default locale for messages and commands. Custom locale files can be created.
        //        Built-in locales: en_us, ko_kr
        //        메세지 및 명령어 기본 언어, 새로운 언어 파일을 만들 수 있습니다
        //        내장된 언어: en_us, ko_kr
        //        """.trimIndent()
        //)
        //storage = StorageType.getType(
        //    getString(
        //        "storage", storage.name(), """
        //        Data storage format. Available options: JSON, MONGODB, MYSQL, MARIADB
        //        데이터 저장 포멧. 사용 가능한 포멧: JSON, MONGODB, MYSQL, MARIADB
        //        """.trimIndent()
        //    )
        //)
    }

}