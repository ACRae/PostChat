package isel.acrae.postchat.activity.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.Chat
import isel.acrae.postchat.room.AppDatabase
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.web.mapper.EntityMapper
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val db : AppDatabase,
    private val services: Services
    ) : ViewModel() {
    private var _messages by mutableStateOf<List<MessageEntity>>(emptyList())
    val messages: List<MessageEntity>
        get() = _messages


    private var _chats by mutableStateOf<List<ChatEntity>>(emptyList())
    val chats: List<ChatEntity>
        get() = _chats


    private var _webChats by mutableStateOf<List<Chat>>(emptyList())
    val webChats: List<Chat>
        get() = _webChats



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

    fun getWebChats(token: String) {
        viewModelScope.launch {
            _webChats = try {
                services.chat.getChats(token).list
            }catch (e : Exception) {
                emptyList()
            }
        }
    }

    fun deleteUserWeb(token: String) : MutableLiveData<Boolean> {
        val done = MutableLiveData(false)
        viewModelScope.launch {
            try {
                services.user.deleteUser(token)
            }catch (e : Exception) {
                throw e
            }
            done.value = true
        }
        return done
    }

}