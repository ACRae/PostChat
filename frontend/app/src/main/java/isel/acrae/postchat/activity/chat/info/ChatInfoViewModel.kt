package isel.acrae.postchat.activity.chat.info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.service.Services
import kotlinx.coroutines.launch

class ChatInfoViewModel(
    private val services: Services,
) : ViewModel() {
    private var _chatInfo by mutableStateOf<Result<ChatInfo>?>(null)

    val chatInfo: ChatInfo?
        get() = _chatInfo?.getOrNull()


    fun getWebUserInfo(token: String, chatId: Int) {
        viewModelScope.launch {
            _chatInfo = try {
                Result.success(
                    services.chat.getChatInfo(token, chatId)
                )
            } catch (e : Exception) {
                Result.failure(e)
            }
        }
    }

}