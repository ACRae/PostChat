package isel.acrae.postchat.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "chat")
data class ChatEntity(
    @PrimaryKey
    val id: Int,
    val name: String?,
    val createdAt: Timestamp
) : RoomEntity