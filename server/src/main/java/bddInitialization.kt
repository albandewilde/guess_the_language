package utils

import java.sql.Connection
import java.sql.DriverManager

fun main(list : List<String>) {
    initBdd();
}

fun initBdd() {

    val url = utils.BDD_URL;

    try {
        var conn: Connection? = DriverManager.getConnection(url)

        if (conn != null) {

            var createTableReq = "CREATE IF NOT EXISTS users(\n" +
                    "id integer PRIMARY KEY AUTOINCREMENT, \n" +
                    "pseudo varchar(35) UNIQUE, \n" +
                    "level integer DEFAULT 0, \n" +
                    "points integer DEFAULT 0 \n" +
                    ");"

            var createTable = conn.prepareStatement(createTableReq)

            createTable.executeUpdate()

            conn.close()
        }
    } catch(e : Exception) {
        e.printStackTrace()
    }
}
