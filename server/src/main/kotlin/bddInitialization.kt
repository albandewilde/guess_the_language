package utils

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource
import java.sql.Connection
import java.sql.DriverManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import java.sql.PreparedStatement
import java.sql.SQLException

fun main(list: List<String>) {
    initBdd()
}

fun initBdd() {
    try {
        var source : ConnectionSource = JdbcConnectionSource(utils.BDD_URL)

        try {
            TableUtils.createTableIfNotExists(source, Users::class.java)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    } catch(e : Exception) {
        e.printStackTrace()
    }
}
