package isel.acrae.postchat.activity.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.CreateUserInput
import isel.acrae.postchat.domain.LoginInput
import isel.acrae.postchat.service.Services
import kotlinx.coroutines.launch

class SignInViewModel(
    private val services: Services
) : ViewModel() {

    private var _token by mutableStateOf<Result<String>?>(null)
    val token: Result<String>?
        get() = _token

    fun login(number: String, region: Int, password: String) {
        viewModelScope.launch {
            _token = try {
                Result.success(
                    services.home.login(LoginInput(number, region, password))
                )
            } catch (e : Exception) {
                Result.failure(e)
            }
        }
    }

    fun register(name: String, number: String, region: Int, password: String) {
        viewModelScope.launch {
            _token = try {
                Result.success(
                    services.home.register(CreateUserInput(name, number, region, password))
                )
            } catch (e : Exception) {
                Result.failure(e)
            }
        }
    }
}