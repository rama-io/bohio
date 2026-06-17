package com.rama.bohio.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.rama.bohio.objects.PrefLanguage
import java.util.Locale

/**
 * Wraps a [Context] with the user-selected locale.
 *
 * Reads the language pref directly from SharedPreferences instead of going
 * through [PrefsManager.getInstance], because this is called from
 * [attachBaseContext] — before the app subclass has had a chance to call
 * [PrefsManager.register].
 */
object LocaleHelper {

    fun wrapContext(context: Context): Context {
        val langCode = readLanguagePref(context)

        if (langCode == PrefLanguage.SYSTEM || langCode.isBlank()) return context

        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    /** Returns the BCP-47 tag for the currently active locale. */
    fun currentLanguageTag(context: Context): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.resources.configuration.locales[0].toLanguageTag()
        else
            @Suppress("DEPRECATION")
            context.resources.configuration.locale.toLanguageTag()

    /**
     * Reads the language pref directly from SharedPreferences.
     * Avoids [PrefsManager.getInstance] which isn't safe this early in the
     * activity lifecycle (before [PrefsManager.register] has been called).
     */
    private fun readLanguagePref(context: Context): String {
        return context
            .getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getString("app:language", PrefLanguage.SYSTEM)
            ?: PrefLanguage.SYSTEM
    }
}