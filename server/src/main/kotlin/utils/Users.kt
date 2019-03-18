package utils

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "users")
class Users {
        @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
        var id = -1
        @DatabaseField
        lateinit var pseudo: String
        @DatabaseField
        var level = 0
        @DatabaseField
        var points = 0
}
