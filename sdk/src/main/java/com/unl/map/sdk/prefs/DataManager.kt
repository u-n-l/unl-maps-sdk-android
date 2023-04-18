package com.unl.map.sdk.prefs


object DataManager {
    fun saveApiKey(apiKey: String) {
        PrefsManager.get().save(PrefsManager.APIKEY, apiKey)
    }

    fun getApiKey(): String? {
        return PrefsManager.get().getString(PrefsManager.APIKEY, "")
    }


    fun saveVpmId(vpmId: String) {
        PrefsManager.get().save(PrefsManager.VPMID, vpmId)
    }

    fun getVpmId(): String? {
        return PrefsManager.get().getString(PrefsManager.VPMID, "")
    }


}