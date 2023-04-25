package com.unl.map.sdk.prefs

import android.content.Context
import androidx.annotation.StringDef
import com.google.gson.Gson
import java.util.concurrent.atomic.AtomicBoolean

class PrefsManager private constructor(context: Context) {
    private val preferenceName = "UNLSDK"
    private val gson = Gson()
    private val preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    companion object {
        const val APIKEY = "APIKEY"
        const val VPMID = "VPMID"
        const val ENVIRONMENT = "ENVIRONMENT"



        @StringDef(
            APIKEY, VPMID,ENVIRONMENT
            )
        @Retention(AnnotationRetention.SOURCE)
        annotation class PrefKey

        private lateinit var instance: PrefsManager
        private val isInitialized = AtomicBoolean()     // To check if instance was previously initialized or not

        fun initialize(context: Context) {
            if (!isInitialized.getAndSet(true)) {
                instance = PrefsManager(context.applicationContext)
            }
        }

        fun get(): PrefsManager = instance
    }

    fun save(@PrefKey key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun save(@PrefKey key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun save(@PrefKey key: String, value: Float) {
        preferences.edit().putFloat(key, value).apply()
    }

    fun save(@PrefKey key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun save(@PrefKey key: String, `object`: Any) {
        // Convert the provided object to JSON string
        save(key, gson.toJson(`object`))
    }

    fun getString(@PrefKey key: String, defValue: String?) = preferences.getString(key, defValue)

    fun getInt(@PrefKey key: String, defValue: Int) = preferences.getInt(key, defValue)

    fun getBoolean(@PrefKey key: String, defValue: Boolean) = preferences.getBoolean(key, defValue)

    fun getFloat(@PrefKey key: String, defValue: Float) = preferences.getFloat(key, defValue)

    fun <T> getObject(@PrefKey key: String, objectClass: Class<T>): T? {
        val jsonString = preferences.getString(key, null)
        return if (jsonString == null || jsonString.isEmpty()) {
            null
        } else {
            try {
                gson.fromJson(jsonString, objectClass)
            } catch (e: Exception) {
                throw IllegalArgumentException("Object stored with key $key is instance of other class")
            }
        }
    }

    fun remove(@PrefKey key: String) {
        preferences.edit().remove(key).apply()
    }

    fun removeAll() {
        preferences.edit().clear().apply()
    }
}