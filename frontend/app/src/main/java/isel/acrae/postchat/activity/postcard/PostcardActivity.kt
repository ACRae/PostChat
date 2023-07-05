package isel.acrae.postchat.activity.postcard

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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.acrae.postchat.Dependencies
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.perferences.TokenStorage
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.ui.theme.PostChatTheme
import isel.acrae.postchat.utils.isDone
import isel.acrae.postchat.utils.handleError

class PostcardActivity : ComponentActivity() {

    companion object {
        private const val POSTCARD_DIR  = "POSTCARD_DIR"
        private const val MESSAGE_ID = "MESSAGE_ID_POSTCARD_ACT"
        fun navigate(origin: Activity, path: String, messageId: Int) {
            with(origin) {
                val intent = Intent(this, PostcardActivity::class.java)
                intent.putExtra(POSTCARD_DIR, path)
                intent.putExtra(MESSAGE_ID, messageId)
                startActivity(intent)
            }
        }
    }


    private val imagesDir by lazy {
        (application as PostChatApplication).imagesDir
    }

    private val services by lazy {
        (application as Dependencies).services
    }

    private val db by lazy {
        (application as PostChatApplication).db
    }

    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<PostcardViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PostcardViewModel(services, db.messageDao()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        val id = intent.getIntExtra(MESSAGE_ID, -1)
        val token = TokenStorage(applicationContext).getTokenOrThrow()
        vm.getDbMessage(id)

        setContent {
            val message = vm.message
            val handwrittenInput = if(message != null) {
                HandwrittenInput(message.handwrittenContent)
            }  else null

            var htrText by remember { mutableStateOf("") }

            PostChatTheme {
                PostCardScreen(
                    isSent = id != -1 && handwrittenInput != null,
                    path = intent.getStringExtra(POSTCARD_DIR) ?: "",
                    imagesDir,
                    htrText,
                ) {
                    vm.htr(token, handwrittenInput!!).isDone(this) {
                        vm.htrMessage?.handleError(applicationContext, {
                            htrText = it
                        })
                    }
                }
            }
        }
    }
}