package com.rama.bohio.managers

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.rama.bohio.objects.PrefFontStyle
import com.rama.bohio.objects.PrefKeys
import com.rama.bohio.objects.PrefLanguage
import com.rama.bohio.objects.PrefTheme
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

internal class TestPrefsManager(context: Context) : PrefsManager(context)

@RunWith(RobolectricTestRunner::class)
class PrefsManagerTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var prefsManager: TestPrefsManager

    @Before
    fun setUp() {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit().clear().commit()
        prefsManager = TestPrefsManager(context)
        PrefsManager.register(prefsManager)
    }

    @Test
    fun `initPrefs sets expected defaults on first run`() {
        prefsManager.initPrefs(sync = true)

        assertThat(prefsManager.getFontStyle()).isEqualTo(PrefFontStyle.JERSEY_25)
        assertThat(prefsManager.getAppLanguage()).isEqualTo(PrefLanguage.SYSTEM)
        assertThat(prefsManager.getUiScale()).isEqualTo(1f)
        assertThat(prefsManager.getTheme()).isEqualTo(PrefTheme.RAMA)
        assertThat(prefsManager.getBoolean(PrefKeys.SYSTEM_BAR_VISIBLE, false)).isTrue()
        assertThat(prefsManager.getBoolean(PrefKeys.SYSTEM_PREVENT_ROTATION, true)).isFalse()
        assertThat(prefsManager.getBoolean(PrefKeys.SETTINGS_SECTION_FONTS, false)).isTrue()
        assertThat(prefsManager.getBoolean(PrefKeys.SETTINGS_SECTION_SYSTEM, false)).isTrue()
        assertThat(prefsManager.getBoolean(PrefKeys.SETTINGS_SECTION_LANGUAGE, false)).isTrue()
        assertThat(prefsManager.getBoolean(PrefKeys.SETTINGS_SECTION_THEMES, false)).isTrue()
    }

    @Test
    fun `initPrefs is a no-op when app language key already present`() {
        prefsManager.initPrefs(sync = true)
        prefsManager.setAppLanguage("fr")
        prefsManager.setTheme(PrefTheme.MAKO)

        prefsManager.initPrefs(sync = true)

        // Defaults were NOT re-applied, our custom values survive
        assertThat(prefsManager.getAppLanguage()).isEqualTo("fr")
        assertThat(prefsManager.getTheme()).isEqualTo(PrefTheme.MAKO)
    }

    @Test
    fun `clearAllPrefs wipes existing values and reapplies defaults`() {
        prefsManager.initPrefs(sync = true)
        prefsManager.setAppLanguage("de")
        prefsManager.setTheme(PrefTheme.DRACULA)
        prefsManager.setBoolean("some:custom:flag", true)

        val result = prefsManager.clearAllPrefs()

        assertThat(result.isSuccess).isTrue()
        assertThat(prefsManager.getAppLanguage()).isEqualTo(PrefLanguage.SYSTEM)
        assertThat(prefsManager.getTheme()).isEqualTo(PrefTheme.RAMA)
        assertThat(prefsManager.getBoolean("some:custom:flag", false)).isFalse()
    }

    @Test
    fun `simple getters and setters round-trip`() {
        prefsManager.setFontStyle(PrefFontStyle.CUSTOM)
        assertThat(prefsManager.getFontStyle()).isEqualTo(PrefFontStyle.CUSTOM)

        prefsManager.setCustomFontPath("/tmp/font.ttf")
        assertThat(prefsManager.getCustomFontPath()).isEqualTo("/tmp/font.ttf")

        prefsManager.setUiScale(1.5f)
        assertThat(prefsManager.getUiScale()).isEqualTo(1.5f)

        prefsManager.setCustomThemeColor(PrefKeys.APP_THEME_H1, 0xFF112233.toInt())
        assertThat(
            prefsManager.getCustomThemeColor(
                PrefKeys.APP_THEME_H1,
                0
            )
        ).isEqualTo(0xFF112233.toInt())

        prefsManager.setString("custom:key", "hello")
        assertThat(prefsManager.getString("custom:key")).isEqualTo("hello")
        assertThat(prefsManager.getString("missing:key", "fallback")).isEqualTo("fallback")
    }

    @Test
    fun `buildExportJson includes booleans ints longs floats strings and sets`() {
        prefsManager.setBoolean("flag", true)
        prefsManager.setString("name", "bohio")
        prefsManager.setUiScale(2f)
        prefsManager.prefs.edit()
            .putInt("int:key", 42)
            .putLong("long:key", 99L)
            .putStringSet("set:key", setOf("a", "b"))
            .commit()

        val json = prefsManager.buildExportJson()

        assertThat(json.getBoolean("flag")).isTrue()
        assertThat(json.getString("name")).isEqualTo("bohio")
        assertThat(json.getDouble(PrefKeys.APP_UI_SCALE)).isEqualTo(2.0)
        assertThat(json.getInt("int:key")).isEqualTo(42)
        assertThat(json.getLong("long:key")).isEqualTo(99L)
        val array = json.getJSONArray("set:key")
        val values = (0 until array.length()).map { array.getString(it) }
        assertThat(values).containsExactly("a", "b")
    }

    @Test
    fun `export then import round-trips all preference values`() {
        prefsManager.initPrefs(sync = true)
        prefsManager.setAppLanguage("es")
        prefsManager.setTheme(PrefTheme.TOKYO_NIGHT)
        prefsManager.setUiScale(1.25f)
        prefsManager.setBoolean("custom:flag", true)
        prefsManager.prefs.edit().putStringSet("custom:set", setOf("x", "y")).commit()

        val uri = writeToTempFile(prefsManager.buildExportJson())

        // Wipe everything so we can prove import restores it
        prefsManager.prefs.edit().clear().commit()

        val success = prefsManager.importFromUri(context, uri)

        assertThat(success).isTrue()
        assertThat(prefsManager.getAppLanguage()).isEqualTo("es")
        assertThat(prefsManager.getTheme()).isEqualTo(PrefTheme.TOKYO_NIGHT)
        assertThat(prefsManager.getUiScale()).isEqualTo(1.25f)
        assertThat(prefsManager.getBoolean("custom:flag", false)).isTrue()
        assertThat(prefsManager.prefs.getStringSet("custom:set", emptySet())).containsExactly(
            "x",
            "y"
        )
    }

    @Test
    fun `import coerces JSON integers to float for known float keys`() {
        // Simulate a hand-edited or older export where ui_scale was serialized as an int
        val json = JSONObject().put(PrefKeys.APP_UI_SCALE, 2)
        val uri = writeToTempFile(json)

        val success = prefsManager.importFromUri(context, uri)

        assertThat(success).isTrue()
        assertThat(prefsManager.getUiScale()).isEqualTo(2f)
    }

    @Test
    fun `importFromUri returns false for malformed json`() {
        val file = File.createTempFile("bad_import", ".json")
        file.writeText("{ not valid json ")
        val uri = Uri.fromFile(file)

        val success = prefsManager.importFromUri(context, uri)

        assertThat(success).isFalse()
    }

    @Test
    fun `importFromUri returns false when stream cannot be opened`() {
        val uri = Uri.parse("content://nonexistent/authority/path")

        val success = prefsManager.importFromUri(context, uri)

        assertThat(success).isFalse()
    }

    @Test
    fun `getInstance returns the most recently registered manager`() {
        assertThat(PrefsManager.getInstance(context)).isSameInstanceAs(prefsManager)

        val other = TestPrefsManager(context)
        PrefsManager.register(other)

        assertThat(PrefsManager.getInstance(context)).isSameInstanceAs(other)
    }

    private fun writeToTempFile(json: JSONObject): Uri {
        val file = File.createTempFile("bohio_export", ".json")
        file.writeText(json.toString())
        return Uri.fromFile(file)
    }
}
