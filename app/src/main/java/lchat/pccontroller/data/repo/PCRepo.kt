package lchat.pccontroller.data.repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lchat.pccontroller.data.PC

@Dao
interface PCRepo {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pc: PC)

    @Query("Select * FROM PC")
    suspend fun getAll(): List<PC>

    @Query("DELETE FROM PC WHERE ip = :ip")
    suspend fun deleteByIp(ip: String)
}