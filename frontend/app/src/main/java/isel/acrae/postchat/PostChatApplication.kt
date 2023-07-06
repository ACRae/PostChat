package isel.acrae.postchat

import android.app.Application
import android.os.Environment
import isel.acrae.postchat.room.AppDatabase
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.mock.MockServices
import isel.acrae.postchat.service.web.WebServices
import isel.acrae.postchat.activity.perferences.TokenStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.io.File

interface Dependencies {
    val services: Services
}

internal enum class Profile {
    TEST,
    PRODUCTION
}

class PostChatApplication : Dependencies, Application() {

    private lateinit var tokenStorage: TokenStorage
    lateinit var templatesDir: String
    lateinit var messageDir: String
    lateinit var imagesDir : String

    private val profile = Profile.TEST

    val db : AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    var contacts : List<String> = emptyList()

    private val port = "9000"
    private val ip = "192.168.1.59" //10.0.2.2
    private val baseUrl = "http://$ip:$port/api/v1"
    private val httpClient: OkHttpClient by lazy { OkHttpClient() }

    override val services = when(profile) {
        Profile.TEST -> MockServices()
        Profile.PRODUCTION -> WebServices(baseUrl, httpClient)
    }

    val saveTemplateFile = fun(bytes: ByteArray, name: String) {
        val file = File(templatesDir, "$name.svg")
        if(!file.exists()) {
            file.createNewFile()
            file.writeBytes(bytes)
        }
    }

    val saveMessageFile = fun(bytes: ByteArray, nameWithExtension: String) {
        val file = File(messageDir, nameWithExtension)
        if(!file.exists()) {
            file.createNewFile()
            file.writeBytes(bytes)
        }
    }
    override fun onCreate() {
        super.onCreate()

        if(profile == Profile.TEST)
            CoroutineScope(Dispatchers.Default).launch {
                db.clearAllTables()
            }

        templatesDir = applicationContext.filesDir.absolutePath + "/templates"
        messageDir = applicationContext.filesDir.absolutePath + "/messages"
        imagesDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath ?: ""

        File(templatesDir).mkdir()
        File(messageDir).mkdir()

        tokenStorage = TokenStorage(this)
    }
}