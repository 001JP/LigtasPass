package ph.kodego.ligtaspass.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtility {
    private var appPreferences : SharedPreferences? = null
    private val PREFS = "appPreferences"

    constructor(context: Context){
        appPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    fun saveStringPreferences(key: String, value: String){
        val prefEditor = appPreferences!!.edit()
        prefEditor.putString(key, value)
        prefEditor.apply()
    }

    fun saveBooleanPreferences(key: String, value: Boolean){
        val prefEditor = appPreferences!!.edit()
        prefEditor.putBoolean(key, value)
        prefEditor.apply()
    }

    fun saveIntPreferences(key: String, value: Int){
        val prefEditor = appPreferences!!.edit()
        prefEditor.putInt(key, value)
        prefEditor.apply()
    }

    fun getStringPreferences(key: String): String? {
        return appPreferences!!.getString(key, "")
    }

    fun getBooleanPreferences(key: String): Boolean {
        return appPreferences!!.getBoolean(key, true)
    }

    fun getIntPreferences(key: String): Int {
        return appPreferences!!.getInt(key, 12)
    }
}