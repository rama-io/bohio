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
import com.rama.bohio.R
import com.rama.bohio.objects.PrefKeys
import com.rama.bohio.objects.PrefTheme
import com.rama.bohio.objects.Themes

object ThemeManager {

    fun paletteFor(theme: String, context: Context? = null): Themes.Palette =
        when (theme) {
            PrefTheme.MAKO            -> Themes.MAKO
            PrefTheme.RAMA            -> Themes.RAMA
            PrefTheme.CATPPUCCIN_MOCHA -> Themes.CATPPUCCIN_MOCHA
            PrefTheme.CATPPUCCIN_LATTE -> Themes.CATPPUCCIN_LATTE
            PrefTheme.DRACULA         -> Themes.DRACULA
            PrefTheme.MELANGE         -> Themes.MELANGE
            PrefTheme.TOKYO_NIGHT     -> Themes.TOKYO_NIGHT
            PrefTheme.CUSTOM          -> if (context != null) buildCustomPalette(context) else Themes.TEYIN
            else                      -> Themes.TEYIN
        }

    private fun buildCustomPalette(context: Context): Themes.Palette {
        val prefs = PrefsManager.getInstance(context)
        val base  = Themes.TEYIN
        fun get(key: String, fallback: Int) = prefs.getCustomThemeColor(key, fallback)
        return Themes.Palette(
            foreground         = get(PrefKeys.APP_THEME_FOREGROUND,         base.foreground),
            bg_1               = get(PrefKeys.APP_THEME_BG_1,               base.bg_1),
            bg_2               = get(PrefKeys.APP_THEME_BG_2,               base.bg_2),
            bg_3               = get(PrefKeys.APP_THEME_BG_3,               base.bg_3),
            accent_1           = get(PrefKeys.APP_THEME_ACCENT_1,           base.accent_1),
            accent_2           = get(PrefKeys.APP_THEME_ACCENT_2,           base.accent_2),
            accent_3           = get(PrefKeys.APP_THEME_ACCENT_3,           base.accent_3),
            disabled           = get(PrefKeys.APP_THEME_DISABLED,           base.disabled),
            input              = get(PrefKeys.APP_THEME_INPUT,              base.input),
            button_1           = get(PrefKeys.APP_THEME_BUTTON_1,           base.button_1),
            button_2           = get(PrefKeys.APP_THEME_BUTTON_2,           base.button_2),
            danger             = get(PrefKeys.APP_THEME_DANGER,             base.danger),
            collapsible_header = get(PrefKeys.APP_THEME_COLLAPSIBLE_HEADER, base.collapsible_header),
            icon               = get(PrefKeys.APP_THEME_ICON,              base.icon),
            h1                 = get(PrefKeys.APP_THEME_H1,                base.h1),
        )
    }

    fun applyTheme(context: Context, root: View) {
        val prefs    = PrefsManager.getInstance(context)
        val palette  = paletteFor(prefs.getTheme(), context)
        val typeface = FontManager.getTypeface(context, prefs.getFontStyle())
        applyRecursively(context, root, palette, typeface)
    }

    private fun applyRecursively(
        context: Context, view: View, palette: Themes.Palette,
        typeface: android.graphics.Typeface?
    ) {
        applyToView(context, view, palette, typeface)
        if (view is ViewGroup) {
            for (i in 0 until view.childCount)
                applyRecursively(context, view.getChildAt(i), palette, typeface)
        }
    }

