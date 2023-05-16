package isel.acrae.postchat.domain

data class Template(
    val name : String,
    val content : String,
): Domain

data class TemplateList(
    val list : List<Template>
): Domain