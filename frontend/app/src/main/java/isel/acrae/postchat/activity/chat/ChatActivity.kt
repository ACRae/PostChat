package isel.acrae.postchat.activity.chat

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
import isel.acrae.postchat.activity.chat.create.ChatCreateViewModel
import isel.acrae.postchat.activity.postcard.draw.DrawActivity
import isel.acrae.postchat.activity.perferences.UserStorage
import isel.acrae.postchat.activity.postcard.PostcardActivity
import isel.acrae.postchat.ui.theme.PostChatTheme
import java.io.File

class ChatActivity : ComponentActivity() {

    data class TemplateHolder(
        val path: String, val name:String
    )

    companion object {
        private const val CHAT_ID = "chatId"
        fun navigate(origin: Activity, chatId: Int) {
            with(origin) {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra(CHAT_ID, chatId)
                startActivity(intent)
            }
        }
    }

    private val services by lazy {
        (application as Dependencies).services
    }

    private val db by lazy {
        (application as PostChatApplication).db
    }

    private val templatesDir by lazy {
        (application as PostChatApplication).templatesDir
    }

    private val messagesDir by lazy {
        (application as PostChatApplication).messageDir
    }


    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<ChatViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatViewModel(services, db.messageDao(), db.chatDao()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatId = intent.getIntExtra(CHAT_ID, -1)
        assert(chatId != -1) { "Illegal state, chatId must be valid" }
        vm.initialize(chatId)

        val templatesPaths =  File(templatesDir).listFiles()?.filter {
            it.extension == "svg" }?.map {
            TemplateHolder(it.absolutePath, it.nameWithoutExtension)
            } ?: emptyList()

        val userStorage = UserStorage(applicationContext)

        setContent {
            val chat = vm.chat
            if(chat != null) {
                PostChatTheme {
                    ChatScreen(
                        userStorage.getPhoneNumber(),
                        messagesDir,
                        getMessages = { vm.messages }, chat = chat,
                        templatesPaths,
                        { DrawActivity.navigate(this, it) },
                        { PostcardActivity.navigate(this, it)}
                    )
                }
            }
        }
    }

}