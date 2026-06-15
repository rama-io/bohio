package com.rama.common.util

import android.content.Context
import android.util.TypedValue

/**
 * Small unit-conversion helpers shared across apps.
 *
 * Port over any additional helpers from existing dimens.kt files
 * (e.g. screen-width-based breakpoints used for grid columns).
 */
object Dimens {

    fun dpToPx(context: Context, dp: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()

    fun spToPx(context: Context, sp: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics
        ).toInt()

    fun pxToDp(context: Context, px: Float): Float =
        px / context.resources.displayMetrics.density
}
