package com.rama.bohio.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import com.rama.bohio.R
import com.rama.bohio.util.ThemeManager

/**
 * Section / folder header row used in list adapters — e.g. the
 * always-visible folder header rows in Tūī's TrackAdapter, or a
 * directory header in Puma/Teyin's FileListAdapter.
 *
 * Styled entirely from shared theme attrs, so it matches each app's
 * palette automatically without per-app overrides.
 *
 * This is a starting template — port over the real widget set
 * (e.g. multi-select row decorations, the ".." up-navigation row style)
 * as additional classes in this package.
 */
class RamaSectionHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    init {
        val padding = resources.getDimensionPixelSize(R.dimen.rama_spacing_sm)
        setPadding(padding, padding, padding, padding)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.rama_text_size_md))
        setBackgroundColor(ThemeManager.resolveColorAttr(context, R.attr.ramaCardBackground))
        setTextColor(ThemeManager.resolveColorAttr(context, R.attr.ramaTextColorSecondary))
    }
}
