package com.rama.bohio.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.rama.bohio.R

/**
 * Shared theme manager: theme-attr resolution, wallpaper screen opacity
 * overlay (0-9 strength), and navigation bar tinting.
 *
 * Port over from Mako:
 *  - lastAppliedTheme cache invalidation on theme/opacity change
 *  - per-theme palette switching (if themes are more than just light/dark)
 *  - background apply logic tied to applyHomeBackground / applyWindowBackground split
 */
object ThemeManager {

    private const val PREFS_NAME = "rama_theme_prefs"
    private const val KEY_OPACITY = "bg_opacity"

    /** Cache key for the last applied theme/opacity combo, to skip redundant work. */
    private var lastAppliedTheme: String? = null

    /**
     * Resolves a color theme-attribute (e.g. R.attr.ramaWindowBg) to an
     * actual color int, for the given context's current theme.
     */
    fun resolveColorAttr(context: Context, attrResId: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrResId, typedValue, true)
        return if (typedValue.resourceId != 0) {
            ContextCompat.getColor(context, typedValue.resourceId)
        } else {
            typedValue.data
        }
    }

    /**
     * Applies the window background using the app's ramaWindowBg attr,
     * adjusted by the stored opacity (0-9, where 9 = fully opaque).
     */
    fun applyWindowBackground(activity: Activity) {
        val baseColor = resolveColorAttr(activity, R.attr.ramaWindowBg)
        val alpha = (getOpacity(activity) / 9f * 255).toInt().coerceIn(0, 255)
        val finalColor = ColorUtils.setAlphaComponent(baseColor, alpha)

        activity.window.decorView.setBackgroundColor(finalColor)
        applyNavigationBarTint(activity, finalColor)

        lastAppliedTheme = "$baseColor:$alpha"
    }

    /** Tints the nav bar to match the background overlay, for OEM consistency. */
    fun applyNavigationBarTint(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.window.isNavigationBarContrastEnforced = false
        }
        @Suppress("DEPRECATION")
        activity.window.navigationBarColor = color
    }

    /** Background opacity, 0 (transparent) - 9 (fully opaque). Default: 9. */
    fun getOpacity(context: Context): Int =
        prefs(context).getInt(KEY_OPACITY, 9)

    fun setOpacity(context: Context, value: Int) {
        prefs(context).edit().putInt(KEY_OPACITY, value.coerceIn(0, 9)).apply()
        lastAppliedTheme = null // invalidate cache
    }

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
