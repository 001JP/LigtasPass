package ph.kodego.ligtaspass.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDAO {

    @Insert
    suspend fun insert(password: PasswordEntity)

    @Query("SELECT * FROM `password-table` ORDER BY id DESC")
    fun fetchAllRecords(): Flow<List<PasswordEntity>>
}