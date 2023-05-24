package isel.acrae.postchat.activity.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import isel.acrae.postchat.Dependencies
import isel.acrae.postchat.activity.home.HomeActivity
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

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, SignInActivity::class.java)
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                )
                startActivity(intent)
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