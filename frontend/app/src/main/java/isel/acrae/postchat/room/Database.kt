package isel.acrae.postchat.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import isel.acrae.postchat.room.dao.ChatDao
import isel.acrae.postchat.room.dao.MessageDao
import isel.acrae.postchat.room.dao.TemplateDao
import isel.acrae.postchat.room.dao.UserDao
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.room.entity.TemplateEntity
import isel.acrae.postchat.room.entity.UserEntity

@Database(
    entities = [
        UserEntity::class, ChatEntity::class,
        TemplateEntity::class, MessageEntity::class,
    ],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun templateDao(): TemplateDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}