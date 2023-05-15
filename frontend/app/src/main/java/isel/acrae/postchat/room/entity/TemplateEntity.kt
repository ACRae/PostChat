package isel.acrae.postchat.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "template")
data class TemplateEntity(
    @PrimaryKey
    val name: String,
    val content: String
)

