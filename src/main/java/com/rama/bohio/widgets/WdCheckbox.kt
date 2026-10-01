package com.rama.bohio.widgets

import android.content.Context
import android.util.AttributeSet
import com.rama.bohio.R

class WdCheckbox : WdCompound {

    constructor(context: Context) : this(context, null)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(
        context = context,
        attrs = attrs,
        layoutResId = R.layout.wd_checkbox,
        canUncheck = true,
        accessibilityClassName = "android.widget.CheckBox"
    )
}