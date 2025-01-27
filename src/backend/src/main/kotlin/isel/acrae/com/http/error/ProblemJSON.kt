package isel.acrae.com.http.error

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.net.URI


/**
 * Problem JSON class representation [RFC-7807](https://www.rfc-editor.org/rfc/rfc7807)
 */
class ProblemJSON private constructor(
    val type: URI,
    val title: String,
    val detail: String,
) {
    companion object {
        fun buildResponseEntity(status: HttpStatus, problem: ProblemTypeDetail) = ResponseEntity
            .status(status)
            .header("Content-Type", MediaType.APPLICATION_PROBLEM_JSON_VALUE)
            .body(
                ProblemJSON(
                    problem.type,
                    status.name,
                    problem.detail,
                )
            )
    }
}
