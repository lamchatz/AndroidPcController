package lchat.pccontroller.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PC")
data class PC(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ip: String,
    val port: String,
    val nickName: String,
    val selected: Boolean
)
