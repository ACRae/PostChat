package isel.acrae.postchat.activity.chat.info

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.acrae.postchat.Dependencies
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.perferences.TokenStorage
import isel.acrae.postchat.activity.signin.SignInViewModel
import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.ui.theme.PostChatTheme

class ChatInfoActivity : ComponentActivity() {

    private val services by lazy {
        (application as Dependencies).services
    }

    private val db by lazy {
        (application as PostChatApplication).db
    }

    companion object {
        private const val CHAT_ID = "ChatInfoActivity" + "chatId"
        fun navigate(origin: Activity, chatId: Int) {
            with(origin) {
                val intent = Intent(this, ChatInfoActivity::class.java)
                intent.putExtra(CHAT_ID, chatId)
                startActivity(intent)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<ChatInfoViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatInfoViewModel(services) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = TokenStorage(applicationContext).getTokenOrThrow()
        vm.getWebUserInfo(token, intent.getIntExtra(CHAT_ID, -1))
        setContent {
            PostChatTheme {
                val chatInfo = vm.chatInfo
                if(chatInfo != null) {
                    ChatInfoScreen(
                        getChatProps = { chatInfo.props },
                        getUserInfo = { chatInfo.usersInfo.asSequence() }
                    )
                }
            }
        }

    }




}