package isel.acrae.postchat.service.mock

import java.util.UUID

fun generateId() =
    UUID.randomUUID().toString().replace(Regex("[a-zA-Z-0]"), "").take(5).toInt()
