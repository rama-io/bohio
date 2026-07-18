package com.rama.bohio.managers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.rama.bohio.objects.PrefKeys
import com.rama.bohio.objects.PrefTheme
import com.rama.bohio.objects.Themes
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ThemeManagerTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit().clear().commit()
        PrefsManager.register(TestPrefsManager(context))
    }

    @Test
    fun `each built-in theme key maps to its matching palette`() {
        assertThat(ThemeManager.paletteFor(PrefTheme.MAKO)).isEqualTo(Themes.MAKO)
        assertThat(ThemeManager.paletteFor(PrefTheme.RAMA)).isEqualTo(Themes.RAMA)
        assertThat(ThemeManager.paletteFor(PrefTheme.TEYIN)).isEqualTo(Themes.TEYIN)
        assertThat(ThemeManager.paletteFor(PrefTheme.CATPPUCCIN_MOCHA)).isEqualTo(Themes.CATPPUCCIN_MOCHA)
        assertThat(ThemeManager.paletteFor(PrefTheme.CATPPUCCIN_LATTE)).isEqualTo(Themes.CATPPUCCIN_LATTE)
        assertThat(ThemeManager.paletteFor(PrefTheme.DRACULA)).isEqualTo(Themes.DRACULA)
        assertThat(ThemeManager.paletteFor(PrefTheme.MELANGE)).isEqualTo(Themes.MELANGE)
        assertThat(ThemeManager.paletteFor(PrefTheme.TOKYO_NIGHT)).isEqualTo(Themes.TOKYO_NIGHT)
        assertThat(ThemeManager.paletteFor(PrefTheme.MONO_DARK)).isEqualTo(Themes.MONO_DARK)
        assertThat(ThemeManager.paletteFor(PrefTheme.MONO_LIGHT)).isEqualTo(Themes.MONO_LIGHT)
    }

    @Test
    fun `unknown theme key falls back to TEYIN`() {
        assertThat(ThemeManager.paletteFor("not-a-real-theme")).isEqualTo(Themes.TEYIN)
        assertThat(ThemeManager.paletteFor("")).isEqualTo(Themes.TEYIN)
    }

    @Test
    fun `custom theme without a context falls back to TEYIN`() {
        assertThat(
            ThemeManager.paletteFor(
                PrefTheme.CUSTOM,
                context = null
            )
        ).isEqualTo(Themes.TEYIN)
    }

    @Test
    fun `custom theme with no saved colors uses TEYIN as the base for every slot`() {
        val palette = ThemeManager.paletteFor(PrefTheme.CUSTOM, context)

        assertThat(palette).isEqualTo(Themes.TEYIN)
    }

    @Test
    fun `custom theme reads overridden colors from prefs and falls back per-slot`() {
        val prefs = PrefsManager.getInstance(context)
        val overriddenH1 = 0xFF112233.toInt()
        val overriddenAccent1 = 0xFF445566.toInt()
        prefs.setCustomThemeColor(PrefKeys.APP_THEME_H1, overriddenH1)
        prefs.setCustomThemeColor(PrefKeys.APP_THEME_ACCENT_1, overriddenAccent1)

        val palette = ThemeManager.paletteFor(PrefTheme.CUSTOM, context)

        assertThat(palette.h1).isEqualTo(overriddenH1)
        assertThat(palette.accent_1).isEqualTo(overriddenAccent1)
        // Untouched slots still fall back to the TEYIN base palette
        assertThat(palette.foreground).isEqualTo(Themes.TEYIN.foreground)
        assertThat(palette.bg_1).isEqualTo(Themes.TEYIN.bg_1)
        assertThat(palette.danger).isEqualTo(Themes.TEYIN.danger)
    }

    @Test
    fun `builtIn map has an entry for every non-custom theme constant`() {
        val nonCustomThemeKeys = listOf(
            PrefTheme.MAKO, PrefTheme.RAMA, PrefTheme.TEYIN,
            PrefTheme.CATPPUCCIN_MOCHA, PrefTheme.CATPPUCCIN_LATTE,
            PrefTheme.DRACULA, PrefTheme.MELANGE, PrefTheme.TOKYO_NIGHT,
            PrefTheme.MONO_DARK, PrefTheme.MONO_LIGHT,
        )

        assertThat(Themes.builtIn.keys).containsExactlyElementsIn(nonCustomThemeKeys)
    }
}
