package isel.acrae.postchat.activity.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import isel.acrae.postchat.activity.home.HomeActivity
import isel.acrae.postchat.activity.perferences.IpStorage
import isel.acrae.postchat.activity.perferences.TokenStorage
import isel.acrae.postchat.activity.perferences.PhoneNumberStorage
import isel.acrae.postchat.ui.theme.PostChatTheme
import isel.acrae.postchat.utils.contacts.ContactUtils
import isel.acrae.postchat.utils.handleError
import isel.acrae.postchat.utils.isDone

class SignInActivity : ComponentActivity() {

    private val services by lazy {
        (application as PostChatApplication).services
    }

    private val db by lazy {
        (application as PostChatApplication).db
    }

    private val setUrl by lazy {
        (application as PostChatApplication).setup
    }


    @Suppress("UNCHECKED_CAST")
    private val vm by viewModels<SignInViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SignInViewModel(services, db.userDao()) as T
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
        val phoneNumberStorage = PhoneNumberStorage(applicationContext)

        val localToken = tokenStorage.getToken()

        if(localToken != null) {
            HomeActivity.navigate(this)
        }

        fun op(region: Int, number: String) {
            vm.token?.handleError(
                applicationContext, onSuccess = {
                phoneNumberStorage.savePhoneNumber(region.toString() + number)
                tokenStorage.saveToken(it)
                vm.saveUser(it, number, region)
                HomeActivity.navigate(this)
            })
        }

        val serverIp = IpStorage(applicationContext)

        setContent {
            var ipValue by remember { mutableStateOf("10.0.2.2") }
            PostChatTheme {
                SignInScreen(
                    onLogin = { number, region, password ->
                        vm.login(number, region, password).isDone(this){
                            op(region, number)
                        }
                    },
                    onRegister = { name, number, region, password ->
                        vm.register(name, number, region, password).isDone(this) {
                            op(region, number)
                        }
                    },
                    onConfigIp = {
                        serverIp.saveIp(ipValue.trim())
                        setUrl(ipValue.trim())
                    },
                    onIpChange = { ipValue = it },
                    onLocal = {
                        serverIp.clearIp()
                        setUrl(null)
                    },
                    ipValue
                )
            }
        }
    }
}