package isel.acrae.postchat.activity.signin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.acrae.postchat.Dependencies
import isel.acrae.postchat.PostChatApplication
import isel.acrae.postchat.activity.home.HomeActivity
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.token.TokenStorage
import isel.acrae.postchat.ui.theme.PostChatTheme

class SignInActivity : ComponentActivity() {

    private val services by lazy {
        (application as Dependencies).services
    }


    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<SignInViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SignInViewModel(services) as T
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenStorage = TokenStorage(applicationContext)
        val localToken = tokenStorage.getToken()

        if(localToken != null) {
            HomeActivity.navigate(this)
        }

        setContent {
            PostChatTheme {
                SignInScreen(
                    onLogin = { number, region, password ->
                        vm.login(number, region, password)
                        val token = vm.token
                        if(token != null && token.isSuccess) {
                            val tokenValue = token.getOrThrow()
                            tokenStorage.saveToken(tokenValue)
                            HomeActivity.navigate(this)
                        }
                    },

                    onRegister = { name, number, region, password ->
                        vm.register(name, number, region, password)
                        val token = vm.token
                        if(token != null && token.isSuccess) {
                            val tokenValue = token.getOrThrow()
                            tokenStorage.saveToken(tokenValue)
                            HomeActivity.navigate(this)
                        }
                    }
                )
            }
        }
    }
}