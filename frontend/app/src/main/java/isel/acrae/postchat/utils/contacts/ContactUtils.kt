package isel.acrae.postchat.utils.contacts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import isel.acrae.postchat.activity.signin.phoneUtil
import java.util.Locale

object ContactUtils {
    @SuppressLint("Range")
    fun getPhoneNumbers(context: Context): List<String> {
        val regionCode = getCountryMobileCode(context) ?: ""
        val phoneNumbers = mutableListOf<String>()
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            while (cursor.moveToNext()) {
                val phoneNumber = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                )
                var tempNumber = phoneNumber.replace(Regex("[ ()*#-]"), "")

                tempNumber = if(!tempNumber.contains("+")) regionCode + tempNumber
                else tempNumber.replace("+", "")

                phoneNumbers.add(tempNumber)
            }
        }
        return phoneNumbers.distinct()
    }
}


fun getCountryMobileCode(context: Context): String? {
    return try {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkCountryIso = telephonyManager.networkCountryIso
        return phoneUtil.getCountryCodeForRegion(
            networkCountryIso.uppercase(Locale.ROOT)
        ).toString()
    } catch (e: Exception) {
        null
    }
}