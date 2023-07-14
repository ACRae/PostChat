package isel.acrae.postchat.activity.postcard.draw

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.chat.ChatActivity
import isel.acrae.postchat.ui.theme.PostChatTheme

class DrawActivity : ComponentActivity() {

    private val templateDir by lazy {
        (application as PostChatApplication).templatesDir
    }


    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<DrawViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DrawViewModel() as T
            }
        }
    }

    companion object {
        private const val TEMPLATE_NAME  = "TEMPLATE_NAME"
        private const val CHAT_ID  = "CHAT_ID"
        fun navigate(origin: Activity, templateName: String, chatId: Int) {
            with(origin) {
                val intent = Intent(this, DrawActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putExtra(TEMPLATE_NAME, templateName)
                intent.putExtra(CHAT_ID, chatId)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)


        windowInsetsController.let { controller ->
            //hide status bar and navigation buttons
            controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val templatePath = templateDir + "/" +
                intent.getStringExtra(TEMPLATE_NAME) + ".svg"

        val templateName = intent.getStringExtra(TEMPLATE_NAME) ?: ""

        setContent {
            PostChatTheme {
                DrawScreen(
                    onSend = {
                        ChatActivity.navigate(
                            this, intent.getIntExtra(CHAT_ID, -1),
                            it, intent.getStringExtra(TEMPLATE_NAME)
                        )
                        finish()
                    },
                    pathPropertiesList = { vm.pathPropList },
                    onAddPath = { pathProps ->
                        vm.addPathProperties(pathProps)
                    },
                    onUndo =  { vm.undo() },
                    onRedo = { vm.redo() },
                    onClear = { vm.clear() },
                    onResetUndo = { vm.resetUndo() },
                    templatePath = templatePath,
                    templateName = templateName
                )
            }
        }
    }
}