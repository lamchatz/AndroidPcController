package lchat.pccontroller.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PC")
data class PC(
    @PrimaryKey
    val ip: String,
    val nickName: String
)
