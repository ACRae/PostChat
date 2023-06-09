package isel.acrae.postchat

import android.app.Application
import android.os.Environment
import isel.acrae.postchat.activity.perferences.IpStorage
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

enum class Profile {
    TEST,
    PRODUCTION
}

class PostChatApplication : Application() {
    private lateinit var tokenStorage: TokenStorage
    lateinit var templatesDir: String
    lateinit var messageDir: String
    lateinit var imagesDir : String
    lateinit var baseUrl : String
    lateinit var services : Services
    lateinit var profile: Profile

    val db : AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    var contacts : List<String> = emptyList()
    private val port = 9000

    private val httpClient: OkHttpClient by lazy { OkHttpClient() }
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

    val setUrl = fun(ip: String?) =
        if(ip == null) {
            baseUrl = "http://10.0.2.2:$port/api/v1"
            profile = Profile.TEST
            services =  MockServices()
        }
        else  {
            baseUrl = "http://$ip:$port/api/v1"
            profile = Profile.PRODUCTION
            services =  WebServices(baseUrl, httpClient)
        }

    override fun onCreate() {
        super.onCreate()

        setUrl(IpStorage(applicationContext).getIp())

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