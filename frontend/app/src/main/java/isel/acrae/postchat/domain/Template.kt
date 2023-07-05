package isel.acrae.postchat.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Template @JsonCreator constructor (
    @JsonProperty("name") val name : String,
    @JsonProperty("content") val content : String,
)

data class TemplateList @JsonCreator constructor(
    @JsonProperty("list") val list : List<Template>
)