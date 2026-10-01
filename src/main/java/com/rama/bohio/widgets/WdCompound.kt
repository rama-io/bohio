package com.rama.bohio.widgets

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.rama.bohio.R

abstract class WdCompound(
    context: Context,
    attrs: AttributeSet?,
    layoutResId: Int,
    private val canUncheck: Boolean,
    private val accessibilityClassName: String
) : LinearLayout(context, attrs) {

    private val check: ImageView
    private val textView: TextView
    private var checked = false
    private var listener: OnCheckedChangeListener? = null

    init {
        inflate(context, layoutResId, this)

        check = findViewById(R.id.check)
        textView = findViewById(R.id.text)

        isClickable = true
        isFocusable = true

        attrs?.let {
            val ta: TypedArray = context.obtainStyledAttributes(
                it,
                intArrayOf(
                    android.R.attr.text,
                    android.R.attr.checked
                )
            )

            ta.getText(0)?.let { text ->
                textView.text = text
            }

            checked = ta.getBoolean(1, false)
            ta.recycle()
        }

        updateCheck()
    }

    fun setText(text: String) {
        textView.text = text
        updateAccessibility()
    }

    fun getText(): String {
        return textView.text.toString()
    }

    fun setTextColor(color: Int) {
        textView.setTextColor(color)
    }

    fun isChecked(): Boolean {
        return checked
    }

    fun setChecked(checked: Boolean) {
        if (this.checked == checked) {
            return
        }

        this.checked = checked

        updateCheck()
        updateAccessibility()

        listener?.onCheckedChanged(checked)

        onCheckedChanged(checked)
    }

    protected open fun onCheckedChanged(checked: Boolean) {
        // For subclasses.
    }

    fun toggle() {
        if (!isEnabled || (checked && !canUncheck)) {
            return
        }

        setChecked(!checked)
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        this.listener = listener
    }

    private fun updateCheck() {
        check.visibility = if (checked) View.VISIBLE else View.GONE
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        updateCheck()
        textView.isEnabled = enabled
    }

    override fun performClick(): Boolean {
        super.performClick()
        toggle()
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                return true
            }

            MotionEvent.ACTION_UP -> {
                isPressed = false

                if (isInside(event.x, event.y)) {
                    performClick()
                }

                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                isPressed = false
                return true
            }
        }

        return true
    }

    private fun isInside(x: Float, y: Float): Boolean {
        return x >= 0 &&
                x < width &&
                y >= 0 &&
                y < height
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        if (
            keyCode == KeyEvent.KEYCODE_SPACE ||
            keyCode == KeyEvent.KEYCODE_ENTER ||
            keyCode == KeyEvent.KEYCODE_DPAD_CENTER
        ) {
            performClick()
            return true
        }

        return super.onKeyUp(keyCode, event)
    }

    override fun onInitializeAccessibilityNodeInfo(
        info: AccessibilityNodeInfo
    ) {
        super.onInitializeAccessibilityNodeInfo(info)

        info.className = accessibilityClassName
        info.isCheckable = true
        info.isChecked = checked
        info.isClickable = isEnabled
        info.isEnabled = isEnabled

        val label = textView.text
        if (!label.isNullOrEmpty()) {
            info.text = label
        }
    }

    private fun updateAccessibility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            sendAccessibilityEvent(
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            )
        }

        invalidate()
    }

    fun interface OnCheckedChangeListener {
        fun onCheckedChanged(isChecked: Boolean)
    }
}
