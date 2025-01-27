package isel.acrae.postchat.service

interface Services {
    val chat : ChatDataService
    val user : UserDataService
    val template : TemplateDataService
    val home: HomeDataService
}