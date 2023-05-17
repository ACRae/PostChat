package isel.acrae.postchat.service.error

import java.net.URI

data class ProblemJSON(
    val type: URI,
    val title: String,
    val detail: String,
) {
    companion object {
        const val MEDIA_TYPE = "application/problem+json"
    }
}