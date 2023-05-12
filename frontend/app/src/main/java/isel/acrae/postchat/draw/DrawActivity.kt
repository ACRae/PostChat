package isel.acrae.postchat.draw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class DrawActivity : ComponentActivity() {
    private val vm by viewModels<DrawViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.let { controller ->
            //hide status bar and navigation buttons
            controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        setContent {
            MaterialTheme {
                DrawScreen(
                    pathPropertiesList = { vm.pathPropList },
                    onAddPath = {
                        vm.addPathProperties(it)
                    },
                    onUndo =  { vm.undo() },
                    onRedo = { vm.redo() },
                    onClear = { vm.clear() },
                    onResetUndo = { vm.resetUndo() }
                )
            }
        }
    }
}