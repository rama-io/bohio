package com.rama.bohio.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.rama.bohio.objects.PrefLanguage
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
    fun `system language returns the same context unchanged`() {
        setLanguagePref(PrefLanguage.SYSTEM)

        val wrapped = LocaleHelper.wrapContext(context)

        assertThat(wrapped).isSameInstanceAs(context)
    }

    @Test
    fun `blank language pref returns the same context unchanged`() {
        setLanguagePref("")

        val wrapped = LocaleHelper.wrapContext(context)

        assertThat(wrapped).isSameInstanceAs(context)
    }

    @Test
    fun `explicit language wraps context with that locale`() {
        setLanguagePref("fr")

        val wrapped = LocaleHelper.wrapContext(context)

        assertThat(LocaleHelper.currentLanguageTag(wrapped)).isEqualTo("fr")
    }

    @Test
    fun `getCurrentLocale reads the first configured locale`() {
        setLanguagePref("de")
        val wrapped = LocaleHelper.wrapContext(context)

        val locale = LocaleHelper.getCurrentLocale(wrapped.resources.configuration)

        assertThat(locale.language).isEqualTo("de")
    }
}
