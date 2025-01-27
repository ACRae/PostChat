package isel.acrae.postchat.activity.perferences

import android.content.Context

class IpStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        "IpPreferences", Context.MODE_PRIVATE
    )

    fun saveIp(ip: String) {
        sharedPreferences.edit().putString("IP", ip).apply()
    }

    fun clearIp() {
        sharedPreferences.edit().remove("IP").apply()
    }

    fun getIp(): String? = sharedPreferences.getString("IP", null)
}