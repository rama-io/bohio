package com.rama.bohio.widgets

import android.content.Context
import android.util.AttributeSet
import com.rama.bohio.R

class WdRadio : WdCompound {

    private var internalListener: InternalOnCheckedChangeListener? = null

    constructor(context: Context) : this(context, null)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(
        context = context,
        attrs = attrs,
        layoutResId = R.layout.wd_radio,
        canUncheck = false,
        accessibilityClassName = "android.widget.RadioButton"
    )

    fun setInternalCheckedChangeListener(
        listener: InternalOnCheckedChangeListener?
    ) {
        internalListener = listener
    }

    override fun onCheckedChanged(checked: Boolean) {
        internalListener?.onCheckedChanged(this, checked)
    }

    fun interface InternalOnCheckedChangeListener {
        fun onCheckedChanged(
            radio: WdRadio,
            checked: Boolean
        )
    }
}
