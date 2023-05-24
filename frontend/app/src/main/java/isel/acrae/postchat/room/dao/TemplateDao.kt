package isel.acrae.postchat.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import isel.acrae.postchat.room.entity.TemplateEntity

@Dao
interface TemplateDao : RoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(templateEntity: TemplateEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<TemplateEntity>)

    @Query("SELECT * FROM TEMPLATE")
    suspend fun getAll() : List<TemplateEntity>

    @Query("SELECT * FROM TEMPLATE LIMIT :limit OFFSET :offset")
    suspend fun getAllPaginated(limit : Int, offset: Int) : List<TemplateEntity>
}
