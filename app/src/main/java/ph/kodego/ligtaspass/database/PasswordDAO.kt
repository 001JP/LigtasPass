package ph.kodego.ligtaspass.database

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface PasswordDAO {

    @Insert
    suspend fun insert(password: PasswordEntity)
}