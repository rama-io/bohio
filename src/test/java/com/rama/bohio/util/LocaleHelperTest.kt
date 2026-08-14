package com.rama.bohio.util

import android.content.Context
import android.content.res.Resources
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.rama.bohio.objects.PrefLanguage
import java.util.Locale
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocaleHelperTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun setLanguagePref(languageTag: String) {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit()
            .putString("app:language", languageTag)
            .apply()
    }

    @Test
    fun `system language wraps context with system locale`() {
        setLanguagePref(PrefLanguage.SYSTEM)

        val wrapped = LocaleHelper.wrapContext(context)
        val expectedLocale = LocaleHelper.getCurrentLocale(Resources.getSystem().configuration)
        val actualLocale = LocaleHelper.getCurrentLocale(wrapped.resources.configuration)

        assertThat(actualLocale).isEqualTo(expectedLocale)
    }

    @Test
    fun `blank language pref wraps context with system locale`() {
        setLanguagePref("")

        val wrapped = LocaleHelper.wrapContext(context)
        val expectedLocale = LocaleHelper.getCurrentLocale(Resources.getSystem().configuration)
        val actualLocale = LocaleHelper.getCurrentLocale(wrapped.resources.configuration)

        assertThat(actualLocale).isEqualTo(expectedLocale)
    }

    @Test
    fun `explicit language wraps context with that locale`() {
        setLanguagePref("fr")

        val wrapped = LocaleHelper.wrapContext(context)

        assertThat(LocaleHelper.currentLanguageTag(wrapped)).isEqualTo("fr")
        assertThat(Locale.getDefault().language).isEqualTo("fr")
    }

    @Test
    fun `system language restores default locale to system locale`() {
        Locale.setDefault(Locale.FRENCH)
        setLanguagePref(PrefLanguage.SYSTEM)

        val wrapped = LocaleHelper.wrapContext(context)
        val expectedLocale = LocaleHelper.getCurrentLocale(Resources.getSystem().configuration)

        assertThat(Locale.getDefault()).isEqualTo(expectedLocale)
    }

    @Test
    fun `getCurrentLocale reads the first configured locale`() {
        setLanguagePref("de")
        val wrapped = LocaleHelper.wrapContext(context)

        val locale = LocaleHelper.getCurrentLocale(wrapped.resources.configuration)

        assertThat(locale.language).isEqualTo("de")
    }
}
