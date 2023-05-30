package isel.acrae.postchat

import android.app.Application
import isel.acrae.postchat.room.AppDatabase
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.mock.MockServices
import isel.acrae.postchat.service.web.WebServices
import isel.acrae.postchat.token.TokenStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.io.File

interface Dependencies {
    val services: Services
}

class PostChatApplication : Dependencies, Application() {

    private lateinit var tokenStorage: TokenStorage
    lateinit var templatesDir: String

    val db : AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    var contacts : List<String> = emptyList()
    override fun onCreate() {
        super.onCreate()
        templatesDir = applicationContext.filesDir.absolutePath + "/templates"
        File(templatesDir).mkdir()
        tokenStorage = TokenStorage(this)
        //tokenStorage.clearToken()
        CoroutineScope(Dispatchers.Default).launch {
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

    val saveTemplateFile = fun(bytes: ByteArray, name: String) {
        val file = File(templatesDir, "$name.svg")
        file.createNewFile()
        file.writeBytes(bytes)
    }
}