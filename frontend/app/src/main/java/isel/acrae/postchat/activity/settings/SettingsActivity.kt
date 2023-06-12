package isel.acrae.postchat.activity.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.home.HomeViewModel
import isel.acrae.postchat.activity.info.InfoActivity
import isel.acrae.postchat.activity.perferences.TokenStorage
import isel.acrae.postchat.activity.postcard.PostcardActivity
import isel.acrae.postchat.activity.signin.SignInActivity

class SettingsActivity : ComponentActivity() {

    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<SettingsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(db) as T
            }
        }
    }

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val db by lazy {
        (application as PostChatApplication).db
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenStorage = TokenStorage(applicationContext)
        setContent {
            SettingsScreen (
                { InfoActivity.navigate(this) },
                {
                    tokenStorage.clearToken()
                    SignInActivity.navigate(this)
                },
                { vm.clearDb() }
            )
        }
    }
}