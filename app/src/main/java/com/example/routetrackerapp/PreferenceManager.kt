package com.example.routetrackerapp

import android.content.Context
import android.content.SharedPreferences

open class PreferenceManager constructor(context: Context)  : IPreferenceHelper{
    //constant variables that can share preferences file

    private val PREFS_NAME ="SharedPreferences"
    private lateinit var preferences: SharedPreferences

    //declare init block :first to be executed

    init {
        //initialize as we declare the type of shared pref
        //mode private means that the shared preferences is limited and only accessible to our app
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    //declare a companion object:

    companion object{
        const val API_KEY = "api_key"
        const val USER_ID = "user_id"
    }

    override fun setApiKey(apiKey: String) {
        preferences[API_KEY] = apiKey
    }

    override fun getApiKey(): String {

        return preferences[API_KEY] ?: ""
    }

    override fun setUserId(userId: String) {

        preferences[USER_ID] = userId
    }

    override fun getUserId(): String {
        return preferences[USER_ID] ?: ""
    }

    override fun  clearPrefs(){
        preferences.edit().clear().apply()
    }

}

private inline fun SharedPreferences.edit(operation : (SharedPreferences.Editor) -> Unit){
    val editor = this.edit()
    operation(editor)
    editor.apply()

}

private inline operator fun <reified T: Any> SharedPreferences.get(
    key:String, defaultValue : T? = null
) : T? {
    return when(T::class) {
        String::class -> getString(key,defaultValue as? String) as T?
        Int ::class -> getInt(key,defaultValue as? Int?: -1) as T?
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?:false) as T?
        Float::class -> getFloat(key,defaultValue as? Float ?: -1f) as T?
        Long::class -> getLong(key,defaultValue as? Long ?: -1) as T?

        else ->throw UnsupportedOperationException("not yet implemented")

    }


}

private operator fun SharedPreferences.set(key: String, value: Any?) {

    when(value){
        is String? -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Boolean -> edit { it.putBoolean(key,value) }
        is Float -> edit { it.putFloat(key, value) }
        is Long -> edit { it.putLong(key, value) }

        else -> throw UnsupportedOperationException("not yet implemented")
    }
}
