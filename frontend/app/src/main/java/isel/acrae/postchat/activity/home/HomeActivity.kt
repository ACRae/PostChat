package isel.acrae.postchat.activity.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.acrae.postchat.Dependencies
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.settings.SettingsActivity
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.token.TokenStorage
import isel.acrae.postchat.ui.theme.PostChatTheme

class HomeActivity : ComponentActivity() {

    private val services by lazy {
        (application as Dependencies).services
    }

    private val db by lazy {
        (application as PostChatApplication).db
    }

    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<HomeViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    services, db.userDao(),
                    db.chatDao(), db.messageDao()
                ) as T
            }
        }
    }

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = TokenStorage(applicationContext).getToken()!!
        vm.initialize(token)

        setContent {
            PostChatTheme {
                HomeScreen(
                    getChats = { vm.chats ?: emptySequence() },
                    getMessages = { vm.messages ?: emptySequence() },
                    createChat = { s, l -> vm.createChat(s, l, token) },
                    onSettings = { SettingsActivity.navigate(this) }
                )
            }
        }
    }
}