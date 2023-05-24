package isel.acrae.postchat.activity.chat.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.room.dao.UserDao
import isel.acrae.postchat.room.entity.UserEntity
import isel.acrae.postchat.service.Services
import kotlinx.coroutines.launch

class ChatCreateViewModel(
    private val services: Services,
    private val userDao: UserDao
) : ViewModel() {
    private var _users by mutableStateOf<Sequence<UserEntity>>(emptySequence())

    val users: Sequence<UserEntity>
        get() {
            getDbUsers()
            return _users
        }


    private fun getDbUsers() {
        viewModelScope.launch {
            _users = userDao.getAll().asSequence()
        }
    }
}