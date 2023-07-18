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
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.chat.ChatActivity
import isel.acrae.postchat.activity.chat.create.ChatCreateActivity
import isel.acrae.postchat.activity.perferences.TokenStorage
import isel.acrae.postchat.activity.settings.SettingsActivity
import isel.acrae.postchat.domain.ChatHolder
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

        (application as PostChatApplication).contacts =
            ContactUtils.getPhoneNumbers(applicationContext)

        vm.initialize(token)
        vm.getWebUsers(token, (application as PostChatApplication).contacts)

        setContent {
            PostChatTheme {
                HomeScreen(
                    getChats = {
                        val latestMess = vm.latestMessages
                        val latestMessId = latestMess.map { m -> m.chatTo }
                        val chats = vm.chats
                        chats.map {
                            if(latestMessId.contains(it.id))
                                ChatHolder.from(
                                    it.copy(createdAt = vm.latestMessages.first {
                                        m -> m.chatTo == it.id }.createdAt), true
                                )
                            else ChatHolder.from(it, false)
                        }
                   },
                    createChat = { ChatCreateActivity.navigate(this) },
                    onSettings = { SettingsActivity.navigate(this) },
                    onChat = { ChatActivity.navigate(this, it) }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (application as PostChatApplication).contacts =
            ContactUtils.getPhoneNumbers(applicationContext)

        val token = TokenStorage(applicationContext).getTokenOrThrow()
        vm.getWebUsers(token, (application as PostChatApplication).contacts)

        Log.i("LATEST MESSAGES", vm.latestMessages.map { Pair(it.createdAt, it.chatTo) }.toList().toString())
    }
}