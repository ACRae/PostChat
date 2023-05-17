package isel.acrae.postchat.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.LoginInput
import isel.acrae.postchat.service.Services
import kotlinx.coroutines.launch

class SignInViewModel(private val services: Services) : ViewModel() {

    private var _token by mutableStateOf<Result<String>?>(null)
    val token: Result<String>?
        get() = _token

    fun login(number: String, region: Int, password: String) {
        viewModelScope.launch {
            try {
                services.home.login(LoginInput(number, region, password))
            }catch (e : Exception) {
                Result.failure<Exception>(e)
            }
        }
    }
}