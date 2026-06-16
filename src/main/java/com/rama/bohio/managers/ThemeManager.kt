package com.rama.bohio.managers

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.rama.bohio.objects.PrefKeys
import com.rama.bohio.objects.PrefTheme
import com.rama.bohio.objects.Themes

object ThemeManager {

    fun paletteFor(theme: String, context: Context? = null): Themes.Palette =
        when (theme) {
            PrefTheme.MAKO -> Themes.MAKO
            PrefTheme.RAMA -> Themes.RAMA
            PrefTheme.CATPPUCCIN_MOCHA -> Themes.CATPPUCCIN_MOCHA
            PrefTheme.CATPPUCCIN_LATTE -> Themes.CATPPUCCIN_LATTE
            PrefTheme.DRACULA -> Themes.DRACULA
            PrefTheme.MELANGE -> Themes.MELANGE
            PrefTheme.TOKYO_NIGHT -> Themes.TOKYO_NIGHT
            PrefTheme.CUSTOM -> if (context != null) buildCustomPalette(context) else Themes.TEYIN
            else -> Themes.TEYIN
        }

    private fun buildCustomPalette(context: Context): Themes.Palette {
        val prefs = PrefsManager.getInstance(context)
        val base = Themes.TEYIN
        fun get(key: String, fallback: Int) = prefs.getCustomThemeColor(key, fallback)
        return Themes.Palette(
            h1 = get(PrefKeys.APP_THEME_H1, base.h1),
            foreground = get(PrefKeys.APP_THEME_FOREGROUND, base.foreground),
            bg_1 = get(PrefKeys.APP_THEME_BG_1, base.bg_1),
            bg_2 = get(PrefKeys.APP_THEME_BG_2, base.bg_2),
            bg_3 = get(PrefKeys.APP_THEME_BG_3, base.bg_3),
            bg_4 = get(PrefKeys.APP_THEME_BG_4, base.bg_4),
            bg_display = get(PrefKeys.APP_THEME_BG_DISPLAY, base.bg_display),
            media_background = get(
                PrefKeys.APP_THEME_MEDIA_BACKGROUND,
                base.media_background
            ),
            accent_1 = get(PrefKeys.APP_THEME_ACCENT_1, base.accent_1),
            accent_2 = get(PrefKeys.APP_THEME_ACCENT_2, base.accent_2),
            accent_3 = get(PrefKeys.APP_THEME_ACCENT_3, base.accent_3),
            accent_4 = get(PrefKeys.APP_THEME_ACCENT_4, base.accent_4),
            disabled = get(PrefKeys.APP_THEME_DISABLED, base.disabled),
            input = get(PrefKeys.APP_THEME_INPUT, base.input),
            button_1 = get(PrefKeys.APP_THEME_BUTTON_1, base.button_1),
            button_1_selected = get(
                PrefKeys.APP_THEME_BUTTON_1_SELECTED,
                base.button_1_selected
            ),
            button_2 = get(PrefKeys.APP_THEME_BUTTON_2, base.button_2),
            danger = get(PrefKeys.APP_THEME_DANGER, base.danger),
            collapsible_header = get(
                PrefKeys.APP_THEME_COLLAPSIBLE_HEADER,
                base.collapsible_header
            ),
            icon = get(PrefKeys.APP_THEME_ICON, base.icon),
            progressbar = get(PrefKeys.APP_THEME_PROGRESS_BAR, base.progressbar),
        )
    }

    fun applyTheme(context: Context, root: View) {
        val prefs = PrefsManager.getInstance(context)
        val palette = paletteFor(prefs.getTheme(), context)
        val typeface = FontManager.getTypeface(context, prefs.getFontStyle())
        applyRecursively(context, root, palette, typeface)
    }

    private fun applyRecursively(
        context: Context,
        view: View,
        palette: Themes.Palette,
        typeface: android.graphics.Typeface?
    ) {
        applyToView(context, view, palette, typeface)
        if (view is ViewGroup) {
            for (i in 0 until view.childCount)
                applyRecursively(
                    context,
                    view.getChildAt(i),
                    palette,
                    typeface
                )
        }
    }

    private fun applyToView(
        context: Context,
        view: View,
        palette: Themes.Palette,
        typeface: android.graphics.Typeface?
    ) {
        // Font + text color
        if (view is TextView) {
            typeface?.let { view.typeface = it }
            when (view) {
                is RadioButton, is CheckBox -> {
                    view.setTextColor(palette.foreground)
                    view.buttonTintList = ColorStateList(
                        arrayOf(
                            intArrayOf(android.R.attr.state_checked),
                            intArrayOf(-android.R.attr.state_checked)
                        ),
                        intArrayOf(palette.accent_1, palette.disabled)
                    )
                }

                else -> {
                    val mapped = mapColor(context, view.currentTextColor, palette)
                    if (mapped != null) view.setTextColor(mapped)
                }
            }
        }

        // Icon tint
        if (view is ImageView) {
            val tint = view.imageTintList?.defaultColor
            if (tint != null) {
                val mapped = mapColor(context, tint, palette) ?: palette.icon
                view.imageTintList = ColorStateList.valueOf(mapped)
            }
        }

        // Background
        val currentColor = resolveDrawableColor(view.background ?: return) ?: return
        val mapped = mapColor(context, currentColor, palette) ?: return
        view.setBackgroundColor(mapped)
    }

    private val builtInThemes = listOf(
        Themes.TEYIN,
        Themes.MAKO,
        Themes.RAMA,
        Themes.CATPPUCCIN_MOCHA,
        Themes.CATPPUCCIN_LATTE,
        Themes.DRACULA,
        Themes.MELANGE,
        Themes.TOKYO_NIGHT
    )

    private fun createColorMap(
        context: Context,
        target: Themes.Palette
    ): Map<Int, Int> {
        val custom = buildCustomPalette(context)

        val palettes = builtInThemes + custom

        return buildMap {
            palettes.forEach { source ->
                put(source.bg_1, target.bg_1)
                put(source.bg_2, target.bg_2)
                put(source.bg_3, target.bg_3)
                put(source.bg_4, target.bg_4)
                put(source.bg_display, target.bg_display)

                put(source.foreground, target.foreground)

                put(source.accent_1, target.accent_1)
                put(source.accent_2, target.accent_2)
                put(source.accent_3, target.accent_3)
                put(source.accent_4, target.accent_4)

                put(source.button_1, target.button_1)
                put(source.button_1_selected, target.button_1_selected)
                put(source.button_2, target.button_2)

                put(source.disabled, target.disabled)
                put(source.input, target.input)
                put(source.danger, target.danger)

                put(source.collapsible_header, target.collapsible_header)
                put(source.icon, target.icon)

                put(source.h1, target.h1)

                put(source.bg_display, target.bg_display)
                put(source.media_background, target.media_background)
                put(source.progressbar, target.progressbar)
            }
        }
    }

    /**
     * Maps a runtime color value to its semantic slot in [palette] by comparing
     * against every known palette variant (including the live custom palette).
     */
    private fun mapColor(
        context: Context,
        color: Int,
        palette: Themes.Palette
    ): Int? {
        return createColorMap(context, palette)[color]
    }

    private fun resolveDrawableColor(drawable: android.graphics.drawable.Drawable): Int? =
        (drawable as? ColorDrawable)?.color
}
