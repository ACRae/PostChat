package isel.acrae.postchat.activity.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.CreateUserInput
import isel.acrae.postchat.domain.LoginInput
import isel.acrae.postchat.room.dao.UserDao
import isel.acrae.postchat.room.entity.UserEntity
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.web.mapper.EntityMapper
import kotlinx.coroutines.launch

class SignInViewModel(
    private val services: Services,
    private val userDao: UserDao,
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

    fun saveUser(token: String, number: String, region: Int) {
        viewModelScope.launch {
            val res = try {
                Result.success(
                    services.user.getUser(token, region.toString() + number)
                )
            } catch (e : Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {
                userDao.insert(EntityMapper.fromUserInfo(res.getOrThrow()))
            }
        }
    }
}