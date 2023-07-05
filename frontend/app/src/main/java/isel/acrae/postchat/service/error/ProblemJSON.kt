package isel.acrae.postchat.service.error

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

data class ProblemJSON @JsonCreator constructor (
    @JsonProperty("type") val type: URI,
    @JsonProperty("title") val title: String,
    @JsonProperty("detail") val detail: String,
) {
    companion object {
        const val MEDIA_TYPE = "application/problem+json"
    }
}