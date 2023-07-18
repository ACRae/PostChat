package isel.acrae.postchat.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val createdAt: String,
) : RoomEntity