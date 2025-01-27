package isel.acrae.com

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseCookie
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult


val mapper: JsonMapper = jacksonMapperBuilder().build()

fun WebTestClient.buildPost(
    uri: String,
    body: Any?,
    expectStatus: HttpStatus,
    cookie: HttpCookie? = null,
    contentType: MediaType = MediaType.APPLICATION_JSON
): FluxExchangeResult<*> = post().uri(uri)
    .contentType(contentType)
    .bodyValue(mapper.writeValueAsString(body))
    .apply {
        if (cookie != null)
            this.cookie(
                cookie.name,
                cookie.value
            )
    }
    .exchange()
    .expectStatus().isEqualTo(expectStatus)
    .returnResult<String>()

fun WebTestClient.buildGet(
    uri: String,
    expectStatus: HttpStatus,
    cookie: HttpCookie? = null
): FluxExchangeResult<*> = get().uri(uri)
    .apply {
        if (cookie != null)
            this.cookie(
                cookie.name,
                cookie.value
            )
    }
    .exchange()
    .expectStatus().isEqualTo(expectStatus)
    .returnResult<String>()


fun FluxExchangeResult<*>.getCookie(name: String): ResponseCookie? {
    return responseCookies.getFirst(name)
}

