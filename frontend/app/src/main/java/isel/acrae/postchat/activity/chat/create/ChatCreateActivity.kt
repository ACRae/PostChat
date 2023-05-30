package isel.acrae.postchat.activity.chat.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import isel.acrae.postchat.Dependencies
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.chat.ChatActivity
import isel.acrae.postchat.token.TokenStorage
import isel.acrae.postchat.ui.theme.PostChatTheme

class ChatCreateActivity : ComponentActivity() {

    private val services by lazy {
        (application as Dependencies).services
    }

    private val db by lazy {
        (application as PostChatApplication).db
    }

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, ChatCreateActivity::class.java)
                startActivity(intent)
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<ChatCreateViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatCreateViewModel(
                    services, db.userDao(), db.chatDao()
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = TokenStorage(applicationContext).getTokenOrThrow()
        setContent {
            PostChatTheme {
                ChatCreateScreen(
                    getUsers = { vm.users },
                    createChat = { s, u ->
                        val chatId = vm.createChat(s,u,token)
                        chatId.observe(this) {
                            if(it != null) {
                                ChatActivity.navigate(this, it)
                            }
                        }
                    }
                )
            }
        }
    }

}