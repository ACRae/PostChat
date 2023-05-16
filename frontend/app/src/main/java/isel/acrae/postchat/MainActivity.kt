package isel.acrae.postchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import isel.acrae.postchat.room.AppDatabase
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.mock.ChatDataMockService
import isel.acrae.postchat.service.mock.HomeDataMockService
import isel.acrae.postchat.service.mock.MockServices
import isel.acrae.postchat.service.mock.TemplateDataMockService
import isel.acrae.postchat.service.mock.UserDataMockService
import isel.acrae.postchat.service.web.ChatDataWebService
import isel.acrae.postchat.service.web.WebServices
import isel.acrae.postchat.ui.theme.PostChatTheme
import okhttp3.OkHttpClient

interface Dependencies {
    val database: AppDatabase
    val services: Services
}


class MainActivity : Dependencies, ComponentActivity() {
    private val baseUrl = "http://localhost:9000/api/v1"
    private val httpClient: OkHttpClient by lazy { OkHttpClient() }
    override val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
    
    private val servicePair = Pair(
        MockServices(),
        WebServices(baseUrl, httpClient, database)
    )

    override val services = servicePair.second

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PostChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PostChatTheme {
        Greeting("Android")
    }
}