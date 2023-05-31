package isel.acrae.postchat.activity.postcard.draw

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.home.HomeActivity
import isel.acrae.postchat.ui.theme.PostChatTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class DrawActivity : ComponentActivity() {
    private val vm by viewModels<DrawViewModel>()

    private val templateDir by lazy {
        (application as PostChatApplication).templatesDir
    }

    companion object {
        private const val TEMPLATE_NAME  = "TEMPLATE_NAME"
        fun navigate(origin: Activity, templateName: String) {
            with(origin) {
                val intent = Intent(this, DrawActivity::class.java)
                intent.putExtra(TEMPLATE_NAME, templateName)
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

        setContent {
            PostChatTheme {
                DrawScreen(
                    pathPropertiesList = { vm.pathPropList },
                    onAddPath = {pathProps ->
                        vm.addPathProperties(pathProps)
                    },
                    onUndo =  { vm.undo() },
                    onRedo = { vm.redo() },
                    onClear = { vm.clear() },
                    onResetUndo = { vm.resetUndo() },
                    templatePath = templateDir + "/" + intent.getStringExtra(TEMPLATE_NAME) + ".svg"
                )
            }
        }
    }
}