    private fun applyToView(
        context: Context, view: View, palette: Themes.Palette,
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
                            intArrayOf( android.R.attr.state_checked),
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

    /**
     * Maps a runtime color value to its semantic slot in [palette] by comparing
     * against every known palette variant (including the live custom palette).
     */
    private fun mapColor(context: Context, color: Int, palette: Themes.Palette): Int? {
        val custom = buildCustomPalette(context)
        val res    = context.resources

        return when (color) {
            Themes.MAKO.bg_1, Themes.TEYIN.bg_1, Themes.RAMA.bg_1,
            Themes.CATPPUCCIN_MOCHA.bg_1, Themes.CATPPUCCIN_LATTE.bg_1,
            Themes.DRACULA.bg_1, Themes.MELANGE.bg_1, Themes.TOKYO_NIGHT.bg_1,
            custom.bg_1, res.getColor(R.color.bg_1) -> palette.bg_1

            Themes.MAKO.bg_2, Themes.TEYIN.bg_2, Themes.RAMA.bg_2,
            Themes.CATPPUCCIN_MOCHA.bg_2, Themes.CATPPUCCIN_LATTE.bg_2,
            Themes.DRACULA.bg_2, Themes.MELANGE.bg_2, Themes.TOKYO_NIGHT.bg_2,
            custom.bg_2, res.getColor(R.color.bg_2) -> palette.bg_2

            Themes.MAKO.bg_3, Themes.TEYIN.bg_3, Themes.RAMA.bg_3,
            Themes.CATPPUCCIN_MOCHA.bg_3, Themes.CATPPUCCIN_LATTE.bg_3,
            Themes.DRACULA.bg_3, Themes.MELANGE.bg_3, Themes.TOKYO_NIGHT.bg_3,
            custom.bg_3, res.getColor(R.color.bg_3) -> palette.bg_3

            Themes.MAKO.button_1, Themes.TEYIN.button_1, Themes.RAMA.button_1,
            Themes.CATPPUCCIN_MOCHA.button_1, Themes.CATPPUCCIN_LATTE.button_1,
            Themes.DRACULA.button_1, Themes.MELANGE.button_1, Themes.TOKYO_NIGHT.button_1,
            custom.button_1, res.getColor(R.color.button_1) -> palette.button_1

            Themes.MAKO.button_2, Themes.TEYIN.button_2, Themes.RAMA.button_2,
            Themes.CATPPUCCIN_MOCHA.button_2, Themes.CATPPUCCIN_LATTE.button_2,
            Themes.DRACULA.button_2, Themes.MELANGE.button_2, Themes.TOKYO_NIGHT.button_2,
            custom.button_2, res.getColor(R.color.button_2) -> palette.button_2

            Themes.MAKO.danger, Themes.TEYIN.danger, Themes.RAMA.danger,
            Themes.CATPPUCCIN_MOCHA.danger, Themes.CATPPUCCIN_LATTE.danger,
            Themes.DRACULA.danger, Themes.MELANGE.danger, Themes.TOKYO_NIGHT.danger,
            custom.danger, res.getColor(R.color.danger) -> palette.danger

            Themes.MAKO.input, Themes.TEYIN.input, Themes.RAMA.input,
            Themes.CATPPUCCIN_MOCHA.input, Themes.CATPPUCCIN_LATTE.input,
            Themes.DRACULA.input, Themes.MELANGE.input, Themes.TOKYO_NIGHT.input,
            custom.input, res.getColor(R.color.input) -> palette.input

            Themes.MAKO.disabled, Themes.TEYIN.disabled, Themes.RAMA.disabled,
            Themes.CATPPUCCIN_MOCHA.disabled, Themes.CATPPUCCIN_LATTE.disabled,
            Themes.DRACULA.disabled, Themes.MELANGE.disabled, Themes.TOKYO_NIGHT.disabled,
            custom.disabled, res.getColor(R.color.disabled) -> palette.disabled

            Themes.MAKO.accent_1, Themes.TEYIN.accent_1, Themes.RAMA.accent_1,
            Themes.CATPPUCCIN_MOCHA.accent_1, Themes.CATPPUCCIN_LATTE.accent_1,
            Themes.DRACULA.accent_1, Themes.MELANGE.accent_1, Themes.TOKYO_NIGHT.accent_1,
            custom.accent_1, res.getColor(R.color.accent_1) -> palette.accent_1

            Themes.MAKO.accent_2, Themes.TEYIN.accent_2, Themes.RAMA.accent_2,
            Themes.CATPPUCCIN_MOCHA.accent_2, Themes.CATPPUCCIN_LATTE.accent_2,
            Themes.DRACULA.accent_2, Themes.MELANGE.accent_2, Themes.TOKYO_NIGHT.accent_2,
            custom.accent_2, res.getColor(R.color.accent_2) -> palette.accent_2

            Themes.MAKO.accent_3, Themes.TEYIN.accent_3, Themes.RAMA.accent_3,
            Themes.CATPPUCCIN_MOCHA.accent_3, Themes.CATPPUCCIN_LATTE.accent_3,
            Themes.DRACULA.accent_3, Themes.MELANGE.accent_3, Themes.TOKYO_NIGHT.accent_3,
            custom.accent_3, res.getColor(R.color.accent_3) -> palette.accent_3

            Themes.MAKO.collapsible_header, Themes.TEYIN.collapsible_header,
            Themes.RAMA.collapsible_header, Themes.CATPPUCCIN_MOCHA.collapsible_header,
            Themes.CATPPUCCIN_LATTE.collapsible_header, Themes.DRACULA.collapsible_header,
            Themes.MELANGE.collapsible_header, Themes.TOKYO_NIGHT.collapsible_header,
            custom.collapsible_header, res.getColor(R.color.collapsible_header) -> palette.collapsible_header

            Themes.MAKO.icon, Themes.TEYIN.icon, Themes.RAMA.icon,
            Themes.CATPPUCCIN_MOCHA.icon, Themes.CATPPUCCIN_LATTE.icon,
            Themes.DRACULA.icon, Themes.MELANGE.icon, Themes.TOKYO_NIGHT.icon,
            custom.icon, res.getColor(R.color.icon) -> palette.icon

            Themes.MAKO.h1, Themes.TEYIN.h1, Themes.RAMA.h1,
            Themes.CATPPUCCIN_MOCHA.h1, Themes.CATPPUCCIN_LATTE.h1,
            Themes.DRACULA.h1, Themes.MELANGE.h1, Themes.TOKYO_NIGHT.h1,
            custom.h1, res.getColor(R.color.h1) -> palette.h1

            Themes.MAKO.foreground, Themes.TEYIN.foreground, Themes.RAMA.foreground,
            Themes.CATPPUCCIN_MOCHA.foreground, Themes.CATPPUCCIN_LATTE.foreground,
            Themes.DRACULA.foreground, Themes.MELANGE.foreground, Themes.TOKYO_NIGHT.foreground,
            custom.foreground, res.getColor(R.color.foreground) -> palette.foreground

            else -> null
        }
    }

    private fun resolveDrawableColor(drawable: android.graphics.drawable.Drawable): Int? =
        (drawable as? ColorDrawable)?.color
}
