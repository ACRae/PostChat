package isel.acrae.com.http.error

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler



@ControllerAdvice
class ErrorHandler {
    companion object {
        private val errorLogger: Logger = LoggerFactory.getLogger("ErrorHandler")
    }

    /**
     * Maps Exception to HttpStatus
     */
    val ApiException.code: HttpStatus
        get() = when (this) {
            is ApiIllegalArgumentException -> HttpStatus.BAD_REQUEST
            is ApiForbiddenOperationException-> HttpStatus.FORBIDDEN
            is ApiInternalErrorException -> HttpStatus.INTERNAL_SERVER_ERROR
            is ApiInvalidContentTypeException -> HttpStatus.UNSUPPORTED_MEDIA_TYPE
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(ex: Exception): ResponseEntity<ProblemJSON> {
        return when(ex) {
            is ApiException -> ProblemJSON.buildResponseEntity(
                ex.code, ex.problemTypeDetail
            )

            is HttpMessageNotReadableException -> ProblemJSON.buildResponseEntity(
                HttpStatus.BAD_REQUEST,
                ProblemTypeDetail.INVALID_INPUT_TYPE
            )

            is MethodArgumentTypeMismatchException -> ProblemJSON.buildResponseEntity(
                HttpStatus.BAD_REQUEST,
                ProblemTypeDetail.INVALID_INPUT_TYPE
            )

            else -> ProblemJSON.buildResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ProblemTypeDetail.DEFAULT(ex.message)
            )

        }.also {
            errorLogger.error("${ex::class.java.name} ${ex.message}")
        }
    }
}