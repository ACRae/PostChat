package isel.acrae.com.service

import isel.acrae.com.domain.isValidPassword
import isel.acrae.com.domain.isValidPhoneNumber
import isel.acrae.com.http.error.ApiIllegalArgumentException
import isel.acrae.com.http.error.ProblemTypeDetail
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class)
fun Any?.checkNotNull(ex : Exception) {
    contract {
        returns() implies (this@checkNotNull != null)
    }
    if(this == null) throw ex
}

@OptIn(ExperimentalContracts::class)
fun Any?.checkNull(ex : Exception) {
    contract {
        returns() implies (this@checkNull == null)
    }
    if(this != null) throw ex
}

internal object Check {

    private fun valid(v: Boolean, ex: Exception): Check {
        if (v) throw ex
        return this
    }

    fun validPassword(password : String) = valid(
        !password.isValidPassword(),
        ApiIllegalArgumentException(ProblemTypeDetail.INVALID_PASSWORD)
    )

    fun validPhoneNumber(phoneNumber: String, region : Int) = valid(
        !isValidPhoneNumber(phoneNumber, region),
        ApiIllegalArgumentException(ProblemTypeDetail.INVALID_PHONE_NUMBER)
    )

    fun validUserName(name : String) = valid(
        name.isBlank(),
        ApiIllegalArgumentException(ProblemTypeDetail.EMPTY_USER_NAME)
    )

}