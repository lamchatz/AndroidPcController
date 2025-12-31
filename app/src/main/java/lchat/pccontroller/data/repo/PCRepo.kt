package lchat.pccontroller.data.repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lchat.pccontroller.data.PC

@Dao
interface PCRepo {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pc: PC)

    @Query("Select * FROM PC")
    suspend fun getAll(): List<PC>

    @Query("DELETE FROM PC WHERE ip = :ip")
    suspend fun deleteByIp(ip: String)

    @Query("Select * from PC where selected = true limit 1")
    fun observeSelectedPC(): Flow<PC?>
}