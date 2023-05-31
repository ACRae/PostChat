package isel.acrae.postchat.activity.perferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenStorage(context: Context) {
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context, "token_prefs", MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("token_key", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token_key", null)
    }

    fun getTokenOrThrow(): String {
        return sharedPreferences.getString("token_key", null) ?:
        throw IllegalStateException("Token key is null")
    }

    fun clearToken() {
        sharedPreferences.edit().remove("token_key").apply()
    }
}
