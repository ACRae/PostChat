package isel.acrae.postchat.activity.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.Chat
import isel.acrae.postchat.room.AppDatabase
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import kotlinx.coroutines.launch

class SettingsViewModel(private val db : AppDatabase) : ViewModel() {
    private var _messages by mutableStateOf<List<MessageEntity>>(emptyList())
    val messages: List<MessageEntity>
        get() = _messages


    private var _chats by mutableStateOf<List<ChatEntity>>(emptyList())
    val chats: List<ChatEntity>
        get() = _chats


    fun clearDb() {
        viewModelScope.launch {
            db.clearAllTables()
        }
    }

    fun listMessages() {
        viewModelScope.launch {
            _messages =  db.messageDao().getAll()
        }
    }

    fun listChats() {
        viewModelScope.launch {
            _chats =  db.chatDao().getAll()
        }
    }
}