package isel.acrae.postchat.activity.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.chat.info.ChatInfoActivity
import isel.acrae.postchat.activity.perferences.PhoneNumberStorage
import isel.acrae.postchat.activity.perferences.TokenStorage
import isel.acrae.postchat.activity.postcard.PostcardActivity
import isel.acrae.postchat.activity.postcard.draw.DrawActivity
import isel.acrae.postchat.ui.theme.PostChatTheme
import isel.acrae.postchat.utils.isDone
import java.io.File
import java.sql.Timestamp

class ChatActivity : ComponentActivity() {

    data class TemplateHolder(
        val path: String, val name:String
    )

    companion object {
        private const val CHAT_ID = "chatId"
        private const val MESSAGE_PATH = "messagePath"
        private const val TEMPLATE_NAME = "templateName"
        fun navigate(origin: Activity, chatId: Int, messagePath: String? = null, templateName: String? = null) {
            with(origin) {
                val intent = Intent(this, ChatActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra(CHAT_ID, chatId)
                intent.putExtra(TEMPLATE_NAME, templateName)
                intent.putExtra(MESSAGE_PATH, messagePath)
                startActivity(intent)
            }
        }
    }

    private val services by lazy {
        (application as PostChatApplication).services
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

    private val saveMessage by lazy {
        (application as PostChatApplication).saveMessageFile
    }


    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<ChatViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatViewModel(
                    services, db.messageDao(),
                    db.chatDao(), saveMessage
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatId = intent.getIntExtra(CHAT_ID, -1)
        assert(chatId != -1) { "Illegal state, chatId must be valid" }
        vm.initialize(chatId)

        val token = TokenStorage(applicationContext).getTokenOrThrow()

        val templatesPaths =  File(templatesDir).listFiles()?.filter {
            it.extension == "svg" }?.map {
            TemplateHolder(it.absolutePath, it.nameWithoutExtension)
            } ?: emptyList()

        val phoneNumberStorage = PhoneNumberStorage(applicationContext)

        setContent {
            var messagePath by remember { mutableStateOf(intent.getStringExtra(MESSAGE_PATH)) }
            var template by remember { mutableStateOf(intent.getStringExtra(TEMPLATE_NAME)) }
            val chat = vm.chat
            if(chat != null) {
                PostChatTheme {
                    ChatScreen(
                        phoneNumberStorage.getPhoneNumber(),
                        messagesDir,
                        getMessages = { vm.messages },
                        messagePath,
                        chat = chat,
                        templates = templatesPaths,
                        templateName = template,
                        onEdit = {
                            DrawActivity.navigate(this, it, chatId)
                        },
                        onPostcardClick = { path, id ->
                            PostcardActivity.navigate(this, path, id)
                        },
                        onSendMessage = { t, path ->
                            val timestamp = Timestamp(System.currentTimeMillis())
                            val done= vm.sendMessage(token, t, path, chatId, timestamp)
                            messagePath = null
                            template = null
                            done.isDone(this) { vm.initialize(chatId) }
                        },
                        onInfo = { ChatInfoActivity.navigate(this, chatId) }
                    )
                }
            }
        }
    }

}