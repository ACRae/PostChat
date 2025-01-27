package isel.acrae.com.http.error

/**
 * @author acrae
 * Represents base API Exception's
 */
sealed class ApiException(
    val problemTypeDetail: ProblemTypeDetail
) : Exception(problemTypeDetail.toString())

class ApiIllegalArgumentException(problemTypeDetail: ProblemTypeDetail)
    : ApiException(problemTypeDetail)
class ApiInternalErrorException(problemTypeDetail: ProblemTypeDetail)
    : ApiException(problemTypeDetail)
class ApiForbiddenOperationException(problemTypeDetail: ProblemTypeDetail)
    : ApiException(problemTypeDetail)
class ApiResourceNotFoundException(problemTypeDetail: ProblemTypeDetail)
    : ApiException(problemTypeDetail)
class ApiInvalidContentTypeException(problemTypeDetail: ProblemTypeDetail)
    : ApiException(problemTypeDetail)