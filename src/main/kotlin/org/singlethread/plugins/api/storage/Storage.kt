package org.singlethread.plugins.api.storage

import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.io.File

object Storage {

    enum class Type(driver: String, url: String = "") {
        SQLITE("org.sqlite.JDBC","jdbc:sqlite:{path}"),
        MYSQL("com.mysql.cj.jdbc.Driver","jdbc:mysql://{host}:{port}/{database}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC")
    }

    fun <T> get(table: Table, where: Pair<Column<T>, T>): ResultRow? {
        return transaction {
            table.select { where.first eq where.second }.singleOrNull()
        }
    }

    fun <W, V> set(table: Table, where: Pair<Column<W>, W>?, target: Pair<Column<V>, V>?) {
        transaction {
            when {
                where != null && target != null -> {
                    val exists = table.select { where.first eq where.second }.any()
                    if (exists) {
                        table.update({ where.first eq where.second }) { it[target.first] = target.second }
                    } else {
                        table.insert {
                            it[where.first] = where.second
                            it[target.first] = target.second
                        }
                    }
                }
                where != null && target == null -> { table.deleteWhere { where.first eq where.second } }
                where == null && target == null -> { table.deleteAll() }
                else -> { error("Invalid usage: where=null & target!=null 은 허용되지 않음") }
            }
        }
    }

    fun add(table: Table, vararg values: Pair<Column<*>, Any>) {
        transaction {
            table.insert {
                values.forEach { (col, value) ->
                    @Suppress("UNCHECKED_CAST")
                    it[col as Column<Any>] = value as Any
                }
            }
        }
    }

    fun init(
        type: Storage.Type,
        plugin: JavaPlugin,
        vararg tables: Table,
        host: String = "localhost",
        port: Int = 3306,
        database: String = "plugin",
        username: String = "root",
        password: String = "password"
    ) {
        val dbUrl = when (type) {
            Storage.Type.SQLITE -> {
                val dbFile = File(plugin.dataFolder, "data.db")
                "jdbc:sqlite:${dbFile.absolutePath}"
            }
            Storage.Type.MYSQL -> {
                "jdbc:mysql://$host:$port/$database?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
            }
        }

        val driver = when (type) {
            Storage.Type.SQLITE -> "org.sqlite.JDBC"
            Storage.Type.MYSQL -> "com.mysql.cj.jdbc.Driver"
        }

        Database.connect(
            url = dbUrl,
            driver = driver,
            user = if (type == Storage.Type.MYSQL) username else "",
            password = if (type == Storage.Type.MYSQL) password else ""
        )

        transaction {
            SchemaUtils.create(*tables)
        }
    }

}