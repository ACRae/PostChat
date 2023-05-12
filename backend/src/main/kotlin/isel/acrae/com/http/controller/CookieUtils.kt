package isel.acrae.com.http.controller

import jakarta.servlet.http.Cookie

/**
 * Max cookie age
 */
const val TOKEN_MAX_AGE = 60 * 60 * 24 * 7

fun buildCookie(
    value: String?,
    maxCookieAge: Int = TOKEN_MAX_AGE
) = Cookie("token", value).apply {
    path = "/api"; isHttpOnly = true
    secure = true; maxAge = maxCookieAge
}
