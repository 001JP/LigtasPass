package ph.kodego.ligtaspass.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password-table")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var emailUsername: String = "",
    var password: String = ""
)