package isel.acrae.postchat.activity.postcard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.ui.theme.PostChatTheme

class PostcardActivity : ComponentActivity() {

    private val messageDir by lazy {
        (application as PostChatApplication).messageDir
    }


    companion object {
        private const val POSTCARD_NAME  = "POSTCARD_NAME"
        fun navigate(origin: Activity, path: String) {
            with(origin) {
                val intent = Intent(this, PostcardActivity::class.java)
                intent.putExtra(POSTCARD_NAME, path)
                startActivity(intent)
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

        setContent {
            PostChatTheme {
                PostCardScreen(
                    path = "$messageDir/${intent.getStringExtra(POSTCARD_NAME)}"
                )
            }
        }
    }
}