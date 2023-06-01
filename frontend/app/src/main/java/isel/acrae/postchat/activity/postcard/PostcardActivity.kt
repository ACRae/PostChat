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

    companion object {
        private const val POSTCARD_DIR  = "POSTCARD_DIR"
        fun navigate(origin: Activity, path: String) {
            with(origin) {
                val intent = Intent(this, PostcardActivity::class.java)
                intent.putExtra(POSTCARD_DIR, path)
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
                    path = intent.getStringExtra(POSTCARD_DIR) ?: ""
                )
            }
        }
    }
}