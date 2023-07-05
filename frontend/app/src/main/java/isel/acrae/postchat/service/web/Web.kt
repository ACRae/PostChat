package isel.acrae.postchat.service.web


import android.util.Log
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import isel.acrae.postchat.service.error.ProblemJSON
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.net.URL
import java.net.URLEncoder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KFunction

open class Web(private val baseURLStr: String) {

    companion object {
        private val mapper = ObjectMapper()
    }

    @Target(AnnotationTarget.FUNCTION)
    annotation class Route(val uri: String)

    fun makeURL(f: KFunction<*>, vararg pathVars: String): URL {
        val uri = (f.annotations.firstOrNull { it is Route } as Route?)?.uri
        checkNotNull(uri) { "No Route Annotation found " }
        val placeholders = uri.split(Regex("\\{([^}]*)\\}"))
        var urlStr = baseURLStr
        for (i in placeholders.indices) {
            urlStr += placeholders[i]
            if (i < placeholders.size - 1) {
                urlStr += pathVars[i]
            }
        }
        return URL(urlStr)
    }

    data class QueryParam(val value: String, val params: String) {
        companion object {
            fun <T> from(query: String, params: List<T>) =
                QueryParam(query, params.joinToString())
        }

        fun toUrlQuery() = URLEncoder.encode(value, "UTF-8") + "=" +
                URLEncoder.encode(params, "UTF-8")
    }

    fun URL.addQuery(queryParam: QueryParam) =
        URL(
            this.toString().apply {
                if (query.isNullOrEmpty())
                    "$this?${queryParam.toUrlQuery()}"
                else
                    "$this&${queryParam.toUrlQuery()}"
            }
        )

    sealed class RequestMethod<T>(val url: URL, val body: T? = null) {
        abstract val contentType : String?
        abstract val name : String
    }

    class Get(url: URL) : RequestMethod<Any>(url, null) {
        override val contentType = null
        override val name = "GET"
    }
    class Post<T>(url: URL, body: T) : RequestMethod<T>(url, body) {
        override val contentType = "application/json"
        override val name = "POST"
    }

    class Put<T>(url: URL, body: T) : RequestMethod<T>(url, body) {
        override val contentType = "application/json"
        override val name = "PUT"
    }

    class Delete(url: URL) : RequestMethod<Any>(url) {
        override val contentType = null
        override val name = "DELETE"
    }

    private fun <T> makeHeaders(token : String?, req: RequestMethod<T>) =
        Headers.Builder().apply {
            if(token != null)
                add("Authorization", "Bearer $token")
            if(req.contentType != null)
                add("Content-Type", req.contentType!!)
        }.build()

    fun <T> buildRequest(req: RequestMethod<T>, token: String? = null) =
        Request.Builder().url(req.url).method(
            req.name, body = if(req.body == null) null else
                mapper.writeValueAsString(req.body).toRequestBody()
        ).headers(makeHeaders(token, req)).build()

    /**
     * Extension function used to send [this] request using [okHttpClient] and process the
     * received response with the given [handler]. Note that [handler] is called from a
     * OkHttp IO Thread.
     *
     * @receiver the request to be sent
     * @param okHttpClient  the client from where the request is sent
     * @param handler       the handler function, which is called from a IO thread.
     *                      Be mindful of threading issues.
     * @return the result of the response [handler]
     * @throws  [IOException] if a communication error occurs.
     */
    suspend fun <T> Request.send(okHttpClient: OkHttpClient, handler: (Response) -> T): T =
        suspendCoroutine { continuation ->
            okHttpClient.newCall(request = this).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
                override fun onResponse(call: Call, response: Response) {
                    try {
                        continuation.resume(handler(response))
                    } catch (e: Exception) {
                        continuation.resumeWithException(e)
                    }
                }
            })
        }


    internal inline fun <reified T> Response.handle(): T {
        val body = this.body?.string()
        Log.i("handleResponse", "body: $body")

        try {
            if(this.isSuccessful) {
                return mapper.readValue(body, T::class.java)
            }
            else {
                val problemJSON = mapper.readValue(body, ProblemJSON::class.java)
                throw Exception(problemJSON.detail)
            }
        } catch (e: JsonMappingException) {
            throw e
        }
    }

    /**
     * TODO("See how to get the token cookie")
     */
    internal fun Response.handleTokenCookie(): String {
        val body = this.body?.string()
        try {
            if(this.isSuccessful) {
                println(headers("Set-Cookie"))
            }
            else {
                val problemJSON = mapper.readValue(body, ProblemJSON::class.java)
                throw Exception(problemJSON.detail)
            }
        } catch (e: JsonMappingException) {
            throw e
        }
        return ""
    }
}