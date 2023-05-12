package isel.acrae.postchat.service.okhttp3

import okhttp3.Cookie
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import java.net.URL


sealed class Method(val url: URL)
class Get(url: URL) : Method(url)
class Post(url: URL, val body: String) : Method(url)
class Put(url: URL, val body: String) : Method(url)
class Delete(url: URL) : Method(url)

val JsonMediaType = "application/json".toMediaType()

/**
 * Builds a request.
 */
internal fun buildRequest(token: String? = null): Request {
    TODO()

        /*.url(requestMethod.url).method(
        method = when (requestMethod) {
            is Get -> "GET"
            is Post -> "POST"
            is Put -> "PUT"
            is Delete -> "DELETE"
        }, body = when (requestMethod) {
            is Post -> requestMethod.body?.toRequestBody()
            is Put -> requestMethod.body.toRequestBody()
            else -> null
        }
    ).headers(
        when (requestMethod) {
            is Post -> headers.add("Content-Type", "application/json").build()
            is Put -> headers.add("Content-Type", "application/json").build()
            else -> headers.build()
        }
    ).build()

         */
}

