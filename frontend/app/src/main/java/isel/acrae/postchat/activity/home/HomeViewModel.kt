package isel.acrae.postchat.activity.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.CreateChatInput
import isel.acrae.postchat.room.dao.ChatDao
import isel.acrae.postchat.room.dao.MessageDao
import isel.acrae.postchat.room.dao.UserDao
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.web.mapper.EntityMapper
import kotlinx.coroutines.launch
import java.sql.Timestamp

class HomeViewModel(
    private val services: Services,
    private val userDao: UserDao,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) : ViewModel() {
    private var _chats by mutableStateOf<Sequence<ChatEntity>>(emptySequence())
    val chats: Sequence<ChatEntity>
        get() {
            getDbChats()
            return _chats
        }

    private var _messages by mutableStateOf<Sequence<MessageEntity>>(emptySequence())
    val messages: Sequence<MessageEntity>
        get() = _messages

    fun initialize(token: String, users: List<String>) {
        getWebChats(token)
        getWebMessages(token)
        getWebUsers(token, users)
    }

    private fun getWebChats(token : String) {
        viewModelScope.launch {
            val res = try {
                Result.success(services.chat.getChats(token))
            }catch (e : Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {
                chatDao.insertAll(
                    EntityMapper.fromChatList(res.getOrThrow().list)
                )
            }
        }
    }


    private fun getWebUsers(
        token : String, users: List<String>
    ) {
        viewModelScope.launch {
            val res = try {
                Result.success(
                    services.user.getUsers(
                    token, users
                ))
            }catch (e : Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {
                userDao.insertAll(
                    EntityMapper.fromUserInfoList(res.getOrThrow().list)
                )
            }
        }
    }

    private fun getWebMessages(token: String) {
        viewModelScope.launch {
            val res = try {
                Result.success(services.chat.getMessages(token))
            }catch (e : Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {
                messageDao.insertAll(EntityMapper.fromMessageList(res.getOrThrow().list))
            }
        }
    }

    fun getDbChats() {
        viewModelScope.launch {
            _chats = chatDao.getAll().asSequence()
        }
    }

    fun getDbMessages() {
        viewModelScope.launch {
            _messages =  messageDao.getAll().asSequence()
        }
    }


    fun createChat(chatName: String, phoneNumbers: List<String>, token: String) {
        viewModelScope.launch {
            services.chat.createChat(token, CreateChatInput(
                phoneNumbers, chatName, Timestamp(System.currentTimeMillis())
            ))
        }
    }


}