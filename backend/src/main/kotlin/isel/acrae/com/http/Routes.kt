package isel.acrae.com.http

/**
 * Contains api routes
 */
object Routes {

    object Home {

        const val LOGIN =
            "/login"

        const val LOGOUT =
            "/logout"

        const val REGISTER =
            "/register"
    }


    object User {

        const val USER =
            "/user"

        const val USER_PHONE =
            "/{phone}"

        const val USER_PHONE_URI =
            USER + USER_PHONE
    }

    object Chat {
        const val CHAT =
            "/chat"

        const val CHAT_ID =
            "/{id}"

        const val CHAT_ID_URI =
            CHAT + CHAT_ID
    }

    object Template {
        const val TEMPLATE =
            "/template"
    }

    object Ocr {
        const val OCR =
            "/ocr"
    }
}