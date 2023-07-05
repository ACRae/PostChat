package isel.acrae.postchat.utils.contacts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract

object ContactUtils {
    @SuppressLint("Range")
    fun getPhoneNumbers(context: Context): List<String> {
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
                phoneNumbers.add(phoneNumber.replace(Regex("[ +()*#-]"), ""))
            }
        }
        return phoneNumbers
    }
}