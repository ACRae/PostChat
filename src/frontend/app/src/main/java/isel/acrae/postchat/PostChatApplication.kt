package isel.acrae.postchat

import android.app.Application
import android.os.Environment
import android.util.Log
import isel.acrae.postchat.activity.perferences.IpStorage
import isel.acrae.postchat.activity.perferences.TokenStorage
import isel.acrae.postchat.room.AppDatabase
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.mock.MockServices
import isel.acrae.postchat.service.web.WebServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

enum class Profile {
    TEST,
    PRODUCTION
}

class PostChatApplication : Application() {
    private lateinit var tokenStorage: TokenStorage
    lateinit var templatesDir: String
    lateinit var messageDir: String
    lateinit var imagesDir : String
    private lateinit var baseUrl : String
    lateinit var services : Services
    lateinit var profile: Profile

    val db : AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    var contacts : List<String> = emptyList()
    private val port = 9000

    private val httpClient: OkHttpClient by lazy { OkHttpClient() }

    private val httpHtrClient : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.MINUTES)
            .callTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .build().also {
                Log.i("USING HTR CLIENT", it.toString())
            }
    }

    val saveTemplateFile = fun(bytes: ByteArray, name: String) {
        val file = File(templatesDir, "$name.svg")
        if(!File(templatesDir).exists()) File(templatesDir).mkdir()
        if(!file.exists()) {
            file.createNewFile()
            file.writeBytes(bytes)
        }
    }
    val saveMessageFile = fun(bytes: ByteArray, nameWithExtension: String) {
        val file = File(messageDir, nameWithExtension)
        if(!File(messageDir).exists()) File(messageDir).mkdir()
        if(!file.exists()) {
            file.createNewFile()
            file.writeBytes(bytes)
        }
    }

    val setup = fun(ip: String?) =
        if(ip == null) {
            baseUrl = "http://10.0.2.2:$port/api/v1"
            profile = Profile.TEST
            services =  MockServices()
        }
        else  {
            baseUrl = "http://$ip:$port/api/v1"
            profile = Profile.PRODUCTION
            services =  WebServices(baseUrl, httpClient, httpHtrClient)
        }

    override fun onCreate() {
        super.onCreate()

        setup(IpStorage(applicationContext).getIp())

        if(profile == Profile.TEST)
            CoroutineScope(Dispatchers.Default).launch {
                db.clearAllTables()
            }

        templatesDir = applicationContext.filesDir.absolutePath + "/templates"
        messageDir = applicationContext.filesDir.absolutePath + "/messages"
        imagesDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath ?: ""

        tokenStorage = TokenStorage(this)
    }
}