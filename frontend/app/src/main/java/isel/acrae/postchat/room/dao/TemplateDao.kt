package isel.acrae.postchat.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import isel.acrae.postchat.room.entity.TemplateEntity

@Dao
interface TemplateDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(templateEntity: TemplateEntity)
}
