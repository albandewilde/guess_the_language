package utils

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "users")
class Users {
        @DatabaseField(unique = true, id = true)
        lateinit var pseudo: String

        @DatabaseField
        var level = 0

        @DatabaseField
        var points = 0
}
