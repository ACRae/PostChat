package isel.acrae.com.http.error

import java.net.URI


private const val BASE_URL = "https://github.com/ACRae/PostChatBackend/docs/ProblemJSON#"
private val typeUri = fun(type: String) = URI(BASE_URL + type)
private val DEFAULT_TYPE = typeUri("something-went-wrong")


/**
 * Class holder for all ProblemJSON Types and corresponding Details
 */
class ProblemTypeDetail private constructor(val type: URI, val detail: String) {

    override fun toString(): String =
        "${this::class.java.simpleName}(type=$type, detail=$detail)"

    companion object {
        val DEFAULT = fun(detail: String?) = ProblemTypeDetail(
            DEFAULT_TYPE, detail ?: "An error has occurred"
        )

        val INVALID_PASSWORD = ProblemTypeDetail(
            typeUri("invalid-password"),
            "Invalid password, must be in range 4 to 30 and have at " +
                    "least 1 uppercase letter and 1 digit"
        )

        val INVALID_PHONE_NUMBER = ProblemTypeDetail(
            typeUri("invalid-phone-number"),
            "Invalid phone number or region"
        )

        val INVALID_TOKEN = ProblemTypeDetail(
            typeUri("invalid-token"),
            "Invalid token, the token you are trying to use is " +
                    "either invalid or expired"
        )

        val INVALID_NAME = ProblemTypeDetail(
            typeUri("invalid-name"),
            "Private chat cannot have a name"
        )

        val REQUIRES_NAME = ProblemTypeDetail(
            typeUri("requires-name"),
            "Group chat requires a name"
        )

        val EMPTY_USER_NAME = ProblemTypeDetail(
            typeUri("empty-user-name"),
            "Empty user name"
        )

        val USER_NOT_FOUND = ProblemTypeDetail(
            typeUri("user-not-found"),
            "User not found"
        )

        val CHAT_NOT_FOUND = ProblemTypeDetail(
            typeUri("chat-not-found"),
            "Chat not found"
        )

        val PRIVATE_CHAT_ALREADY_FOUND = ProblemTypeDetail(
            typeUri("private-chat-already-created"),
            "Private chat already created"
        )

        val TEMPLATE_NOT_FOUND = ProblemTypeDetail(
            typeUri("template-not-found"),
            "Template not found"
        )

        val INVALID_INPUT_TYPE = ProblemTypeDetail(
            typeUri("invalid-request"),
            "Invalid request, please check your request and try again"
        )

        val INVALID_CONTENT_TYPE = ProblemTypeDetail(
            typeUri("invalid-content-type"),
            "Invalid content type, please try using 'application/json' content type"
        )

        val USER_ALREADY_EXISTS = ProblemTypeDetail(
            typeUri("user-already-exists"),
            "User already exists, unable to register"
        )

        val NOT_FOUND = ProblemTypeDetail(
            typeUri("not-found"),
            "Resource not found"
        )

    }
}