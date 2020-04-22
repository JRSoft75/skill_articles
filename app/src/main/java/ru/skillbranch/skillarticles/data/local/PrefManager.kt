package ru.skillbranch.skillarticles.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import ru.skillbranch.skillarticles.data.delegates.PrefDelegate

class PrefManager(context: Context) {
    internal val preferences: SharedPreferences by lazy { PreferenceManager(context).sharedPreferences}

    val storedBoolean by PrefDelegate(false)
    val storedString by PrefDelegate("")
    val storedInt by PrefDelegate(0)
    val storedLong by PrefDelegate(0)
    val storedFloat by PrefDelegate(0f)

    fun clearAll(){
        preferences.edit().clear().apply()
    }

}