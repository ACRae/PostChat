package isel.acrae.postchat

import android.app.Application
import androidx.room.Room
import isel.acrae.postchat.room.AppDatabase
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.mock.MockServices
import isel.acrae.postchat.service.web.WebServices
import isel.acrae.postchat.token.TokenStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

interface Dependencies {
    val services: Services
}

class PostChatApplication : Dependencies, Application() {

    private lateinit var tokenStorage: TokenStorage

    val db : AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }


    override fun onCreate() {
        super.onCreate()
        tokenStorage = TokenStorage(this)
        //tokenStorage.clearToken()
        CoroutineScope(Dispatchers.IO).launch {
            db.clearAllTables()
        }
    }

    private val baseUrl = "http://localhost:9000/api/v1"
    private val httpClient: OkHttpClient by lazy { OkHttpClient() }


    private val servicePair = Pair(
        MockServices(),
        WebServices(baseUrl, httpClient)
    )

    override val services = servicePair.first
}