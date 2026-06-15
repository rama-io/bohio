// package com.rama.bohio.util

// import android.content.Context
// import android.content.res.Configuration
// import java.util.Locale

// /**
//  * Shared locale helper for applying a user-selected app language
//  * independent of the system locale (e.g. Simplified Chinese for community
//  * translations), wired via CsActivity.attachBaseContext.
//  *
//  * Port over from existing apps:
//  *  - the actual settings UI that calls setLanguage()
//  *  - any locale-list-based (LocaleListCompat) handling if needed for newer APIs,
//  *    keeping the API 21 fallback path here
//  */
// object LocaleHelper {

//     private const val PREFS_NAME = "rama_locale_prefs"
//     private const val KEY_LANGUAGE = "app_language"

//     /** Wraps [context] with the persisted language override, if any is set. */
//     fun wrap(context: Context): Context {
//         val lang = getPersistedLanguage(context) ?: return context
//         val locale = Locale(lang)
//         val config = Configuration(context.resources.configuration)
//         config.setLocale(locale)
//         return context.createConfigurationContext(config)
//     }

//     /** Persists [languageTag] (e.g. "zh", "en") as the app-wide language override. */
//     fun setLanguage(context: Context, languageTag: String) {
//         prefs(context).edit().putString(KEY_LANGUAGE, languageTag).apply()
//     }

//     /** Clears the override, falling back to the system locale. */
//     fun clearLanguage(context: Context) {
//         prefs(context).edit().remove(KEY_LANGUAGE).apply()
//     }

//     fun getPersistedLanguage(context: Context): String? =
//         prefs(context).getString(KEY_LANGUAGE, null)

//     private fun prefs(context: Context) =
//         context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
// }
