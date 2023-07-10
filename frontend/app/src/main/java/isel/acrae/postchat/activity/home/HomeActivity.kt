package isel.acrae.postchat.activity.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.chat.ChatActivity
import isel.acrae.postchat.activity.chat.create.ChatCreateActivity
import isel.acrae.postchat.activity.settings.SettingsActivity
import isel.acrae.postchat.activity.perferences.TokenStorage
import isel.acrae.postchat.ui.theme.PostChatTheme
import isel.acrae.postchat.utils.contacts.ContactUtils

class HomeActivity : ComponentActivity() {

    private val services by lazy {
        (application as PostChatApplication).services
    }

    private val db by lazy {
        (application as PostChatApplication).db
    }

    private val saveTemplate by lazy {
        (application as PostChatApplication).saveTemplateFile
    }

    private val saveMessage by lazy {
        (application as PostChatApplication).saveMessageFile
    }

    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<HomeViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    services, db.userDao(),
                    db.chatDao(), db.messageDao(),
                    db.templateDao(), saveTemplate,
                    saveMessage
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
        val token = TokenStorage(applicationContext).getTokenOrThrow()
        (application as PostChatApplication).contacts = ContactUtils.getPhoneNumbers(applicationContext)

        vm.initialize(token, (application as PostChatApplication).contacts)

        setContent {
            PostChatTheme {
                rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        (application as PostChatApplication).contacts =
                            ContactUtils.getPhoneNumbers(applicationContext)

                    } else {
                        finish()
                    }
                }
                HomeScreen(
                    getChats = { vm.chats },
                    //getMessages = { vm.messages },
                    createChat = { ChatCreateActivity.navigate(this) },
                    onSettings = { SettingsActivity.navigate(this) },
                    onChat = { ChatActivity.navigate(this, it) }
                )
            }
        }
    }
}