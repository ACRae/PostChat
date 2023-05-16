package isel.acrae.postchat.service.web

import isel.acrae.postchat.room.AppDatabase
import isel.acrae.postchat.service.ChatDataService
import isel.acrae.postchat.service.HomeDataService
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.TemplateDataService
import isel.acrae.postchat.service.UserDataService
import okhttp3.OkHttpClient

class WebServices(
    private val baseUrl : String,
    private val httpClient: OkHttpClient,
    private val db: AppDatabase,
) : Services {

    override val chat: ChatDataService by lazy {
        ChatDataWebService(db.chatDao(), db.messageDao(), baseUrl, httpClient)
    }

    override val user: UserDataService by lazy {
        UserDataWebService(db.userDao(), baseUrl, httpClient)
    }

    override val template: TemplateDataService by lazy {
        TemplateDataWebService(db.templateDao(), baseUrl, httpClient)
    }

    override val home: HomeDataService by lazy {
        HomeDataWebService(baseUrl, httpClient)
    }

}