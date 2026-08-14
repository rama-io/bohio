package com.rama.bohio.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.rama.bohio.objects.PrefLanguage
import java.util.Locale

object LocaleHelper {

    fun wrapContext(context: Context): Context {
        val langCode = readLanguagePref(context)
        val isSystem = langCode == PrefLanguage.SYSTEM || langCode.isBlank()

        val systemConfig = Resources.getSystem().configuration
        val locale = if (isSystem) {
            getCurrentLocale(systemConfig)
        } else {
            // supported_language_codes stores Android resource-qualifier style tags
            // (e.g. "pt-rBR", "fil-rPH") to mirror the res/values-xx-rYY folder names.
            // Locale APIs expect real BCP-47 tags ("pt-BR"), so strip the "r".
            val normalizedTag = langCode.replace(Regex("-r([A-Za-z]{2})$"), "-$1")
            Locale.forLanguageTag(normalizedTag)
        }
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isSystem) config.setLocales(systemConfig.locales) else config.setLocale(locale)
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

    // Returns the BCP-47 tag for the currently active locale.
    fun currentLanguageTag(context: Context): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.resources.configuration.locales[0].toLanguageTag()
        else
            @Suppress("DEPRECATION")
            context.resources.configuration.locale.toLanguageTag()

    fun getCurrentLocale(configuration: Configuration): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            configuration.locale
        }
    }

    private fun readLanguagePref(context: Context): String {
        return context
            .getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getString("app:language", PrefLanguage.SYSTEM)
            ?: PrefLanguage.SYSTEM
    }
}