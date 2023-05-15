package isel.acrae.postchat.service.problem

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