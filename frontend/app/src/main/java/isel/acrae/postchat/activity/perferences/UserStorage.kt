package isel.acrae.postchat.activity.perferences

import android.content.Context
import android.util.Log


class UserStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        "UserPreferences", Context.MODE_PRIVATE
    )

    fun savePhoneNumber(phoneNumber: String) {
        sharedPreferences.edit().putString("PhoneNumber", phoneNumber).apply()
    }

    fun getPhoneNumber(): String = sharedPreferences.getString("PhoneNumber", null)!!
}