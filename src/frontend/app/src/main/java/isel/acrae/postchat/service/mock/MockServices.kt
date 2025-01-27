package isel.acrae.postchat.service.mock

import isel.acrae.postchat.service.ChatDataService
import isel.acrae.postchat.service.HomeDataService
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.TemplateDataService
import isel.acrae.postchat.service.UserDataService

class MockServices : Services {
    override val chat: ChatDataService =
        ChatDataMockService()

    override val user: UserDataService =
        UserDataMockService()

    override val template: TemplateDataService =
        TemplateDataMockService()

    override val home: HomeDataService =
        HomeDataMockService()
}