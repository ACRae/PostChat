package isel.acrae.postchat.activity.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.info.InfoActivity
import isel.acrae.postchat.activity.perferences.TokenStorage
import isel.acrae.postchat.activity.signin.SignInActivity
import isel.acrae.postchat.ui.theme.PostChatTheme
import isel.acrae.postchat.utils.isDone
import java.io.File

class SettingsActivity : ComponentActivity() {

    private val db by lazy {
        (application as PostChatApplication).db
    }

    private val services by lazy {
        (application as PostChatApplication).services
    }

    private val templatesDir by lazy {
        (application as PostChatApplication).templatesDir
    }

    private val messagesDir by lazy {
        (application as PostChatApplication).messageDir
    }


    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<SettingsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(db, services) as T
            }
        }
    }

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenStorage = TokenStorage(applicationContext)
        vm.listMessages()
        vm.listChats()
        vm.listUsers()
        vm.getWebChats(tokenStorage.getTokenOrThrow())
        setContent {
            PostChatTheme {
                SettingsScreen(
                    { InfoActivity.navigate(this) },
                    {
                        File(templatesDir).deleteRecursively()
                        File(messagesDir).deleteRecursively()
                        SignInActivity.navigate(this)
                        tokenStorage.clearToken()
                        vm.clearDb()
                    },
                    { vm.clearDb() },
                    {
                        vm.messages.map { Pair(it.id, it.fileName) }.toString()
                    },
                    {
                        vm.chats.map { it.name }.toString()
                    },
                    {
                        vm.webChats.map { Pair(it.name, it.createdAt) }.toString()
                    },
                    {
                        vm.deleteUserWeb(tokenStorage.getTokenOrThrow()).isDone(this) {
                            SignInActivity.navigate(this)
                            tokenStorage.clearToken()
                            vm.clearDb()
                        }
                    },
                    { vm.users.toString() }
                )
            }
        }
    }
}